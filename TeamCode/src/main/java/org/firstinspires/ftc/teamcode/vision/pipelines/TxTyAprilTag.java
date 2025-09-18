package org.firstinspires.ftc.teamcode.vision.pipelines;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.vision.descriptors.AprilTagDescriptor;

import java.util.ArrayList;
import java.util.List;

public class TxTyAprilTag {
    Limelight3A limelight;
    public TxTyAprilTag(Limelight3A limelight){
        this.limelight = limelight;
        limelight.setPollRateHz(11);
        limelight.pipelineSwitch(3);
    }
    public List<AprilTagDescriptor> getAprilTagDescriptors(){
        List<AprilTagDescriptor> aprilTagDescriptors = new ArrayList<>();
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()){
            for (LLResultTypes.FiducialResult fiducialResult : result.getFiducialResults()){
                AprilTagDescriptor aprilTagDescriptor = new AprilTagDescriptor();
                aprilTagDescriptor.setId(fiducialResult.getFiducialId());
                aprilTagDescriptor.setTx(fiducialResult.getTargetXDegrees());
                aprilTagDescriptor.setTy(fiducialResult.getTargetYDegrees());
                aprilTagDescriptors.add(aprilTagDescriptor);
            }
            return aprilTagDescriptors;
        }
        return null;
    }
}
