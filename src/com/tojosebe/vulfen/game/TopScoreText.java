package com.tojosebe.vulfen.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.vulfox.math.Vector2f;

public class TopScoreText {

	private final float FADE = 0.9f;
	private final int TIME_BEFORE_FADE = 1000;
	private long mStartTime;

	private String mTopScoreText;
	private Vector2f mPosition = new Vector2f(0,-300);
	private float mAlpha = 1.0f;
	private Paint mPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border

	private boolean mIsDone = false;

	public TopScoreText(String topScoreText, int color, float mScale) {
		mTopScoreText = topScoreText;
		mStartTime = System.currentTimeMillis();
		
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(24 * mScale);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);

		// make red green and blue a little darker for the text border.
		int r = color & 0x00FF0000;
		r = r >> 16;
		r -= 150;
		if (r < 0) {
			r = 0;
		}
		int g = color & 0x0000FF00;
		g = g >> 8;
		g -= 150;
		if (g < 0) {
			g = 0;
		}
		int b = color & 0x000000FF;
		b -= 150;
		if (b < 0) {
			b = 0;
		}
		mStrokePaint.setARGB(0xFF, r, g, b);
		mStrokePaint.setTextSize(24 * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(3 * mScale);
	}

	public void update(float timeStep) {
		
		if (System.currentTimeMillis() > mStartTime + TIME_BEFORE_FADE) {
			if (mPosition != null) {
				mAlpha -= FADE * timeStep;
		
				if (mAlpha < 0.0f) {
					mAlpha = 0.0f;
					mIsDone = true;
				}
		
				int alpha = (int) (mAlpha * 255);
				mPaint.setAlpha(alpha);
				mStrokePaint.setAlpha(alpha);
			}
		}
	}

	public void draw(Canvas canvas) {
		if (mPosition != null) {
			canvas.drawText(mTopScoreText, mPosition.getX(), mPosition.getY(),
					mStrokePaint);
			canvas.drawText(mTopScoreText, mPosition.getX(), mPosition.getY(),
					mPaint);
		}
	}

	public boolean isDone() {
		return mIsDone;
	}

	/**
	 * @param mPosition the mPosition to set
	 */
	public void setPosition(float x, float y) {
		
		this.mPosition.set(x, y);
		
//		if (mPosition.getX() == 0.0f && mPosition.getY() == 0.0f) {
			Rect pointsRect = new Rect();
			mPaint.getTextBounds(mTopScoreText, 0, mTopScoreText.length(),
					pointsRect);
			mPosition.subT(new Vector2f(pointsRect.width() / 2.0f, -pointsRect
					.height() / 2.0f));
//		}
		
		
	}
}
