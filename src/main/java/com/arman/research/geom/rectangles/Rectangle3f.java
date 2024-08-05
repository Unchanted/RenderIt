package com.arman.research.geom.rectangles;

import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.Transformable;
import com.arman.research.geom.vectors.Vector3f;

public class Rectangle3f implements Transformable {

    private Vector3f origin;
    private Vector3f du;
    private Vector3f dv;
    private float width;
    private float height;

    public Rectangle3f() {
        origin = new Vector3f();
        du = new Vector3f(1, 0, 0);
        dv = new Vector3f(0, 1, 0);
        width = 0;
        height = 0;
    }

    public Rectangle3f(Vector3f origin, Vector3f du, Vector3f dv, float width, float height) {
        this.origin = new Vector3f(origin);
        this.du = new Vector3f(du);
        this.dv = new Vector3f(dv);
        this.du.normalize();
        this.dv.normalize();
        this.width = width;
        this.height = height;
    }

    public void setTo(Rectangle3f r) {
        origin.setTo(r.origin);
        du.setTo(r.du);
        dv.setTo(r.dv);
        width = r.width;
        height = r.height;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getDu() {
        return du;
    }

    public Vector3f getDv() {
        return dv;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Vector3f calcNormal() {
        Vector3f normal = new Vector3f(du.cross(dv));
        normal.normalize();
        return normal;
    }

    @Override
    public void add(Vector3f v) {
        origin.add(v);
    }

    @Override
    public void subtract(Vector3f v) {
        origin.subtract(v);
    }

    @Override
    public void add(Transform3f t) {
        addRotation(t);
        add(t.getLocation());
    }

    @Override
    public void subtract(Transform3f t) {
        subtract(t.getLocation());
        subtractRotation(t);
    }

    @Override
    public void addRotation(Transform3f t) {
        origin.addRotation(t);
        du.addRotation(t);
        dv.addRotation(t);
    }

    @Override
    public void subtractRotation(Transform3f t) {
        origin.subtractRotation(t);
        du.subtractRotation(t);
        dv.subtractRotation(t);
    }
}
