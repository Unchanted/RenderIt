package com.arman.renderers;

import com.arman.geom.Polygon3D;
import com.arman.models.Edge;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GlobalEdgeTable {

    private List<EdgeData>[] globalEdges;
    private Polygon3D poly;

    public GlobalEdgeTable(int height) {
        this(height, null);
    }

    public GlobalEdgeTable(int height, Polygon3D poly) {
        this.set(height, poly);
    }

    public void set(Polygon3D poly) {
        this.poly = poly;
        for (Edge edge : poly.edges()) {
            this.add(edge);
        }
    }

    public void set(int height, Polygon3D poly) {
        this.globalEdges = new LinkedList[height];
        for (int i = 0; i < this.globalEdges.length; i++) {
            this.globalEdges[i] = new LinkedList<>();
        }
        if (poly != null) {
            this.set(poly);
        }
    }

    public float getTopY() {
        for (int i = 0; i < this.globalEdges.length; i++) {
            if (!this.globalEdges[i].isEmpty()) {
                return i;
            }
        }
        return this.height();
    }

    public float getBottomY() {
        for (int i = this.globalEdges.length - 1; i >= 0; i--) {
            if (!this.globalEdges[i].isEmpty()) {
                return i;
            }
        }
        return 0;
    }

    public void add(Edge edge) {
        EdgeData ed = new EdgeData(edge, this.poly);
        this.globalEdges[(int) ed.getTopY()].add(ed);
    }

    public void addAll(Collection<? extends Edge> edges) {
        for (Edge e : edges) {
            this.add(e);
        }
    }

    public List<EdgeData> get(int topY) {
        return this.globalEdges[topY];
    }

    public int height() {
        return this.globalEdges.length;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < this.globalEdges.length; i++) {
            for (int j = 0; j < this.globalEdges[i].size(); j++) {
                res += i + ": ";
                res += this.globalEdges[i].get(j).toString();
                res += " ";
            }
            if (!this.globalEdges[i].isEmpty()) {
                res += "\n";
            }
        }
        return res;
    }

}
