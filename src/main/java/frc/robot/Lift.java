package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Lift{

    private Solenoid liftfl;
    private Solenoid liftbl;
    private Solenoid liftfr;
    private Solenoid liftbr;
    private Joystick driverJoystick;


    public Lift(Solenoid liftfl, Solenoid liftbl, Solenoid liftfr, Solenoid liftbr){

        this.liftfl = liftfl;
        this.liftbl = liftbl;
        this.liftfr = liftfr;
        this.liftbr = liftbr;

    }

    public void liftOperate(Joystick driverJoystick){

        this.driverJoystick = driverJoystick;

        // Front lifts up & down
        if (driverJoystick.getRawButton(7)){
            liftfl.set(true);
            liftfr.set(true);
        }else if (!driverJoystick.getRawButton(7)){
            liftfl.set(false);
            liftfr.set(false);
        }

        //Back lifts up & down
        if (driverJoystick.getRawButton(8)){
            liftbl.set(true);
            liftbr.set(true);
        }else if (!driverJoystick.getRawButton(8)){
            liftbl.set(false);
            liftbr.set(false);
        }
    }
}