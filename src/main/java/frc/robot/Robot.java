package frc.robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.opencv.ml.Ml;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.*;
import com.ctre.phoenix.sensors.PigeonIMU.CalibrationMode;

//Robot Name: Impulse
public class Robot extends SampleRobot {

  // PigeonIMU
  PigeonIMU imu = new PigeonIMU(config.can_pigeon);

  // Quadrature Encoder
  Encoder elevatorEncoder = new Encoder(config.encoder_elevator[0], config.encoder_elevator[1]);

  // Elevator
  private static CANSparkMax m_elevator = new CANSparkMax(config.can_elevator, MotorType.kBrushless);

  // Compressor
  private static Compressor compressor = new Compressor(config.can_PCM_0);

  // Solenoids
  private static Solenoid hatchExtend = new Solenoid(config.solenoid_hatch_extend[0], config.solenoid_hatch_extend[1]);
  private static Solenoid airDump = new Solenoid(config.solenoid_vacuum_release[0], config.solenoid_vacuum_release[1]);
  private static Solenoid solenoid_hpod = new Solenoid(config.solenoid_hpod[0], config.solenoid_hpod[1]);
  private static Solenoid gearShift = new Solenoid(config.solenoid_gear_shift[0], config.solenoid_gear_shift[1]);

  // Vacuum Pump
  private static TalonSRX vacuumPump = new TalonSRX(config.can_vacuum_pump);

  // Arms/Climb
  private static Solenoid arm = new Solenoid(config.solenoid_arm[0], config.solenoid_arm[1]);
  private static CANSparkMax liftArm = new CANSparkMax(config.can_lift_arm, MotorType.kBrushless);
  private static CANSparkMax liftBase = new CANSparkMax(config.can_lift_back, MotorType.kBrushless);

  // Intake
  private static TalonSRX intakeRoller = new TalonSRX(config.can_intake_roller);

  // Drive Base
  private static CANSparkMax m_left = new CANSparkMax(config.can_drive_left1, MotorType.kBrushless);
  private static CANSparkMax m_right = new CANSparkMax(config.can_drive_right1, MotorType.kBrushless);
  private static CANSparkMax m_left2 = new CANSparkMax(config.can_drive_left2, MotorType.kBrushless);
  private static CANSparkMax m_right2 = new CANSparkMax(config.can_drive_right2, MotorType.kBrushless);
  private static CANSparkMax m_center = new CANSparkMax(config.can_drive_center, MotorType.kBrushless);
  HolonomicDrive holoDrive = new HolonomicDrive(m_left, m_right, m_center, imu, solenoid_hpod);

  // Driver Controls
  private static Joystick joy_base = new Joystick(0);

  // Co-Driver Controls
  private static Joystick joy_co = new Joystick(1);

  // Initialize Methods
  private Elevator elevator;
  private IntakeControls intake;
  private Lift climber;

  private TalonSRX lift_wheel = new TalonSRX(config.can_lift_wheel);

  public Robot() {

  }

  @Override
  public void robotInit() {

    // Init Elevator
    elevator = new Elevator(m_elevator, joy_co, true);

    // Init Camera
    CameraServer camera = CameraServer.getInstance();
    camera.startAutomaticCapture();

    // Init Drive Train
    m_left.setInverted(false);
    m_right.setInverted(true);
    m_right2.follow(m_right);
    m_left2.follow(m_left);
    m_center.setInverted(true);

    // Init Intake
    intake = new IntakeControls(joy_co, joy_base, airDump, hatchExtend, intakeRoller, vacuumPump, arm, elevator);

    // Neutral Mode Talons
    intakeRoller.setNeutralMode(NeutralMode.Brake);

    // Init Compressor
    compressor.start();

    // Init Climber
    climber = new Lift(liftArm, liftBase);

  }

  public void autonomous() {

    // m_left.set(-0.1);
    // m_right.set(-0.1);
    // Timer.delay(3);
    // m_right.set(0.0);
    // m_left.set(0.0);

    while (isAutonomous() && !isDisabled()) {
      climber.liftOperate(joy_base);
      elevator.PositionControl();
      intake.OperateIntake();

      // Gear Shifting

      gearShift.set(joy_base.getRawButton(2));

      solenoid_hpod.set(joy_base.getTrigger());
      // NON-FIELD CENTRIC

      if (joy_base.getTwist() > 0.2 || joy_base.getTwist() < -0.2) {
        m_left.set(joy_base.getY() - (joy_base.getTwist() / 3));
        m_right.set(joy_base.getY() + (joy_base.getTwist() / 3));
      } else {
        m_left.set(joy_base.getY());
        m_right.set(joy_base.getY());
      }
      solenoid_hpod.set(joy_base.getTrigger());
      m_center.set(joy_base.getX());

      // Cargo Intake Controlls for Driver Control
      if (joy_base.getRawButton(3)) {
        intakeRoller.set(ControlMode.PercentOutput, 1);
      } else if (joy_base.getRawButton(4)) {
        intakeRoller.set(ControlMode.PercentOutput, -1);
      } else {
        intakeRoller.set(ControlMode.PercentOutput, 0);
      }

    }
  }

  @Override
  public void operatorControl() {

    while (isOperatorControl() && !isDisabled()) {

      climber.liftOperate(joy_base);
      elevator.PositionControl();
      intake.OperateIntake();

      if (joy_base.getPOV() == 0) {
        lift_wheel.set(ControlMode.PercentOutput, 0.2);
      } else if (joy_base.getPOV() == 180) {
        lift_wheel.set(ControlMode.PercentOutput, -0.2);
      } else {
        lift_wheel.set(ControlMode.PercentOutput, 0.0);
      }

      // Gear Shifting

      gearShift.set(joy_base.getRawButton(2));

      solenoid_hpod.set(joy_base.getTrigger());
      // NON-FIELD CENTRIC

      if (joy_base.getTwist() > 0.2 || joy_base.getTwist() < -0.2) {
        m_left.set(joy_base.getY() - (joy_base.getTwist() / 3));
        m_right.set(joy_base.getY() + (joy_base.getTwist() / 3));
      } else {
        m_left.set(joy_base.getY());
        m_right.set(joy_base.getY());
      }
      solenoid_hpod.set(joy_base.getTrigger());
      m_center.set(joy_base.getX());

      // Cargo Intake Controlls for Driver Control
      if (joy_base.getRawButton(3)) {
        intakeRoller.set(ControlMode.PercentOutput, 1);
      } else if (joy_base.getRawButton(4)) {
        intakeRoller.set(ControlMode.PercentOutput, -1);
      } else {
        intakeRoller.set(ControlMode.PercentOutput, 0);
      }

      // 2ms loop time
      Timer.delay(0.002);
    }
  }

  @Override
  public void test() {
    // imu.enterCalibrationMode(CalibrationMode.Temperature);
    while (isTest() && !isDisabled()){

      climber.liftTestMode(joy_base);
      
    }
  }

  public void disabled() {
    // holoDrive.disable();

  }
}