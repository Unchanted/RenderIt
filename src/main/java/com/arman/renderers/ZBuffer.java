package com.arman.renderers;

import com.arman.geom.Vector3D;

public class ZBuffer {

    private short[] depthBuffer;
    private int width;
    private int height;

    public ZBuffer(int width, int height) {
        this.depthBuffer = new short[width * height];
        this.width = width;
        this.height = height;
        this.clear();
    }

    public void clear() {
        for (int i = 0; i < this.depthBuffer.length; i++) {
            this.depthBuffer[i] = 0;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int get(int index) {
        return this.depthBuffer[index];
    }

    public int get(float x, float y) {
        return this.depthBuffer[(int) (x + y * this.width)];
    }

    public void set(int index, short z) {
        this.depthBuffer[index] = z;
    }

    public void set(float x, float y, short z) {
        this.depthBuffer[(int) (x + y * this.width)] = z;
    }

    public void set(Vector3D v) {
        this.set(v.getX(), v.getY(), (short) v.getZ());
    }

    public boolean checkDepth(int index, short z) {
        if (z < this.depthBuffer[index]) {
            this.depthBuffer[index] = z;
            return true;
        } else {
            return false;
        }
    }

}
