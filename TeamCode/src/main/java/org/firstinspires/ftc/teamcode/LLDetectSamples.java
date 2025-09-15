package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;

public class LLDetectSamples {
    Limelight3A limelight;
    final double CAMERA_HEIGHT_INCHES;
    final double CAMERA_ANGLE_DEGREES;
    List<String> queryClassNames;
    double fx = 1218.145;
    double fy = 1219.481;
    double cx = 621.829;
    double cy = 500.362;
    public LLDetectSamples(List<String> queryClassNames, HardwareMap hardwareMap, final double CAMERA_VERTICAL_HEIGHT_INCHES, final double CAMERA_DOWNWARD_ANGLE_DEGREES){

        this.queryClassNames = queryClassNames;

        this.limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(0);

        this.CAMERA_HEIGHT_INCHES = CAMERA_VERTICAL_HEIGHT_INCHES;
        this.CAMERA_ANGLE_DEGREES = CAMERA_DOWNWARD_ANGLE_DEGREES;
    }

    public List<DetectionDescriptor> detectSamples(){
        List<DetectionDescriptor> detectionDescriptors = new ArrayList<>(); // Output array
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            for (LLResultTypes.DetectorResult detectorResult : result.getDetectorResults()){
                String className = detectorResult.getClassName();

                if (queryClassNames.contains(className)) {
                    List<List<Double>> corners = detectorResult.getTargetCorners();
                    double[] targetLocationPixels = new double[2];
                    targetLocationPixels[0] = (corners.get(2).get(0) + corners.get(3).get(0)) / 2; // We want the target to be the bottom center of the bounding box
                    targetLocationPixels[1] = (corners.get(3).get(1));

                    double tx = Math.toDegrees(Math.atan2(targetLocationPixels[0] - cx, fx));
                    double ty = Math.toDegrees(Math.atan2(cy - targetLocationPixels[1], fy)); // Y dimension of camera increases downward
                    double verticalAngle = CAMERA_ANGLE_DEGREES + ty;

                    double depth = CAMERA_HEIGHT_INCHES * Math.tan(Math.toRadians(verticalAngle));

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx));

                    DetectionDescriptor detection = new DetectionDescriptor();
                    detection.setTx(tx);
                    detection.setTy(ty);
                    detection.setX(horizontalOffset);
                    detection.setY(depth);
                    detection.setClassName(className);
                    detection.setCorners(corners);
                    detectionDescriptors.add(detection);

                    detection.getData()[0] = targetLocationPixels[0];
                    detection.getData()[1] = targetLocationPixels[1];
                    detection.getData()[2] = cx;
                    detection.getData()[3] = cy;
                }
            }
            return detectionDescriptors;
        }
        return null;
    }
    public void startLLScanning(){
        limelight.start();
    }

}
