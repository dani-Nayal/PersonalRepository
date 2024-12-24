package org.firstinspires.ftc.teamcode;

import java.util.HashMap;

public class RobotState {
    HashMap<MotorEnum, Integer> targets = new HashMap<>(10);
    HashMap<ServoEnum, Double> positions = new HashMap<>(10);
    public RobotState(){
        targets.put(MotorEnum.MOTOR, 0);
        positions.put(ServoEnum.SERVO, 0.0);
    }
    public int getMotorTarget(MotorEnum motorEnum) throws IllegalArgumentException{
        Integer target = targets.get(motorEnum);
        if (target == null) {
            throw new IllegalArgumentException("No target set for " + motorEnum.name());
        }
        return target;
    }
    public void setMotorTarget(MotorEnum motorEnum, int target){
        targets.put(motorEnum, target);
    }
    public double getServoPosition(ServoEnum servoEnum) throws IllegalArgumentException{
        Double position = positions.get(servoEnum);
        if (position == null){
            throw new IllegalArgumentException("No position set for" + servoEnum.name());
        }
        return position;
    }
    public void setServoPosition(ServoEnum servoEnum, double position){
        HardwareConfig hw = HardwareConfig.getHardwareConfig();

        position = Math.max(position, hw.getServoConfig(servoEnum).minServoPosition);
        position = Math.min(position, hw.getServoConfig(servoEnum).maxServoPosition);

        positions.put(servoEnum, position);

    }
}
