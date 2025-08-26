package org.firstinspires.ftc.teamcode;

public class DetectionPose2D {
    public double x;
    public double y;
    public String className;

    public DetectionPose2D(double x, double y, String className){
        this.x = x;
        this.y = y;
        this.className = className;
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

}
