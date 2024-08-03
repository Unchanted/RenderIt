package com.arman.models;

import com.arman.geom.*;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

    private String name;
    private String fileName;

    private List<Vector3D> vertices;
    private List<Line3D> edges;
    private List<Polygon3D> faces;

    public Mesh() {
        this("unnamed");
    }

    public Mesh(String name) {
        this(name, new ArrayList<Vector3D>(), new ArrayList<Line3D>(), new ArrayList<Polygon3D>());
    }

    public Mesh(String name, List<Vector3D> vertices, List<Line3D> edges, List<Polygon3D> faces) {
        this.name = name;
        this.vertices = vertices;
        this.edges = edges;
        this.faces = faces;
    }

    public Mesh(String name, List<Polygon3D> faces) {
        this(name, new ArrayList<Vector3D>(), new ArrayList<Line3D>(), faces);
    }

    public List<Vector3D> getVertices() {
        return this.vertices;
    }

    public List<Line3D> getEdges() {
        return this.edges;
    }

    public List<Polygon3D> getFaces() {
        return this.faces;
    }

    public void addFace(Polygon3D poly) {
        this.faces.add(poly);
    }

    public void addEdge(Line3D l) {
        this.edges.add(l);
    }

    public void addVertex(Vector3D v) {
        this.vertices.add(v);
    }

    public void add(Vector3D v) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).add(v);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).add(v);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).add(v);
        }
    }

    public void subtract(Vector3D v) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).subtract(v);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).subtract(v);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).subtract(v);
        }
    }

    public void add(Rotation3D rotation) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).add(rotation);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).add(rotation);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).add(rotation);
        }
    }

    public void subtract(Rotation3D rotation) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).subtract(rotation);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).subtract(rotation);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).subtract(rotation);
        }
    }

    public void add(Transform3D transform) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).add(transform);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).add(transform);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).add(transform);
        }
    }

    public void subtract(Transform3D transform) {
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).subtract(transform);
        }
        for (int i = 0; i < this.edges.size(); i++) {
            this.edges.get(i).subtract(transform);
        }
        for (int i = 0; i < this.faces.size(); i++) {
            this.faces.get(i).subtract(transform);
        }
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }

}
