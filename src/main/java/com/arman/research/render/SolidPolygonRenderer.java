package com.arman.research.render;

import com.arman.research.geom.polygons.Polygon3f;
import com.arman.research.geom.polygons.SolidPolygon3f;
import com.arman.research.geom.transforms.Transform3f;

import java.awt.*;

import static com.arman.research.geom.polygons.SolidPolygon3f.STANDARD_COLOR;

public class SolidPolygonRenderer extends PolygonRenderer {

    public SolidPolygonRenderer(Transform3f camera, View view) {
        super(camera, view);
    }

    public SolidPolygonRenderer(Transform3f camera, View view, boolean clearEveryFrame) {
        super(camera, view, clearEveryFrame);
    }

    @Override
    public void drawCurrentPolygon(Graphics2D g, Polygon3f p) {
        if (p instanceof SolidPolygon3f) {
            g.setColor(((SolidPolygon3f) p).getColor());
        } else {
            g.setColor(STANDARD_COLOR);
        }
        int y = getScanConverter().getTop();
        while (y <= getScanConverter().getBottom()) {
            ScanConverter.Scan scan = getScanConverter().getScan(y);
            if (scan.isValid()) {
                g.drawLine(scan.getLeft(), y, scan.getRight(), y);
            }
            y++;
        }
    }

}
