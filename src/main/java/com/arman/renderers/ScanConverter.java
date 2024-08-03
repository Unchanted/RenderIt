package com.arman.renderers;

import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;
import org.jetbrains.annotations.NotNull;

public class ScanConverter {

    public static final int SCALE_BITS = 16;
    public static final int SCALE = 1 << SCALE_BITS;
    public static final int SCALE_MASK = SCALE - 1;

    private ScanLine[] scanLines;
    private int top, bottom;
    private BoundingBox bounds;

    public ScanConverter(BoundingBox bounds) {
        this.bounds = bounds;
    }

    private void clearScan() {
        for (int i = 0; i < this.scanLines.length; i++) {
            this.scanLines[i] = new ScanLine();
        }
        this.top = Integer.MAX_VALUE;
        this.bottom = Integer.MIN_VALUE;
    }

    public int getTop() {
        return this.top;
    }

    public int getBottom() {
        return this.bottom;
    }

    public boolean convert(@NotNull Polygon3D p) {
        int height = this.bounds.y + this.bounds.height;
        this.scanLines = new ScanLine[height];
        clearScan();
        int minX = this.bounds.x;
        int maxX = this.bounds.x + this.bounds.width - 1;
        int minY = this.bounds.y;
        int maxY = this.bounds.y + this.bounds.height - 1;
        for (int i = 0; i < p.vertexCount(); i++) {
            Vector3D v1 = p.get(i);
            Vector3D v2 = p.get((i + 1) % p.vertexCount());
            if (v1.getY() > v2.getY()) {
                Vector3D temp = v2;
                v2 = v1;
                v1 = temp;
            }
            float dy = v2.getY() - v1.getY();
            if (dy == 0) {
                continue;
            }
            int startY = (int) Math.max(Math.ceil(v1.getY()), minY);
            int endY = (int) Math.min(Math.floor(v2.getY()), maxY);
            this.top = Math.min(this.top, startY);
            this.bottom = Math.max(this.bottom, endY);
            float dx = v2.getX() - v1.getX();
            if (dx == 0) {
                int x = (int) Math.ceil(v1.getX());
                x = Math.min(maxX + 1, Math.max(x, minX));
                for (int y = startY; y <= endY; y++) {
                    this.scanLines[y].setBounds(x);
                }
            } else {
                float gradient = dx / dy;
                float sx = v1.getX() + (startY - v1.getY()) * gradient;
                if (sx < minX) {
                    int y = (int) (v1.getY() + (minX - v1.getX()) / gradient);
                    y = Math.min(y, endY);
                    while (startY <= y) {
                        this.scanLines[startY].setBounds(minX);
                        startY++;
                    }
                } else if (sx > maxX) {
                    int y = (int) (v1.getY() + (maxX - v1.getX()) / gradient);
                    y = Math.min(y, endY);
                    while (startY <= y) {
                        this.scanLines[startY].setBounds(maxX + 1);
                        startY++;
                    }
                }
                if (startY > endY) {
                    continue;
                }
                float ex = v1.getX() + (endY - v1.getY()) * gradient;
                if (ex < minX) {
                    int y = (int) Math.ceil(v1.getY() + (minX - v1.getX()) / gradient);
                    y = Math.max(y, startY);
                    while (endY >= y) {
                        this.scanLines[endY].setBounds(minX);
                        endY--;
                    }
                } else if (ex > maxX) {
                    int y = (int) Math.ceil(v1.getY() + (maxX - v1.getX()) / gradient);
                    y = Math.max(y, startY);
                    while (endY >= y) {
                        this.scanLines[endY].setBounds(maxX + 1);
                        endY--;
                    }
                }
                if (startY > endY) {
                    continue;
                }
                int xScaled = (int) (SCALE * v1.getX() + SCALE * (startY - v1.getY()) * gradient) + SCALE_MASK;
                int dxScaled = (int) (SCALE * gradient);
                for (int y = startY; y <= endY; y++) {
                    this.scanLines[y].setBounds(xScaled >> SCALE_BITS);
                    xScaled += dxScaled;
                }
            }
        }
        for (int y = this.top; y <= this.bottom; y++) {
            if (scanLines[y].isValid()) {
                return true;
            }
        }
        return false;
    }

    public ScanLine getScanLine(int index) {
        return this.scanLines[index];
    }

    @Override
    public String toString() {
        String res = "";
        for (int y = 0; y < this.scanLines.length; y++) {
            res += y + ": " + this.scanLines[y];
            res += "\n";
        }
        return res;
    }

}
