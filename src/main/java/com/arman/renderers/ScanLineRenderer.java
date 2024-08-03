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


public class ScanLineRenderer implements Renderer {

    private ActiveEdgeTable activeEdgeTable;
    private GlobalEdgeTable globalEdgeTable;
    private View view;
    private Camera camera;
    private boolean clearEveryFrame;

    public ScanLineRenderer(Camera camera, View view, boolean clearEveryFrame) {
        this.view = view;
        this.camera = camera;
        this.clearEveryFrame = clearEveryFrame;
    }

    public ScanLineRenderer(Camera camera, View view) {
        this(camera, view, true);
    }

    public ScanLineRenderer(Transform3D camera, View view, boolean clearEveryFrame) {
        this(new Camera(camera), view, clearEveryFrame);
    }

    public ScanLineRenderer(Transform3D camera, View view) {
        this(camera, view, true);
    }

    private void reset() {
        int height = this.view.getTop() + this.view.getHeight();
        this.globalEdgeTable = new GlobalEdgeTable(height);
        this.activeEdgeTable = new ActiveEdgeTable();
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
                drawNext(g, poly);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, Vector3D v) {
        g.drawLine((int) v.getX(), (int) v.getY(), (int) v.getX(), (int) v.getY());
    }

    @Override
    public void draw(Graphics2D g, Line3D l) {
        g.drawLine((int) l.get0().getX(), (int) l.get0().getY(), (int) l.get1().getX(), (int) l.get1().getY());
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
        reset();
        this.globalEdgeTable.set(poly);
        int y = (int) this.globalEdgeTable.getTopY();
        this.activeEdgeTable.set(y);
        while (y < this.globalEdgeTable.getBottomY()) {
            y = this.activeEdgeTable.getScanLine();
            this.activeEdgeTable.addAll(this.globalEdgeTable.get(y));
            this.activeEdgeTable.cleanUp(y);
            this.activeEdgeTable.sort();
            for (int i = 0; i < this.activeEdgeTable.size() - 1; i += 2) {
                float x1 = this.activeEdgeTable.get(i).getX();
                float x2 = this.activeEdgeTable.get(i + 1).getX();
                g.drawLine((int) x1, y, (int) x2, y);
            }
            this.activeEdgeTable.nextLine();
        }
    }

}
