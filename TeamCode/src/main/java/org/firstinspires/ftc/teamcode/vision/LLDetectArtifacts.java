package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DetectionDescriptor;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class LLDetectArtifacts {
    Limelight3A limelight;
    final double CAMERA_HEIGHT_INCHES;
    final double CAMERA_ANGLE_DEGREES;
    final double[][] K;
    List<String> queryClassNames;

    public LLDetectArtifacts(List<String> queryClassNames, HardwareMap hardwareMap, final double CAMERA_VERTICAL_HEIGHT_INCHES, final double CAMERA_DOWNWARD_ANGLE_DEGREES, final double[][] K){

        this.queryClassNames = queryClassNames;

        this.limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(0);

        this.CAMERA_HEIGHT_INCHES = CAMERA_VERTICAL_HEIGHT_INCHES;
        this.CAMERA_ANGLE_DEGREES = CAMERA_DOWNWARD_ANGLE_DEGREES;
        this.K = K;
    }

    public List<DetectionDescriptor> detectArtifacts(){
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
                    targetLocationPixels[0] = (corners.get(1).get(0) - corners.get(0).get(0)) / 2; // We want the target to be the bottom center of the bounding box
                    targetLocationPixels[1] = (corners.get(0).get(1));

                    double tx = Math.toDegrees(Math.atan2((targetLocationPixels[0] - K[0][2]), K[0][0]));
                    double ty = Math.toDegrees(Math.atan2(targetLocationPixels[1] - K[1][2], K[1][1]));
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
                }
            }
            return detectionDescriptors;
        }
        return null;
    }
}
