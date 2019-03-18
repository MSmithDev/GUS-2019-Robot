package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Lift{

    
    private CANSparkMax lift_arm;
    private CANSparkMax lift_base;
    private Joystick joy_base;


    public Lift(CANSparkMax lift_arm, CANSparkMax lift_base){

        this.lift_arm = lift_arm;
        this.lift_base = lift_base;

    }

    public void liftOperate(Joystick driverJoystick){

        this.joy_base = driverJoystick;
        //retract back
       if(joy_base.getRawButton(10)){
        lift_base.set(-1);
       } else if (joy_base.getRawButton(10)){
        lift_base.set(0);
       }

       //half back
       if(joy_base.getRawButton(9)){
        lift_base.set(.5);
       } else if (joy_base.getRawButton(9)){
        lift_base.set(0);
       }     

       //full back
       if(joy_base.getRawButton(8)){
        lift_base.set(1);
       } else if (joy_base.getRawButton(8)){
        lift_base.set(0);
       }


       //retract arm
       if(joy_base.getRawButton(5)){
        lift_arm.set(-1);
       } else if (joy_base.getRawButton(5)){
        lift_arm.set(0);
       }

       //half arm
       if(joy_base.getRawButton(6)){
        lift_arm.set(.5);
       } else if (joy_base.getRawButton(6)){
        lift_arm.set(0);
       }

       //full arm
       if(joy_base.getRawButton(7)){
        lift_arm.set(1);
       } else if (joy_base.getRawButton(7)){
        lift_arm.set(0);
       }
      
    }
}