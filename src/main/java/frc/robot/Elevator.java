
package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class Elevator {
    private CANSparkMax m_elevator;
    private Joystick joy_co;
    private Boolean debug;
    private CANEncoder m_encoder;
    private CANPIDController m_pidController;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
    public double setPoint, processVariable;

    public Elevator(CANSparkMax m_elevator, Joystick joy_co, boolean debug) {
        this.m_elevator = m_elevator;
        this.joy_co = joy_co;
        this.debug = debug;
        this.m_encoder = m_encoder;

        // Invert Rotation
        m_elevator.setInverted(true);
        // Smart Motion Parameters
        m_elevator.restoreFactoryDefaults();

        m_pidController = m_elevator.getPIDController();
        m_encoder = m_elevator.getEncoder();
        setPoint = 0;
        // PID coefficients
        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0.000156;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;

        // Smart Motion Coefficients
        maxVel = 5700; // rpm
        maxAcc = 3500;

        // set PID coefficients
        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);

        int smartMotionSlot = 0;
        m_pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        m_pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        m_pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        m_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        
        // Debug mode
        if (debug) {
            // display PID coefficients on SmartDashboard
            SmartDashboard.putNumber("P Gain", kP);
            SmartDashboard.putNumber("I Gain", kI);
            SmartDashboard.putNumber("D Gain", kD);
            SmartDashboard.putNumber("I Zone", kIz);
            SmartDashboard.putNumber("Feed Forward", kFF);
            SmartDashboard.putNumber("Max Output", kMaxOutput);
            SmartDashboard.putNumber("Min Output", kMinOutput);

            // display Smart Motion coefficients
            SmartDashboard.putNumber("Max Velocity", maxVel);
            SmartDashboard.putNumber("Min Velocity", minVel);
            SmartDashboard.putNumber("Max Acceleration", maxAcc);
            SmartDashboard.putNumber("Allowed Closed Loop Error", allowedErr);
            SmartDashboard.putNumber("Set Position", 0);
            SmartDashboard.putNumber("Set Velocity", 0);
        }

    }

    public void PositionControl() {

        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);
        double maxV = SmartDashboard.getNumber("Max Velocity", 0);
        double minV = SmartDashboard.getNumber("Min Velocity", 0);
        double maxA = SmartDashboard.getNumber("Max Acceleration", 0);
        double allE = SmartDashboard.getNumber("Allowed Closed Loop Error", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to
        // controller
        if ((p != kP)) {
            m_pidController.setP(p);
            kP = p;
        }
        if ((i != kI)) {
            m_pidController.setI(i);
            kI = i;
        }
        if ((d != kD)) {
            m_pidController.setD(d);
            kD = d;
        }
        if ((iz != kIz)) {
            m_pidController.setIZone(iz);
            kIz = iz;
        }
        if ((ff != kFF)) {
            m_pidController.setFF(ff);
            kFF = ff;
        }
        if ((max != kMaxOutput) || (min != kMinOutput)) {
            m_pidController.setOutputRange(min, max);
            kMinOutput = min;
            kMaxOutput = max;
        }
        if ((maxV != maxVel)) {
            m_pidController.setSmartMotionMaxVelocity(maxV, 0);
            maxVel = maxV;
        }
        if ((minV != minVel)) {
            m_pidController.setSmartMotionMaxVelocity(minV, 0);
            minVel = minV;
        }
        if ((maxA != maxAcc)) {
            m_pidController.setSmartMotionMaxAccel(maxA, 0);
            maxAcc = maxA;
        }
        if ((allE != allowedErr)) {
            m_pidController.setSmartMotionAllowedClosedLoopError(allE, 0);
            allE = allowedErr;
        }

        /**
         * As with other PID modes, Smart Motion is set by calling the setReference
         * method on an existing pid object and setting the control type to kSmartMotion
         */

        if (joy_co.getRawButton(7)) {
            m_encoder.setPosition(0.0);
            setPoint = 0;
        } else {
        }

        // HATCH POSITIONS
        if (joy_co.getRawButton(1) && !(joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -15.0;
        }
        if (joy_co.getRawButton(3) && !(joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -120.0;
        }
        if (joy_co.getRawButton(4) && !(joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -220.0;
        }
        // BALL POSITIONS
        if (joy_co.getRawButton(1) && (joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -0.0;
        }
        if (joy_co.getRawButton(3) && (joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -90.0;
        }
        if (joy_co.getRawButton(4) && (joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -195.0;
        }
        if (joy_co.getRawButton(2) && (joy_co.getRawAxis(3) > 0.3)) {
            setPoint = -285.0;
        }
        
        //Set MAX and MIN Heights
        if (setPoint < -300) {
            setPoint = -300;
        } else if (setPoint > 5) {
            setPoint = 5;
        }
        
        //Manual override while pressing joystick down
        if(joy_co.getRawButton(9)){
            m_elevator.set(joy_co.getRawAxis(1));
            setPoint = m_encoder.getPosition();
        }
        else {
        m_pidController.setReference(setPoint, ControlType.kSmartMotion);
        }


        processVariable = m_encoder.getPosition();

        SmartDashboard.putNumber("SetPoint", setPoint);
        SmartDashboard.putNumber("Process Variable", processVariable);
        SmartDashboard.putNumber("Output", m_elevator.getAppliedOutput());
        SmartDashboard.putNumber("Encoder Count", m_encoder.getPosition());

    }

}