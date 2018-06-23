package edu.self.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CollectionUtils {
    public static Map diff(Map m1, Map m2) {
        Collection keyDiff = diff(m1.keySet(), m2.keySet());
        Map diff = new HashMap();
        for (Object key : keyDiff) {
            diff.put(key, m1.get(key));
        }
        return diff;
    }

    public static Collection diff(Collection c1, Collection c2) {
        c1 = new ArrayList(c1);
        // it is a fix of a bug caused by custom comparator (see sort method in TextAnalyzerSimple)
        c2 = new ArrayList(c2);
        c1.removeAll(c2);
        return c1;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Map<K, V> sort(final Map<K, V> map) {
        Comparator<K> comparator = new Comparator<K>() {
            @Override
            public int compare(K key1, K key2) {
                int cmp = map.get(key1).compareTo(map.get(key2));
                if (cmp != 0) {
                    return -cmp; //reverse order
                }
                // return 0 would skip entries with the same values
                return key1.compareTo(key2);
            }
        };
        TreeMap<K, V> mapSorted = new TreeMap<K, V>(comparator);
        mapSorted.putAll(map);
        return mapSorted;
    }
}
