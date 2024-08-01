package com.arman.geom;

import com.arman.main.View;
import com.arman.models.Edge;

public class Polygon3D {

    protected Vector3D[] vertices;

    public Polygon3D() {
        this.vertices = new Vector3D[0];
    }

    public Polygon3D(Vector3D... vertices) {
        this.vertices = vertices;
    }

    public Polygon3D(Polygon3D p) {
        this.set(p);
    }

    public void project(View view) {
        for (int i = 0; i < this.vertexCount(); i++) {
            view.project(this.vertices[i]);
        }
    }

    public Vector3D normal() {
        Vector3D u = new Vector3D(vertices[2]);
        u.subtract(vertices[1]);
        Vector3D v = new Vector3D(vertices[0]);
        v.subtract(vertices[1]);
        Vector3D normal = u.cross(v);
        normal.normalize();
        return normal;
    }

    public Vector3D get(int index) {
        return this.vertices[index];
    }

    public Edge[] edges() {
        Edge[] edges = new Edge[this.vertexCount()];
        for (int i = 0; i < this.vertexCount(); i++) {
            edges[i] = new Edge(this.vertices[i], this.vertices[(i + 1) % this.vertexCount()]);
        }
        return edges;
    }

    public void scale(float scaleFactor) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].multiply(scaleFactor);
        }
    }

    public void addVertex(int index, Vector3D vertex) {
        Vector3D[] newVertices = new Vector3D[this.vertices.length + 1];
        newVertices[index] = vertex;
        for (int i = 0; i < index; i++) {
            newVertices[i] = this.vertices[i];
        }
        for (int i = index + 1; i < newVertices.length; i++) {
            newVertices[i] = this.vertices[i - 1];
        }
        this.vertices = newVertices;
    }

    public void addVertex(int index, float x, float y, float z) {
        this.addVertex(index, new Vector3D(x, y, z));
    }

    public void addVertex(Vector3D vertex) {
        addVertex(this.vertices.length, vertex);
    }

    public void removeVertex(int index) {
        Vector3D[] newVertices = new Vector3D[this.vertices.length - 1];
        for (int i = 0; i < index; i++) {
            newVertices[i] = this.vertices[i];
        }
        for (int i = index + 1; i < newVertices.length; i++) {
            newVertices[i - 1] = this.vertices[i];
        }
    }

    public void add(Vector3D v) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].add(v);
        }
    }

    public void subtract(Vector3D v) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].subtract(v);
        }
    }

    public void add(Rotation3D rotation) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].add(rotation);
        }
    }

    public void subtract(Rotation3D rotation) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].subtract(rotation);
        }
    }

    public void add(Transform3D transform) {
        add(transform.getRotation());
        add(transform.getTranslation());
        scale(transform.getScaleFactor());
    }

    public void subtract(Transform3D transform) {
        scale(1 / transform.getScaleFactor());
        subtract(transform.getTranslation());
        subtract(transform.getRotation());
    }

    public void set(Polygon3D p) {
        this.vertices = new Vector3D[p.vertices.length];
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i] = new Vector3D(p.vertices[i]);
        }
    }

    public int vertexCount() {
        return this.vertices.length;
    }

    public boolean isConvex() {
        int n = this.vertexCount();
        if (n <= 3) {
            return true;
        }
        boolean sign = false;
        for (int i = 0; i < n; i++) {
            Vector3D v1 = this.vertices[i];
            Vector3D v2 = this.vertices[(i + 1) % n];
            Vector3D v3 = this.vertices[(i + 2) % n];
            double dx1 = v3.getX() - v2.getX();
            double dy1 = v3.getY() - v2.getY();
            double dx2 = v1.getX() - v2.getX();
            double dy2 = v1.getY() - v2.getY();
            double cross = dx1 * dy2 - dy1 * dx2;
            if (i == 0) {
                sign = cross > 0;
            } else if (sign != (cross > 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean isTriangle() {
        return this.vertexCount() == 3;
    }

    public boolean isFacing(Vector3D v) {
        Vector3D u = new Vector3D(v);
        u.subtract(this.vertices[0]);
        Vector3D normal1 = normal();
        Vector3D normal2 = new Vector3D(normal1);
        normal1.setZ(-Math.abs(normal1.getZ()));
        normal2.setZ(Math.abs(normal2.getZ()));
        return (normal1.dot(u) >= 0 || normal2.dot(u) >= 0);
    }

    public boolean clip(float clipZ) {
        boolean visible = false;
        for (int i = 0; i < this.vertexCount(); i++) {
            int j = (i + 1) % this.vertexCount();
            Vector3D v1 = this.vertices[i];
            Vector3D v2 = this.vertices[j];
            if (v1.getZ() > clipZ) {
                visible = true;
            }
            if (v1.getZ() < v2.getZ()) {
                Vector3D temp = v1;
                v1 = v2;
                v2 = temp;
            }
            if (v1.getZ() > clipZ && v2.getZ() < clipZ) {
                float scale = (clipZ - v1.getZ()) / (v2.getZ() - v1.getZ());
                addVertex(j, v1.getX() + scale * (v2.getX() - v1.getX()), v1.getY() + scale * (v2.getY() - v1.getY()), clipZ);
                i++;
            }
        }
        if (!visible) {
            return false;
        }
        for (int i = this.vertexCount() - 1; i >= 0; i--) {
            if (this.vertices[i].getZ() < clipZ) {
                removeVertex(i);
            }
        }
        return this.vertexCount() >= 3;
    }

    public Rectangle3D getBounds() {
        return null;
    }

    @Override
    public String toString() {
        String res = "Polygon3D={";
        for (int i = 0; i < vertexCount(); i++) {
            res += vertices[i].toString();
        }
        res += "}";
        return res;
    }

}
