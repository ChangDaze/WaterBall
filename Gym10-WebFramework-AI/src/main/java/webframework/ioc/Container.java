package webframework.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The IoC container (BUILD_SPEC F).
 *
 * <pre>{@code
 * container.register(UserController.class);                             // singleton (default)
 * container.register(UserService.class,    new PrototypeScope());
 * container.register(RequestId.class,      new HttpRequestScope());
 * UserController controller = container.get(UserController.class);
 * }</pre>
 *
 * <p>Two properties are worth naming because they are what the design is for:
 *
 * <ul>
 *   <li><b>Lazy.</b> {@code register} records intent only; nothing is constructed until the first
 *       {@code get} of that registration.</li>
 *   <li><b>Open to new lifecycles.</b> This class never branches on the kind of scope it holds, so a
 *       lifecycle written outside the framework works without a line changing here.</li>
 * </ul>
 */
public final class Container {

    private final Map<String, Registration> byName = new LinkedHashMap<>();
    private final Map<Class<?>, Registration> byType = new LinkedHashMap<>();

    /**
     * The chain of types currently being constructed on this thread, purely to turn a dependency
     * cycle into a readable error instead of a StackOverflowError.
     */
    private final ThreadLocal<Set<Class<?>>> underConstruction = ThreadLocal.withInitial(LinkedHashSet::new);

    /**
     * Registers a type as a singleton — the default lifecycle.
     */
    public Container register(Class<?> type) {
        return register(type, new SingletonScope());
    }

    public Container register(Class<?> type, Lifecycle lifecycle) {
        return register(defaultNameOf(type), type, lifecycle);
    }

    public Container register(String name, Class<?> type, Lifecycle lifecycle) {
        if (byName.containsKey(name)) {
            throw new ContainerException("Name \"" + name + "\" is already registered to "
                    + byName.get(name).getType().getName());
        }
        if (byType.containsKey(type)) {
            throw new ContainerException("Type " + type.getName() + " is already registered as \""
                    + byType.get(type).getName() + "\"");
        }
        Registration registration =
                new Registration(name, type, lifecycle, () -> instantiate(type));
        byName.put(name, registration);
        byType.put(type, registration);
        return this;
    }

    /**
     * Lookup by type.
     *
     * @throws ContainerException if the type is not registered
     */
    public <T> T get(Class<T> type) {
        Registration registration = byType.get(type);
        if (registration == null) {
            throw new ContainerException("No registration for type " + type.getName());
        }
        return type.cast(registration.resolve());
    }

    /**
     * Lookup by name.
     *
     * @throws ContainerException if the name is not registered
     */
    public Object get(String name) {
        Registration registration = byName.get(name);
        if (registration == null) {
            throw new ContainerException("No registration named \"" + name + "\"");
        }
        return registration.resolve();
    }

    public boolean isRegistered(Class<?> type) {
        return byType.containsKey(type);
    }

    /**
     * Builds one instance, resolving each constructor parameter through the container so that a
     * dependency's own lifecycle is honoured (recursively, all the way down).
     */
    private Object instantiate(Class<?> type) {
        Set<Class<?>> chain = underConstruction.get();
        if (!chain.add(type)) {
            throw new ContainerException("Dependency cycle: " + describeCycle(chain, type));
        }
        try {
            Constructor<?> constructor = chooseConstructor(type);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] arguments = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                arguments[i] = resolveDependency(type, parameterTypes[i]);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch (InvocationTargetException e) {
            throw new ContainerException("Constructor of " + type.getName() + " threw "
                    + e.getCause(), e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new ContainerException("Cannot instantiate " + type.getName(), e);
        } finally {
            chain.remove(type);
            if (chain.isEmpty()) {
                underConstruction.remove();
            }
        }
    }

    private Object resolveDependency(Class<?> dependent, Class<?> dependencyType) {
        if (!byType.containsKey(dependencyType)) {
            throw new ContainerException(dependent.getName() + " needs "
                    + dependencyType.getName() + ", which is not registered");
        }
        return get(dependencyType);
    }

    /**
     * One constructor: use it. Several: the greediest one, on the assumption that the fullest
     * constructor is the one the class actually wants — the same convention every DI container uses.
     */
    private static Constructor<?> chooseConstructor(Class<?> type) {
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new ContainerException("Cannot instantiate " + type.getName()
                    + ": it is abstract. Register a concrete type instead.");
        }
        List<Constructor<?>> constructors = new ArrayList<>(List.of(type.getDeclaredConstructors()));
        constructors.removeIf(candidate -> candidate.isSynthetic());
        if (constructors.isEmpty()) {
            throw new ContainerException("No usable constructor on " + type.getName());
        }
        constructors.sort(Comparator.comparingInt(
                (Constructor<?> candidate) -> candidate.getParameterCount()).reversed());
        return constructors.get(0);
    }

    private static String describeCycle(Set<Class<?>> chain, Class<?> repeated) {
        StringBuilder description = new StringBuilder();
        for (Class<?> type : chain) {
            description.append(type.getSimpleName()).append(" -> ");
        }
        return description.append(repeated.getSimpleName()).toString();
    }

    private static String defaultNameOf(Class<?> type) {
        return type.getSimpleName();
    }
}
