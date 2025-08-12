package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

@Autonomous
public class RunVision extends OpMode {
    LLDetectSamples detectorRed;
    @Override
    public void init(){
        detectorRed = new LLDetectSamples("red", hardwareMap);

    }

    @Override
    public void start(){
        detectorRed.startLLScanning();
    }

    @Override
    public void loop(){
        List<DetectionPose2D> results = detectorRed.detectSamples();

        for (DetectionPose2D detection : results){
            telemetry.addData("x", detection.getX());
            telemetry.addData("y", detection.getY());
            telemetry.addData("class name", detection.getClassName());
            telemetry.addData("orientation", detection.getOrientationDegrees());
        }
        telemetry.update();
    }
}
