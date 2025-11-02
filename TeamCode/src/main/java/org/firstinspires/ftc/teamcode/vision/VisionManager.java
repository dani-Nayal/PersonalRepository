package org.firstinspires.ftc.teamcode.vision;

import  com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.vision.descriptors.AprilTagDescriptor;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;
import org.firstinspires.ftc.teamcode.vision.pipelines.BotPoseMT2;
import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;
import org.firstinspires.ftc.teamcode.vision.pipelines.TxTyAprilTag;

import java.util.ArrayList;
import java.util.List;

public class VisionManager {
    // Translation from reference point, usually the lowest center middle of the robot for pedro
    final double CAMERA_VERTICAL_HEIGHT_INCHES = 5;
    final double CAMERA_OFFSET_X_INCHES = 0; // Increases to the right from reference point
    final double CAMERA_OFFSET_Y_INCHES = 0; // Increases up from the reference point
    final double CAMERA_DOWNWARD_PITCH_DEGREES = 65; // 90 degrees is facing straight forward
    double fx = 1218.145;
    double fy = 1219.481;
    double cx = 621.829;
    double cy = 500.362;
    final double[][] K = {
            {fx, 0.0, cx},
            {0.0, fy, cy},
            {0.0, 0.0, 1.0}
    };
    FindArtifactRelativePositions artifactDetector;
    TxTyAprilTag aprilTagDetector;
    BotPoseMT2 llLocalizer;
    Limelight3A limelight;
    IMU imu;
    List<String> queryClasses = new ArrayList<>();
    public VisionManager(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        imu = hardwareMap.get(IMU.class, "imu");
        queryClasses.add("purple");
        queryClasses.add("green");
        artifactDetector = new FindArtifactRelativePositions(
                queryClasses, limelight,
                CAMERA_VERTICAL_HEIGHT_INCHES,
                CAMERA_OFFSET_X_INCHES,
                CAMERA_OFFSET_Y_INCHES,
                CAMERA_DOWNWARD_PITCH_DEGREES,
                K);
        aprilTagDetector = new TxTyAprilTag(limelight);
        llLocalizer = new BotPoseMT2(limelight, imu);
    }

    public List<DetectionDescriptor> getDetectionDescriptors(){ // Returns a list of all detections from a single frame
        return artifactDetector.getDetectionDescriptors();
    }

    public Pose3D getBotPose(){
        return llLocalizer.getBotPoseMT2();
    }

    public List<AprilTagDescriptor> getAprilTagDescriptors(){ // Returns a list of all detections from a single frame
        return aprilTagDetector.getAprilTagDescriptors();
    }
    public void startLimelight(){
        limelight.start();
    }
}
