package com.inha.endgame.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class rVector3D implements Serializable {
    private static final long serialVersionUID = 1151561654154545L;

    private float x;
    private float y;
    private float z;

    public rVector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }


    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public rVector3D add(rVector3D other) {
        return new rVector3D(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public rVector3D normalize(rVector3D rot, double velocity, int frameCount) {
        double rotationAngleY = Math.toRadians(rot.y); // 예시로 45도를 라디안으로 변환
        double[][] rotationMatrixY = {
                {Math.cos(rotationAngleY), 0, Math.sin(rotationAngleY)},
                {0, 1, 0},
                {-Math.sin(rotationAngleY), 0, Math.cos(rotationAngleY)}
        };

        double[] originalDirection = {1, 0, 1};
        double[] rotatedDirection = matrixVectorMultiply(rotationMatrixY, originalDirection);

        return new rVector3D((float)(rotatedDirection[0] * velocity / frameCount), (float)(rotatedDirection[1] * velocity / frameCount), (float)(rotatedDirection[2] * velocity / frameCount));
    }

    public static double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0;
            for (int j = 0; j < vector.length; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }
        return result;
    }

    @Override
    public String toString() {
        return "rVector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

