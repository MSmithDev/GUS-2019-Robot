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
import com.ctre.phoenix.sensors.*;

//Robot name Impulse
public class Robot extends SampleRobot {

private static boolean cam0 = false;
  String[] hosts = { "10.2.28.11", "10.2.28.10" };

  // PigeonIMU
  PigeonIMU imu = new PigeonIMU(9);

  // Drive Base
  private static CANSparkMax m_left = new CANSparkMax(1, MotorType.kBrushless);
  private static CANSparkMax m_right = new CANSparkMax(3, MotorType.kBrushless);
  private static CANSparkMax m_center = new CANSparkMax(5, MotorType.kBrushless);
  
  HolonomicDrive holoDrive = new HolonomicDrive(m_left, m_right, m_center, imu);

  // Elevator
  private static CANSparkMax m_elevator = new CANSparkMax(6, MotorType.kBrushless);

  // Compressors & Pneumatics
  private static Compressor compressor = new Compressor();

  // Solenoids/Hatch Panels
  private static Solenoid hatchExtend = new Solenoid(0 , 8);
  private static Solenoid airDump = new Solenoid(0, 7);

  // Arm
  private static CANSparkMax m_arm = new CANSparkMax(7, MotorType.kBrushless);
  
  // Driver Controls
  private static Joystick joy_base = new Joystick(0);

  //Co-Driver Controls
  private static Joystick joy_co = new Joystick(1);
  
  // Initialize Methods
  private Arm arm;
  private Elevator elevator;
  public Robot() {

  }

  @Override
  public void robotInit() {

    // Axis Camera
    CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]);
    //
    //Init Elevator
    elevator = new Elevator(m_elevator, joy_co, true);
    
    //Init Arm
    arm = new Arm(m_arm, joy_co, true);

  }

  public void autonomous() {

  }

  @Override
  public void operatorControl() {
    //TEMP Disable Drive
    //holoDrive.fieldCentric(joy_base);

    while (isOperatorControl() && !isDisabled()) {

      //TODO
      /*
      // Switch Camera Feeds
      if (joy_base.getRawButton(5) == true && cam0 == false) {
        CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]);
        cam0 = true;
      } else if (joy_base.getRawButton(5) == true && cam0 == true) {
        CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[1]);
        cam0 = false;
      }
      */

      elevator.PositionControl();
      arm.PositionControl();


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