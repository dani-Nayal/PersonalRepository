package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.HardwareConfig;
import org.firstinspires.ftc.teamcode.MotorEnum;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.PID;


@TeleOp
@Config
public class MeasureMotorAcceleration extends LinearOpMode {
    HardwareConfig hw;
    RobotState state;
    PID pid;
    MotorEnum motorEnum = MotorEnum.MOTOR;
    double greatestSpeed = 0;
    double currentSpeed = 0;
    double lastSpeed = 0;
    double currentAcceleration = 0;
    double lastAcceleration = 0;
    double greatestAcceleration = 0;
    ElapsedTime timer = new ElapsedTime();
    @Override
    public void runOpMode(){
        HardwareConfig.makeHardwareConfig(hardwareMap);
        hw = HardwareConfig.getHardwareConfig();
        state = new RobotState();
        pid = new PID();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = dashboard.getTelemetry();

        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.a){
                state.setMotorTarget(motorEnum, 0);
            }
            else if (gamepad1.b){
                state.setMotorTarget(motorEnum,3500);
            }
            DcMotorEx dcMotorEx = (DcMotorEx) (hw.getMotorConfig(motorEnum).motor);
            currentSpeed = dcMotorEx.getVelocity();

            if (currentSpeed > greatestSpeed){
                greatestSpeed = currentSpeed;
            }

            double currentEncoderPosition = hw.getMotorConfig(motorEnum).motor.getCurrentPosition();

            currentAcceleration = (currentSpeed - lastSpeed) / timer.seconds();

            if (currentAcceleration > greatestAcceleration){
                greatestAcceleration = currentAcceleration;
            }

            hw.getMotorConfig(motorEnum).motor.setPower(pid.getPIDOutput(motorEnum, state.getMotorTarget(motorEnum)));

            telemetry.addData("greatest acceleration", greatestAcceleration);
            telemetry.addData("greatest speed", greatestSpeed);
            telemetry.addData("speed", greatestSpeed);
            telemetry.addData("position", currentEncoderPosition);
            telemetry.addData("acceleration", currentAcceleration);
            telemetry.update();

            lastSpeed = currentSpeed;
            lastAcceleration = currentAcceleration;
            timer.reset();
        }
    }
}
