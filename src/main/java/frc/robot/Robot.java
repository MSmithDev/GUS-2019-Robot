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
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.*;

//Robot Name: Impulse
public class Robot extends SampleRobot {

  // PigeonIMU
  PigeonIMU imu = new PigeonIMU(config.can_pigeon);

  // Quadrature Encoder
  Encoder elevatorEncoder = new Encoder(config.encoder_elevator[0], config.encoder_elevator[1]);

  // Elevator
  private static CANSparkMax m_elevator = new CANSparkMax(config.can_elevator, MotorType.kBrushless);

  // Compressor
  private static Compressor compressor = new Compressor(config.can_compressor_0);

  // Solenoids
  private static Solenoid hatchExtend = new Solenoid(config.solenoid_hatch_extend[0], config.solenoid_hatch_extend[1]);
  private static Solenoid airDump = new Solenoid(config.solenoid_vacuum_release[0], config.solenoid_vacuum_release[1]);
  private static Solenoid solenoid_hpod = new Solenoid(config.solenoid_hpod[0], config.solenoid_hpod[1]);
  private static Solenoid gearShift = new Solenoid(config.solenoid_gear_shift[0], config.solenoid_gear_shift[1]);
  //private static Solenoid liftFL = new Solenoid(config.solenoid_liftfl[0], config.solenoid_liftfl[1]);
  //private static Solenoid liftBL = new Solenoid(config.solenoid_liftbl[0], config.solenoid_liftbl[1]);
  //private static Solenoid liftFR = new Solenoid(config.solenoid_liftfr[0], config.solenoid_liftfr[1]);
  //private static Solenoid liftBR = new Solenoid(config.solenoid_liftbr[0], config.solenoid_liftbr[1]);
  //private static Lift lift = new Lift(liftFL, liftBL, liftFR, liftBR);

  // Vacuum Pump
  private static TalonSRX vacuumPump = new TalonSRX(config.can_vacuum_pump);

  // Arm
  private static Solenoid arm = new Solenoid(config.solenoid_arm[0], config.solenoid_arm[1]);

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

  public Robot() {

  }

  @Override
  public void robotInit() {
    
    // Init Elevator
    elevator = new Elevator(m_elevator, joy_co, true);

    // Init Camera
    

    // Init Drive Train
    m_left.setInverted(false);
    m_right.setInverted(true);
    m_right2.follow(m_right);
    m_left2.follow(m_left);
    m_center.setInverted(true);

    // Init Intake
    intake = new IntakeControls(joy_co, joy_base, airDump, hatchExtend, intakeRoller, vacuumPump, arm);

    // Init Compressors
    compressor.start();
  }

  public void autonomous() {

  }

  @Override
  public void operatorControl() {
    // TEMP Disable Drive
    // holoDrive.fieldCentric(joy_base);

    while (isOperatorControl() && !isDisabled()) {
      

      SmartDashboard.putNumber("POV index", joy_co.getPOV());


      elevator.PositionControl();
      intake.OperateIntake();
      //lift.liftOperate(joy_base);


      // TEMP TESTING
      
        if(joy_base.getTwist() > 0.2 || joy_base.getTwist() < -0.2){
        m_left.set(joy_base.getY()-(joy_base.getTwist()/3));
        m_right.set(joy_base.getY()+(joy_base.getTwist()/3)); } else {
        m_left.set(joy_base.getY()); m_right.set(joy_base.getY()); }
        solenoid_hpod.set(joy_base.getTrigger()); 
        m_center.set(joy_base.getX());
       
      // arm.set(joy_base.getTrigger());

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