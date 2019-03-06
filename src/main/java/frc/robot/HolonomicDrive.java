package frc.robot;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HolonomicDrive {

    private CANSparkMax left;
    private CANSparkMax right;
    private CANSparkMax center;
    private PigeonIMU imu;
    private Joystick joystick;
    private Thread t;
    private Solenoid hpod;

    public HolonomicDrive(CANSparkMax left, CANSparkMax right, CANSparkMax center, PigeonIMU imu, Solenoid hpod) {

        this.hpod = hpod;
        this.left = left;
        this.right = right;
        this.center = center;
        this.imu = imu;

        
    }

    public void fieldCentric(Joystick joystick3D) {
        this.joystick = joystick3D;
        
        left.setInverted(true);
        right.setInverted(true);
        System.out.println("DEBUG: Starting Field Centric Thread");
        t = new Thread(() -> {
            DifferentialDrive drive = new DifferentialDrive(left, right);
            while (!Thread.interrupted()) {
                double xSpeed = joystick.getRawAxis(0);
                double ySpeed = joystick.getRawAxis(1);
                ySpeed = limit(ySpeed);
                ySpeed = applyDeadband(ySpeed, 0.02);

                xSpeed = limit(xSpeed);
                xSpeed = applyDeadband(xSpeed, 0.02);

                Vector2d input = new Vector2d(xSpeed, ySpeed);
                input.rotate(imu.getFusedHeading());
                
                

                drive.arcadeDrive(-input.y, joystick.getRawAxis(2));
                center.set(input.x);

                SmartDashboard.putNumber("Vector X", input.x);
                SmartDashboard.putNumber("Vector Y", input.y);
                SmartDashboard.putNumber("Gyro", imu.getFusedHeading());

                if (joystick3D.getRawButton(2)){
                    hpod.set(true);
                }
            }
            drive.close();
        });
        t.start();

    }

    public void disable() {
        if (t != null) {
            System.out.println("Killing field centric thread");
            t.interrupt();
        }
    }

    public double limit(double value) {
        if (value > 1.0) {
            return 1.0;
        }
        if (value < -1.0) {
            return -1.0;
        }
        return value;
    }

    public double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

}