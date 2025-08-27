package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.ArrayList;
import java.util.List;

public class LLDetectSamples {
    Limelight3A limelight;
    final double CAMERA_HEIGHT_INCHES;
    final double CAMERA_ANGLE_DEGREES;
    List<String> queryClassNames;

    public LLDetectSamples(List<String> queryClassNames, Limelight3A limelight, final double CAMERA_VERTICAL_HEIGHT_INCHES, final double CAMERA_DOWNWARD_ANGLE_DEGREES){
        this.queryClassNames = queryClassNames;

        this.limelight = limelight;

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
                    double tx = detectorResult.getTargetXDegrees();
                    double ty = detectorResult.getTargetYDegrees();
                    double verticalAngle = CAMERA_ANGLE_DEGREES + ty;

                    double depth = CAMERA_HEIGHT_INCHES * Math.tan(Math.toRadians(verticalAngle));

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx));

                    DetectionDescriptor detection = new DetectionDescriptor();
                    detection.setTx(tx);
                    detection.setTy(ty);
                    detection.setX(horizontalOffset);
                    detection.setY(depth);
                    detection.setClassName(className);
                    detectionDescriptors.add(detection);
                }
            }
            return detectionDescriptors;
        }
        return null;
    }
    public List<DetectionDescriptor> detectSamplesWithOrientation(){
        List<DetectionDescriptor> detectionDescriptors = new ArrayList<>(); // Output array
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            for (LLResultTypes.DetectorResult detectorResult : result.getDetectorResults()){
                String className = detectorResult.getClassName();

                if (queryClassNames.contains(className)) {
                    double tx = detectorResult.getTargetXDegrees();
                    double ty = detectorResult.getTargetYDegrees();
                    double verticalAngle = CAMERA_ANGLE_DEGREES + ty;

                    double depth = CAMERA_HEIGHT_INCHES * Math.tan(Math.toRadians(verticalAngle));

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx));

                    List<List<Double>> corners = detectorResult.getTargetCorners();

                    DetectionDescriptor detection = new DetectionDescriptor();
                    detection.setTx(tx);
                    detection.setTy(ty);
                    detection.setX(horizontalOffset);
                    detection.setY(depth);
                    detection.setCorners(corners);
                    detection.setClassName(className);
                    detectionDescriptors.add(detection);
                }
            }
            limelight.pipelineSwitch(1);

            for (DetectionDescriptor detectionDescriptor : detectionDescriptors){
                List<List<Double>> corners = detectionDescriptor.getCorners();

                limelight.updatePythonInputs(
                        corners.get(0).get(0),
                        corners.get(0).get(1),
                        corners.get(1).get(0),
                        corners.get(1).get(1),
                        corners.get(2).get(0),
                        corners.get(2).get(1),
                        corners.get(3).get(0),
                        corners.get(3).get(1)
                );

                double orientationDegrees;
                double[] pythonOutputs;

                do {
                    pythonOutputs = result.getPythonOutput();
                } while (pythonOutputs == null);

                orientationDegrees = pythonOutputs[0];
                detectionDescriptor.setOrientationDegrees(orientationDegrees);
            }


            return detectionDescriptors;
        }
        return null;
    }

    public void startLLScanning(){
        limelight.start();
    }

}
