package com.arman.geom;

import org.jetbrains.annotations.NotNull;

public class Quaternion {

    private float x, y, z, w;

    public Quaternion() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public Quaternion(float angle, Vector3D axis) {
        this.set(angle, axis);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(@NotNull Quaternion quaternion) {
        this.set(quaternion);
    }

    public void set(Quaternion quaternion) {
        this.x = quaternion.x;
        this.y = quaternion.y;
        this.z = quaternion.z;
        this.w = quaternion.w;
    }

    public void set(float angle, Vector3D axis) {
        this.x = (float) (axis.getX() * Math.sin(angle / 2));
        this.y = (float) (axis.getY() * Math.sin(angle / 2));
        this.z = (float) (axis.getZ() * Math.sin(angle / 2));
        this.w = (float) Math.cos(angle / 2);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public Quaternion normalized() {
        float l = length();
        return new Quaternion(this.x / l, this.y / l, this.z / l, this.w / l);
    }

    public void normalize() {
        float l = length();
        this.x /= l;
        this.y /= l;
        this.z /= l;
        this.w /= l;
    }

    public void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public Vector3D transform(Vector3D v) {
        Quaternion q1 = new Quaternion(this);
        q1.conjugate();
        Quaternion q2 = new Quaternion(v.getX(), v.getY(), v.getZ(), 0);
        Quaternion q3 = this.multiplied(q2.multiplied(q1));
        v.setX(q1.x);
        v.setY(q1.y);
        v.setZ(q1.z);
        return v;
    }

    public void multiply(Quaternion q) {
        float newX = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        float newY = this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z;
        float newZ = this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x;
        float newW = this.w * q.w + this.x * q.x + this.y * q.y - this.z * q.z;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.w = newW;
    }

    public Quaternion multiplied(Quaternion q) {
        float newX = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        float newY = this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z;
        float newZ = this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x;
        float newW = this.w * q.w + this.x * q.x + this.y * q.y - this.z * q.z;
        return new Quaternion(newX, newY, newZ, newW);
    }

    public void add(Quaternion q) {
        this.x += q.x;
        this.y += q.y;
        this.z += q.z;
        this.w += q.w;
    }

    public void add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }

    public void subtract(Quaternion q) {
        this.x -= q.x;
        this.y -= q.y;
        this.z -= q.z;
        this.w -= q.w;
    }

    public void subtract(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
    }

    @NotNull
    public static Quaternion identity() {
        return new Quaternion(0, 0, 0, 1);
    }

    public float dot(Quaternion q) {
        return this.x * q.x + this.y * q.y + this.z * q.z + this.w * q.w;
    }

    public void multiply(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        this.w *= f;
    }

    public void divide(float f) {
        this.x /= f;
        this.y /= f;
        this.z /= f;
        this.w /= f;
    }

}
