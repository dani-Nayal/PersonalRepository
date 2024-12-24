package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@TeleOp
public class TestMotionProfiles extends LinearOpMode {
    HardwareConfig hw;
    RobotState state;
    MotorControl control;
    MotorEnum motorEnum = MotorEnum.MOTOR;
    @Override
    public void runOpMode(){
        HardwareConfig.makeHardwareConfig(hardwareMap);
        hw = HardwareConfig.getHardwareConfig();
        state = new RobotState();
        control = new MotorControl();

        FtcDashboard dashboard = FtcDashboard.getInstance();

        Telemetry telemetry = dashboard.getTelemetry();
        
        waitForStart();
        
        while (opModeIsActive()){

            if (gamepad1.a){
                state.setMotorTarget(motorEnum, (int) (hw.getMotorConfig(motorEnum).maxTarget * 0.25));
            }
            else if (gamepad1.b){
                state.setMotorTarget(motorEnum, (int) (hw.getMotorConfig(motorEnum).maxTarget * 0.5));
            }
            else if (gamepad1.y){
                state.setMotorTarget(motorEnum, (int) (hw.getMotorConfig(motorEnum).maxTarget * 0.75));
            }
            else if (gamepad1.x){
                state.setMotorTarget(motorEnum, hw.getMotorConfig(motorEnum).maxTarget);
            }

            control.runTrapezoidalMotorControl(motorEnum);

            DcMotorEx dcMotorEx = (DcMotorEx) (hw.getMotorConfig(motorEnum).motor);
            double currentSpeed = dcMotorEx.getVelocity();

            telemetry.addData("current speed", currentSpeed);
            telemetry.update();
        }
    }
}
