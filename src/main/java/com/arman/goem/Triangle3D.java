package com.arman.geom;

public class Triangle3D extends Polygon3D {

    public Triangle3D(Vector3D v1, Vector3D v2, Vector3D v3) {
        this.vertices = new Vector3D[]{v1, v2, v3};
    }

    public Triangle3D(Triangle3D triangle) {
        this.set(triangle);
    }

    public void set(Triangle3D triangle) {
        this.vertices = new Vector3D[3];
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i] = new Vector3D(triangle.vertices[i]);
        }
    }

}
