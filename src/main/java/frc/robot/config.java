package frc.robot;

class config {

public static int can_drive_left1 = 1;
public static int can_drive_left2 = 2;
public static int can_drive_right1 = 3;
public static int can_drive_right2 = 4;
public static int can_drive_center = 5;
public static int can_elevator = 6;
public static int can_intake_roller = 8;
public static int can_vacuum_pump = 9;
public static int can_PCM_0 = 10;
public static int can_PCM_1 = 7;
public static int can_pigeon = 19;
public static int can_lift_wheel = 21;
public static int[] solenoid_hpod = {18,0}; // PCM18 Channel 0
public static int[] solenoid_arm = {18,1}; // PCM18 Channel 1
public static int[] solenoid_vacuum_release = {18, 4}; // PCM18 Channel 4
public static int[] solenoid_hatch_extend = {18, 3}; // PCM18 Channel 3
public static int[] solenoid_gear_shift = {18, 2}; // PCM18 Channel 2
public static int[] encoder_elevator = {0, 1}; // DIO 0, DIO 1 
//Lift 
public static int[] solenoid_lift_front_retract = {20, 5}; // PCM18 Channel 5
public static int[] solenoid_lift_front_half = {20, 3}; // PCM18 Channel 6
public static int[] solenoid_lift_front_full = {20, 4}; // PCM18 Channel 7

public static int[] solenoid_lift_back_retract = {20, 2}; // PCM19 Channel 0
public static int[] solenoid_lift_back_half = {20, 1}; // PCM19 Channel 0
public static int[] solenoid_lift_back_full = {20, 0}; // PCM19 Channel 0

//### Elevator Positions ### 
//Ground
public static double pos_ground = 0.0;
//Hatch
public static double pos_hatch_1 = -14.0;
public static double pos_hatch_2 = -67.0;
public static double pos_hatch_3 = -120.0;
//Hatch drop offset
public static double pos_hatch_rub = 3;

//Ball
public static double pos_ball_depo = -4.0;
public static double pos_ball_1 = -45.0;
public static double pos_ball_2 = -93.5;
public static double pos_ball_3 = -145.0;



}