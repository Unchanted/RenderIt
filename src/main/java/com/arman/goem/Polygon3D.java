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
        for (Vector3D vertex : vertices) {
            view.project(vertex);
        }
    }

    public Vector3D normal() {
        Vector3D u = new Vector3D(vertices[2]).subtract(vertices[1]);
        Vector3D v = new Vector3D(vertices[0]).subtract(vertices[1]);
        return u.cross(v).normalize();
    }

    public Vector3D get(int index) {
        return vertices[index];
    }

    public Edge[] edges() {
        int count = vertexCount();
        Edge[] edges = new Edge[count];
        for (int i = 0; i < count; i++) {
            edges[i] = new Edge(vertices[i], vertices[(i + 1) % count]);
        }
        return edges;
    }

    public void scale(float scaleFactor) {
        for (Vector3D vertex : vertices) {
            vertex.multiply(scaleFactor);
        }
    }

    public void addVertex(int index, Vector3D vertex) {
        Vector3D[] newVertices = new Vector3D[vertices.length + 1];
        System.arraycopy(vertices, 0, newVertices, 0, index);
        newVertices[index] = vertex;
        System.arraycopy(vertices, index, newVertices, index + 1, vertices.length - index);
        vertices = newVertices;
    }

    public void addVertex(int index, float x, float y, float z) {
        addVertex(index, new Vector3D(x, y, z));
    }

    public void addVertex(Vector3D vertex) {
        addVertex(vertices.length, vertex);
    }

    public void removeVertex(int index) {
        Vector3D[] newVertices = new Vector3D[vertices.length - 1];
        System.arraycopy(vertices, 0, newVertices, 0, index);
        System.arraycopy(vertices, index + 1, newVertices, index, vertices.length - index - 1);
        vertices = newVertices;
    }

    public void add(Vector3D v) {
        for (Vector3D vertex : vertices) {
            vertex.add(v);
        }
    }

    public void subtract(Vector3D v) {
        for (Vector3D vertex : vertices) {
            vertex.subtract(v);
        }
    }

    public void add(Rotation3D rotation) {
        for (Vector3D vertex : vertices) {
            vertex.add(rotation);
        }
    }

    public void subtract(Rotation3D rotation) {
        for (Vector3D vertex : vertices) {
            vertex.subtract(rotation);
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
        for (int i = 0; i < p.vertices.length; i++) {
            this.vertices[i] = new Vector3D(p.vertices[i]);
        }
    }

    private int vertexCount() {
        return vertices.length;
    }
}
