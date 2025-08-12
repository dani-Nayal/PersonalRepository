package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;

public class LLDetectSamples {
    Limelight3A limelight;
    final double CAMERA_HEIGHT_INCHES = 10.56;
    final double CAMERA_ANGLE_DEGREES = 25;
    String queryClassName;

    public LLDetectSamples(String queryClassName, HardwareMap hardwareMap){
        this.queryClassName = queryClassName;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(0);
    }

    public List<DetectionPose2D> detectSamples(){
        LLResult result = limelight.getLatestResult();
        List<DetectionPose2D> detections = new ArrayList<>();
        if (result != null && result.isValid()){
            for (LLResultTypes.DetectorResult detection : result.getDetectorResults()){

                String className = detection.getClassName();

                if (className.equals(queryClassName)) {
                    double ty = -detection.getTargetXDegrees(); // Compensates for camera rotation
                    double tx = detection.getTargetYDegrees();
                    double trigAngle = CAMERA_ANGLE_DEGREES + ty;

                    double radians = Math.toRadians(trigAngle);
                    radians = Math.max(Math.toRadians(1), radians);
                    double depth = CAMERA_HEIGHT_INCHES / Math.tan(radians);

                    double horizontalOffset = depth * Math.tan(Math.toRadians(tx));

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

                    double[] pythonOutputs = result.getPythonOutput();
                    if (pythonOutputs != null && pythonOutputs.length > 0){
                        orientationDegrees = pythonOutputs[0];
                    }
                    else{
                        throw new RuntimeException("No python outputs");
                    }
                    detections.add(new DetectionPose2D(depth, horizontalOffset, className, orientationDegrees));
                    DetectionPose2D detectionPose2D = new DetectionPose2D(1,1,"1", 1);
                    detectionPose2D.getX();
                }
            }
            return detections;
        }
        return null;
    }

    public void startLLScanning(){
        limelight.start();
    }

}
