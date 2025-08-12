package org.firstinspires.ftc.teamcode;

public class DetectionPose2D {
    public double x;
    public double y;
    public String className;
    public double orientationDegrees;

    public DetectionPose2D(double x, double y, String className, double orientationDegrees){
        this.x = x;
        this.y = y;
        this.className = className;
        this.orientationDegrees = orientationDegrees;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public String getClassName(){
        return className;
    }

    public double getOrientationDegrees(){
        return orientationDegrees;
    }

    public double getOrientationRadians(){
        return Math.toRadians(orientationDegrees);
    }
}
