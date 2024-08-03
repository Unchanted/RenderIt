package com.arman.projectors;

import com.arman.geom.Vector3D;

public class Orthographic extends Projection {

    private Vector3D minimum, maximum;

    public Orthographic(Vector3D minimum, Vector3D maximum) {
        this.setViewPort(minimum, maximum);
    }

    public void setViewPort(Vector3D minimum, Vector3D maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Vector3D project(Vector3D v) {
        float x = v.getX();
        float y = v.getY();
        float z = v.getZ();
        float l = minimum.getX();
        float r = maximum.getX();
        float b = minimum.getY();
        float t = maximum.getY();
        float n = minimum.getZ();
        float f = maximum.getZ();
        return new Vector3D((2 * x - (l + r)) / (r - 1),
                (2 * y - (t + b)) / (t - b), (-2 * z + (f + n)) / (f - n));
    }

}
