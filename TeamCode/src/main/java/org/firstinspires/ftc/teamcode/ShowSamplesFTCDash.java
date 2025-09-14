package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class ShowSamplesFTCDash extends OpMode {
    double SAMPLE_LENGTH = 3.5;
    double SAMPLE_WIDTH = 1.5;
    final double[][] K = {
            {1218.145, 0.0, 621.829},
            {0.0, 1219.481, 500.362},
            {0.0, 0.0, 1.0}
    };
    FtcDashboard ftcDashboard;
    LLDetectSamples detector;
    @Override
    public void init(){
        List<String> queryClassNames = new ArrayList<>();
        queryClassNames.add("red");
        queryClassNames.add("blue");
        queryClassNames.add("yellow");
        ftcDashboard = FtcDashboard.getInstance();
        detector = new LLDetectSamples(queryClassNames, hardwareMap, 5, 65, K);
    }
    @Override
    public void start(){
        detector.startLLScanning();
    }
    @Override
    public void loop(){
        List<DetectionDescriptor> detections = detector.detectSamples();
        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        fieldOverlay.setStroke("blue");
        fieldOverlay.strokeCircle(0, 0, 9);

        if (detections != null) {
            for (DetectionDescriptor detection : detections){
                fieldOverlay.setFill(detection.getClassName());
                fieldOverlay.fillRect(
                        detection.x - SAMPLE_LENGTH / 2, detection.y - SAMPLE_WIDTH / 2,
                        3.5, 1.5
                );
                packet.put("class", detection.getClassName());
                packet.put("x", detection.getX());
                packet.put("y", detection.getY());
                packet.put("tx", detection.getTx());
                packet.put("ty", detection.getTy());
                packet.put("corners", detection.getCorners());
                packet.put("target pixels x", detection.getData()[0]);
                packet.put("target pixels y", detection.getData()[1]);
            }
            ftcDashboard.sendTelemetryPacket(packet);
        }
        else{
            packet.addLine("nothing detected");
        }
    }
}
