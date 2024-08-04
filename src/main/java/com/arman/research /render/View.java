package com.arman.research.render;

import com.arman.research.geom.vectors.Vector3f;

import java.awt.*;

public class View {

    private Rectangle bounds;
    private float angle;
    private float distanceToCamera;

    public View(int x, int y, int width, int height, float angle) {
        bounds = new Rectangle();
        this.angle = angle;
        setBounds(x, y, width, height);
    }

    public void setBounds(int x, int y, int width, int height) {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
        distanceToCamera = (bounds.width / 2) / (float) Math.tan(angle / 2);
    }

    public void setAngle(float angle) {
        this.angle = angle;
        distanceToCamera = (bounds.width / 2) / (float) Math.tan(angle / 2);
    }

    public float getAngle() {
        return angle;
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public int getY() {
        return bounds.y;
    }

    public int getX() {
        return bounds.x;
    }

    public float getDistanceToCamera() {
        return distanceToCamera;
    }

    public float worldXToScreenX(float x) {
        return x + bounds.x + bounds.width / 2;
    }

    public float worldYToScreenY(float y) {
        return -y + bounds.y + bounds.height / 2;
    }

    public float screenXToWorldX(float x) {
        return x - bounds.x - bounds.width / 2;
    }

    public float screenYToWorldY(float y) {
        return -y + bounds.y + bounds.height / 2;
    }

    public void project(Vector3f v) {
        v.setX(distanceToCamera * v.getX() / -v.getZ());
        v.setY(distanceToCamera * v.getY() / -v.getZ());
        v.setX(worldXToScreenX(v.getX()));
        v.setY(worldYToScreenY(v.getY()));
    }

}
