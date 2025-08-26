package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
@Autonomous
public class TestDetection extends OpMode {
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(11);
        limelight.pipelineSwitch(0);
    }
    @Override
    public void start(){
        limelight.start();
    }
    @Override
    public void loop(){
        LLResult result = limelight.getLatestResult();
        int num_detections = 0;
        if (result != null && result.isValid()){
            for (LLResultTypes.DetectorResult detection : result.getDetectorResults()){
                num_detections++;
            }
        }
        else {
            telemetry.addLine("Nothing detected");

        }
        telemetry.addData("num detections", num_detections);
        telemetry.update();
    }

}
