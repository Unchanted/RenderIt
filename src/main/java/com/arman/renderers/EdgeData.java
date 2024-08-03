package com.arman.renderers;

import com.arman.geom.Polygon3D;
import com.arman.models.Edge;

public class EdgeData {

    private Edge edge;
    private float currentX;
    private Polygon3D poly;

    public EdgeData(Edge edge, Polygon3D poly) {
        this.edge = edge;
        this.currentX = this.getTopX();
        this.poly = poly;
    }

    public float getTopY() {
        return this.edge.getTop().getY();
    }

    public float getTopX() {
        return this.edge.getTop().getX();
    }

    public float getBottomX() {
        return this.edge.getBottom().getX();
    }

    public float getX() {
        return this.currentX;
    }

    public void increment() {
        this.currentX += getXIncrement();
    }

    public float getBottomY() {
        return this.edge.getBottom().getY();
    }

    public float getXIncrement() {
        return 1 / this.edge.slope();
    }

    public float getLeft() {
        return this.edge.getLeft().getX();
    }

    public float getRight() {
        return this.edge.getRight().getX();
    }

    public boolean isValid() {
        return this.getLeft() <= this.getRight();
    }

    public boolean endsAt(int scanLine) {
        return this.getBottomY() == scanLine;
    }

    public void setToIntersect(int scanLine) {
        this.currentX = intersectX(scanLine);
    }

    public float intersectX(int scanLine) {
        float m = this.edge.slope();
        float b = this.getTopY();
        return (scanLine - b) / m;
    }

    @Override
    public String toString() {
        return this.edge.toString();
    }

}
