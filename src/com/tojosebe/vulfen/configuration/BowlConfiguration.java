package com.tojosebe.vulfen.configuration;

public class BowlConfiguration {

	private float minSpeed = 500.0f;
	private float maxSpeed = 1700.0f;
	
	private float friction = 150.0f;
	
	private int enemyEnemyValue = 50;
	private int penguinEnemyValue = 100;
	private int penguinPenguinValue = 50;
	
	public BowlConfiguration(BowlConfiguration bowlConfiguration) {
		minSpeed = bowlConfiguration.minSpeed;
		maxSpeed = bowlConfiguration.maxSpeed;
		friction = bowlConfiguration.friction;
		enemyEnemyValue = bowlConfiguration.enemyEnemyValue;
		penguinEnemyValue = bowlConfiguration.penguinEnemyValue;
		penguinPenguinValue = bowlConfiguration.penguinPenguinValue;
	}
	
	public BowlConfiguration() {
	}
	
	/**
	 * @return the minSpeed
	 */
	public float getMinSpeed() {
		return minSpeed;
	}
	/**
	 * @param minSpeed the minSpeed to set
	 */
	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}
	/**
	 * @return the maxSpeed
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}
	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	/**
	 * @return the friction
	 */
	public float getFriction() {
		return friction;
	}
	/**
	 * @param friction the friction to set
	 */
	public void setFriction(float friction) {
		this.friction = friction;
	}
	/**
	 * @return the yellowYellowValue
	 */
	public int getEnemyEnemyValue() {
		return enemyEnemyValue;
	}
	/**
	 * @param yellowYellowValue the yellowYellowValue to set
	 */
	public void setEnemyEnemyValue(int yellowYellowValue) {
		this.enemyEnemyValue = yellowYellowValue;
	}
	/**
	 * @return the redYellowValue
	 */
	public int getPenguinEnemyValue() {
		return penguinEnemyValue;
	}
	/**
	 * @param redYellowValue the redYellowValue to set
	 */
	public void setPenguinEnemyValue(int redYellowValue) {
		this.penguinEnemyValue = redYellowValue;
	}
	/**
	 * @return the redRedValue
	 */
	public int getPenguinPenguinValue() {
		return penguinPenguinValue;
	}
	/**
	 * @param redRedValue the redRedValue to set
	 */
	public void setPenguinPenguinValue(int redRedValue) {
		this.penguinPenguinValue = redRedValue;
	}
	
}
