package com.arman.geom;

public class Rotation3D {

    private float cosAngleX, sinAngleX;
    private float cosAngleY, sinAngleY;
    private float cosAngleZ, sinAngleZ;

    public Rotation3D() {
        this(0.0f, 0.0f, 0.0f);
    }

    public Rotation3D(float angleX, float angleY, float angleZ) {
        this((float) Math.cos(angleX), (float) Math.sin(angleX), (float) Math.cos(angleY),
                (float) Math.sin(angleY), (float) Math.cos(angleZ), (float) Math.sin(angleZ));
    }

    public Rotation3D(float cosAngleX, float sinAngleX, float cosAngleY, float sinAngleY, float cosAngleZ, float sinAngleZ) {
        this.cosAngleX = cosAngleX;
        this.sinAngleX = sinAngleX;
        this.cosAngleY = cosAngleY;
        this.sinAngleY = sinAngleY;
        this.cosAngleZ = cosAngleZ;
        this.sinAngleZ = sinAngleZ;
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

    public void setAngleX(float angle) {
        this.cosAngleX = (float) Math.cos(angle);
        this.sinAngleX = (float) Math.sin(angle);
    }

    public void setAngleY(float angle) {
        this.cosAngleY = (float) Math.cos(angle);
        this.sinAngleY = (float) Math.sin(angle);
    }

    public void setAngleZ(float angle) {
        this.cosAngleZ = (float) Math.cos(angle);
        this.sinAngleZ = (float) Math.sin(angle);
    }

    public void setAngle(float angleX, float angleY, float angleZ) {
        this.setAngleX(angleX);
        this.setAngleY(angleY);
        this.setAngleZ(angleZ);
    }

    public void setAngle(float cosAngleX, float sinAngleX, float cosAngleY, float sinAngleY, float cosAngleZ, float sinAngleZ) {
        this.cosAngleX = cosAngleX;
        this.sinAngleX = sinAngleX;
        this.cosAngleY = cosAngleY;
        this.sinAngleY = sinAngleY;
        this.cosAngleZ = cosAngleZ;
        this.sinAngleZ = sinAngleZ;
    }

    public void set(Rotation3D rotation) {
        this.setAngleX(rotation.getAngleX());
        this.setAngleY(rotation.getAngleY());
        this.setAngleZ(rotation.getAngleZ());
    }

    public float getCosAngleX() {
        return cosAngleX;
    }

    public void setCosAngleX(float cosAngleX) {
        this.cosAngleX = cosAngleX;
    }

    public float getSinAngleX() {
        return sinAngleX;
    }

    public void setSinAngleX(float sinAngleX) {
        this.sinAngleX = sinAngleX;
    }

    public float getCosAngleY() {
        return cosAngleY;
    }

    public void setCosAngleY(float cosAngleY) {
        this.cosAngleY = cosAngleY;
    }

    public float getSinAngleY() {
        return sinAngleY;
    }

    public void setSinAngleY(float sinAngleY) {
        this.sinAngleY = sinAngleY;
    }

    public float getCosAngleZ() {
        return cosAngleZ;
    }

    public void setCosAngleZ(float cosAngleZ) {
        this.cosAngleZ = cosAngleZ;
    }

    public float getSinAngleZ() {
        return sinAngleZ;
    }

    public void setSinAngleZ(float sinAngleZ) {
        this.sinAngleZ = sinAngleZ;
    }

    public void rotateX(float angle) {
        if (angle != 0) {
            this.setAngleX(this.getAngleX() + angle);
        }
    }

    public void rotateY(float angle) {
        if (angle != 0) {
            this.setAngleY(this.getAngleY() + angle);
        }
    }

    public void rotateZ(float angle) {
        if (angle != 0) {
            this.setAngleZ(this.getAngleZ() + angle);
        }
    }

    public void rotate(float angleX, float angleY, float angleZ) {
        this.rotateX(angleX);
        this.rotateY(angleY);
        this.rotateZ(angleZ);
    }

}
