package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * List with maximum size that removes the oldest element
 * if the size is being exceeded.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class RotatingList<T> {
    private List<T> list = new ArrayList<>();
    private int size;

    RotatingList(int size) {
        this.size = size;
    }

    public void push(T object) {
        if (list.size() == size) {
            list.remove(0);
        }
        list.add(object);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size(){
        return list.size();
    }
}
