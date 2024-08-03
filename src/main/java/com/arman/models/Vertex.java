package com.arman.models;

import com.arman.geom.Vector3D;

import java.awt.*;

public class Vertex extends Vector3D {

    private Color color;

    public Vertex() {
        this(Color.WHITE);
    }

    public Vertex(Color color) {
        super();
        this.color = color;
    }

    public Vertex(int red, int green, int blue) {
        this(new Color(red, green, blue));
    }

    public Vertex(Vector3D v) {
        this(v, Color.WHITE);
    }

    public Vertex(Vector3D v, Color color) {
        super(v);
        this.color = color;
    }

    public Vertex(float x, float y, float z) {
        this(new Vector3D(x, y, z));
    }

    public Vertex(Vector3D vector, int red, int green, int blue) {
        this(vector, new Color(red, green, blue));
    }

    public Vertex(Vertex v) {
        super(v);
        this.color = v.color;
    }

    public void set(Vector3D v) {
        super.set(v);
        if (v instanceof Vertex) {
            this.color = ((Vertex) v).color;
        } else {
            this.color = Color.WHITE;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
