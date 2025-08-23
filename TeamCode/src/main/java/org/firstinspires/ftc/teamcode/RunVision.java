package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

@Autonomous
public class RunVision extends OpMode {
    LLDetectSamples detectorRed;
    final double CAMERA_HEIGHT_INCHES = 10.56;
    final double CAMERA_ANGLE_DEGREES = 25;
    @Override
    public void init(){
        detectorRed = new LLDetectSamples("red", hardwareMap, CAMERA_HEIGHT_INCHES, CAMERA_ANGLE_DEGREES);

    }

    @Override
    public void start(){
        detectorRed.startLLScanning();
    }

    @Override
    public void loop(){
        List<DetectionPose2D> results = detectorRed.detectSamples();

        if (results != null){
            int num_detections = 0;
            // int detectionID = 1;
            for (DetectionPose2D detection : results){
                num_detections++;
                /*
                telemetry.addLine("---- DETECTION " + detectionID + " ----");
                telemetry.addData("x", detection.getX());
                telemetry.addData("y", detection.getY());
                telemetry.addData("class name", detection.getClassName());
                telemetry.addData("orientation", detection.getOrientationDegrees());
                telemetry.addData("pipeline #", detectorRed.getLimelightInstance().getStatus().getPipelineIndex());
                telemetry.addLine("                                         ");
                detectionID++;
                 */

            }
            telemetry.addData("num detections", num_detections);
            telemetry.update();
        }
        else {
            telemetry.addLine("Detector results are null");
            telemetry.addData("pipeline #", detectorRed.getLimelightInstance().getStatus().getPipelineIndex());
            telemetry.update();
        }
    }
}
