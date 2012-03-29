package com.tojosebe.vulfen.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.vulfox.Screen;

public class SelectScreen extends Screen {

	private Paint mBackgroundPaint = new Paint();
	private Paint mButtonPaint = new Paint();
	
	@Override
	protected void initialize() {
		mBackgroundPaint.setColor(Color.rgb(80, 255, 255));
		mButtonPaint.setColor(Color.GRAY);
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			mScreenManager.addScreen(new BowlScreen(new BowlConfiguration()));
		}
	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawRect(0, 0, mWidth, mHeight, mBackgroundPaint);
		
		canvas.drawRect(10, 10, mWidth - 20, 100, mButtonPaint);

	}

}
