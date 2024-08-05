package com.arman.research.geom.vectors;

import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.Transformable;

public class Vector3f implements Transformable {

    public static final Vector3f ORIGIN = new Vector3f();

    private float x;
    private float y;
    private float z;

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

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        this(v.x, v.y, v.z);
    }

    public Vector3f(Vector4f v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public boolean equals(Vector3f v) {
        return x == v.x && y == v.y && z == v.z;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f cross(Vector3f v) {
        return new Vector3f(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    public float dot(Vector3f v) {
        return x * v.x + y * v.y + z * v.z;
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
        rotateX(t.getCosAngleX(), t.getSinAngleX());
        rotateZ(t.getCosAngleZ(), t.getSinAngleZ());
        rotateY(t.getCosAngleY(), t.getSinAngleY());
    }

    @Override
    public void subtractRotation(Transform3f t) {
        rotateY(t.getCosAngleY(), -t.getSinAngleY());
        rotateZ(t.getCosAngleZ(), -t.getSinAngleZ());
        rotateX(t.getCosAngleX(), -t.getSinAngleX());
    }

    @Override
    public void add(Vector3f v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    @Override
    public void subtract(Vector3f v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public void multiply(float f) {
        x *= f;
        y *= f;
        z *= f;
    }

    public void divide(float f) {
        x /= f;
        y /= f;
        z /= f;
    }

    public void rotateX(float a) {
        rotateX((float) Math.cos(a), (float) Math.sin(a));
    }

    public void rotateX(float cosA, float sinA) {
        float ny = y * cosA - z * sinA;
        float nz = y * sinA + z * cosA;
        y = ny;
        z = nz;
    }

    public void rotateY(float a) {
        rotateY((float) Math.cos(a), (float) Math.sin(a));
    }

    public void rotateY(float cosA, float sinA) {
        float nx = z * sinA + x * cosA;
        float nz = z * cosA - x * sinA;
        x = nx;
        z = nz;
    }

    public void rotateZ(float a) {
        rotateZ((float) Math.cos(a), (float) Math.sin(a));
    }

    public void rotateZ(float cosA, float sinA) {
        float nx = x * cosA - y * sinA;
        float ny = x * sinA + y * cosA;
        x = nx;
        y = ny;
    }

    public void setTo(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    @Override
    public String toString() {
        return "Vector3f{" + x + ", " + y + ", " + z + "}";
    }

}
