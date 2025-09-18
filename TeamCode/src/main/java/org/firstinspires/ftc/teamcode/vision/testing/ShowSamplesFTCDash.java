package org.firstinspires.ftc.teamcode.vision.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;
import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class ShowSamplesFTCDash extends OpMode {
    double SAMPLE_LENGTH = 3.5;
    double SAMPLE_WIDTH = 1.5;
    double fx = 1218.145;
    double fy = 1219.481;
    double cx = 621.829;
    double cy = 500.362;
    final double[][] K = {
            {fx, 0.0, cx},
            {0.0, fy, cy},
            {0.0, 0.0, 1.0}
    };
    FtcDashboard ftcDashboard;
    FindArtifactRelativePositions detector;
    Limelight3A limelight;
    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        List<String> queryClassNames = new ArrayList<>();
        queryClassNames.add("red");
        queryClassNames.add("blue");
        queryClassNames.add("yellow");
        ftcDashboard = FtcDashboard.getInstance();
        detector = new FindArtifactRelativePositions(queryClassNames, limelight, 5, 0, 0, 65, K);
    }

    @Override
    public void loop(){
        List<DetectionDescriptor> detections = detector.getDetectionDescriptors();
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
                packet.put("cx", detection.getData()[0]);
                packet.put("cy", detection.getData()[1]);
            }
            ftcDashboard.sendTelemetryPacket(packet);
        }
        else{
            packet.addLine("nothing detected");
        }
    }
}
