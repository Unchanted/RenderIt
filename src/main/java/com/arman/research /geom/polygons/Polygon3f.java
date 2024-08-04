package com.arman.research.geom.polygons;

import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.Transformable;
import com.arman.research.geom.rectangles.Rectangle3f;
import com.arman.research.geom.vectors.Vector3f;
import com.arman.research.render.View;

public class Polygon3f implements Transformable {

    private static Vector3f temp1 = new Vector3f();
    private static Vector3f temp2 = new Vector3f();

    private Vector3f[] vertices;
    private int vertexCount;

    public Polygon3f() {
        vertexCount = 0;
        vertices = new Vector3f[0];
    }

    public Polygon3f(Vector3f... vectors) {
        this.vertices = vectors;
        vertexCount = vectors.length;
    }

    public Polygon3f(Polygon3f p) {
        setTo(p);
    }

    public void setTo(Polygon3f p) {
        vertexCount = p.vertexCount;
        vertices = new Vector3f[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            vertices[i] = new Vector3f(p.vertices[i]);
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Vector3f getVertex(int i) {
        return vertices[i];
    }

    public void project(View view) {
        for (int i = 0; i < vertexCount; i++) {
            view.project(vertices[i]);
        }
    }

    public Vector3f calcNormal() {
        temp1.setTo(vertices[2]);
        temp1.subtract(vertices[1]);
        temp2.setTo(vertices[0]);
        temp2.subtract(vertices[1]);
        Vector3f normal = new Vector3f(temp1.cross(temp2));
        normal.normalize();
        return normal;
    }

    @Override
    public void add(Vector3f v) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].add(v);
        }
    }

    @Override
    public void subtract(Vector3f v) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].subtract(v);
        }
    }

    @Override
    public void add(Transform3f t) {
        addRotation(t);
        add(t.getLocation());
    }

    @Override
    public void subtract(Transform3f t) {
        subtract(t.getLocation());
        subtractRotation(t);
    }

    @Override
    public void addRotation(Transform3f t) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].addRotation(t);
        }
    }

    @Override
    public void subtractRotation(Transform3f t) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].subtractRotation(t);
        }
    }

    public boolean isFacing(Vector3f v) {
        temp1.setTo(v);
        temp1.subtract(vertices[0]);
        return calcNormal().dot(temp1) >= 0;
    }

    public boolean clip(float clipZ) {
        Vector3f[] newVertices = new Vector3f[3 * vertexCount];
        System.arraycopy(vertices, 0, newVertices, 0, vertices.length);
        for (int i = vertices.length; i < newVertices.length; i++) {
            newVertices[i] = new Vector3f();
        }
        vertices = newVertices;
        boolean hidden = true;
        for (int i = 0; i < vertexCount; i++) {
            int next = (i + 1) % vertexCount;
            Vector3f v1 = vertices[i];
            Vector3f v2 = vertices[next];
            if (v1.getZ() < clipZ) {
                hidden = false;
            }
            if (v1.getZ() > v2.getZ()) {
                Vector3f temp = v1;
                v1 = v2;
                v2 = temp;
            }
            if (v1.getZ() < clipZ && v2.getZ() > clipZ) {
                float scale = (clipZ - v1.getZ()) / (v2.getZ() - v1.getZ());
                addVertex(next, v1.getX() + scale * (v2.getX() - v1.getX()), v1.getY() + scale * (v2.getY() - v1.getY()), clipZ);
                i++;
            }
        }
        if (hidden) {
            return false;
        }
        for (int i = vertexCount - 1; i >= 0; i--) {
            if (vertices[i].getZ() > clipZ) {
                removeVertex(i);
            }
        }
        return vertexCount >= 3;
    }

    public void addVertex(int i, float x, float y, float z) {
        Vector3f v = vertices[vertices.length - 1];
        v.setTo(new Vector3f(x, y, z));
        for (int j = vertices.length - 1; j > i; j--) {
            vertices[j] = vertices[j - 1];
        }
        vertices[i] = v;
        vertexCount++;
    }

    public void removeVertex(int i) {
        Vector3f v = vertices[i];
        for (int j = i; j < vertices.length - 1; j++) {
            vertices[j] = vertices[j + 1];
        }
        vertices[vertices.length - 1] = v;
        vertexCount--;
    }

    public void addVertex(int i, Vector3f v) {
        addVertex(i, v.getX(), v.getY(), v.getZ());
    }

    public Rectangle3f calcBounds() {
        Rectangle3f bounds = new Rectangle3f();
        float minArea = Float.MAX_VALUE;
        Vector3f u = new Vector3f();
        Vector3f v = new Vector3f();
        Vector3f d = new Vector3f();
        for (int i = 0; i < vertexCount; i++) {
            u.setTo(getVertex((i + 1) % vertexCount));
            u.subtract(getVertex(i));
            u.normalize();
            v = new Vector3f(calcNormal().cross(u));
            v.normalize();
            float uMin = 0;
            float uMax = 0;
            float vMin = 0;
            float vMax = 0;
            for (int j = 0; j < vertexCount; j++) {
                if (j != i) {
                    d.setTo(getVertex(j));
                    d.subtract(getVertex(i));
                    float ul = d.dot(u);
                    float vl = d.dot(v);
                    uMin = Math.min(ul, uMin);
                    uMax = Math.max(ul, uMax);
                    vMin = Math.min(vl, vMin);
                    vMax = Math.max(vl, vMax);
                }
            }
            float area = (uMax - uMin) * (vMax - vMin);
            if (area < minArea) {
                minArea = area;
                Vector3f origin = getVertex(i);
                d.setTo(u);
                d.multiply(uMin);
                origin.add(d);
                d.setTo(v);
                d.multiply(vMin);
                origin.add(d);
                bounds.setTo(new Rectangle3f(origin, u, v, uMax - uMin, vMax - vMin));
            }
        }
        return bounds;
    }

}
