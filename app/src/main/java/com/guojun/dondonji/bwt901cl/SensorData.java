package com.example.guojun.dondonji.bwt901cl;

public class SensorData {
    String date;
    String time;

    double accelerationX;
    double accelerationY;
    double accelerationZ;

    double angularVelocityX;
    double angularVelocityY;
    double angularVelocityZ;

    double angleX;
    double angleY;
    double angleZ;

    double magneticX;
    double magneticY;
    double magneticZ;

    double quaternions0;
    double quaternions1;
    double quaternions2;
    double quaternions3;

    double temperature;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public double getAccelerationZ() {
        return accelerationZ;
    }

    public double getAngularVelocityX() {
        return angularVelocityX;
    }

    public double getAngularVelocityY() {
        return angularVelocityY;
    }

    public double getAngularVelocityZ() {
        return angularVelocityZ;
    }

    public double getAngleX() {
        return angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public double getAngleZ() {
        return angleZ;
    }

    public double getMagneticX() {
        return magneticX;
    }

    public double getMagneticY() {
        return magneticY;
    }

    public double getMagneticZ() {
        return magneticZ;
    }

    public double getQuaternions0() {
        return quaternions0;
    }

    public double getQuaternions1() {
        return quaternions1;
    }

    public double getQuaternions2() {
        return quaternions2;
    }

    public double getQuaternions3() {
        return quaternions3;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return
                " date='" + date + '\'' + "\n" +
                " time='" + time + '\'' + "\n\n" +
                " accelerationX=" + accelerationX + "\n" +
                " accelerationY=" + accelerationY + "\n" +
                " accelerationZ=" + accelerationZ + "\n\n" +
                " angularVelocityX=" + angularVelocityX + "\n" +
                " angularVelocityY=" + angularVelocityY + "\n" +
                " angularVelocityZ=" + angularVelocityZ + "\n\n" +
                " angleX=" + angleX + "\n" +
                " angleY=" + angleY + "\n" +
                " angleZ=" + angleZ + "\n\n" +
                " magneticX=" + magneticX + "\n" +
                " magneticY=" + magneticY + "\n" +
                " magneticZ=" + magneticZ + "\n\n" +
                " quaternions0=" + quaternions0 + "\n" +
                " quaternions1=" + quaternions1 + "\n" +
                " quaternions2=" + quaternions2 + "\n" +
                " quaternions3=" + quaternions3 + "\n\n" +
                " temperature=" + temperature + "\n" ;
    }
}
