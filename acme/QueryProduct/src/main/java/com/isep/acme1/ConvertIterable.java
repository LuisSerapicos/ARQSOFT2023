package com.isep.acme1;

import java.util.Iterator;

public class ConvertIterable<T> implements Iterable<T> {
    private final Iterable<T> iterable;

    public ConvertIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    // You can add additional methods or functionality as needed
}
