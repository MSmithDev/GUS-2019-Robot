package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

class Vacuum {
    private TalonSRX vacuum;

    public Vacuum(TalonSRX vacuum_motor) {

        this.vacuum = vacuum_motor;
        
    }


    public void start(){
        vacuum.set(ControlMode.PercentOutput, -1.0);
    }

    public void stop(){
        vacuum.set(ControlMode.PercentOutput, 0.0);
    }



}