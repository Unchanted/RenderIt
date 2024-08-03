package com.arman.renderers;

import com.arman.geom.Line3D;
import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;
import com.arman.models.Mesh;

import java.awt.*;

public interface Renderer {

    void before(Graphics2D g);

    void after(Graphics2D g);

    void draw(Graphics2D g, Polygon3D poly);

    void draw(Graphics2D g, Vector3D v);

    void draw(Graphics2D g, Line3D l);

    void draw(Graphics2D g, Mesh m);

}
