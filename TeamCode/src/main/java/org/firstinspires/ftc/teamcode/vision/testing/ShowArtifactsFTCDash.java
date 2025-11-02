package org.firstinspires.ftc.teamcode.vision.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.VisionManager;
import org.firstinspires.ftc.teamcode.vision.pipelines.FindArtifactRelativePositions;
import org.firstinspires.ftc.teamcode.vision.descriptors.DetectionDescriptor;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class ShowArtifactsFTCDash extends OpMode {
    double ARTIFACT_DIAMETER_INCHES = 5;
    FtcDashboard ftcDashboard;
    VisionManager visionManager;
    @Override
    public void init(){
        visionManager = new VisionManager(hardwareMap);
        ftcDashboard = FtcDashboard.getInstance();
    }

    @Override
    public void loop(){
        List<DetectionDescriptor> detections = visionManager.getDetectionDescriptors();
        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        if (detections != null) {
            for (DetectionDescriptor detection : detections){
                fieldOverlay.setFill(detection.getClassName());
                fieldOverlay.fillCircle(detection.x, detection.y, ARTIFACT_DIAMETER_INCHES / 2);

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
