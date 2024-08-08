
package com.arman.renderers;

import com.arman.geom.Polygon3D;
import com.arman.models.Edge;

import java.util.*;

public class ActiveEdgeTable {

    private final List<EdgeData> activeEdges;
    private int scanLine;

    public ActiveEdgeTable() {
        this(0);
    }

    public ActiveEdgeTable(int scanLine) {
        this.activeEdges = new LinkedList<>();
        set(scanLine);
    }

    public void nextLine() {
        scanLine++;
        activeEdges.forEach(EdgeData::increment);
    }

    public void set(int scanLine) {
        this.scanLine = scanLine;
        activeEdges.forEach(edge -> edge.setToIntersect(scanLine));
    }

    public void cleanUp(int bottomY) {
        activeEdges.removeIf(edge -> edge.endsAt(bottomY));
    }

    public void add(EdgeData edge) {
        activeEdges.add(edge);
        edge.setToIntersect(scanLine);
    }

    public void add(Edge edge, Polygon3D poly) {
        add(new EdgeData(edge, poly));
    }

    public void addAll(Collection<? extends Edge> edges, Polygon3D poly) {
        edges.forEach(edge -> add(new EdgeData(edge, poly)));
    }

    public void addAll(Collection<? extends EdgeData> edges) {
        activeEdges.addAll(edges);
    }

    public int size() {
        return activeEdges.size();
    }

    public boolean isValid() {
        return activeEdges.stream().anyMatch(EdgeData::isValid);
    }

    public EdgeData get(int index) {
        return activeEdges.get(index);
    }

    public void sort() {
        activeEdges.sort(Comparator.comparingDouble(EdgeData::getX));
    }

    public boolean isEmpty() {
        return activeEdges.isEmpty();
    }

    public int getScanLine() {
        return scanLine;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (EdgeData edge : activeEdges) {
            res.append(scanLine).append(": ").append(edge).append(" ");
        }
        return res.toString().trim() + "\n";
    }
}

