package com.tojosebe.vulfen.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Points {

	private final float FADE = 0.6f;
	
	private Vector2f mRise = new Vector2f(0.0f, -55.0f);
	
	private int mPoints;
	private Vector2f mPosition;
	private float mAlpha = 1.0f;
	private Paint mPaint = new Paint();
	
	private String mPointsString;
	
	private boolean mIsDone = false;
	
	public Points(int points, Vector2f position, int color, float mScale)
	{							
		mPoints = points;			
		mPosition = position;				
		mRise.mulT(mScale);
		
		mPaint.setColor(color);
		mPaint.setTextSize(22 * mScale);				
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
				
		
		mPointsString = Integer.toString(mPoints);
		Rect pointsRect = new Rect();
		mPaint.getTextBounds(mPointsString, 0, mPointsString.length(), pointsRect);
		mPosition.subT(new Vector2f(pointsRect.width()/2.0f, -pointsRect.height()/2.0f));
	}
	
	public void update(float timeStep)
	{	
		mPosition.addT(mRise.mul(timeStep));
		mAlpha -= FADE * timeStep;
		
		if(mAlpha < 0.0f)
		{
			mAlpha = 0.0f;
			mIsDone = true;
		}				
		
		int alpha = (int)(mAlpha * 255);
		mPaint.setAlpha(alpha);
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawText(mPointsString, mPosition.getX(), mPosition.getY(), mPaint);
	}
	
	public boolean isDone()
	{
		return mIsDone;
	}
}
