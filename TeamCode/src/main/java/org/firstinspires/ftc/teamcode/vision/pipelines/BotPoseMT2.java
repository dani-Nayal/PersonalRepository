package org.firstinspires.ftc.teamcode.vision.pipelines;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class BotPoseMT2 {
    Limelight3A limelight;
    IMU imu;
    public BotPoseMT2(Limelight3A limelight, IMU imu){
        this.limelight = limelight;
        this.imu = imu;
    }
    public Pose2D getBotPoseMT2(){
        double robotYaw =
    }
}
