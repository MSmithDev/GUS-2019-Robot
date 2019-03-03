package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class IntakeControls {

    private Joystick coJoystick;
    private Joystick drJoystick;
    private Solenoid airDump;
    private Solenoid hatchExtend;
    private Solenoid arm;
    private TalonSRX intakeRoller;
    private Vacuum vacuum; 
    private boolean vacuumState = true;
    private boolean hatchState = true;

    public IntakeControls(Joystick coJoystick, Joystick drJoystick, Solenoid airDump, Solenoid hatchExtend,
            TalonSRX intakeRoller, TalonSRX vacuumMotor, Solenoid arm) {

        this.drJoystick = drJoystick;
        this.coJoystick = coJoystick;
        this.airDump = airDump;
        this.hatchExtend = hatchExtend;
        this.intakeRoller = intakeRoller;
        this.arm = arm;

        vacuum = new Vacuum(vacuumMotor);

    }

    public void OperateIntake() {

        // Arm Control
        if (coJoystick.getPOV() == 180){
            arm.set(true);
        } else if (coJoystick.getPOV() == 0){
            arm.set(false);
        }
        
        // Roller Control
        
        if (coJoystick.getPOV() == 90){
            intakeRoller.set(ControlMode.PercentOutput, 1);
        } else if (coJoystick.getPOV() == 270){
            intakeRoller.set(ControlMode.PercentOutput, -1);
        } else {
            intakeRoller.set(ControlMode.PercentOutput, 0);
        }

        // Extend Hatch Panel
        if (coJoystick.getRawButton(2)&& !(coJoystick.getRawAxis(3) > 0.3)){
            hatchExtend.set(true);
        } else{
            hatchExtend.set(false);
         
        }
        

        
        

        if(coJoystick.getRawButton(6)) {
            vacuum.start();
            airDump.set(false);
        }
        if(coJoystick.getRawButton(5)) {
            vacuum.stop();
            airDump.set(true);
        }



    }
}