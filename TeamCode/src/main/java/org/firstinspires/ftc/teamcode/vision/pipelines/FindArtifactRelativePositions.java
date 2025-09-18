package org.firstinspires.ftc.teamcode.vision.pipelines;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;

import java.util.ArrayList;
import java.util.List;

public class FindArtifactRelativePositions {
    List<String> queryClassNames;
    Limelight3A limelight;
    double[][] K;
    final double CAMERA_VERTICAL_HEIGHT_INCHES;
    final double CAMERA_OFFSET_X_INCHES;
    final double CAMERA_OFFSET_Y_INCHES;
    final double CAMERA_DOWNWARD_ANGLE_DEGREES;
    public FindArtifactRelativePositions(List<String> queryClassNames,
                                         Limelight3A limelight,
                                         final double CAMERA_VERTICAL_HEIGHT_INCHES,
                                         final double CAMERA_OFFSET_X_INCHES,
                                         final double CAMERA_OFFSET_Y_INCHES,
                                         final double CAMERA_DOWNWARD_ANGLE_DEGREES,
                                         final double[][] K)
    {
        this.queryClassNames = queryClassNames;

        this.limelight = limelight;

        this.CAMERA_VERTICAL_HEIGHT_INCHES = CAMERA_VERTICAL_HEIGHT_INCHES;
        this.CAMERA_OFFSET_X_INCHES = CAMERA_OFFSET_X_INCHES;
        this.CAMERA_OFFSET_Y_INCHES = CAMERA_OFFSET_Y_INCHES;
        this.CAMERA_DOWNWARD_ANGLE_DEGREES = CAMERA_DOWNWARD_ANGLE_DEGREES;

        this.K = K;

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(0);
    }

    public List<DetectionDescriptor> getDetectionDescriptors(){
        if (!limelight.isRunning()){
            limelight.start();
        }
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

                    double tx = Math.toDegrees(Math.atan2(targetLocationPixels[0] - K[0][2], K[0][0]));
                    double ty = Math.toDegrees(Math.atan2(K[2][2] - targetLocationPixels[1], K[1][1])); // Y dimension of camera increases downward
                    double verticalAngle = CAMERA_DOWNWARD_ANGLE_DEGREES + ty;

                    double depth = CAMERA_VERTICAL_HEIGHT_INCHES * Math.tan(Math.toRadians(verticalAngle)) + CAMERA_OFFSET_Y_INCHES;

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx)) + CAMERA_OFFSET_X_INCHES;

                    DetectionDescriptor detection = new DetectionDescriptor();
                    detection.setTx(tx);
                    detection.setTy(ty);
                    detection.setX(horizontalOffset);
                    detection.setY(depth);
                    detection.setClassName(className);
                    detection.setCorners(corners);
                    detection.setTargetPixels(targetLocationPixels);
                    detectionDescriptors.add(detection);

                    detection.getData()[0] = K[0][2];
                    detection.getData()[1] = K[1][2];
                }
            }
            return detectionDescriptors;
        }
        return null;
    }
}
