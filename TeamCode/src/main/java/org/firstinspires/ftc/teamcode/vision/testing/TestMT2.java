package org.firstinspires.ftc.teamcode.vision.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.VisionManager;

@Autonomous
public class TestMT2 extends OpMode {
    VisionManager visionManager;
    @Override
    public void init() {
        visionManager = new VisionManager(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        telemetry.addData("yaw", visionManager.getBotPoseAprilTags().getHeading());
        telemetry.addData("x", visionManager.getBotPoseAprilTags().getX());
        telemetry.addData("y", visionManager.getBotPoseAprilTags().getY());
        telemetry.update();
    }
}
