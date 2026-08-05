package webframework.ioc;

import java.util.function.Supplier;

/**
 * Strategy: decides how often a registered type is actually instantiated.
 *
 * <p>BUILD_SPEC F: new lifecycles must be addable without modifying {@link Container}. That holds
 * because Container never asks what kind of lifecycle it has — it only ever calls this method and
 * passes the provider that knows how to build the object.
 */
public interface Lifecycle {

    /**
     * @param name     the registration's name, usable as a cache key
     * @param provider builds a brand-new instance, dependencies included; call it only when the
     *                 lifecycle actually needs one, which is what keeps construction lazy
     */
    Object get(String name, Supplier<Object> provider);
}
