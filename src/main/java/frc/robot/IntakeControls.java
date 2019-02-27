package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeControls{

    private Joystick coJoystick;
    private Solenoid airDump;
    private Solenoid hatchExtend;
    private TalonSRX intakeRoller;
    private Vacuum vacuum;

    public IntakeControls(Joystick coJoystick, Solenoid airDump, Solenoid hatchExtend, TalonSRX intakeRoller, TalonSRX vacuumMotor) {

        this.coJoystick = coJoystick;
        this.airDump = airDump;
        this.hatchExtend = hatchExtend;
        this.intakeRoller = intakeRoller;
        vacuum = new Vacuum(vacuumMotor);

    }

    public void OperateIntake(){

        // Intake Roller
        if (coJoystick.getRawButton(5)){
            intakeRoller.set(ControlMode.PercentOutput, 1);
        }
        // Outtake Roller
        else if (coJoystick.getRawAxis(2) > 0.1){
            intakeRoller.set(ControlMode.PercentOutput, -1);
        }
        else {
            intakeRoller.set(ControlMode.PercentOutput, 0.0);
        }
        // Hatch Mechanism Extend & De-Extend
        if (coJoystick.getRawButton(4)){
            hatchExtend.set(true);
        }else if (!coJoystick.getRawButton(4)){
            hatchExtend.set(false);
        }
        // Vacuum Function
        if (coJoystick.getRawButton(6)){
            vacuum.start();
        }
        // Vacuum Dump
        if (coJoystick.getRawAxis(3) > 0.1){
            vacuum.stop();          
        }
    }
}