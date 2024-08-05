package com.arman.research.core;

import com.arman.research.geom.polygons.Polygon3f;
import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.render.PolygonRenderer;
import com.arman.research.render.SolidPolygonRenderer;
import com.arman.research.render.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Core3D extends Core {

    private PolygonRenderer polygonRenderer;
    private View view;
    private List<Polygon3f> polygons;
    private boolean drawFrameRate = false;
    private boolean drawInstructions = true;
    private int numFrames;
    private long startTime;
    private float frameRate;

    public void init(DisplayMode[] modes) {
        super.init(modes);
        createPolygonRenderer();
        polygons = new ArrayList<>();
        createPolygons();
    }

    public void addPolygon(Polygon3f p) {
        polygons.add(p);
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setPolygonRenderer(PolygonRenderer polygonRenderer) {
        this.polygonRenderer = polygonRenderer;
    }

    public abstract void createPolygons();

    public void createPolygonRenderer() {
        view = new View(0, 0, getScreen().getWidth(), getScreen().getHeight(), (float) Math.toRadians(75));
        Transform3f camera = new Transform3f(0, 100, 0);
        polygonRenderer = new SolidPolygonRenderer(camera, view);
    }

    public void setViewBounds(int width, int height) {
        width = Math.min(width, getScreen().getWidth());
        height = Math.min(height, getScreen().getHeight());
        width = Math.max(64, width);
        height = Math.max(48, height);
        view.setBounds((getScreen().getWidth() - width) / 2, (getScreen().getHeight() - height) / 2, width, height);
        for (int i = 0; i < 2; i++) {
            Graphics2D g = getScreen().getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getScreen().getWidth(), getScreen().getHeight());
            getScreen().update();
        }
    }

    public void update(long elapsedTime) {

    }

    public void draw(Graphics2D g) {
        int viewX1 = view.getX();
        int viewY1 = view.getY();
        int viewX2 = viewX1 + view.getWidth();
        int viewY2 = viewY1 + view.getHeight();
        if (viewX1 != 0 || viewY1 != 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, viewX1, getScreen().getHeight());
            g.fillRect(viewX2, 0, getScreen().getWidth() - viewX2, getScreen().getHeight());
            g.fillRect(viewX1, 0, view.getWidth(), viewY1);
            g.fillRect(viewX1, viewY2, view.getWidth(), getScreen().getHeight() - viewY2);
        }
        drawPolygons(g);
        drawText(g);
    }

    public void drawPolygons(Graphics2D g) {
        polygonRenderer.before(g);
        for (int i = 0; i < polygons.size(); i++) {
            polygonRenderer.draw(g, polygons.get(i));
        }
        polygonRenderer.after(g);
    }

    public void drawText(Graphics2D g) {
        g.setColor(Color.WHITE);
        if (drawInstructions) {
            g.drawString("Use the mouse/arrow keys to move. " + "Press Esc to exit.", 5, getFontSize());
        }
        if (drawFrameRate) {
            calcFrameRate();
            g.drawString(frameRate + " frames/sec", 5, getScreen().getHeight() - 5);
        }
    }

    public void calcFrameRate() {
        numFrames++;
        long currTime = System.currentTimeMillis();
        if (currTime > startTime + 500) {
            frameRate = (float) numFrames * 1000 / (currTime - startTime);
            startTime = currTime;
            numFrames = 0;
        }
    }

}
