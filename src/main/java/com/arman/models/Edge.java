package com.arman.models;

import com.arman.geom.Line3D;
import com.arman.geom.Vector3D;

import java.awt.*;

public class Edge extends Line3D {

    private Color color;

    public Edge() {
        this(Color.WHITE);
    }

    public Edge(Color color) {
        super();
        this.color = color;
    }

    public Edge(int red, int green, int blue) {
        this(new Color(red, green, blue));
    }

    public Edge(Vector3D v1, Vector3D v2, Color color) {
        super(v1, v2);
        this.color = color;
    }

    public Edge(Vector3D v1, Vector3D v2) {
        this(v1, v2, Color.WHITE);
    }

    public Edge(Edge e) {
        this.set(e);
    }

    public void set(Line3D l) {
        super.set(l);
        if (l instanceof Edge) {
            this.color = ((Edge) l).color;
        } else {
            this.color = Color.WHITE;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
