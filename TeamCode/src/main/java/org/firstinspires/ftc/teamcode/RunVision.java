package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class RunVision extends OpMode {
    LLDetectSamples detectorRed;
    final double CAMERA_HEIGHT_INCHES = 10.56;
    final double CAMERA_ANGLE_DEGREES = 25;
    List<String> acceptedClasses = new ArrayList<>();
    @Override
    public void init(){
        acceptedClasses.add("red");
        acceptedClasses.add("blue");
        acceptedClasses.add("yellow");
        detectorRed = new LLDetectSamples(acceptedClasses, hardwareMap, CAMERA_HEIGHT_INCHES, CAMERA_ANGLE_DEGREES);
    }

    @Override
    public void start(){
        detectorRed.startLLScanning();
    }

    @Override
    public void loop(){
        List<DetectionPose2D> results = detectorRed.detectSamples();

        if (results != null){
            for (DetectionPose2D result : results) {
                telemetry.addData("name", result.getClassName());
                telemetry.addData("x", result.getX());
                telemetry.addData("y", result.getY());
            }
        }
        else {
            telemetry.addLine("nothing detected");
        }
        telemetry.update();
    }
}
