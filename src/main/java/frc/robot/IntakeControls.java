package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeControls{

    Joystick coJoystick;
    Solenoid airDump;
    Solenoid hatchExtend;


    public IntakeControls(Joystick coJoystick, Solenoid airDump, Solenoid hatchExtend) {

        this.coJoystick = coJoystick;
        this.airDump = airDump;
        this.hatchExtend = hatchExtend;

    }

    public OperateIntake(){

        






    }
}