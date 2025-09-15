package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class RunVision extends OpMode {
    LLDetectSamples detectorRed;
    final double CAMERA_HEIGHT_INCHES = 5;
    final double CAMERA_ANGLE_DEGREES = 65;
    final double[][] K = {
        {1218.145, 0.0, 621.829},
        {0.0, 1219.481, 500.362},
        {0.0, 0.0, 1.0}
    };
    List<String> acceptedClasses = new ArrayList<>();
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
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
        List<DetectionDescriptor> results = detectorRed.detectSamples();

        if (results != null){
            for (DetectionDescriptor result : results) {
                telemetry.addData("class", result.getClassName());
                telemetry.addData("x", result.getX());
                telemetry.addData("y", result.getY());
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
            }
        }
        else {
            telemetry.addLine("nothing detected");
        }
        telemetry.update();
    }
}
