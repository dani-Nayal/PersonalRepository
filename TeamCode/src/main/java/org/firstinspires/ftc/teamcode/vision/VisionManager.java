package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import  com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.vision.descriptors.AprilTagDescriptor;
import org.firstinspires.ftc.teamcode.vision.descriptors.ArtifactDescriptor;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;
import org.firstinspires.ftc.teamcode.vision.pipelines.GetBotPoseMT2;
import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;
import org.firstinspires.ftc.teamcode.vision.pipelines.TxTyAprilTag;

import java.util.ArrayList;
import java.util.List;

public class VisionManager {
    // Translation from reference point, usually the lowest center middle of the robot for pedro, to center of Limelight lens
    final double CAMERA_VERTICAL_HEIGHT_INCHES = 5; // Increases up from reference point
    final double CAMERA_OFFSET_X_INCHES = 0; // Increases to the right from reference point
    final double CAMERA_OFFSET_Y_INCHES = 0; // Increases forward from the reference point
    final double CAMERA_DOWNWARD_PITCH_DEGREES = 65; // 90 degrees is facing straight forward, decreases looking down
    final int NN_PIPELINE_INDEX = 0;
    final int APRIL_TAGS_PIPELINE_INDEX = 2;
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
    GetBotPoseMT2 llLocalizer;
    Limelight3A limelight;
    IMU imu;
    List<String> queryClasses = new ArrayList<>();
    public VisionManager(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(11);
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
        llLocalizer = new GetBotPoseMT2(limelight, imu);
    }

    public List<DetectionDescriptor> getDetectionDescriptors(){ // Returns a list of all detections from a single frame
        if (!limelight.isRunning()){
            limelight.start();
        }
        limelight.pipelineSwitch(NN_PIPELINE_INDEX);
        return artifactDetector.getDetectionDescriptors();
    }

    public List<ArtifactDescriptor> getArtifactDescriptors(){
        if (!limelight.isRunning()){
            limelight.start();
        }
        List<DetectionDescriptor> detectionDescriptors = artifactDetector.getDetectionDescriptors();
        for (DetectionDescriptor detection: detectionDescriptors){
            double leftRightOffset = detection.getLeftRightOffset();
            double forwardOffset = detection.getForwardOffset();

        }
        return null;
    }

    public Pose3D getBotPoseAprilTags(){
        if (!limelight.isRunning()){
            limelight.start();
        }
        return llLocalizer.getBotPoseMT2();
    }

    public List<AprilTagDescriptor> getAprilTagDescriptors(){ // Returns a list of all detections from a single frame
        if (!limelight.isRunning()){
            limelight.start();
        }
        limelight.pipelineSwitch(APRIL_TAGS_PIPELINE_INDEX);
        return aprilTagDetector.getAprilTagDescriptors();
    }
    public void stopLimelight(){
        limelight.stop();
    }

    public String getCurrentPipeline(){
        if (!limelight.isRunning()){
            limelight.start();
        }
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){

            int index = result.getPipelineIndex();

            if (index == NN_PIPELINE_INDEX) return "Neural Network";
            else if (index == APRIL_TAGS_PIPELINE_INDEX) return "April Tag";
            else return "Unknown Pipeline";
        }

        return "Result is needed to determine pipeline index";
    }
}
