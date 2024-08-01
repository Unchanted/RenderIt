package com.arman.geom;

import com.arman.util.ExtraMath;
import org.jetbrains.annotations.NotNull;

public class Vector2D {

    private float x, y;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(float x, float y) {
        this.set(x, y);
    }

    public Vector2D(@NotNull Vector2D v) {
        this.set(v);
    }

    public void set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void multiply(float f) {
        this.x *= f;
        this.y *= f;
    }

    public void divide(float f) {
        this.x /= f;
        this.y /= f;
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void lerp(Vector2D v1, Vector2D v2, double p) {
        this.x = (float) ExtraMath.lerp(v1.x, v2.x, p);
        this.y = (float) ExtraMath.lerp(v1.y, v2.y, p);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2D normalized() {
        float l = length();
        return new Vector2D(this.x / l, this.y / l);
    }

    public void normalize() {
        this.divide(length());
    }

    public void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
    }

}
