package com.tojosebe.vulfen.game;

public class BowlConfiguration {

	private int lives = 5;
	
	private float minSpeed = 500.0f;
	private float maxSpeed = 1700.0f;
	
	private float friction = 100.0f;
	
	private int yellowYellowValue = 10;
	private int redYellowValue = 25;
	private int redRedValue = 100;
	
	/**
	 * @return the lives
	 */
	public int getLives() {
		return lives;
	}
	/**
	 * @param lives the lives to set
	 */
	public void setLives(int lives) {
		this.lives = lives;
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
	public int getYellowYellowValue() {
		return yellowYellowValue;
	}
	/**
	 * @param yellowYellowValue the yellowYellowValue to set
	 */
	public void setYellowYellowValue(int yellowYellowValue) {
		this.yellowYellowValue = yellowYellowValue;
	}
	/**
	 * @return the redYellowValue
	 */
	public int getRedYellowValue() {
		return redYellowValue;
	}
	/**
	 * @param redYellowValue the redYellowValue to set
	 */
	public void setRedYellowValue(int redYellowValue) {
		this.redYellowValue = redYellowValue;
	}
	/**
	 * @return the redRedValue
	 */
	public int getRedRedValue() {
		return redRedValue;
	}
	/**
	 * @param redRedValue the redRedValue to set
	 */
	public void setRedRedValue(int redRedValue) {
		this.redRedValue = redRedValue;
	}
	
}
