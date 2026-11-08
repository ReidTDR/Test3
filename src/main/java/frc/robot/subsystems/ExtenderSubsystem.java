// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class ExtenderSubsystem extends SubsystemBase {
  /** Creates a new ExtenderSubsystem. */
  TalonFX m_extenderMotor;

  private final VoltageOut m_voltageOut = new VoltageOut(0);

 private final  CurrentLimitsConfigs m_currentlimitconfigs = new CurrentLimitsConfigs()
  .withStatorCurrentLimit(40)
  .withStatorCurrentLimitEnable(true)
  .withSupplyCurrentLimit(60)
  .withSupplyCurrentLimitEnable(true);

  public ExtenderSubsystem() {
    m_extenderMotor = new TalonFX(Constants.EXTENDER_MOTOR_ID);
    m_extenderMotor.getConfigurator().apply(m_currentlimitconfigs);
    m_extenderMotor.setNeutralMode(NeutralModeValue.Coast);
  }
  
  public double getAngle(){
    return m_extenderMotor.getPosition().getValueAsDouble(); //Currently gets rotations, need to convert to actual angle 
  }

  public void rotate(double speed){
    speed = Math.max(-1,Math.min(1,speed));

    if (Math.abs(speed) < 0.005)

    if (getAngle() > 80){
      speed = 0;
    }

    if (getAngle() <-80){
      speed = 0;
    }

        m_extenderMotor.setControl(m_voltageOut.withOutput(speed * 12 ));

  }

  public void motorOff(){
    m_extenderMotor.setControl(m_voltageOut.withOutput(0));
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
