package com.arman.research.geom.polygons;

import com.arman.research.geom.transforms.MovingTransform3f;
import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.Transformable;
import com.arman.research.geom.vectors.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PolygonGroup implements Transformable {

    private String name;
    private String fileName;
    private List<Object> polygons;
    private MovingTransform3f transform;
    private int index;

    public PolygonGroup() {
        this("unnamed");
    }

    public PolygonGroup(String name) {
        this.name = name;
        polygons = new ArrayList<>();
        transform = new MovingTransform3f();
        index = 0;
    }

    public MovingTransform3f getTransform() {
        return transform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void add(Polygon3f p) {
        polygons.add(p);
    }

    public void add(PolygonGroup pg) {
        polygons.add(pg);
    }

    public Object clone() {
        PolygonGroup pg = new PolygonGroup(name);
        pg.fileName = fileName;
        for (int i = 0; i < polygons.size(); i++) {
            Object o = polygons.get(i);
            if (o instanceof Polygon3f) {
                pg.add((Polygon3f) o);
            } else {
                PolygonGroup g = (PolygonGroup) o;
                pg.add((PolygonGroup) g.clone());
            }
        }
        pg.transform = (MovingTransform3f) transform.clone();
        return pg;
    }

    public PolygonGroup getGroup(String name) {
        if (this.name != null && this.name.equals(name)) {
            return this;
        }
        for (int i = 0; i < polygons.size(); i++) {
            Object o = polygons.get(i);
            if (o instanceof PolygonGroup) {
                PolygonGroup pg = ((PolygonGroup) o).getGroup(name);
                if (pg != null) {
                    return pg;
                }
            }
        }
        return null;
    }

    public void resetIterator() {
        index = 0;
        for (int i = 0; i < polygons.size(); i++) {
            Object o = polygons.get(i);
            if (o instanceof PolygonGroup) {
                ((PolygonGroup) o).resetIterator();
            }
        }
    }

    public boolean hasNext() {
        return (index < polygons.size());
    }

    public Polygon3f nextPolygon() {
        Object o = polygons.get(index);
        if (o instanceof PolygonGroup) {
            PolygonGroup pg = (PolygonGroup) o;
            Polygon3f p = pg.nextPolygon();
            if (!pg.hasNext()) {
                index++;
            }
            return p;
        } else {
            index++;
            return (Polygon3f) o;
        }
    }

    public void nextPolygonTransformed(Polygon3f p) {
        Object o = polygons.get(index);
        if (o instanceof PolygonGroup) {
            PolygonGroup pg = (PolygonGroup) o;
            pg.nextPolygonTransformed(p);
            if (!pg.hasNext()) {
                index++;
            }
        } else {
            index++;
            p.setTo((Polygon3f) o);
        }
        p.add(transform);
    }

    public void update(long elapsedTime) {
        transform.update(elapsedTime);
        for (int i = 0; i < polygons.size(); i++) {
            Object o = polygons.get(i);
            if (o instanceof PolygonGroup) {
                PolygonGroup pg = (PolygonGroup) o;
                pg.update(elapsedTime);
            }
        }
    }

    @Override
    public void add(Vector3f v) {
        transform.getLocation().add(v);
    }

    @Override
    public void subtract(Vector3f v) {
        transform.getLocation().subtract(v);
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
        transform.rotateAngleX(t.getAngleX());
        transform.rotateAngleY(t.getAngleY());
        transform.rotateAngleZ(t.getAngleZ());
    }

    @Override
    public void subtractRotation(Transform3f t) {
        transform.rotateAngleX(-t.getAngleX());
        transform.rotateAngleY(-t.getAngleY());
        transform.rotateAngleZ(-t.getAngleZ());
    }

}
