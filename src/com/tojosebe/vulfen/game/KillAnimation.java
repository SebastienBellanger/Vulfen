package com.tojosebe.vulfen.game;

public class KillAnimation {

	private float fromX;
	private float fromY;
	private float toX;
	private float toY;
	private float currentX;
	private float currentY;
	private long animationTime = 200;
	private long startTime = 0;
	private boolean done = false;

	public KillAnimation(float fromX, float fromY, float toX, float toY) {
		startTime = System.currentTimeMillis();
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		currentX = fromX;
		currentY = fromY;
	}

	public void update() {

		float wayToGoY = toY - fromY;
		float wayToGoX = toX - fromX;

		long timeSinceStart = System.currentTimeMillis() - startTime;

		float percentageComplete = timeSinceStart / (float) animationTime;

		if (percentageComplete >= 1.0) {
			currentX = toX;
			currentY = toY;
			done = true;
		} else {
			currentX = fromX + percentageComplete * wayToGoX;
			currentY = fromY + percentageComplete * wayToGoY;
		}

	}

	/**
	 * @return the currentX
	 */
	public float getCurrentX() {
		return currentX;
	}

	/**
	 * @return the currentY
	 */
	public float getCurrentY() {
		return currentY;
	}

	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

}
