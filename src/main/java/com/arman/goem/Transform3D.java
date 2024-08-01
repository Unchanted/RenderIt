package com.arman.geom;

import org.jetbrains.annotations.NotNull;

public class Transform3D {

    private Vector3D translation;
    private float scaleFactor;
    private Rotation3D rotation;

    public Transform3D() {
        this(new Vector3D());
    }

    public Transform3D(Vector3D translation) {
        this(translation, 1.0f);
    }

    public Transform3D(float scaleFactor) {
        this(new Vector3D(), scaleFactor);
    }

    public Transform3D(Vector3D translation, float scaleFactor) {
        this(translation, scaleFactor, new Rotation3D());
    }

    public Transform3D(float angleX, float angleY, float angleZ) {
        this(new Vector3D(), angleX, angleY, angleZ);
    }

    public Transform3D(@NotNull Rotation3D rotation) {
        this(new Vector3D(), rotation);
    }

    public Transform3D(Vector3D translation, float angleX, float angleY, float angleZ) {
        this(translation, 1.0f, angleX, angleY, angleZ);
    }

    public Transform3D(float scaleFactor, float angleX, float angleY, float angleZ) {
        this(new Vector3D(), scaleFactor, angleX, angleY, angleZ);
    }

    public Transform3D(float scaleFactor, @NotNull Rotation3D rotation) {
        this(new Vector3D(), scaleFactor, rotation);
    }

    public Transform3D(Vector3D translation, @NotNull Rotation3D rotation) {
        this(new Vector3D(), 1.0f, rotation);
    }

    public Transform3D(Vector3D translation, float scaleFactor, float angleX, float angleY, float angleZ) {
        this(translation, scaleFactor, (float) Math.cos(angleX), (float) Math.sin(angleX), (float) Math.cos(angleY),
                (float) Math.sin(angleY), (float) Math.cos(angleZ), (float) Math.sin(angleZ));
    }

    public Transform3D(Vector3D translation, float scaleFactor, float cosAngleX, float sinAngleX,
                       float cosAngleY, float sinAngleY, float cosAngleZ, float sinAngleZ) {
        this.translation = translation;
        this.scaleFactor = scaleFactor;
        this.rotation = new Rotation3D(cosAngleX, sinAngleX, cosAngleY, sinAngleY, cosAngleZ, sinAngleZ);
    }

    public Transform3D(Vector3D translation, float scaleFactor, Rotation3D rotation) {
        this.translation = translation;
        this.scaleFactor = scaleFactor;
        this.rotation = rotation;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public Vector3D getTranslation() {
        return translation;
    }

    public Rotation3D getRotation() {
        return rotation;
    }

    public void translate(Vector3D v) {
        this.translation.add(v);
    }

}
