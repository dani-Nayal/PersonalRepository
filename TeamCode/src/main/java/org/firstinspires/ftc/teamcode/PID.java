package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {
    HardwareConfig hw;
    RobotState state;
    double lastError = 0;
    double lastReference = 0;
    double integralSum = 0;
    double integralSumLimit = 0.25;
    ElapsedTime timer = new ElapsedTime();
    public PID(){
        hw = HardwareConfig.getHardwareConfig();
        state = new RobotState();
    }
    public double getPIDOutput(MotorEnum motorEnum, double reference){

        double encoderPosition = hw.getMotorConfig(motorEnum).motor.getCurrentPosition();

        double error = reference - encoderPosition;

        double derivative = (error - lastError) / timer.seconds();

        integralSum = integralSum + (error * timer.seconds());

        if (reference != lastReference){
            integralSum = 0;
        }
        if (integralSum > integralSumLimit){
            integralSum = integralSumLimit;
        }
        if (integralSum < -integralSumLimit){
            integralSum = -integralSumLimit;
        }
        double proportionalPower = error * hw.getMotorConfig(motorEnum).kP;
        double integralPower = integralSum * hw.getMotorConfig(motorEnum).kI;
        double derivativePower = derivative * hw.getMotorConfig(motorEnum).kD;
        double outPower = proportionalPower + integralPower + derivativePower;

        lastError = error;
        lastReference = reference;
        timer.reset();

        return outPower;
    }
}
