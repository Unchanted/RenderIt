package com.arman.research.render;

import java.util.Arrays;

public class Bitmap {

    private int width;
    private int height;
    private int[] pixels;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public void fill(int rgb) {
        Arrays.fill(pixels, rgb);
    }

    public void setPixel(int i, int rgb) {
        if (i >= 0 && i < pixels.length) {
            this.pixels[i] = rgb;
        }
    }

    public void setPixel(int i, int j, int rgb) {
        if (i >= 0 && i < height && j >= 0 && j < width) {
            this.pixels[i * width + j] = rgb;
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, int rgb) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (dx < 0) {
            dx1 = -1;
            dx2 = -1;
        } else if (dx > 0) {
            dx1 = 1;
            dx2 = 1;
        }
        if (dy < 0) {
            dy1 = -1;
        } else if (dy > 0) {
            dy1 = 1;
        }
        int longest = Math.abs(dx);
        int shortest = Math.abs(dy);
        if (longest < shortest) {
            longest = Math.abs(dy);
            shortest = Math.abs(dx);
            if (dy < 0) {
                dy2 = -1;
            } else if (dy > 0) {
                dy2 = 1;
            }
            dx2 = 0;
        }
        int x = x0;
        int y = y0;
        int numerator = longest >> 1;
        for (int i = 0; i < longest; i++) {
            setPixel(x, y, rgb);
            numerator += shortest;
            if (numerator > longest) {
                numerator -= longest;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }
    }

    public int[] getPixels() {
        return pixels.clone();
    }

}
