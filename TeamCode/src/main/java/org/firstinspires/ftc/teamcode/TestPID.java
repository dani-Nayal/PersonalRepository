package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
@Config
public class TestPID extends LinearOpMode {
    HardwareConfig hw;
    RobotState state;
    public static double KP = 0.0427;
    public static double KI;
    public static double KD = 0.0005;
    @Override
    public void runOpMode(){
        HardwareConfig.makeHardwareConfig(hardwareMap);
        hw = HardwareConfig.getHardwareConfig();
        state = new RobotState();

        MotorEnum testingMotor = MotorEnum.MOTOR;

        FtcDashboard dashboard = FtcDashboard.getInstance();

        Telemetry telemetry = dashboard.getTelemetry();

        ElapsedTime timer = new ElapsedTime();

        double lastError = 0;

        double lastReference = 0;

        double integralSum = 0;

        double integralSumLimit = 0.25;

        waitForStart();

        while (opModeIsActive()){

            if (gamepad1.a){
                state.setMotorTarget(testingMotor, (int) (hw.getMotorConfig(testingMotor).maxTarget * 0.25));
            }
            else if (gamepad1.b){
                state.setMotorTarget(testingMotor, (int) (hw.getMotorConfig(testingMotor).maxTarget * 0.5));
            }
            else if (gamepad1.y){
                state.setMotorTarget(testingMotor, (int) (hw.getMotorConfig(testingMotor).maxTarget * 0.75));
            }
            else if (gamepad1.x){
                state.setMotorTarget(testingMotor, hw.getMotorConfig(testingMotor).maxTarget);
            }

            double reference = state.getMotorTarget(testingMotor);

            double encoderPosition = hw.getMotorConfig(testingMotor).motor.getCurrentPosition();

            double error = state.getMotorTarget(testingMotor) - encoderPosition;

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

            double proportionalPower = error * KP;

            double integralPower = integralSum * KI;

            double derivativePower = derivative * KD;

            double outPower = proportionalPower + integralPower + derivativePower;

            hw.getMotorConfig(testingMotor).motor.setPower(outPower);

            lastError = error;

            lastReference = reference;

            timer.reset();

            telemetry.addData("motor target", state.getMotorTarget(testingMotor));
            telemetry.addData("motor position", hw.getMotorConfig(testingMotor).motor.getCurrentPosition());
            telemetry.addData("motor power", outPower);
            telemetry.update();
        }
    }
}
