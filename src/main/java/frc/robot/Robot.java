package frc.robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
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

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.*;

//Robot name Impulse
public class Robot extends SampleRobot {

  private static boolean cam0 = false;
  String[] hosts = { "10.2.28.11", "10.2.28.10" };

  // PigeonIMU
  PigeonIMU imu = new PigeonIMU(config.can_pigeon);

  // Drive Base
  private static CANSparkMax m_left = new CANSparkMax(config.can_drive_left, MotorType.kBrushless);
  private static CANSparkMax m_right = new CANSparkMax(config.can_drive_right, MotorType.kBrushless);
  private static CANSparkMax m_center = new CANSparkMax(config.can_drive_center, MotorType.kBrushless);
  HolonomicDrive holoDrive = new HolonomicDrive(m_left, m_right, m_center, imu);

  // Elevator
  private static CANSparkMax m_elevator = new CANSparkMax(config.can_elevator, MotorType.kBrushless);

  // Compressors & Pneumatics
  private static Compressor compressor = new Compressor();

  // Solenoids/Hatch Panels
  private static Solenoid hatchExtend = new Solenoid(config.solenoid_hatch_extend[0], config.solenoid_hatch_extend[1]);
  private static Solenoid airDump = new Solenoid(config.solenoid_vacuum_release[0], config.solenoid_vacuum_release[1]);

  // Vacuum Pump
  private static TalonSRX vacuumPump = new TalonSRX(config.can_vacuum_pump);

  // Arm
  private static CANSparkMax m_arm = new CANSparkMax(config.can_intake_arm, MotorType.kBrushless);

  // Intake
  private static TalonSRX intakeRoller = new TalonSRX(config.can_intake_roller);

  // Driver Controls
  private static Joystick joy_base = new Joystick(0);

  // Co-Driver Controls
  private static Joystick joy_co = new Joystick(1);

  // Initialize Methods
  private Arm arm;
  private Elevator elevator;
  private IntakeControls intake;

  public Robot() {

  }

  @Override
  public void robotInit() {

    // Init Axis Camera
    CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]);
    
    // Init Elevator
    elevator = new Elevator(m_elevator, joy_co, true);

    // Init Arm
    arm = new Arm(m_arm, joy_co, true);

    // Init Intake
    intake = new IntakeControls(joy_co, airDump, hatchExtend, intakeRoller);

  }

  public void autonomous() {

  }

  @Override
  public void operatorControl() {
    // TEMP Disable Drive
    // holoDrive.fieldCentric(joy_base);

    while (isOperatorControl() && !isDisabled()) {

      // TODO
      /*
       * // Switch Camera Feeds if (joy_base.getRawButton(5) == true && cam0 == false)
       * { CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]); cam0 =
       * true; } else if (joy_base.getRawButton(5) == true && cam0 == true) {
       * CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[1]); cam0 = false;
       * }
       */

      elevator.PositionControl();
      arm.PositionControl();
      intake.OperateIntake();

      // Used to allow the devices to reset
      Timer.delay(0.005);
    }
  }

  @Override
  public void test() {

  }

  public void disabled() {
    holoDrive.disable();
  }
}