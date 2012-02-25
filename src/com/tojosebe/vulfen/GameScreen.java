package com.tojosebe.vulfen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;

import com.vulfox.Screen;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class GameScreen extends Screen {

	private static final long TAP_TIME = 100;
	private static final float GRAVITATIONAL_CONSTANT = 0.005f;
	private static final float COLLISION_ENERGY_LOSS = 0.05f;

	private Paint mBackgroundPaint;

	private boolean mTouchDown = false;
	private long mTouchTime = 0;
	private Vector2f mLastTouch = new Vector2f();
	private Vector2f mTouchDelta = new Vector2f();
	private Vector2f mScreenOffset = new Vector2f();

	private List<Pong> mPongs = new ArrayList<Pong>();

	Random random = new Random();

	@Override
	protected void initialize() {
		Pong firstPong = new Pong();
		firstPong.setRadius(64);
		firstPong.getPosition().set(100, 100);

		Pong secondPong = new Pong();
		secondPong.setRadius(61.0f);
		secondPong.getPosition().set(mWidth - 100, mHeight - 100);
		// secondPong.getVelocity().set(30, -20);

		// mPongs.add(firstPong);
		// mPongs.add(secondPong);

		Random random = new Random();
		for (int i = 0; i < 0; i++) {
			Pong newPong = new Pong();
			newPong.setRadius(random.nextFloat() * 15.0f + 1.0f);
			newPong.getPosition().set(random.nextFloat() * mWidth,
					random.nextFloat() * mHeight);

			mPongs.add(newPong);
		}

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(Color.BLACK);
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {

		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			mTouchTime = motionEvent.getEventTime();
		}
		
		if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			mTouchDelta.set(motionEvent.getX(), motionEvent.getY());
			mTouchDelta.subT(mLastTouch);
			mScreenOffset.addT(mTouchDelta);
		}

		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

			if (motionEvent.getEventTime() - mTouchTime < TAP_TIME) {
				Pong newPong = new Pong();
				newPong.setRadius(random.nextFloat() * 25.0f + 8.0f);
				newPong.getPosition().set(motionEvent.getX() - mScreenOffset.getX(),
						motionEvent.getY() - mScreenOffset.getY());
				// newPong.getVelocity().set(random.nextFloat() * 50.0f - 25.0f,
				// random.nextFloat() * 50 - 25);
				mPongs.add(newPong);
			}
		}

		mLastTouch.set(motionEvent.getX(), motionEvent.getY());
	}

	@Override
	public void update(float timeStep) {

		// Calculate collisions between spheres
		for (int i = 0; i < mPongs.size(); i++) {
			for (int j = i + 1; j < mPongs.size(); j++) {

				Pong first = mPongs.get(i);
				Pong second = mPongs.get(j);

				Vector2f collision = Vector2fPool.getInstance().aquire();
				Vector2f mtd = Vector2fPool.getInstance().aquire();
				Vector2f mtdFirst = Vector2fPool.getInstance().aquire();
				Vector2f mtdSecond = Vector2fPool.getInstance().aquire();
				Vector2f collisionFirst = Vector2fPool.getInstance().aquire();
				Vector2f collisionSecond = Vector2fPool.getInstance().aquire();

				first.getPosition().sub(second.getPosition(), collision);
				float lengthSqared = collision.getLengthSquared();

				float sumRadius = first.getRadius() + second.getRadius();
				if (lengthSqared < sumRadius * sumRadius) {

					float length = (float) Math.sqrt(lengthSqared);
					collision.mul((sumRadius - length) / length, mtd);

					float sumMass = first.getMass() + second.getMass();

					mtd.mul(second.getMass() / sumMass, mtdFirst);
					mtd.mul(first.getMass() / sumMass, mtdSecond);
					
					first.getPosition().addT(mtdFirst);
					second.getPosition().subT(mtdSecond);

					collision.normalizeT();

					float aci = first.getVelocity().dot(collision);
					float bci = second.getVelocity().dot(collision);

					float acf = bci;
					float bcf = aci;

					collision.mul((acf - aci) * 2.0f
							* (1.0f - COLLISION_ENERGY_LOSS) * second.getMass()
							/ sumMass, collisionFirst);
					collision.mul((bcf - bci) * 2.0f
							* (1.0f - COLLISION_ENERGY_LOSS) * first.getMass()
							/ sumMass, collisionSecond);

					
					first.getVelocity().addT(collisionFirst);
					second.getVelocity().addT(collisionSecond);
				}

				Vector2fPool.getInstance().release(collision);
				Vector2fPool.getInstance().release(mtd);
				Vector2fPool.getInstance().release(mtdFirst);
				Vector2fPool.getInstance().release(mtdSecond);
				Vector2fPool.getInstance().release(collisionFirst);
				Vector2fPool.getInstance().release(collisionSecond);

			}
		}

		// Calculate gravity
		for (Pong fromPong : mPongs) {
			for (Pong toPong : mPongs) {
				if (!fromPong.equals(toPong)) {
					Vector2f force = Vector2fPool.getInstance().aquire();

					force.set(toPong.getPosition());
					force.subT(fromPong.getPosition());
					float lengthSquared = force.getLengthSquared();
					if (lengthSquared != 0.0f) {

						float attractionForce = GRAVITATIONAL_CONSTANT
								* fromPong.getMass() * toPong.getMass()
								/ lengthSquared;
						force.setLength(attractionForce);
						fromPong.getForce().addT(force);
					}

					Vector2fPool.getInstance().release(force);
				}
			}
		}

		// Apply force and calculate collisions against screen edges
		for (Pong pong : mPongs) {
			pong.update(timeStep);
			pong.getForce().set(0.0f, 0.0f);

//			if (pong.getPosition().getX() < pong.getRadius()) {
//				pong.getPosition().setX(
//						2.0f * pong.getRadius() - pong.getPosition().getX());
//				pong.getVelocity().setX(-pong.getVelocity().getX());
//			} else if (pong.getPosition().getX() > mWidth - pong.getRadius()) {
//				pong.getPosition().setX(
//						2.0f * (mWidth - pong.getRadius())
//								- pong.getPosition().getX());
//				pong.getVelocity().setX(-pong.getVelocity().getX());
//			}
//			if (pong.getPosition().getY() < pong.getRadius()) {
//				pong.getPosition().setY(
//						2.0f * pong.getRadius() - pong.getPosition().getY());
//				pong.getVelocity().setY(-pong.getVelocity().getY());
//			} else if (pong.getPosition().getY() > mHeight - pong.getRadius()) {
//				pong.getPosition().setY(
//						2.0f * (mHeight - pong.getRadius())
//								- pong.getPosition().getY());
//				pong.getVelocity().setY(-pong.getVelocity().getY());
//			}
		}

	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawRect(0, 0, mWidth, mHeight, mBackgroundPaint);

		for (Pong pong : mPongs) {
			pong.draw(canvas, mScreenOffset);
		}
	}

}
