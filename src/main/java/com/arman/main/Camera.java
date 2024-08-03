package com.arman.main;

import com.arman.geom.Transform3D;
import com.arman.geom.Vector3D;

public class Camera {

    private Transform3D transform;

    public Camera() {
        this(new Transform3D());
    }

    public Camera(float x, float y, float z) {
        this(new Transform3D(x, y, z));
    }

    public Camera(Transform3D transform) {
        this.transform = transform;
    }

    public Vector3D getLocation() {
        return this.transform.getTranslation();
    }

    public Transform3D getTransform() {
        return this.transform;
    }

}
