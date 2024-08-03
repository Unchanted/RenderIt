package com.arman.geom;

import com.arman.Tuple;

public class Line3D {

    private Vector3D v1, v2;

    public Line3D(Vector3D v1, Vector3D v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Line3D(Tuple<Vector3D> vertices) {
        this.v1 = vertices.get(0);
        this.v2 = vertices.get(1);
    }

    public Line3D(Line3D l) {
        this.set(l);
    }

    public Line3D() {
        this(new Vector3D(), new Vector3D());
    }

    public Vector3D get0() {
        return this.v1;
    }

    public Vector3D get1() {
        return this.v2;
    }

    public void set(Line3D l) {
        this.v1 = new Vector3D(l.v1);
        this.v2 = new Vector3D(l.v2);
    }

    public void set(Vector3D v1, Vector3D v2) {
        this.v1 = new Vector3D(v1);
        this.v2 = new Vector3D(v2);
    }

    public Vector3D getLeft() {
        if (this.v1.getX() < this.v2.getX()) {
            return this.v1;
        }
        return this.v2;
    }

    public Vector3D getRight() {
        if (this.v1.getX() > this.v2.getX()) {
            return this.v1;
        }
        return this.v2;
    }

    public Vector3D getOther(Vector3D v) {
        if (v.equals(this.v1)) {
            return this.v2;
        }
        return this.v1;
    }

    public Vector3D getTop() {
        if (this.v1.getY() < this.v2.getY()) {
            return this.v1;
        }
        return this.v2;
    }

    public Vector3D getBottom() {
        if (this.v1.getY() > this.v2.getY()) {
            return this.v1;
        }
        return this.v2;
    }

    public void add(Vector3D v) {
        this.v1.add(v);
        this.v2.add(v);
    }

    public void subtract(Vector3D v) {
        this.v1.subtract(v);
        this.v2.subtract(v);
    }

    public void add(Rotation3D rotation) {
        this.v1.add(rotation);
        this.v2.add(rotation);
    }

    public void subtract(Rotation3D rotation) {
        this.v1.subtract(rotation);
        this.v2.subtract(rotation);
    }

    public void add(Transform3D transform) {
        this.v1.add(transform);
        this.v2.add(transform);
    }

    public void subtract(Transform3D transform) {
        this.v1.subtract(transform);
        this.v2.subtract(transform);
    }

    public float slope() {
        Vector3D left = getLeft();
        Vector3D right = getOther(left);
        return (right.getY() - left.getY()) / (right.getX() - left.getX());
    }

}
