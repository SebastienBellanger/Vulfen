package com.tojosebe.vulfen.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.vulfox.math.Vector2f;

public class Score {

	private final float FADE = 0.6f;

	private Vector2f mRise = new Vector2f(0.0f, -55.0f);

	private int mPoints;
	private Vector2f mPosition;
	private float mAlpha = 1.0f;
	private Paint mPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border

	private String mPointsString;

	private boolean mIsDone = false;

	public Score(int points, Vector2f position, int color, float mScale) {
		mPoints = points;
		mPosition = position;
		mRise.mulT(mScale);

		mPaint.setColor(color);
		mPaint.setTextSize(22 * mScale);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mPaint.setAntiAlias(true);

		mPointsString = Integer.toString(mPoints);
		Rect pointsRect = new Rect();
		mPaint.getTextBounds(mPointsString, 0, mPointsString.length(),
				pointsRect);
		mPosition.subT(new Vector2f(pointsRect.width() / 2.0f, -pointsRect
				.height() / 2.0f));

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
//		mStrokePaint.setColor(0xFF000000 | (r << 16) | (g << 8) | (b));

		mStrokePaint.setTextSize(22 * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(3);
	}

	public void update(float timeStep) {
		mPosition.addT(mRise.mul(timeStep));
		mAlpha -= FADE * timeStep;

		if (mAlpha < 0.0f) {
			mAlpha = 0.0f;
			mIsDone = true;
		}

		int alpha = (int) (mAlpha * 255);
		mPaint.setAlpha(alpha);
		mStrokePaint.setAlpha(alpha);
	}

	public void draw(Canvas canvas) {
		canvas.drawText(mPointsString, mPosition.getX(), mPosition.getY(),
				mStrokePaint);
		canvas.drawText(mPointsString, mPosition.getX(), mPosition.getY(),
				mPaint);
	}

	public boolean isDone() {
		return mIsDone;
	}
}
