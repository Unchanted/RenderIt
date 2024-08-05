package com.arman.research.geom.polygons;

import com.arman.research.geom.rectangles.Rectangle3f;
import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.vectors.Vector3f;
import com.arman.research.render.textures.Texture;

public class TexturedPolygon3f extends Polygon3f {

    private Rectangle3f bounds;
    private Texture texture;

    public TexturedPolygon3f() {
        bounds = new Rectangle3f();
    }

    public TexturedPolygon3f(Vector3f... vertices) {
        super(vertices);
        bounds = new Rectangle3f();
    }

    public void setTo(Polygon3f p) {
        super.setTo(p);
        if (p instanceof TexturedPolygon3f) {
            TexturedPolygon3f tp = (TexturedPolygon3f) p;
            bounds.setTo(tp.bounds);
            texture = tp.texture;
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle3f getBounds() {
        return bounds;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        bounds.setWidth(texture.getWidth());
        bounds.setHeight(texture.getHeight());
    }

    public void setTexture(Texture texture, Rectangle3f bounds) {
        setTexture(texture);
        bounds.setTo(bounds);
    }

    public void add(Vector3f v) {
        super.add(v);
        bounds.add(v);
    }

    public void subtract(Vector3f v) {
        super.subtract(v);
        bounds.subtract(v);
    }

    public void addRotation(Transform3f t) {
        super.addRotation(t);
        bounds.addRotation(t);
    }

    public void subtractRotation(Transform3f t) {
        super.subtractRotation(t);
        bounds.subtractRotation(t);
    }

    @Override
    public Rectangle3f calcBounds() {
        Vector3f u = new Vector3f(bounds.getDu());
        Vector3f v = new Vector3f(bounds.getDv());
        Vector3f d = new Vector3f();
        u.normalize();
        v.normalize();
        float uMin = 0;
        float uMax = 0;
        float vMin = 0;
        float vMax = 0;
        for (int i = 0; i < getVertexCount(); i++) {
            d.setTo(getVertex(i));
            d.subtract(getVertex(0));
            float ul = d.dot(u);
            float vl = d.dot(v);
            uMin = Math.min(ul, uMin);
            uMax = Math.max(ul, uMax);
            vMin = Math.min(vl, vMin);
            vMax = Math.max(vl, vMax);
        }
        Rectangle3f bounds = new Rectangle3f();
        Vector3f origin = getVertex(0);
        d.setTo(u);
        d.multiply(uMin);
        origin.add(d);
        d.setTo(v);
        d.multiply(vMin);
        origin.add(d);
        bounds.getOrigin().setTo(origin);
        bounds.getDu().setTo(u);
        bounds.getDv().setTo(v);
        bounds.setWidth(uMax - uMin);
        bounds.setHeight(vMax - vMin);
        return bounds;
    }

}
