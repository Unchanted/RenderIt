package com.arman.geom;

import org.jetbrains.annotations.NotNull;

public class Rectangle3D extends Polygon3D {

    private Vector3D origin;
    private Vector3D du, dv;
    private float width, height;

    public Rectangle3D(Vector3D origin, @NotNull Vector3D du, @NotNull Vector3D dv, float width, float height) {
        this.origin = origin;
        this.du = du;
        this.dv = dv;
        this.du.normalize();
        this.dv.normalize();
        this.width = width;
        this.height = height;
    }

    public Rectangle3D(@NotNull Rectangle3D rectangle) {
        this.origin = new Vector3D(rectangle.origin);
        this.du = new Vector3D(rectangle.du);
        this.dv = new Vector3D(rectangle.dv);
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    public void set(Rectangle3D rectangle) {
        this.origin.set(rectangle.origin);
        this.du.set(rectangle.du);
        this.dv.set(rectangle.dv);
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    public Vector3D normal() {
        Vector3D normal = new Vector3D(du.cross(dv));
        normal.normalize();
        return normal;
    }

    public void add(Vector3D v) {
        this.origin.add(v);
    }

    public void subtract(Vector3D v) {
        this.origin.subtract(v);
    }

    public void add(Rotation3D rotation) {
        this.origin.add(rotation);
        this.du.add(rotation);
        this.dv.add(rotation);
    }

    public void subtract(Rotation3D rotation) {
        this.origin.subtract(rotation);
        this.du.subtract(rotation);
        this.dv.subtract(rotation);
    }

    public void add(Transform3D transform) {
        this.origin.add(transform);
        this.du.add(transform.getRotation());
        this.dv.add(transform.getRotation());
    }

    public void subtract(Transform3D transform) {
        this.origin.subtract(transform);
        this.du.add(transform.getRotation());
        this.dv.add(transform.getRotation());
    }

}
