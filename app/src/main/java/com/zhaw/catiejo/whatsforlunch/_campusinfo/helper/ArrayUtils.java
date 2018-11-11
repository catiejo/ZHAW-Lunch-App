package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

import java.lang.reflect.Array;

/**
 * Various utilities for array manipulations.
 */
public final class ArrayUtils {
    private ArrayUtils() {
    }

    /**
     * Returns a new array that contains the concatenated contents of the two arrays passed as arguments which may be
     * null. The returned object array of type {@code T} is guaranteed not to be null.
     *
     * @param first the first array of elements to concatenate.
     * @param second the second array of elements to concatenate.
     * @param type the component type of the returned array.
     * @param <T> the component type of the passed and returned arrays.
     * @return Object array of type T containing the resulting array.
     */
    public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
        if (first == null && second == null) {
            return newArray(type, 0);
        }
        if (first == null && second != null) {
            return second;
        }
        if (second == null && first != null) {
            return first;
        }


        T[] result = newArray(type, first.length + second.length);
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Creates empty array of type {@code T} with the specified length.
     *
     * @param type the component type of the returned array.
     * @param length the length of the returned array.
     * @param <T> the component type of the returnes array.
     * @return Object array of type {@code T} with size {@code length}.
     */
    public static <T> T[] newArray(Class<T> type, int length) {
        @SuppressWarnings("unchecked")
        T[]result = (T[]) Array.newInstance(type, length);
        return result;
    }
}
