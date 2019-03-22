package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.sun.java.swing.plaf.windows.TMSchema.Control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift {

    private CANSparkMax lift_arm;
    private CANSparkMax lift_base;
    private Joystick joy_base;
    private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    private CANPIDController m_pid_arm, m_pid_base;
    private CANEncoder m_pid_arm_encoder, m_pid_base_encoder;
    private double armSetpoint, baseSetpoint;

    public Lift(CANSparkMax lift_arm, CANSparkMax lift_base) {

        this.lift_arm = lift_arm;
        this.lift_base = lift_base;

        m_pid_arm = lift_arm.getPIDController();
        m_pid_base = lift_base.getPIDController();

        m_pid_arm_encoder = lift_arm.getEncoder();
        m_pid_base_encoder = lift_base.getEncoder();

        lift_arm.restoreFactoryDefaults();
        lift_base.restoreFactoryDefaults();

        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;

        // set PID coefficients
        m_pid_arm.setP(kP);
        m_pid_arm.setI(kI);
        m_pid_arm.setD(kD);
        m_pid_arm.setIZone(kIz);
        m_pid_arm.setFF(kFF);
        m_pid_arm.setOutputRange(kMinOutput, kMaxOutput);

        // set PID coefficients
        m_pid_base.setP(kP);
        m_pid_base.setI(kI);
        m_pid_base.setD(kD);
        m_pid_base.setIZone(kIz);
        m_pid_base.setFF(kFF);
        m_pid_base.setOutputRange(kMinOutput, kMaxOutput);

        armSetpoint = 0;
        baseSetpoint = 0;

    }

    public void liftOperate(Joystick driverJoystick) {

        this.joy_base = driverJoystick;
        // operate back
        if (joy_base.getRawButton(10)) {
            // base up rpm
            baseSetpoint = 5600;

        } else if (joy_base.getRawButton(8)) {
            // base down rpm
            baseSetpoint = -5600;
        } else {
            // base idle rpm
            m_pid_base.setIAccum(0);
            baseSetpoint = 0;
        }

        // operate arm
        if (joy_base.getRawButton(7)) {
            // arm down rpm
            armSetpoint = 1071;
        } else if (joy_base.getRawButton(5)) {
            // arm up rpm
            armSetpoint = -1091;
        } else {
            // arm idle rpm
            armSetpoint = 0;
            m_pid_arm.setIAccum(0);
        }

        // Arm "Limit Switch"
        if (m_pid_arm_encoder.getPosition() < 237 && m_pid_arm_encoder.getPosition() >= 0) {
            m_pid_arm.setReference(armSetpoint, ControlType.kVelocity);
        } else if (m_pid_arm_encoder.getPosition() >= 237 && joy_base.getRawButton(7)) {
            m_pid_arm.setReference(armSetpoint, ControlType.kVelocity);
        } else if (m_pid_arm_encoder.getPosition() <= 0 && joy_base.getRawButton(5)) {
            m_pid_arm.setReference(armSetpoint, ControlType.kVelocity);
        } else if (!joy_base.getRawButton(5) && !joy_base.getRawButton(7)) {
            m_pid_arm.setReference(0, ControlType.kVelocity);
        }

        // Base "Limit Switch"
        if (m_pid_base_encoder.getPosition() < 100 && m_pid_base_encoder.getPosition() >= 0) {
            m_pid_base.setReference(baseSetpoint, ControlType.kVelocity);
        } else if (m_pid_base_encoder.getPosition() >= 100 && joy_base.getRawButton(8)) {
            m_pid_base.setReference(baseSetpoint, ControlType.kVelocity);
        } else if (m_pid_base_encoder.getPosition() <= 0 && joy_base.getRawButton(10)) {
            m_pid_base.setReference(baseSetpoint, ControlType.kVelocity);
        } else if (!joy_base.getRawButton(8) && !joy_base.getRawButton(10)){
            m_pid_base.setReference(0, ControlType.kVelocity);
        }

        m_pid_base.setReference(baseSetpoint, ControlType.kVelocity);

    }

    public void liftTestMode(Joystick driverJoystick) {

        this.joy_base = driverJoystick;

        // operate back
        if (joy_base.getRawButton(10)) {
            // base up rpm
            baseSetpoint = 5600;

        } else if (joy_base.getRawButton(8)) {
            // base down rpm
            baseSetpoint = -5600;
        } else {
            // base idle rpm
            m_pid_base.setIAccum(0);
            baseSetpoint = 0;
        }

        // operate arm
        if (joy_base.getRawButton(7)) {
            // arm down rpm
            armSetpoint = 1071;
        } else if (joy_base.getRawButton(5)) {
            // arm up rpm
            armSetpoint = -1091;
        } else {
            // arm idle rpm
            armSetpoint = 0;
            m_pid_arm.setIAccum(0);
        }

        m_pid_base.setReference(baseSetpoint, ControlType.kVelocity);

    }

}