package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FadeInOutAnimation {

	private float currentFadeOutAnimationSize = 1.0f; // 100 percent i.e. full
														// size
	private boolean mIsDone = false;

	private int mFadeOutTime;
	private long fadeOutStartTime;

	private Rect mDrawRect = new Rect();

	private Bitmap mBitmap;
	
	private Paint mPaint = new Paint();

	private int left;
	private int top;

	private float mX;
	private float mY;

	private float mWidth;
	private float mHeight;

	public FadeInOutAnimation(float x, float y, Bitmap bitmap, int fadeOutTime) {
		mBitmap = bitmap;
		mX = x;
		mY = y;
		mFadeOutTime = fadeOutTime;

		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
		
		mPaint.setAntiAlias(true);

		mDrawRect.left = (int) (x - mWidth * 0.5f);
		mDrawRect.right = mDrawRect.right + (int) mWidth;
		mDrawRect.top = (int) (y - mHeight * 0.5f);
		mDrawRect.bottom = mDrawRect.top + (int) mHeight;

		left = mDrawRect.left;
		top = mDrawRect.top;
		
		fadeOutStartTime = System.currentTimeMillis();
	}

	public void update(float timeStep) {

		long fadeTimeSinceStart = System.currentTimeMillis() - fadeOutStartTime;

		if (fadeTimeSinceStart < mFadeOutTime) {
			currentFadeOutAnimationSize = 1.0f - 1.0f * ((float) fadeTimeSinceStart / (float) mFadeOutTime);
		} else {
			currentFadeOutAnimationSize = 0.0f;
			mIsDone = true;
		}
	}

	public void draw(Canvas canvas) {
		float x = mX * 0.5f;
		float y = mY * 0.5f;

		mDrawRect
				.set((int) x, (int) y, (int) (x + mWidth), (int) (y + mHeight));

		float currentWidth = (float) (mWidth * currentFadeOutAnimationSize);
		mDrawRect.left = left + (int) ((mWidth - currentWidth) * 0.5f);
		mDrawRect.right = mDrawRect.left + (int) currentWidth;

		float currentHeight = (float) (mHeight * currentFadeOutAnimationSize);
		mDrawRect.top = top + (int) ((mHeight - currentHeight) * 0.5f);
		mDrawRect.bottom = mDrawRect.top + (int) currentHeight;

		canvas.drawBitmap(mBitmap, null, mDrawRect, mPaint);
	}

	public boolean isDone() {
		return mIsDone;
	}
}
