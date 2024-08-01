package com.arman.geom;

import org.jetbrains.annotations.NotNull;

public class Vector3D {

    private float x, y, z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(@NotNull Vector3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
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

    public void set(Vector3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void add(Vector3D v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public void subtract(Vector3D v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public void add(Rotation3D rotation) {
        rotateX(rotation.getCosAngleX(), rotation.getSinAngleX());
        rotateZ(rotation.getCosAngleZ(), rotation.getSinAngleZ());
        rotateY(rotation.getCosAngleY(), rotation.getSinAngleY());
    }

    public void subtract(Rotation3D rotation) {
        rotateY(rotation.getCosAngleY(), -rotation.getSinAngleY());
        rotateZ(rotation.getCosAngleZ(), -rotation.getSinAngleZ());
        rotateX(rotation.getCosAngleX(), -rotation.getSinAngleX());
    }

    public void add(Transform3D transform) {
        add(transform.getRotation());
        add(transform.getTranslation());
        multiply(transform.getScaleFactor());
    }

    public void subtract(Transform3D transform) {
        divide(transform.getScaleFactor());
        subtract(transform.getTranslation());
        subtract(transform.getRotation());
    }

    public void rotateX(float angle) {
        float cosAngle = (float) Math.cos(angle);
        float sinAngle = (float) Math.sin(angle);
        rotateX(cosAngle, sinAngle);
    }

    public void rotateX(float cosAngle, float sinAngle) {
        float tempY = this.y * cosAngle - this.z * sinAngle;
        float tempZ = this.y * sinAngle + this.z * cosAngle;
        this.y = tempY;
        this.z = tempZ;
    }

    public void rotateY(float angle) {
        float cosAngle = (float) Math.cos(angle);
        float sinAngle = (float) Math.sin(angle);
        rotateY(cosAngle, sinAngle);
    }

    public void rotateY(float cosAngle, float sinAngle) {
        float tempX = this.z * sinAngle + this.x * cosAngle;
        float tempZ = this.z * cosAngle - this.x * sinAngle;
        this.x = tempX;
        this.z = tempZ;
    }

    public void rotateZ(float angle) {
        float cosAngle = (float) Math.cos(angle);
        float sinAngle = (float) Math.sin(angle);
        rotateZ(cosAngle, sinAngle);
    }

    public void rotateZ(float cosAngle, float sinAngle) {
        float tempX = this.x * cosAngle - this.y * sinAngle;
        float tempY = this.x * sinAngle + this.y * cosAngle;
        this.x = tempX;
        this.y = tempY;
    }

    public void multiply(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
    }

    public void divide(float f) {
        this.x /= f;
        this.y /= f;
        this.z /= f;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3D normalized() {
        float l = length();
        return new Vector3D(this.x / l, this.y / l, this.z / l);
    }

    public void normalize() {
        this.divide(length());
    }

    public void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
    }

    public float dot(Vector3D v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    @Override
    public String toString() {
        return "Vector3D={" + this.x + ", " + this.y + ", " + this.z + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector3D) {
            Vector3D v = (Vector3D) o;
            return this.x == v.x && this.y == v.y && this.z == v.z;
        }
        return false;
    }

}
