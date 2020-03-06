package com.example.arduinosensors;

/**
 * Created by sideyilmaz on 07/05/2017.
 */

public class Data {
    float ax;
    float ay;
    float az;
    float gx;
    float gy;
    float gz;
    float mx;
    float my;
    float mz;
    float yaw;
    float pitch;
    float roll;

    public Data(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz, float yaw, float pitch, float roll) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.gx = gx;
        this.gy = gy;
        this.gz = gz;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }


    public float getAx() {
        return ax;
    }

    public float getAy() {
        return ay;
    }

    public float getAz() {
        return az;
    }

    public float getGx() {
        return gx;
    }

    public float getGy() {
        return gy;
    }

    public float getGz() {
        return gz;
    }

    public float getMx() {
        return mx;
    }

    public float getMy() {
        return my;
    }

    public float getMz() {
        return mz;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }
}
