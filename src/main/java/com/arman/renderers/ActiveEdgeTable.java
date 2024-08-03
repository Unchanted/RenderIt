package com.arman.renderers;

import com.arman.geom.Polygon3D;
import com.arman.models.Edge;

import java.util.*;

public class ActiveEdgeTable {

    private List<EdgeData> activeEdges;
    private int scanLine;

    public ActiveEdgeTable() {
        this(0);
    }

    public ActiveEdgeTable(int scanLine) {
        this.activeEdges = new LinkedList<>();
        this.set(scanLine);
    }

    public void nextLine() {
        this.scanLine++;
        for (int i = 0; i < this.activeEdges.size(); i++) {
            this.activeEdges.get(i).increment();
        }
    }

    public void set(int scanLine) {
        this.scanLine = scanLine;
        for (int i = 0; i < this.activeEdges.size(); i++) {
            this.activeEdges.get(i).setToIntersect(scanLine);
        }
    }

    public void cleanUp(int bottomY) {
        for (int i = 0; i < this.activeEdges.size(); i++) {
            EdgeData ae = this.activeEdges.get(i);
            if (ae.endsAt(bottomY)) {
                this.activeEdges.remove(i);
                i--;
            }
        }
    }

    public void add(EdgeData edge) {
        this.activeEdges.add(edge);
        for (int i = 0; i < this.activeEdges.size(); i++) {
            this.activeEdges.get(i).setToIntersect(scanLine);
        }
    }

    public void add(Edge edge, Polygon3D poly) {
        this.add(new EdgeData(edge, poly));
        for (int i = 0; i < this.activeEdges.size(); i++) {
            this.activeEdges.get(i).setToIntersect(scanLine);
        }
    }

    public void addAll(Collection<? extends Edge> edges, Polygon3D poly) {
        for (Edge e : edges) {
            this.add(e, poly);
        }
    }

    public void addAll(Collection<? extends EdgeData> edges) {
        this.activeEdges.addAll(edges);
    }

    public int size() {
        return this.activeEdges.size();
    }

    public boolean isValid() {
        for (int i = 0; i < this.activeEdges.size(); i++) {
            if (this.activeEdges.get(i).isValid()) {
                return true;
            }
        }
        return false;
    }

    public EdgeData get(int index) {
        return this.activeEdges.get(index);
    }

    public void sort() {
        Collections.sort(this.activeEdges, new Comparator<EdgeData>() {
            @Override
            public int compare(EdgeData o1, EdgeData o2) {
                float x1 = o1.getX();
                float x2 = o2.getX();
                if (x1 < x2) {
                    return -1;
                } else if (x1 > x2) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public boolean isEmpty() {
        return this.activeEdges.isEmpty();
    }

    public int getScanLine() {
        return this.scanLine;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < this.activeEdges.size(); i++) {
            res += this.scanLine + ": ";
            res += this.activeEdges.get(i).toString();
            res += " ";
        }
        res = res.trim();
        res += "\n";
        return res;
    }

}
