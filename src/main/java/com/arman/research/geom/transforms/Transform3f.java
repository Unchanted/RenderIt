package com.arman.research.geom.transforms;

import com.arman.research.geom.vectors.Vector3f;

public class Transform3f {

    private Vector3f location;
    private float cosAngleX;
    private float sinAngleX;
    private float cosAngleY;
    private float sinAngleY;
    private float cosAngleZ;
    private float sinAngleZ;

    public Transform3f() {
        this(0, 0, 0);
    }

    public Transform3f(float x, float y, float z) {
        location = new Vector3f(x, y, z);
        setAngle(0, 0, 0);
    }

    public Transform3f(Transform3f t) {
        location = new Vector3f();
        setTo(t);
    }

    public void setAngle(float ax, float ay, float az) {
        setAngleX(ax);
        setAngleY(ay);
        setAngleZ(az);
    }

    public void setAngleX(float a) {
        setCosAngleX((float) Math.cos(a));
        setSinAngleX((float) Math.sin(a));
    }

    public void setAngleY(float a) {
        setCosAngleY((float) Math.cos(a));
        setSinAngleY((float) Math.sin(a));
    }

    public void setAngleZ(float a) {
        setCosAngleZ((float) Math.cos(a));
        setSinAngleZ((float) Math.sin(a));
    }

    public void setCosAngleX(float a) {
        this.cosAngleX = a;
    }

    public void setSinAngleX(float a) {
        this.sinAngleX = a;
    }

    public void setCosAngleY(float a) {
        this.cosAngleY = a;
    }

    public void setSinAngleY(float a) {
        this.sinAngleY = a;
    }

    public void setCosAngleZ(float a) {
        this.cosAngleZ = a;
    }

    public void setSinAngleZ(float a) {
        this.sinAngleZ = a;
    }

    public void setTo(Transform3f t) {
        this.location.setTo(t.location);
        this.cosAngleX = t.cosAngleX;
        this.sinAngleX = t.sinAngleX;
        this.cosAngleY = t.cosAngleY;
        this.sinAngleY = t.sinAngleY;
        this.cosAngleZ = t.cosAngleZ;
        this.sinAngleZ = t.sinAngleZ;
    }

    public Vector3f getLocation() {
        return location;
    }

    public float getCosAngleX() {
        return cosAngleX;
    }

    public float getSinAngleX() {
        return sinAngleX;
    }

    public float getCosAngleY() {
        return cosAngleY;
    }

    public float getSinAngleY() {
        return sinAngleY;
    }

    public float getCosAngleZ() {
        return cosAngleZ;
    }

    public float getSinAngleZ() {
        return sinAngleZ;
    }

    public float getAngleX() {
        return (float) Math.atan2(sinAngleX, cosAngleX);
    }

    public float getAngleY() {
        return (float) Math.atan2(sinAngleY, cosAngleY);
    }

    public float getAngleZ() {
        return (float) Math.atan2(sinAngleZ, cosAngleZ);
    }

    public void rotateAngleX(float a) {
        if (a != 0) {
            setAngleX(getAngleX() + a);
        }
    }

    public void rotateAngleY(float a) {
        if (a != 0) {
            setAngleY(getAngleY() + a);
        }
    }

    public void rotateAngleZ(float a) {
        if (a != 0) {
            setAngleY(getAngleZ() + a);
        }
    }

    public void rotateAngle(float ax, float ay, float az) {
        rotateAngleX(ax);
        rotateAngleY(ay);
        rotateAngleZ(az);
    }

}
