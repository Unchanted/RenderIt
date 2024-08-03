package com.arman.renderers;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BoundingBox extends Rectangle {

    public BoundingBox(float x, float y, float width, float height) {
        super((int) x, (int) y, (int) width, (int) height);
    }

    public BoundingBox(float width, float height) {
        this(0, 0, width, height);
    }

    public BoundingBox(@NotNull Rectangle rect) {
        this(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean contains(float x, float y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public boolean intersects(BoundingBox box) {
        if (box.width > 0 && box.height > 0 && this.width > 0 && this.height > 0) {
            return (box.width + box.x) > this.x &&
                    (box.height + box.y) > this.y &&
                    (this.width + this.x) > box.x &&
                    (this.height + this.y) > box.y;
        }
        return false;
    }

    public float area() {
        return this.width * this.height;
    }

    public float perimeter() {
        return 2 * this.width + 2 * this.height;
    }

}
