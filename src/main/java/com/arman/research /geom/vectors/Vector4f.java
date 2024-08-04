package com.arman.research.geom.vectors;

public class Vector4f {

    private float t;
    private float x;
    private float y;
    private float z;

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vector4f() {
        this(0, 0, 0, 0);
    }

    public Vector4f(float t, float x, float y, float z) {
        this.t = t;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector4f(Vector4f v) {
        this(v.t, v.x, v.y, v.z);
    }

    public Vector4f(Vector3f v, float f) {
        this(f, v.getX(), v.getY(), v.getZ());
    }

}
