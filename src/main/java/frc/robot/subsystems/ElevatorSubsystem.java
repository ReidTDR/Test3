// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase {

  TalonFX m_elevatorMotor;
  private final VoltageOut m_VoltageOut = new VoltageOut(0);

  private double ff;
  private double targetPose;
  private boolean PIDMode;

  private PIDController pidController = new PIDController(.1,.01,.002);

  CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs()
  .withStatorCurrentLimit(40)
  .withStatorCurrentLimitEnable(true)
  .withSupplyCurrentLimit(60)
  .withSupplyCurrentLimitEnable(true);


  public ElevatorSubsystem() {
  m_elevatorMotor = new TalonFX(1);
  m_elevatorMotor.getConfigurator().apply(currentLimitsConfigs);
  m_elevatorMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  public double getHeight(){
    return m_elevatorMotor.getPosition().getValueAsDouble() * 360 / Constants.ELEVATOR_GEAR_RATIO;
  }

  public void elevatorStop(){
    m_elevatorMotor.setControl(m_VoltageOut.withOutput(0));
  }

  public void setTarget(double target){
    targetPose = target;
    PIDMode = true;
  }
  
  @Override
  public void periodic() {
    if (PIDMode) {
      ff = -20;
      double fb = pidController.calculate(getHeight(), targetPose);
      m_elevatorMotor.setControl(m_VoltageOut.withOutput(ff-fb));
    }
  }
  public Command goToPose(double target){
    return runOnce(() -> setTarget(target));
  }
}
