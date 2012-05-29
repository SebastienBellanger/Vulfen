package com.tojosebe.vulfen.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.vulfox.component.ScreenComponent;

public class SpriteComponent extends ScreenComponent {

	private Rect mDrawRect = new Rect();
	private Bitmap mBitmap;
	private Paint mPaint;
	private boolean mAnimateFromLarge;
	private boolean mAnimateFromSmall;
	private long mStart;
	
	private int animationMillis = 150;
	private float currentAnimationSize = 1.0f; //100 percent i.e. full size
	
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int width;
	private int height;
	
	
	public SpriteComponent(Rect drawRect, Bitmap bitmap, boolean antialias, boolean animateFromLarge, boolean animateFromSmall, int animationTimeMillis) {
		mDrawRect.set(drawRect);
		mBitmap = bitmap;
		mPaint = new Paint();
		mPaint.setAntiAlias(antialias);
		mAnimateFromLarge = animateFromLarge;
		mAnimateFromSmall = animateFromSmall;
		animationMillis = animationTimeMillis;
		
		if (mAnimateFromSmall) {
			currentAnimationSize = 0.0f;
		} else if (mAnimateFromLarge) {
			currentAnimationSize = 2.0f;
		} 
		
		mStart = System.currentTimeMillis();
		left = drawRect.left;
		right = drawRect.right;
		top = drawRect.top;
		bottom = drawRect.bottom;
		width = right - left;
		height = bottom - top;
	}
	
	@Override
	public void update(float timeStep) {
		long timeSinceStart = System.currentTimeMillis() - mStart;
		if (timeSinceStart > animationMillis) {
			currentAnimationSize = 1.0f;
			return;
		} else {
			if (mAnimateFromSmall) {
				currentAnimationSize = (float)timeSinceStart / (float)animationMillis;
			} else if (mAnimateFromLarge) {
				currentAnimationSize = 2.0f - 1.0f * ((float)timeSinceStart / (float)animationMillis);
				if (left == 100) {
					System.out.println(currentAnimationSize);
				}
			} 
		}
	}

	@Override
	public void draw(Canvas canvas) {

		if (mAnimateFromSmall || mAnimateFromLarge) {
			float currentWidth = (float)(width * currentAnimationSize);
			mDrawRect.left = left + (int)((width - currentWidth) * 0.5f);
			mDrawRect.right = mDrawRect.left + (int)currentWidth;
			
			float currentHeight = (float)(height * currentAnimationSize);
			mDrawRect.top = top + (int)((height - currentHeight) * 0.5f);
			mDrawRect.bottom = mDrawRect.top + (int)currentHeight;
		}
		
		canvas.drawBitmap(mBitmap, null, mDrawRect, mPaint);
	}

	@Override
	public void handleActionDown(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent,
			boolean insideConponent) {
		return false;
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	public void setRect(Rect rect) {
		mDrawRect.set(rect);
		left = rect.left;
		right = rect.right;
		top = rect.top;
		bottom = rect.bottom;
		width = right - left;
		height = bottom - top;
	}

}
