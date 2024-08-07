package com.arman.research.render.textures;

import com.arman.research.geom.polygons.TexturedPolygon3f;
import com.arman.research.geom.rectangles.Rectangle3f;
import com.arman.research.geom.vectors.Vector3f;
import com.arman.research.render.lights.PointLight3f;

import java.lang.ref.SoftReference;
import java.util.List;

public class ShadedSurface extends Texture {

    public static final int SURFACE_BORDER_SIZE = 1;
    public static final int SHADE_RES_BITS = 4;
    public static final int SHADE_RES = 1 << SHADE_RES_BITS;
    public static final int SHADE_RES_MASK = SHADE_RES - 1;
    public static final int SHADE_RES_SQUARED = SHADE_RES * SHADE_RES;
    public static final int SHADE_RES_SQUARED_BITS = SHADE_RES_BITS * 2;

    private short[] buffer;
    private SoftReference<short[]> bufferReference;
    private boolean dirty;
    private ShadedTexture texture;
    private Rectangle3f textureBounds;
    private Rectangle3f surfaceBounds;
    private byte[] shadeMap;
    private int shadeMapWidth;
    private int shadeMapHeight;
    private int shade;
    private int shadeIncrement;

    public ShadedSurface(int width, int height) {
        this(null, width, height);
    }

    public ShadedSurface(short[] buffer, int width, int height) {
        super(width, height);
        this.buffer = buffer;
        this.bufferReference = new SoftReference<>(buffer);
        this.textureBounds = new Rectangle3f();
        this.dirty = true;
    }

    public static void createShadedSurface(TexturedPolygon3f polygon, ShadedTexture texture, List<PointLight3f> lights, float ambientLightIntensity) {
        Vector3f origin = polygon.getVertex(0);
        Vector3f dv = new Vector3f(polygon.getVertex(1)).subtract(origin);
        Vector3f du = new Vector3f(polygon.calcNormal().cross(dv));
        Rectangle3f bounds = new Rectangle3f(origin, du, dv, texture.getWidth(), texture.getHeight());
        createShadedSurface(polygon, texture, bounds, lights, ambientLightIntensity);
    }

    public static void createShadedSurface(TexturedPolygon3f polygon, ShadedTexture texture, Rectangle3f textureBounds, List<PointLight3f> lights, float ambientLightIntensity) {
        polygon.setTexture(texture, textureBounds);
        Rectangle3f surfaceBounds = polygon.calcBounds();
        adjustSurfaceBounds(surfaceBounds);

        int width = (int) Math.ceil(surfaceBounds.getWidth() + SURFACE_BORDER_SIZE * 2);
        int height = (int) Math.ceil(surfaceBounds.getHeight() + SURFACE_BORDER_SIZE * 2);
        surfaceBounds.setWidth(width);
        surfaceBounds.setHeight(height);

        ShadedSurface surface = new ShadedSurface(width, height);
        surface.setTexture(texture, textureBounds);
        surface.setSurfaceBounds(surfaceBounds);
        surface.buildShadeMap(lights, ambientLightIntensity);

        polygon.setTexture(surface, surfaceBounds);
    }

    private static void adjustSurfaceBounds(Rectangle3f surfaceBounds) {
        Vector3f du = new Vector3f(surfaceBounds.getDu()).multiply(SURFACE_BORDER_SIZE);
        Vector3f dv = new Vector3f(surfaceBounds.getDv()).multiply(SURFACE_BORDER_SIZE);
        surfaceBounds.getOrigin().subtract(du).subtract(dv);
    }

    public short getColor(int x, int y) {
        x = clamp(x, 0, getWidth() - 1);
        y = clamp(y, 0, getHeight() - 1);
        return buffer[x + y * getWidth()];
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void newSurface(int width, int height) {
        buffer = new short[width * height];
        bufferReference = new SoftReference<>(buffer);
    }

    public void clear() {
        buffer = null;
    }

    public boolean isCleared() {
        return buffer == null;
    }

    public boolean retrieveSurface() {
        if (buffer == null) {
            buffer = bufferReference.get();
        }
        return buffer != null;
    }

    public void setTexture(ShadedTexture texture) {
        this.texture = texture;
        textureBounds.setWidth(texture.getWidth());
        textureBounds.setHeight(texture.getHeight());
    }

    public void setTexture(ShadedTexture texture, Rectangle3f bounds) {
        setTexture(texture);
        textureBounds.setTo(bounds);
    }

    public void setSurfaceBounds(Rectangle3f surfaceBounds) {
        this.surfaceBounds = surfaceBounds;
    }

    public Rectangle3f getSurfaceBounds() {
        return surfaceBounds;
    }

    public void buildSurface() {
        if (retrieveSurface()) {
            return;
        }
        int width = (int) surfaceBounds.getWidth();
        int height = (int) surfaceBounds.getHeight();
        newSurface(width, height);

        buildSurfaceBuffer(width, height);
    }

    private void buildSurfaceBuffer(int width, int height) {
        Vector3f origin = textureBounds.getOrigin();
        Vector3f du = textureBounds.getDu();
        Vector3f dv = textureBounds.getDv();
        Vector3f d = new Vector3f(surfaceBounds.getOrigin()).subtract(origin);

        int su = (int) (d.dot(du) - SURFACE_BORDER_SIZE);
        int sv = (int) (d.dot(dv) - SURFACE_BORDER_SIZE);
        int offset = 0;
        int shadeMapOffsetU = SHADE_RES - SURFACE_BORDER_SIZE - su;
        int shadeMapOffsetV = SHADE_RES - SURFACE_BORDER_SIZE - sv;

        for (int v = sv; v < sv + height; v++) {
            texture.setCurrentRow(v);
            int u = su;
            int amount = SURFACE_BORDER_SIZE;
            while (u < su + width) {
                getInterpolatedShade(u + shadeMapOffsetU, v + shadeMapOffsetV);
                int eu = Math.min(su + width, u + amount);
                while (u < eu) {
                    buffer[offset++] = texture.getColorCurrentRow(u, shade >> SHADE_RES_SQUARED_BITS);
                    shade += shadeIncrement;
                    u++;
                }
                amount = SHADE_RES;
            }
        }
    }

    private void getInterpolatedShade(int u, int v) {
        // Implement shade interpolation logic here
    }

    private void buildShadeMap(List<PointLight3f> lights, float ambientLightIntensity) {
        // Implement shade map building logic here
    }
}
