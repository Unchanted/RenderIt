package com.arman.projectors;

import com.arman.geom.Line3D;
import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;

public abstract class Projection {

    public static Projection identity() {
        return new Projection() {
            public Vector3D project(Vector3D v) {
                return v;
            }
        };
    }

    abstract public Vector3D project(Vector3D v);

    public Line3D project(Line3D l) {
        Vector3D projV1 = new Vector3D(project(l.get0()));
        Vector3D projV2 = new Vector3D(project(l.get1()));
        return new Line3D(projV1, projV2);
    }

    public Polygon3D project(Polygon3D p) {
        Vector3D[] projections = new Vector3D[p.vertexCount()];
        for (int i = 0; i < p.vertexCount(); i++) {
            Vector3D v = p.get(i);
            projections[i] = project(v);
        }
        return new Polygon3D(projections);
    }

}
