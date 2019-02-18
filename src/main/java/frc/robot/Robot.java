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

public class Robot<hDrive> extends SampleRobot {

  private static boolean cam0 = false;
  String[] hosts = { "10.2.28.11", "10.2.28.10" };

  // Device

  // Network Tables
  NetworkTableInstance inst = NetworkTableInstance.create();
  NetworkTableInstance inst0 = NetworkTableInstance.create();
  private NetworkTableEntry instance = new NetworkTableEntry(inst, 1);
  private NetworkTableEntry instance0 = new NetworkTableEntry(inst0, 2);

  // PigeonIMU
  PigeonIMU pigeon = new PigeonIMU(0);

  // Drive Base
  private static CANSparkMax mLeft = new CANSparkMax(1, MotorType.kBrushless);
  private static CANSparkMax mRight = new CANSparkMax(3, MotorType.kBrushless);
  private static CANSparkMax middleWheel = new CANSparkMax(5, MotorType.kBrushless);
  HolonomicDrive holoDrive = new HolonomicDrive(mLeft, mRight, middleWheel, pigeon);

  // Elevator
  private static CANSparkMax elevator = new CANSparkMax(6, MotorType.kBrushless);

  // Compressors & Pneumatics
  // private static Compressor compressor = new Compressor();

  // Solenoids
  Solenoid sole0 = new Solenoid(18, 0);

  // Cargo
  // private static PWMTalonSRX cargoArm = new PWMTalonSRX(1);
  // private static PWMTalonSRX cargoIntake = new PWMTalonSRX(2);
  // private static PWMVictorSPX cargoRollers = new PWMVictorSPX(1);

  // Controls

  // Driver Controls
  private static Joystick driverJoystick0 = new Joystick(0);

  public Robot() {

  }

  @Override
  public void robotInit() {

    // Axis Camera
    CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]);

  }

  @Override
  public void autonomous() {

  }

  @Override
  public void operatorControl() {

    holoDrive.fieldCentric(driverJoystick0);

    while (isOperatorControl() && !isDisabled()) {

      // Switch Camera Feeds
      if (driverJoystick0.getRawButton(5) == true && cam0 == false) {
        CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[0]);
        cam0 = true;
      } else if (driverJoystick0.getRawButton(5) == true && cam0 == true) {
        CameraServer.getInstance().addAxisCamera("AxisCam0", hosts[1]);
        cam0 = false;
      }

      // Elevator
      elevator.set((driverJoystick0.getRawAxis(1)) / 2);

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