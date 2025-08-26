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

public LLDetectSamples(List<String> queryClassNames, HardwareMap hardwareMap, final double CAMERA_VERTICAL_HEIGHT_INCHES, final double CAMERA_DOWNWARD_ANGLE_DEGREES){
        this.queryClassNames = queryClassNames;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(0);

        this.CAMERA_HEIGHT_INCHES = CAMERA_VERTICAL_HEIGHT_INCHES;
        this.CAMERA_ANGLE_DEGREES = CAMERA_DOWNWARD_ANGLE_DEGREES;

    }

    public List<DetectionPose2D> detectSamples(){
        limelight.pipelineSwitch(0);
        LLResult result = limelight.getLatestResult();
        List<DetectionPose2D> detections = new ArrayList<>();
        if (result != null && result.isValid()){
            for (LLResultTypes.DetectorResult detection : result.getDetectorResults()){

                String className = detection.getClassName();

                if ((className.equals("blue") && queryClassNames.contains("blue")) || (className.equals("red") && queryClassNames.contains("red")) || (className.equals("yellow") && queryClassNames.contains("yellow"))) {
                    double tx = detection.getTargetXDegrees(); // Compensates for camera rotation
                    double ty = detection.getTargetYDegrees();
                    double trigAngle = CAMERA_ANGLE_DEGREES + ty;

                    double radians = Math.toRadians(trigAngle);
                    double depth = CAMERA_HEIGHT_INCHES / Math.tan(radians);

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx));

                    /*
                    List<List<Double>> corners = detection.getTargetCorners();

                    limelight.pipelineSwitch(1);

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

                     */

                    detections.add(new DetectionPose2D(depth, horizontalOffset, className));
                }
            }
            return detections;
        }
        return null;
    }

    public void startLLScanning(){
        limelight.start();
    }

    public Limelight3A getLimelightInstance(){
        return limelight;
    }

}
