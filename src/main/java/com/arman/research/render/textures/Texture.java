package com.arman.research.render.textures;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public abstract class Texture {

    private int width;
    private int height;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract short getColor(int x, int y);

    public static Texture createTexture(String fileName) {
        return createTexture(fileName, false);
    }

    public static Texture createTexture(String fileName, boolean shaded) {
        try {
            return createTexture(ImageIO.read(new File(fileName)), shaded);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public static Texture createTexture(BufferedImage image) {
        return createTexture(image, false);
    }

    public static Texture createTexture(BufferedImage image, boolean shaded) {
        int type = image.getType();
        int width = image.getWidth();
        int height = image.getHeight();
        if (!isPowerOfTwo(width) || !isPowerOfTwo(height)) {
            throw new IllegalArgumentException();
        }
        if (shaded) {
            if (type != BufferedImage.TYPE_BYTE_INDEXED) {
                BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
                Graphics2D g = newImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                image = newImage;
            }
            DataBuffer dest = image.getRaster().getDataBuffer();
            return new ShadedTexture(((DataBufferByte) dest).getData(), numBits(width - 1), numBits(height - 1), (IndexColorModel) image.getColorModel());
        } else {
            if (type != BufferedImage.TYPE_USHORT_565_RGB) {
                BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
                Graphics2D g = newImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                image = newImage;
            }
            DataBuffer dest = image.getRaster().getDataBuffer();
            return new PowerOf2Texture(((DataBufferUShort) dest).getData(), numBits(width - 1), numBits(height - 1));
        }
    }

    public static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0;
    }

    public static int numBits(int n) {
        int count = 0;
        while (n > 0) {
            count += (n & 1);
            n >>= 1;
        }
        return count;
    }

}
