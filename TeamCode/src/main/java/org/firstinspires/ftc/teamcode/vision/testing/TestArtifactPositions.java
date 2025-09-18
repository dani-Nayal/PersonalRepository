package org.firstinspires.ftc.teamcode.vision.testing;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class TestArtifactPositions extends OpMode {
    FindArtifactRelativePositions detector;
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
        detector = new FindArtifactRelativePositions(acceptedClasses, limelight, 5, 0, 0, 65, K);
    }

    @Override
    public void loop(){
        List<DetectionDescriptor> results = detector.getDetectionDescriptors();

        if (results != null){
            for (DetectionDescriptor result : results) {
                telemetry.addData("class", result.getClassName());
                telemetry.addData("x", result.getX());
                telemetry.addData("y", result.getY());
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("target pixels x", result.getTargetPixels()[0]);
                telemetry.addData("target pixels y", result.getTargetPixels()[1]);
            }
        }
        else {
            telemetry.addLine("nothing detected");
        }
        telemetry.update();
    }
}
