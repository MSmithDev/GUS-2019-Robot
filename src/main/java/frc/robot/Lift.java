package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Lift{

    
    private Solenoid lift_front_retract;
    private Solenoid lift_front_half;
    private Solenoid lift_front_full;
    private Solenoid lift_back_retract;
    private Solenoid lift_back_half;
    private Solenoid lift_back_full;
    private Joystick joy_base;


    public Lift(Solenoid f_retract, Solenoid f_half, Solenoid f_full, Solenoid b_retract, Solenoid b_half, Solenoid b_full){

        this.lift_front_retract = f_retract;
        this.lift_front_half = f_half;
        this.lift_front_full = f_full;

        this.lift_back_retract = b_retract;
        this.lift_back_half = b_half;
        this.lift_back_full = b_full;

        

    }

    public void liftOperate(Joystick driverJoystick){

        this.joy_base = driverJoystick;
        //retract back
       if(joy_base.getRawButton(10)){
        lift_back_retract.set(false);
        lift_back_full.set(false);
        lift_back_half.set(false);
       }

       //half back
       if(joy_base.getRawButton(9)){
        lift_back_retract.set(true);
        lift_back_full.set(false);
        lift_back_half.set(true);
       }

       //full back
       if(joy_base.getRawButton(8)){
        lift_back_retract.set(true);
        lift_back_full.set(true);
        lift_back_half.set(true);
       }


       //retract front
       if(joy_base.getRawButton(5)){
        lift_front_retract.set(false);
        lift_front_full.set(false);
        lift_front_half.set(false);
       }

       //half front
       if(joy_base.getRawButton(6)){
        lift_front_retract.set(true);
        lift_front_full.set(false);
        lift_front_half.set(true);
       }

       //full front
       if(joy_base.getRawButton(7)){
        lift_front_retract.set(true);
        lift_front_full.set(true);
        lift_front_half.set(true);
       }
      
}
}