package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeControls{

    Joystick coJoystick;
    Solenoid airDump;
    Solenoid hatchExtend;
    TalonSRX intakeRoller;


    public IntakeControls(Joystick coJoystick, Solenoid airDump, Solenoid hatchExtend, TalonSRX intakeRoller) {

        this.coJoystick = coJoystick;
        this.airDump = airDump;
        this.hatchExtend = hatchExtend;
        this.intakeRoller = intakeRoller;

    }

    public OperateIntake(){

        // Intake Roller
        if (coJoystick.getRawButton(5)){
            intakeRoller

        }






    }
}