package com.arman.renderers;

import com.arman.Tuple;
import com.arman.geom.Line3D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Graphics {

    private static final int RGB_WHITE = 16777215;

    private int[] pixels;
    private int width;
    private int height;
    private int currentRgb;

    public Graphics(@NotNull BufferedImage image) {
        this.currentRgb = 0;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = 0;
        }
    }

    public void drawPixel(int x, int y) {
        if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
            int alpha = (this.currentRgb >> 24) & 0xFF;
            if (alpha == 255) {
                pixels[x + y * this.width] = this.currentRgb;
            } else if (alpha != 0) {
                int color = pixels[x + y * this.width];
                int red = ((color >> 16) & 0xFF) - (int) ((((color >> 16) & 0xFF) - ((this.currentRgb >> 16) & 0xFF)) * (alpha / 255f));
                int green = ((color >> 8) & 0xFF) - (int) ((((color >> 8) & 0xFF) - ((this.currentRgb >> 8) & 0xFF)) * (alpha / 255f));
                int blue = ((color) & 0xFF) - (int) ((((color) & 0xFF) - ((this.currentRgb) & 0xFF)) * (alpha / 255f));
                pixels[x + y * this.width] = (red << 16 | green << 8 | blue);
            }
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        int fraction = 0, xstep = 1, ystep = 1;
        if (dy < 0) {
            dy = -dy;
            ystep = -1;
        }
        if (dx < 0) {
            dx = -dx;
            xstep = -1;
        }
        dy <<= 1;
        dx <<= 1;
        this.drawPixel(x1, y1);
        if (dx > dy) {
            fraction = dy - (dx >> 1);
        }
        while (x1 != x2) {
            if (fraction >= 0) {
                y1 += ystep;
                fraction -= dx;
                x1 += xstep;
                fraction += dy;
                this.drawPixel(x1, y1);
            } else {
                fraction = dx - (dy >> 1);
                while (y1 != y2) {
                    if (fraction >= 0) {
                        x1 += xstep;
                        fraction -= dy;
                    }
                    y1 += ystep;
                    fraction += dx;
                    this.drawPixel(x1, y1);
                }
            }
        }
    }

    public void drawCircle(int x, int y, int radius) {
        int x0 = radius - 1;
        int y0 = 0;
        int dx = 1;
        int dy = 1;
        int fraction = dx - (radius << 1);
        while (x0 >= y0) {
            this.drawPixel(x + x0, y + y0);
            this.drawPixel(x + y0, y + x0);
            this.drawPixel(x - y0, y + x0);
            this.drawPixel(x - x0, y + y0);
            this.drawPixel(x - x0, y - y0);
            this.drawPixel(x - y0, y - x0);
            this.drawPixel(x + y0, y - x0);
            this.drawPixel(x + x0, y - y0);
            if (fraction <= 0) {
                y0++;
                fraction += dy;
                dy += 2;
            }
            if (fraction > 0) {
                x0--;
                dx += 2;
                fraction += dx - (radius << 1);
            }
        }
    }

    public void setColor(Color color) {
        this.currentRgb = color.getRGB();
    }

    public Color getColor() {
        return new Color(this.currentRgb, true);
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public Tuple getSize() {
        return new Tuple<>(this.width, this.height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
