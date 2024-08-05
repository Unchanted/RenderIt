package com.arman.research.geom.matrices;

import com.arman.research.geom.vectors.Vector4f;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.matrix = Stream.of(matrix)
                .flatMap(Stream::of)
                .toArray(Float[]::new);
    }

    public Matrix4f multiply(Matrix4f m) {
        Float[] result = new Float[SIZE];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                result[i * DIMENSION + j] = 0f;
                for (int k = 0; k < DIMENSION; k++) {
                    result[i * DIMENSION + j] += this.matrix[i * DIMENSION + k] * m.matrix[k * DIMENSION + j];
                }
            }
        }
        return new Matrix4f(result);
    }

    public Vector4f transform(Vector4f v) {
        return new Vector4f(
                dotProduct(0, v), 
                dotProduct(1, v), 
                dotProduct(2, v), 
                dotProduct(3, v)
        );
    }

    private float dotProduct(int row, Vector4f v) {
        return matrix[row * 4] * v.getX() +
               matrix[row * 4 + 1] * v.getY() +
               matrix[row * 4 + 2] * v.getZ() +
               matrix[row * 4 + 3] * v.getT();
    }

    public float get(int i) {
        return matrix[i];
    }

    public float get(int i, int j) {
        return matrix[i * 4 + j];
    }

    public void set(int i, float f) {
        matrix[i] = f;
    }

    public void set(int i, int j, float f) {
        matrix[i * 4 + j] = f;
    }

    public float dot(Matrix4f m) {
        return (float) Arrays.stream(matrix)
                .mapToDouble(Float::doubleValue)
                .sum();
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
        Float[] result = IDENTITY.matrix.clone();
        result[3] = x;
        result[7] = y;
        result[11] = z;
        return new Matrix4f(result);
    }

    public Matrix4f rotate(float x, float y, float z) {
        return rotateZ(z).multiply(rotateY(y)).multiply(rotateX(x));
    }

    private Matrix4f rotateX(float angle) {
        double rad = Math.toRadians(angle);
        Float[] result = IDENTITY.matrix.clone();
        result[5] = (float) Math.cos(rad);
        result[6] = -(float) Math.sin(rad);
        result[9] = (float) Math.sin(rad);
        result[10] = (float) Math.cos(rad);
        return new Matrix4f(result);
    }

    private Matrix4f rotateY(float angle) {
        double rad = Math.toRadians(angle);
        Float[] result = IDENTITY.matrix.clone();
        result[0] = (float) Math.cos(rad);
        result[2] = (float) Math.sin(rad);
        result[8] = -(float) Math.sin(rad);
        result[10] = (float) Math.cos(rad);
        return new Matrix4f(result);
    }

    private Matrix4f rotateZ(float angle) {
        double rad = Math.toRadians(angle);
        Float[] result = IDENTITY.matrix.clone();
        result[0] = (float) Math.cos(rad);
        result[1] = -(float) Math.sin(rad);
        result[4] = (float) Math.sin(rad);
        result[5] = (float) Math.cos(rad);
        return new Matrix4f(result);
    }
}
