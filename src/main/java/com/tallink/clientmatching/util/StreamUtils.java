package com.tallink.clientmatching.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class StreamUtils {

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static <K,V> Map<K,V> newTreeMap(Map.Entry<K,V>... entries) {
        return Stream.of(entries).collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                throwingMerger(), TreeMap::new));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
        Map<Object,Boolean> seen = new HashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

}
