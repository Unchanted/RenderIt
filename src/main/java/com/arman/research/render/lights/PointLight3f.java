package com.arman.research.render.lights;

import com.arman.research.geom.vectors.Vector3f;

public class PointLight3f extends Vector3f {

    public static final float NO_DISTANCE_FALLOFF = -1;

    private float intensity;
    private float distanceFalloff;

    public PointLight3f() {
        this(0, 0, 0, 1, NO_DISTANCE_FALLOFF);
    }

    public PointLight3f(PointLight3f p) {
        setTo(p);
    }

    public PointLight3f(float x, float y, float z, float intensity) {
        this(x, y, z, intensity, NO_DISTANCE_FALLOFF);
    }

    public PointLight3f(float x, float y, float z, float intensity, float distanceFalloff) {
        setTo(new Vector3f(x, y, z));
        setIntensity(intensity);
        setDistanceFalloff(distanceFalloff);
    }

    public void setTo(PointLight3f p) {
        setTo(new Vector3f(p.getX(), p.getY(), p.getZ()));
        this.intensity = p.intensity;
        this.distanceFalloff = p.distanceFalloff;
    }

    public float getIntensity(float dist) {
        if (distanceFalloff == NO_DISTANCE_FALLOFF) {
            return intensity;
        } else if (dist >= distanceFalloff) {
            return 0;
        } else {
            return intensity * (distanceFalloff - dist) / (distanceFalloff + dist);
        }
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getDistanceFalloff() {
        return distanceFalloff;
    }

    public void setDistanceFalloff(float distanceFalloff) {
        this.distanceFalloff = distanceFalloff;
    }

}
