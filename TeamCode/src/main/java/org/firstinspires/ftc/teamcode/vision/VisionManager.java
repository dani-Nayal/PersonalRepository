package org.firstinspires.ftc.teamcode.vision;

import  com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.vision.descriptors.AprilTagDescriptor;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;
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
    Limelight3A limelight;
    List<String> queryClasses = new ArrayList<>();
    public VisionManager(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
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
    }

    public List<DetectionDescriptor> getArtifactsRelativePosition(){ // Returns a list of all detections from a single frame
        return artifactDetector.getDetectionDescriptors();
    }

    public List<AprilTagDescriptor> getTxTy(){ // Returns a list of all detections from a single frame
        return aprilTagDetector.getAprilTagDescriptors();
    }
}
