package com.tojosebe.vulfen.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.vulfox.component.ButtonComponent;
import com.vulfox.component.ScreenComponent;

public abstract class WorldButton extends ButtonComponent {

	private String mWorldName;
	private int mTotalStages;
	private int mClearedStages;
	
	private RectF mBackgroundRect = new RectF();
	private Paint mBackgroundStrokePaint = new Paint();
	private Paint mBackgroundFillPaint = new Paint();
	
	private Paint mWorldNamePaint = new Paint();
	
	public WorldButton(String worldName, int totalStages, int clearedStages) {
		mWorldName = worldName;
		mTotalStages = totalStages;
		mClearedStages = clearedStages;
		
		setWidth(250);
		setHeight(250);
		
		mBackgroundFillPaint.setColor(Color.GRAY);
		
		mBackgroundStrokePaint.setColor(Color.WHITE);
		mBackgroundStrokePaint.setStrokeWidth(5.0f);
		mBackgroundStrokePaint.setStyle(Paint.Style.STROKE);
		
		mWorldNamePaint.setFakeBoldText(true);
		mWorldNamePaint.setTextSize(32.0f);
		mWorldNamePaint.setTextAlign(Paint.Align.CENTER);
		mWorldNamePaint.setColor(Color.BLACK);
	}
	
	@Override
	public void draw(Canvas canvas) {
		mBackgroundRect.set(getPositionX(), getPositionY(), getPositionX() + getWidth(), getPositionY() + getHeight());
		canvas.drawRoundRect(mBackgroundRect, getWidth()/4.0f, getHeight()/4.0f, mBackgroundFillPaint);
		canvas.drawRoundRect(mBackgroundRect, getWidth()/4.0f, getHeight()/4.0f, mBackgroundStrokePaint);
		canvas.drawText(mWorldName, getPositionX() + getWidth() / 2, getPositionY() + 90, mWorldNamePaint);
	}

	
}
