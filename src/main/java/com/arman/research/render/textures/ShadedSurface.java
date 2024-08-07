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
    private SoftReference bufferReference;
    private boolean dirty;
    private ShadedTexture texture;
    private Rectangle3f textureBounds;
    private Rectangle3f surfaceBounds;
    private byte[] shadeMap;
    private int shadeMapWidth;
    private int shadeMapHeight;
    private int shade;
    private int shadeInc;

    public ShadedSurface(int width, int height) {
        this(null, width, height);
    }

    public ShadedSurface(short[] buffer, int width, int height) {
        super(width, height);
        this.buffer = buffer;
        bufferReference = new SoftReference(buffer);
        textureBounds = new Rectangle3f();
        dirty = true;
    }

    public static void createShadedSurface(TexturedPolygon3f p, ShadedTexture t, List<PointLight3f> lights, float ambientLightIntensity) {
        Vector3f origin = p.getVertex(0);
        Vector3f dv = new Vector3f(p.getVertex(1));
        dv.subtract(origin);
        Vector3f du = new Vector3f(p.calcNormal().cross(dv));
        Rectangle3f bounds = new Rectangle3f(origin, du, dv, t.getWidth(), t.getHeight());
        createShadedSurface(p, t, bounds, lights, ambientLightIntensity);
    }

    public static void createShadedSurface(TexturedPolygon3f p, ShadedTexture t, Rectangle3f textureBounds, List<PointLight3f> lights, float ambientLightIntensity) {
        p.setTexture(t, textureBounds);
        Rectangle3f surfaceBounds = p.calcBounds();
        Vector3f du = new Vector3f(surfaceBounds.getDu());
        Vector3f dv = new Vector3f(surfaceBounds.getDv());
        du.multiply(SURFACE_BORDER_SIZE);
        dv.multiply(SURFACE_BORDER_SIZE);
        surfaceBounds.getOrigin().subtract(du);
        surfaceBounds.getOrigin().subtract(dv);
        int width = (int) Math.ceil(surfaceBounds.getWidth() + SURFACE_BORDER_SIZE * 2);
        int height = (int) Math.ceil(surfaceBounds.getHeight() + SURFACE_BORDER_SIZE * 2);
        surfaceBounds.setWidth(width);
        surfaceBounds.setHeight(height);
        ShadedSurface surface = new ShadedSurface(width, height);
        surface.setTexture(t, textureBounds);
        surface.setSurfaceBounds(surfaceBounds);
        surface.buildShadeMap(lights, ambientLightIntensity);
        p.setTexture(surface, surfaceBounds);
    }

    public short getColor(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x >= getWidth()) {
            x = getWidth() - 1;
        }
        if (y < 0) {
            y = 0;
        } else if (y >= getHeight()) {
            y = getHeight() - 1;
        }
        return buffer[x + y * getWidth()];
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void newSurface(int width, int height) {
        buffer = new short[width * height];
        bufferReference = new SoftReference(buffer);
    }

    public void clear() {
        buffer = null;
    }

    public boolean isCleared() {
        return buffer == null;
    }

    public boolean retrieveSurface() {
        if (buffer == null) {
            buffer = (short[]) bufferReference.get();
        }
        return buffer != null;
    }

    public void setTexture(ShadedTexture t) {
        this.texture = t;
        textureBounds.setWidth(t.getWidth());
        textureBounds.setHeight(t.getHeight());
    }

    public void setTexture(ShadedTexture t, Rectangle3f bounds) {
        setTexture(t);
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
        Vector3f origin = textureBounds.getOrigin();
        Vector3f du = textureBounds.getDu();
        Vector3f dv = textureBounds.getDv();
        Vector3f d = new Vector3f(surfaceBounds.getOrigin());
        d.subtract(origin);
        int su = (int) ((d.dot(du) - SURFACE_BORDER_SIZE));
        int sv = (int) ((d.dot(dv) - SURFACE_BORDER_SIZE));
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
                    shade += shadeInc;
                    u++;
                }
                amount = SHADE_RES;
            }
        }
    }

    public int getInterpolatedShade(int u, int v) {
        int fu = u & SHADE_RES_MASK;
        int fv = v & SHADE_RES_MASK;
        int offset = (u >> SHADE_RES_BITS) + ((v >> SHADE_RES_BITS) * shadeMapWidth);
        int shade00 = (SHADE_RES - fv) * shadeMap[offset];
        int shade01 = fv * shadeMap[offset + shadeMapWidth];
        int shade10 = (SHADE_RES - fv) * shadeMap[offset + 1];
        int shade11 = fv * shadeMap[offset + shadeMapWidth + 1];
        shade = SHADE_RES_SQUARED / 2 + (SHADE_RES - fu) * shade00 + (SHADE_RES - fu) * shade01 + fu * shade10 + fu * shade11;
        shadeInc = -shade00 - shade01 + shade10 + shade11;
        return shade >> SHADE_RES_SQUARED_BITS;
    }

    public int getShade(int u, int v) {
        return shadeMap[u + v * shadeMapWidth];
    }

    public void buildShadeMap(List<PointLight3f> lights, float ambientLightIntensity) {
        Vector3f surfaceNormal = surfaceBounds.calcNormal();
        int width = (int) surfaceBounds.getWidth() - SURFACE_BORDER_SIZE * 2;
        int height = (int) surfaceBounds.getHeight() - SURFACE_BORDER_SIZE * 2;
        shadeMapWidth = width / SHADE_RES + 4;
        shadeMapHeight = height / SHADE_RES + 4;
        shadeMap = new byte[shadeMapWidth * shadeMapHeight];
        Vector3f origin = new Vector3f(surfaceBounds.getOrigin());
        Vector3f du = new Vector3f(surfaceBounds.getDu());
        Vector3f dv = new Vector3f(surfaceBounds.getDv());
        du.multiply(SHADE_RES - SURFACE_BORDER_SIZE);
        dv.multiply(SHADE_RES - SURFACE_BORDER_SIZE);
        origin.subtract(du);
        origin.subtract(dv);
        Vector3f point = new Vector3f();
        du.setTo(surfaceBounds.getDu());
        dv.setTo(surfaceBounds.getDv());
        du.multiply(SHADE_RES);
        dv.multiply(SHADE_RES);
        for (int v = 0; v < shadeMapHeight; v++) {
            point.setTo(origin);
            for (int u = 0; u < shadeMapWidth; u++) {
                shadeMap[u + v * shadeMapWidth] = calcShade(surfaceNormal, point, lights, ambientLightIntensity);
                point.add(du);
            }
            origin.add(dv);
        }
    }

    public byte calcShade(Vector3f normal, Vector3f point, List<PointLight3f> lights, float ambientLightIntensity) {
        float intensity = 0;
        Vector3f directionToLight = new Vector3f();
        for (int i = 0; i < lights.size(); i++) {
            PointLight3f light = lights.get(i);
            directionToLight.setTo(light);
            directionToLight.subtract(point);
            float dist = directionToLight.length();
            directionToLight.normalize();
            float lightIntensity = light.getIntensity(dist) * directionToLight.dot(normal);
            lightIntensity = Math.min(lightIntensity, 1);
            lightIntensity = Math.max(lightIntensity, 0);
            intensity += lightIntensity;
        }
        intensity = Math.min(intensity, 1);
        intensity = Math.max(intensity, 0);
        intensity += ambientLightIntensity;
        intensity = Math.min(intensity, 1);
        intensity = Math.max(intensity, 0);
        int level = Math.round(intensity * ShadedTexture.MAX_LEVEL);
        return (byte) level;
    }

}
