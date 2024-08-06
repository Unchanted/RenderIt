package com.arman.research.geom.matrices;

import com.arman.research.geom.vectors.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix4f {

    public static final Matrix4f IDENTITY = new Matrix4f(new Float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f});

    public static final int DIMENSION = 4;
    public static final int SIZE = DIMENSION * DIMENSION;

    private Float[] matrix;

    public Matrix4f() {
        matrix = new Float[SIZE];
    }

    public Matrix4f(Float[] matrix) {
        this.matrix = matrix.clone();
    }

    public Matrix4f(Matrix4f m) {
        this(m.matrix);
    }

    public Matrix4f(Float[][] matrix) {
        List<Float> list = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            list.addAll(Arrays.asList(matrix[i]));
        }
        this.matrix = new Float[list.size()];
        this.matrix = list.toArray(this.matrix);
    }

    public Matrix4f multiply(Matrix4f m) {
        Float[] result = new Float[SIZE];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                for (int k = 0; k < DIMENSION; k++) {
                    if (result[i * DIMENSION + j] == null) {
                        result[i * DIMENSION + j] = 0f;
                    }
                    result[i * DIMENSION + j] += this.matrix[i * DIMENSION + k] + m.matrix[k * DIMENSION + j];
                }
            }
        }
        return new Matrix4f(result);
    }

    public Vector4f transform(Vector4f v) {
        return new Vector4f(matrix[0] * v.getX() + matrix[1] * v.getY() + matrix[2] * v.getZ() + matrix[3] * v.getT(),
                matrix[4] * v.getX() + matrix[5] * v.getY() + matrix[6] * v.getZ() + matrix[7] * v.getT(),
                matrix[8] * v.getX() + matrix[9] * v.getY() + matrix[10] * v.getZ() + matrix[11] * v.getT(),
                matrix[12] * v.getX() + matrix[13] * v.getY() + matrix[14] * v.getZ() + matrix[15] * v.getT());
    }

    public float get(int i) {
        return matrix[i];
    }

    public float get(int i, int j) {
        return matrix[i * 4 + j];
    }

    public void set(int i, float f) {
        this.matrix[i] = f;
    }

    public void set(int i, int j, float f) {
        this.matrix[i * 4 + j] = f;
    }

    public float dot(Matrix4f m) {
        float result = 0f;
        for (int i = 0; i < matrix.length; i++) {
            result += matrix[i] * m.matrix[i];
        }
        return result;
    }

    public Matrix4f transpose() {
        Float[] result = new Float[SIZE];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                result[j * DIMENSION + i] = matrix[i * DIMENSION + j];
            }
        }
        return new Matrix4f(result);
    }

    public Matrix4f add(Matrix4f m) {
        Float[] result = new Float[SIZE];
        for (int i = 0; i < SIZE; i++) {
            result[i] = matrix[i] + m.matrix[i];
        }
        return new Matrix4f(result);
    }

    public Matrix4f subtract(Matrix4f m) {
        Float[] result = new Float[SIZE];
        for (int i = 0; i < SIZE; i++) {
            result[i] = matrix[i] - m.matrix[i];
        }
        return new Matrix4f(result);
    }

    public Matrix4f translate(float x, float y, float z) {
        Float[] result = new Float[SIZE];
        result[0] = 1f;
        result[1] = 0f;
        result[2] = 0f;
        result[3] = x;
        result[4] = 0f;
        result[5] = 1f;
        result[6] = 0f;
        result[7] = y;
        result[8] = 0f;
        result[9] = 0f;
        result[10] = 1f;
        result[11] = z;
        result[12] = 0f;
        result[13] = 0f;
        result[14] = 0f;
        result[15] = 1f;
        return new Matrix4f(result);
    }

    public Matrix4f rotate(float x, float y, float z) {
        double xx = Math.toRadians(x);
        double yy = Math.toRadians(y);
        double zz = Math.toRadians(z);
        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();
        rz.matrix[0] = (float) Math.cos(zz);
        rz.matrix[1] = -(float) Math.sin(zz);
        rz.matrix[2] = 0f;
        rz.matrix[3] = 0f;
        rz.matrix[4] = (float) Math.sin(zz);
        rz.matrix[5] = (float) Math.cos(zz);
        rz.matrix[6] = 0f;
        rz.matrix[7] = 0f;
        rz.matrix[8] = 0f;
        rz.matrix[9] = 0f;
        rz.matrix[10] = 1f;
        rz.matrix[11] = 0f;
        rz.matrix[12] = 0f;
        rz.matrix[13] = 0f;
        rz.matrix[14] = 0f;
        rz.matrix[15] = 1f;
        rx.matrix[0] = 1f;
        rx.matrix[1] = 0f;
        rx.matrix[2] = 0f;
        rx.matrix[3] = 0f;
        rx.matrix[4] = 0f;
        rx.matrix[5] = (float) Math.cos(xx);
        rx.matrix[6] = -(float) Math.sin(xx);
        rx.matrix[7] = 0f;
        rx.matrix[8] = 0f;
        rx.matrix[9] = (float) Math.sin(xx);
        rx.matrix[10] = (float) Math.cos(xx);
        rx.matrix[11] = 0f;
        rx.matrix[12] = 0f;
        rx.matrix[13] = 0f;
        rx.matrix[14] = 0f;
        rx.matrix[15] = 1f;
        ry.matrix[0] = (float) Math.cos(yy);
        ry.matrix[1] = 0f;
        ry.matrix[2] = -(float) Math.sin(yy);
        ry.matrix[3] = 0f;
        ry.matrix[4] = 0f;
        ry.matrix[5] = 1f;
        ry.matrix[6] = 0f;
        ry.matrix[7] = 0f;
        ry.matrix[8] = (float) Math.sin(yy);
        ry.matrix[9] = 0f;
        ry.matrix[10] = (float) Math.cos(yy);
        ry.matrix[11] = 0f;
        ry.matrix[12] = 0f;
        ry.matrix[13] = 1f;
        ry.matrix[14] = 0f;
        ry.matrix[15] = 1f;
        return new Matrix4f(rz.multiply(ry.multiply(rx)).matrix.clone());
    }

    public Matrix4f scale(float x, float y, float z) {
        Float[] result = new Float[SIZE];
        result[0] = x;
        result[1] = 0f;
        result[2] = 0f;
        result[3] = 0f;
        result[4] = 0f;
        result[5] = y;
        result[6] = 0f;
        result[7] = 0f;
        result[8] = 0f;
        result[9] = 0f;
        result[10] = z;
        result[11] = 0f;
        result[12] = 0f;
        result[13] = 0f;
        result[14] = 0f;
        result[15] = 1f;
        return new Matrix4f(result);
    }

}
