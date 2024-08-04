
package com.arman.research.geom.polygons;

import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.Transformable;
import com.arman.research.geom.rectangles.Rectangle3f;
import com.arman.research.geom.vectors.Vector3f;
import com.arman.research.render.View;

public class Polygon3f implements Transformable {

    private static final Vector3f TEMP1 = new Vector3f();
    private static final Vector3f TEMP2 = new Vector3f();

    private Vector3f[] vertices;
    private int vertexCount;

    public Polygon3f() {
        this.vertices = new Vector3f[0];
        this.vertexCount = 0;
    }

    public Polygon3f(Vector3f... vectors) {
        this.vertices = vectors;
        this.vertexCount = vectors.length;
    }

    public Polygon3f(Polygon3f p) {
        setTo(p);
    }

    public void setTo(Polygon3f p) {
        this.vertexCount = p.vertexCount;
        this.vertices = new Vector3f[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            this.vertices[i] = new Vector3f(p.vertices[i]);
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Vector3f getVertex(int index) {
        return vertices[index];
    }

    public void project(View view) {
        for (Vector3f vertex : vertices) {
            view.project(vertex);
        }
    }

    public Vector3f calcNormal() {
        TEMP1.setTo(vertices[2]).subtract(vertices[1]);
        TEMP2.setTo(vertices[0]).subtract(vertices[1]);
        return new Vector3f(TEMP1.cross(TEMP2)).normalize();
    }

    @Override
    public void add(Vector3f v) {
        for (Vector3f vertex : vertices) {
            vertex.add(v);
        }
    }

    @Override
    public void subtract(Vector3f v) {
        for (Vector3f vertex : vertices) {
            vertex.subtract(v);
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
        for (Vector3f vertex : vertices) {
            vertex.addRotation(t);
        }
    }

    @Override
    public void subtractRotation(Transform3f t) {
        for (Vector3f vertex : vertices) {
            vertex.subtractRotation(t);
        }
    }
}
