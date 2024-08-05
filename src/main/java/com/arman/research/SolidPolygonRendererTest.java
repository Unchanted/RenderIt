package com.arman.research;

import com.arman.research.core.Core3D;
import com.arman.research.geom.polygons.Polygon3f;
import com.arman.research.geom.polygons.PolygonGroup;
import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.loader.ObjectLoader;
import com.arman.research.render.SolidPolygonRenderer;
import com.arman.research.render.View;

import java.io.IOException;

public class SolidPolygonRendererTest extends Core3D {

    public static void main(String[] args) {
        new SolidPolygonRendererTest().run();
    }

    public void createPolygons() {
        PolygonGroup pg = null;
        ObjectLoader loader = new ObjectLoader();
        try {
            pg = loader.loadObject("./monkey.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pg != null) {
            while (pg.hasNext()) {
                Polygon3f p = pg.nextPolygon();
                addPolygon(p);
            }
        }
    }

    public void createPolygonRenderer() {
        setView(new View(0, 0, getScreen().getWidth(), getScreen().getHeight(), (float) Math.toRadians(75)));
        Transform3f camera = new Transform3f(200, 200, 1000);
        setPolygonRenderer(new SolidPolygonRenderer(camera, getView()));
    }

}
