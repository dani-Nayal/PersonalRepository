package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class RunVision extends OpMode {
    LLDetectSamples detectorRed;
    final double CAMERA_HEIGHT_INCHES = 10.56;
    final double CAMERA_ANGLE_DEGREES = 65;
    List<String> acceptedClasses = new ArrayList<>();
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        acceptedClasses.add("red");
        acceptedClasses.add("blue");
        acceptedClasses.add("yellow");
        detectorRed = new LLDetectSamples(acceptedClasses, limelight, CAMERA_HEIGHT_INCHES, CAMERA_ANGLE_DEGREES);
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
                telemetry.addData("name", result.getClassName());
                telemetry.addData("x", result.getX());
                telemetry.addData("y", result.getY());
                telemetry.addData("degrees", result.getOrientationDegrees());
            }
        }
        else {
            telemetry.addLine("nothing detected");
        }
        telemetry.update();
    }
}
