package frc.robot;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeControls {

    private Joystick coJoystick;
    private Joystick drJoystick;
    private Solenoid airDump;
    private Solenoid hatchExtend;
    private Solenoid arm;
    private TalonSRX intakeRoller;
    private Vacuum vacuum;
    private Elevator elevator;
    private boolean hatchDropped = false;

    public IntakeControls(Joystick coJoystick, Joystick drJoystick, Solenoid airDump, Solenoid hatchExtend,
            TalonSRX intakeRoller, TalonSRX vacuumMotor, Solenoid arm, Elevator elevator) {

        this.drJoystick = drJoystick;
        this.coJoystick = coJoystick;
        this.airDump = airDump;
        this.hatchExtend = hatchExtend;
        this.intakeRoller = intakeRoller;
        this.arm = arm;
        this.elevator = elevator;

        vacuum = new Vacuum(vacuumMotor);

    }

    public void OperateIntake() {

        // Arm Control
        if (coJoystick.getPOV() == 180) {
            arm.set(true);
        } else if (coJoystick.getPOV() == 0) {
            arm.set(false);
        }

        // Roller Control
        // if (coJoystick.getPOV() == 90) {
        //     intakeRoller.set(ControlMode.PercentOutput, 1);
        // } else if (coJoystick.getPOV() == 270) {
        //     intakeRoller.set(ControlMode.PercentOutput, -1);
        // } else {
        //     intakeRoller.set(ControlMode.PercentOutput, 0);
        // }


        // Extend Hatch Panel
        if (coJoystick.getRawButton(2) && !(coJoystick.getRawAxis(3) > 0.3)) {
            hatchExtend.set(true);
        } else {
            hatchExtend.set(false);
        }

        // Vacuum & Air Controls
        if (coJoystick.getRawButton(6) && !(coJoystick.getRawAxis(3) > .3)) {
            vacuum.start();
            airDump.set(false);
            hatchDropped = false;
        }
        if (coJoystick.getRawButton(5) && !(coJoystick.getRawAxis(3) > .3)) {
            vacuum.stop();
            airDump.set(true);
            
            if(!hatchDropped) {
                elevator.hatch_rub();
                hatchDropped = true;
            }


        }

    }
}