package com.arman.geom;

import org.jetbrains.annotations.NotNull;

public class Matrix4D {

    public static final Matrix4D IDENTITY = new Matrix4D(new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    });

    public static final int DIMENSION = 4;
    public static final int SIZE = DIMENSION * DIMENSION;

    private float[] matrix;

    public Matrix4D() {
        this.matrix = new float[SIZE];
    }

    public Matrix4D(float[] matrix) {
        this.matrix = matrix;
    }

    public Matrix4D(@NotNull Matrix4D matrix) {
        this.matrix = matrix.matrix;
    }

    @NotNull
    public static Matrix4D translation(float x, float y, float z) {
        return new Matrix4D(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                x, y, z, 1
        });
    }

    @NotNull
    public static Matrix4D rotationX(float x) {
        float a = (float) Math.toRadians(x);
        return new Matrix4D(new float[]{
                1, 0, 0, 0,
                0, (float) Math.cos(a), (float) Math.sin(a), 0,
                0, (float) -Math.sin(a), (float) Math.cos(a), 0,
                0, 0, 0, 1
        });
    }

    @NotNull
    public static Matrix4D rotationY(float y) {
        float a = (float) Math.toRadians(y);
        return new Matrix4D(new float[]{
                (float) Math.cos(a), 0, (float) -Math.sin(a), 0,
                0, 1, 0, 0,
                (float) Math.sin(a), 0, (float) Math.cos(a), 0,
                0, 0, 0, 1
        });
    }

    @NotNull
    public static Matrix4D rotationZ(float z) {
        float a = (float) Math.toRadians(z);
        return new Matrix4D(new float[]{
                (float) Math.cos(a), (float) Math.sin(a), 0, 0,
                (float) -Math.sin(a), (float) Math.cos(a), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
    }

    @NotNull
    public static Matrix4D rotation(float x, float y, float z) {
        Matrix4D rotationX = Matrix4D.rotationX(x);
        Matrix4D rotationY = Matrix4D.rotationY(y);
        Matrix4D rotationZ = Matrix4D.rotationZ(z);
        return new Matrix4D(rotationZ.multiplication(rotationY.multiplication(rotationX)));
    }

    public void multiply(Matrix4D matrix) {
        this.matrix = this.multiplication(matrix).matrix;
    }

    public Matrix4D multiplication(Matrix4D matrix) {
        float[] result = new float[SIZE];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                for (int k = 0; k < DIMENSION; k++) {
                    result[i * DIMENSION + j] += this.matrix[i * DIMENSION + k] + matrix.matrix[k * DIMENSION + j];
                }
            }
        }
        return new Matrix4D(result);
    }

    public float dot(Matrix4D matrix) {
        float result = 0.0f;
        for (int i = 0; i < SIZE; i++) {
            result += this.matrix[i] * matrix.matrix[i];
        }
        return result;
    }

    public Matrix4D transpose() {
        float[] result = new float[SIZE];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                result[j * DIMENSION + i] = this.matrix[i * DIMENSION + j];
            }
        }
        return new Matrix4D(result);
    }

    public void add(Matrix4D matrix) {
        for (int i = 0; i < SIZE; i++) {
            this.matrix[i] += matrix.matrix[i];
        }
    }

    public void subtract(Matrix4D matrix) {
        for (int i = 0; i < SIZE; i++) {
            this.matrix[i] -= matrix.matrix[i];
        }
    }

    public void multiply(float f) {
        for (int i = 0; i < SIZE; i++) {
            this.matrix[i] *= f;
        }
    }

}
