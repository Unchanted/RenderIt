package com.arman.research.render;

import com.arman.research.geom.polygons.Polygon3f;

import java.awt.*;

public interface Renderer {

    boolean draw(Graphics2D g, Polygon3f p);

}
