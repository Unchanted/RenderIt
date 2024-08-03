package com.arman.models;

import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;

import java.awt.*;

public class Face extends Polygon3D {

    private Color color;

    public Face() {
        this(Color.WHITE);
    }

    public Face(Color color) {
        super();
        this.color = color;
    }

    public Face(int red, int green, int blue) {
        this(new Color(red, green, blue));
    }

    public Face(Vector3D... vertices) {
        this(Color.WHITE, vertices);
    }

    public Face(Color color, Vector3D... vertices) {
        super(vertices);
        this.color = color;
    }

    public Face(Polygon3D poly) {
        this.set(poly);
    }

    public void set(Polygon3D poly) {
        super.set(poly);
        if (poly instanceof Face) {
            this.color = ((Face) poly).color;
        } else {
            this.color = Color.WHITE;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
