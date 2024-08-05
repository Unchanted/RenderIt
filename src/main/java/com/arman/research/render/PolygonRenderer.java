package com.arman.research.render;

import com.arman.research.geom.polygons.Polygon3f;
import com.arman.research.geom.transforms.Transform3f;

import java.awt.*;

public abstract class PolygonRenderer implements Renderer {

    private ScanConverter scanConverter;
    private Transform3f camera;
    private View view;
    private boolean clearEveryFrame;

    public PolygonRenderer(Transform3f camera, View view) {
        this(camera, view, true);
    }

    public PolygonRenderer(Transform3f camera, View view, boolean clearEveryFrame) {
        this.camera = camera;
        this.view = view;
        this.clearEveryFrame = clearEveryFrame;
        scanConverter = new ScanConverter(view);
    }

    public Transform3f getCamera() {
        return camera;
    }

    public void before(Graphics2D g) {
        if (clearEveryFrame) {
            g.setColor(Color.BLACK);
            g.fillRect(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        }
    }

    public void after(Graphics2D g) {

    }

    public boolean draw(Graphics2D g, Polygon3f p) {
        if (p.isFacing(camera.getLocation())) {
            Polygon3f dp = new Polygon3f(p);
            dp.subtract(camera);
            boolean visible = dp.clip(-1);
            if (visible) {
                dp.project(view);
                visible = scanConverter.convert(dp);
                if (visible) {
                    drawCurrentPolygon(g, p);
                    return true;
                }
            }
        }
        return false;
    }

    public abstract void drawCurrentPolygon(Graphics2D g, Polygon3f p);

    public ScanConverter getScanConverter() {
        return scanConverter;
    }

}
