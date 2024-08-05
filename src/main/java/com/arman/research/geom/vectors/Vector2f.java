package com.arman.research.geom.vectors;

public class Vector2f {

    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2f() {
        this(0, 0);
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f v) {
        this(v.x, v.y);
    }

    public Vector2f(Vector3f v) {
        this(v.getX(), v.getY());
    }

    public Vector2f(Vector4f v) {
        this(v.getX(), v.getY());
    }

    @Override
    public String toString() {
        return "Vector2f{" + x + ", " + y + "}";
    }

}
