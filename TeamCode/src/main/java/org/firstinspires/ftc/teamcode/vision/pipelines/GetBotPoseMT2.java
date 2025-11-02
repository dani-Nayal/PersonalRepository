package org.firstinspires.ftc.teamcode.vision.pipelines;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class GetBotPoseMT2 {
    Limelight3A limelight;
    IMU imu;
    public GetBotPoseMT2(Limelight3A limelight, IMU imu){
        this.limelight = limelight;
        this.imu = imu;

        limelight.setPollRateHz(11);

        limelight.pipelineSwitch(1); /** TODO: Find if index is correct and make pipeline **/
    }
    public Pose3D getBotPoseMT2(){
        double robotYaw = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            return result.getBotpose_MT2();
        }
        return null;
    }
}
