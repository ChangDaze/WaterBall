package ObserverPattern.common;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Utils {
    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        }catch (InterruptedException ignored){

        }
    }

    public static <T, K> List<K> selectDistinct(Collection<T> t, Function<T, K> mapping) {
        return t.stream().map(mapping).distinct().collect(toList());
    }

    public static <T, V, K> List<Integer> count(Collection<T> t, Collection<V> v, Function<V, K> mapping) {
        return t.stream().map(val->(int)v.stream().filter(s->val.equals(mapping.apply(s))).count()).collect(toList());
    }
}
