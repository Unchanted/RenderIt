package com.arman.research.render.textures;

public class PowerOf2Texture extends Texture {

    private short[] buffer;
    private int widthBits;
    private int widthMask;
    private int heightBits;
    private int heightMask;

    public PowerOf2Texture(short[] buffer, int widthBits, int heightBits) {
        super(1 << widthBits, 1 << heightBits);
        this.buffer = buffer;
        this.widthBits = widthBits;
        this.heightBits = heightBits;
        this.widthMask = getWidth() - 1;
        this.heightMask = getHeight() - 1;
    }

    @Override
    public short getColor(int x, int y) {
        return buffer[(x & widthMask) + ((y & heightMask) << widthBits)];
    }

}
