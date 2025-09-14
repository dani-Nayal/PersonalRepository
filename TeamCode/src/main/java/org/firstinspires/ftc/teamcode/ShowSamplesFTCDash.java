package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class ShowSamplesFTCDash extends OpMode {
    double SAMPLE_LENGTH = 3.5;
    double SAMPLE_WIDTH = 1.5;
    FtcDashboard ftcDashboard;
    LLDetectSamples detector;
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        List<String> queryClassNames = new ArrayList<>();
        queryClassNames.add("red");
        queryClassNames.add("blue");
        queryClassNames.add("yellow");
        ftcDashboard = FtcDashboard.getInstance();
        detector = new LLDetectSamples(queryClassNames, limelight, 5, 65);
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
            }
            ftcDashboard.sendTelemetryPacket(packet);
        }
        else{
            packet.addLine("nothing detected");
        }

    }

}
