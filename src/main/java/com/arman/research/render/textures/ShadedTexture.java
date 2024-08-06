package com.arman.research.render.textures;

import java.awt.*;
import java.awt.image.IndexColorModel;

public class ShadedTexture extends Texture {

    public static final int NUM_SHADE_LEVELS = 64;
    public static final int MAX_LEVEL = NUM_SHADE_LEVELS - 1;
    public static final int PALETTE_SIZE_BITS = 8;
    public static final int PALETTE_SIZE = 1 << PALETTE_SIZE_BITS;

    private byte[] buffer;
    private IndexColorModel palette;
    private short[] shadeTable;
    private int defaultShadeLevel;
    private int widthBits;
    private int widthMask;
    private int heightBits;
    private int heightMask;
    private int currentRow;

    public ShadedTexture(byte[] buffer, int widthBits, int heightBits, IndexColorModel palette) {
        this(buffer, widthBits, heightBits, palette, Color.BLACK);
    }

    public ShadedTexture(byte[] buffer, int widthBits, int heightBits, IndexColorModel palette, Color shade) {
        super(1 << widthBits, 1 << heightBits);
        this.buffer = buffer;
        this.widthBits = widthBits;
        this.heightBits = heightBits;
        this.widthMask = getWidth() - 1;
        this.heightMask = getHeight() - 1;
        this.buffer = buffer;
        this.palette = palette;
        defaultShadeLevel = MAX_LEVEL;
        buildShadeTable(shade);
    }

    public void buildShadeTable(Color shade) {
        shadeTable = new short[NUM_SHADE_LEVELS * PALETTE_SIZE];
        for (int lvl = 0; lvl < NUM_SHADE_LEVELS; lvl++) {
            for (int i = 0; i < palette.getMapSize(); i++) {
                int red = calcColor(palette.getRed(i), shade.getRed(), lvl);
                int green = calcColor(palette.getGreen(i), shade.getGreen(), lvl);
                int blue = calcColor(palette.getBlue(i), shade.getBlue(), lvl);
                int idx = lvl * PALETTE_SIZE + i;
                shadeTable[idx] = (short) (((red >> 3) << 11) | ((green >> 2) << 5) | (blue >> 3));
            }
        }
    }

    private int calcColor(int paletteRgb, int rgb, int lvl) {
        return (paletteRgb - rgb) * (lvl + 1) / NUM_SHADE_LEVELS + rgb;
    }

    public void setDefaultShadeLevel(int lvl) {
        this.defaultShadeLevel = lvl;
    }

    public int getDefaultShadeLevel() {
        return defaultShadeLevel;
    }

    @Override
    public short getColor(int x, int y) {
        return getColor(x, y, defaultShadeLevel);
    }

    public short getColor(int x, int y, int lvl) {
        return shadeTable[(lvl << PALETTE_SIZE_BITS) | (0xFF & buffer[(x & widthMask) | ((y & heightMask) << widthBits)])];
    }

    public void setCurrentRow(int y) {
        currentRow = (y & heightMask) << widthBits;
    }

    public short getColorCurrentRow(int x, int lvl) {
        return shadeTable[(lvl << PALETTE_SIZE_BITS) | (0xFF & buffer[(x & widthMask) | currentRow])];
    }

}
