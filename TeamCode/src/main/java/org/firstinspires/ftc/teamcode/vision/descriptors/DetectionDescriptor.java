package org.firstinspires.ftc.teamcode.vision.descriptors;

import java.util.List;

public class DetectionDescriptor {
    double tx;
    double ty;
    String className;
    public double xOffset;
    public double yOffset;
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

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double y) {
        this.yOffset = y;
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
