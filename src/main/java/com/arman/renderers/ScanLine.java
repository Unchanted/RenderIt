package com.arman.renderers;

public class ScanLine {

    private int left, right;

    public ScanLine() {
        this.clear();
    }

    public ScanLine(ScanLine scanLine) {
        this.set(scanLine);
    }

    public ScanLine(int left, int right) {
        this.set(left, right);
    }

    public void clear() {
        this.setLeft(Integer.MAX_VALUE);
        this.setRight(Integer.MIN_VALUE);
    }

    public boolean isValid() {
        return this.getLeft() <= this.getRight();
    }

    public void setBounds(int x) {
        if (x < this.getLeft()) {
            this.setLeft(x);
        }
        if (x - 1 > this.getRight()) {
            this.setRight(x - 1);
        }
    }

    public void set(ScanLine scanLine) {
        this.setLeft(scanLine.left);
        this.setRight(scanLine.right);
    }

    public void set(int left, int right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public boolean equals(Object o) {
        if (o instanceof ScanLine) {
            ScanLine scanLine = (ScanLine) o;
            return this.left == scanLine.left && this.right == scanLine.right;
        }
        return false;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "ScanLine={" + this.getLeft() + ", " + this.getRight() + "}";
    }

}
