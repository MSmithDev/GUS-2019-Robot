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
public static int[] solenoid_hpod = {18,0}; // PCM18 Channel 0
public static int[] solenoid_arm = {18,1}; // PCM18 Channel 1
public static int[] solenoid_vacuum_release = {18, 4}; // PCM18 Channel 2
public static int[] solenoid_hatch_extend = {18, 3}; // PCM18 Channel 3
public static int[] solenoid_gear_shift = {18, 2}; // PCM18 Channel 4
public static int[] encoder_elevator = {0, 1}; // DIO 0, DIO 1 
//public static int[] solenoid_liftfl = {18, 5}; // PCM18 Channel 5
//public static int[] solenoid_liftbl = {18, 6}; // PCM18 Channel 6
//public static int[] solenoid_liftfr = {18, 7}; // PCM18 Channel 7
//public static int[] solenoid_liftbr = {19, 0}; // PCM19 Channel 0

}