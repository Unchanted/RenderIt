package com.arman.research.geom.polygons;

import com.arman.research.geom.vectors.Vector3f;

import java.awt.*;

public class SolidPolygon3f extends Polygon3f {

    public static final Color STANDARD_COLOR = new Color(0, 255, 0, 100);

    private Color color = STANDARD_COLOR;

    public SolidPolygon3f() {
        super();
    }

    public SolidPolygon3f(Vector3f... vertices) {
        super(vertices);
    }

    public void setTo(Polygon3f p) {
        super.setTo(p);
        if (p instanceof SolidPolygon3f) {
            color = ((SolidPolygon3f) p).color;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
