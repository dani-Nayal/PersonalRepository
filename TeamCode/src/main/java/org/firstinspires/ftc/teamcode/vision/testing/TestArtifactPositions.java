package org.firstinspires.ftc.teamcode.vision.testing;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.VisionManager;
import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class TestArtifactPositions extends OpMode {
    VisionManager visionManager;

    @Override
    public void init(){
        visionManager = new VisionManager(hardwareMap);
    }

    @Override
    public void loop(){
        List<DetectionDescriptor> detections = visionManager.getDetectionDescriptors();

        if (detections != null){
            for (DetectionDescriptor detection : detections) {
                telemetry.addData("class", detection.getClassName());
                telemetry.addData("x", detection.getX());
                telemetry.addData("y", detection.getY());
                telemetry.addData("tx", detection.getTx());
                telemetry.addData("ty", detection.getTy());
                telemetry.addData("target pixels x", detection.getTargetPixels()[0]);
                telemetry.addData("target pixels y", detection.getTargetPixels()[1]);
            }
        }
        else {
            telemetry.addLine("nothing detected");
        }
        telemetry.update();
    }
}
