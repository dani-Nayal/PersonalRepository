package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Collections;
import java.util.List;
@Autonomous
public class TestSnapScript extends OpMode {
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(11);
        limelight.pipelineSwitch(1);
    }
    @Override
    public void loop(){
        limelight.pipelineSwitch(0);
        LLResult result = limelight.getLatestResult();
        List<List<Double>> corners = Collections.emptyList();
        if (result != null && result.isValid()){
            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();
            for (LLResultTypes.DetectorResult detection : detections){
                corners = detection.getTargetCorners();
            }
            limelight.pipelineSwitch(1);
            limelight.updatePythonInputs(
                    corners.get(0).get(0),
                    corners.get(0).get(1),
                    corners.get(1).get(0),
                    corners.get(1).get(1),
                    corners.get(2).get(0),
                    corners.get(2).get(1),
                    corners.get(3).get(0),
                    corners.get(3).get(1)
            );
            double orientationDegrees;
            double[] pythonOutputs;

            do {

                pythonOutputs = result.getPythonOutput();
            } while (pythonOutputs == null);

            orientationDegrees = pythonOutputs[0];
            telemetry.addData("degrees", orientationDegrees);
        }
        else {
            telemetry.addLine("Nothing detected");
        }
        telemetry.update();
    }
    @Override
    public void start(){
        limelight.start();
    }
}
