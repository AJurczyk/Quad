package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * List with maximum maxSize that removes the oldest element
 * if the maxSize is being exceeded.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class RotatingList<T> {
    private final List<T> list = new ArrayList<>();
    private final int maxSize;

    RotatingList(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Add to list.
     *
     * @param object object to add
     */
    public void add(T object) {
        if (list.size() == maxSize) {
            list.remove(0);
        }
        list.add(object);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }
}
