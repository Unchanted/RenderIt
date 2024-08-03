package com.arman.renderers;

import com.arman.geom.Line3D;
import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;
import com.arman.models.Mesh;

import java.awt.*;

public class ZBufferRenderer implements Renderer {

    private ZBuffer zBuffer;

    public ZBufferRenderer(BoundingBox bounds) {
        this(bounds.width, bounds.height);
    }

    public ZBufferRenderer(float width, float height) {
        this.zBuffer = new ZBuffer((int) width, (int) height);
    }

    @Override
    public void before(Graphics2D g) {

    }

    @Override
    public void after(Graphics2D g) {

    }

    @Override
    public void draw(Graphics2D g, Polygon3D poly) {
        
    }

    @Override
    public void draw(Graphics2D g, Vector3D v) {

    }

    @Override
    public void draw(Graphics2D g, Line3D l) {

    }

    @Override
    public void draw(Graphics2D g, Mesh m) {

    }

}
