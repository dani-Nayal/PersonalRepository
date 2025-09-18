package org.firstinspires.ftc.teamcode.vision.descriptors;

import java.util.List;

public class DetectionDescriptor {
    double tx;
    double ty;
    String className;
    public double x;
    public double y;
    List<List<Double>> corners;
    double[] targetPixels = new double[2];
    double[] data = new double[10];
    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public void setTy(double ty) {
        this.ty = ty;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double[] getData(){return data;}
    public List<List<Double>> getCorners() {
        return corners;
    }
    public void setCorners(List<List<Double>> corners) {
        this.corners = corners;
    }
    public double[] getTargetPixels(){
        return targetPixels;
    }
    public void setTargetPixels(double x, double y){
        targetPixels[0] = x;
        targetPixels[1] = y;
    }
    public void setTargetPixels(double[] targetPixels){
        this.targetPixels = targetPixels;
    }

}
