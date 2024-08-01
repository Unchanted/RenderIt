package com.arman;

public class Tuple<T> {

    private T t1;
    private T t2;

    public Tuple(T t1, T t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Tuple() {
        this.t1 = null;
        this.t2 = null;
    }

    public T get(int index) {
        if (index == 0) {
            return t1;
        } else if (index == 1) {
            return t2;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void set(int index, T t) {
        if (index == 0) {
            this.t1 = t;
        } else if (index == 1) {
            this.t2 = t;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return 2;
    }

}
