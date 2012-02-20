package com.tojosebe.vulfen;

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

	private Pong mPong;
	private Paint mPongPaint;
	private Paint mRopePaint;
	private Paint mBackgroundPaint;
	
	private boolean mTouchDown = false;
	private Vector2f mLastTouch = new Vector2f();

	@Override
	protected void initialize() {
		mPong = new Pong();
		mPong.mass = 1;
		mPong.radius = 32;
		mPong.position = new Vector2f(100, 100);
		mPong.velocity = new Vector2f(40, 50);
		mPong.force = new Vector2f();

		mPongPaint = new Paint();
		mPongPaint.setColor(Color.YELLOW);
		
		mRopePaint = new Paint();
		mRopePaint.setColor(Color.RED);
		mRopePaint.setStyle(Style.STROKE);
		mRopePaint.setStrokeWidth(7.0f);

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setColor(Color.BLACK);
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {

		if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
		{
			mTouchDown = true;
		}
		else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
		{
			mTouchDown = false;
		}
		
		mLastTouch.set(motionEvent.getX(), motionEvent.getY());
	}

	@Override
	public void update(float timeStep) {

		if(mTouchDown) {
			Vector2f force = Vector2fPool.getInstance().aquire();
			
			force.set(mLastTouch);
			force.subT(mPong.position);			
			force.setLength(1000.0f);			
			mPong.force.addT(force);
			
			Vector2fPool.getInstance().release(force);
		}
		
		mPong.update(timeStep);
		mPong.force.set(0.0f, 0.0f);

		if (mPong.position.getX() < mPong.radius) {
			mPong.position.setX(2.0f * mPong.radius - mPong.position.getX());
			mPong.velocity.setX(-mPong.velocity.getX());
		} else if (mPong.position.getX() > mWidth - mPong.radius) {
			mPong.position.setX(2.0f * (mWidth - mPong.radius)
					- mPong.position.getX());
			mPong.velocity.setX(-mPong.velocity.getX());
		}
		if (mPong.position.getY() < mPong.radius) {
			mPong.position.setY(2.0f * mPong.radius - mPong.position.getY());
			mPong.velocity.setY(-mPong.velocity.getY());
		} else if (mPong.position.getY() > mHeight - mPong.radius) {
			mPong.position.setY(2.0f * (mHeight - mPong.radius)
					- mPong.position.getY());
			mPong.velocity.setY(-mPong.velocity.getY());
		}

	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawRect(0, 0, mWidth, mHeight, mBackgroundPaint);
		
		if(mTouchDown) {
			canvas.drawLine(
					mPong.position.getX(), mPong.position.getY(),
					mLastTouch.getX(), mLastTouch.getY(), mRopePaint);
		}

		canvas.drawCircle(mPong.position.getX(), mPong.position.getY(),
				mPong.radius, mPongPaint);

	}

}
