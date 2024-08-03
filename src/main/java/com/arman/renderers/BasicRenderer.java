package com.arman.renderers;

import com.arman.geom.Line3D;
import com.arman.geom.Polygon3D;
import com.arman.geom.Transform3D;
import com.arman.geom.Vector3D;
import com.arman.main.Camera;
import com.arman.main.View;
import com.arman.models.Mesh;

import java.awt.*;
import java.util.List;

public class BasicRenderer implements Renderer {

    protected ScanConverter scanConverter;
    protected View view;
    protected Camera camera;
    protected boolean clearEveryFrame;

    public BasicRenderer(Camera camera, View view) {
        this(camera, view, true);
    }

    public BasicRenderer(Camera camera, View view, boolean clearEveryFrame) {
        this.view = view;
        this.camera = camera;
        this.scanConverter = new ScanConverter(view.getBounds());
        this.clearEveryFrame = clearEveryFrame;
    }

    public BasicRenderer(Transform3D camera, View view) {
        this(camera, view, true);
    }

    public BasicRenderer(Transform3D camera, View view, boolean clearEveryFrame) {
        this(new Camera(camera), view, clearEveryFrame);
    }

    @Override
    public void before(Graphics2D g) {
        if (clearEveryFrame) {
            g.setColor(Color.BLACK);
            g.fillRect(view.getLeft(), view.getTop(), view.getWidth(), view.getHeight());
        }
    }

    @Override
    public void after(Graphics2D g) {

    }

    @Override
    public void draw(Graphics2D g, Polygon3D poly) {
        if (poly.isFacing(camera.getLocation())) {
            Polygon3D drawPoly = new Polygon3D(poly);
            drawPoly.subtract(camera.getTransform());
            boolean visible = drawPoly.clip(1);
            if (visible) {
//                drawPoly.project(this.view);
                visible = this.scanConverter.convert(drawPoly);
                if (visible) {
                    drawNext(g, poly);
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g, Vector3D v) {

    }

    @Override
    public void draw(Graphics2D g, Line3D l) {

    }

    @Override
    public void draw(Graphics2D g, Mesh m) {
        List<Polygon3D> faces = m.getFaces();
        for (int i = 0; i < faces.size(); i++) {
            Polygon3D poly = faces.get(i);
            draw(g, poly);
        }
    }

    protected void drawNext(Graphics2D g, Polygon3D poly) {
        g.setColor(Color.WHITE);
        int y = this.scanConverter.getTop();
        while (y <= this.scanConverter.getBottom()) {
            ScanLine scanLine = this.scanConverter.getScanLine(y);
            if (scanLine.isValid()) {
                g.drawLine(scanLine.getLeft(), y, scanLine.getRight(), y);
            }
            y++;
        }
    }

}
