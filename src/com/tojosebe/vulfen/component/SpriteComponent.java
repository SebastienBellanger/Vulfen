package com.tojosebe.vulfen.component;

import com.vulfox.math.Vector2f;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpriteComponent {

	
	private Vector2f position;
	
	private Rect mDrawRect = new Rect();
	private Bitmap mBitmap;
	private Paint mPaint;
	private boolean mAnimateFromLarge;
	private boolean mAnimateFromSmall;
	private long mStart;
	
	private int animationMillis = 150;
	private float currentAnimationSize = 1.0f; //100 percent i.e. full size
	
	private int left;
	private int top;
	
	private float mWidth;
	private float mHeight;
	
	private boolean animatingDone = false;
	
	public SpriteComponent(Bitmap bitmap, boolean antialias, boolean animateFromLarge, boolean animateFromSmall, int animationTimeMillis, float x, float y, float width, float height) {

		mDrawRect.left = (int)(x - width * 0.5f);
		mDrawRect.right = mDrawRect.right + (int)width;
		mDrawRect.top = (int)(y - height * 0.5f); 
		mDrawRect.bottom = mDrawRect.top + (int)height;

		left = mDrawRect.left;
		top = mDrawRect.top;
		
		mWidth = width;
		mHeight = height;
		
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
	}
	
	
	public void update(float timeStep) {
		long timeSinceStart = System.currentTimeMillis() - mStart;
		if (timeSinceStart > animationMillis) {
			currentAnimationSize = 1.0f;
			animatingDone = true;
			return;
		} else {
			animatingDone = false;
			if (mAnimateFromSmall) {
				currentAnimationSize = (float)timeSinceStart / (float)animationMillis;
			} else if (mAnimateFromLarge) {
				currentAnimationSize = 2.0f - 1.0f * ((float)timeSinceStart / (float)animationMillis);
			} 
		}
	}

	
	public void draw(Canvas canvas) {
		
		float x = position.getX() - getWidth() * 0.5f;
		float y = position.getY() - getHeight() * 0.5f;

		mDrawRect.set((int)x, (int)y, (int)(x + getWidth()), (int)(y + getHeight()));

		if ((mAnimateFromSmall || mAnimateFromLarge) && !animatingDone) {		
			
			float currentWidth = (float)(mWidth * currentAnimationSize);
			mDrawRect.left = left + (int)((mWidth - currentWidth) * 0.5f);
			mDrawRect.right = mDrawRect.left + (int)currentWidth;
			
			float currentHeight = (float)(mHeight * currentAnimationSize);
			mDrawRect.top = top + (int)((mHeight - currentHeight) * 0.5f);
			mDrawRect.bottom = mDrawRect.top + (int)currentHeight;
		}
		
		canvas.drawBitmap(mBitmap, null, mDrawRect, mPaint);
	}

	/**
	 * @return the mBitmap
	 */
	public Bitmap getSpriteBitmap() {
		return mBitmap;
	}

	/**
	 * @param mBitmap the mBitmap to set
	 */
	public void setSpriteBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	/**
	 * @return the animatingDone
	 */
	public boolean isAnimatingDone() {
		return animatingDone;
	}

	/**
	 * @return the mWidth
	 */
	public float getWidth() {
		return mWidth;
	}

	/**
	 * @param mWidth the mWidth to set
	 */
	public void setWidth(float mWidth) {
		this.mWidth = mWidth;
	}

	/**
	 * @return the mHeight
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * @param mHeight the mHeight to set
	 */
	public void setHeight(float mHeight) {
		this.mHeight = mHeight;
	}
	
	public void setPosition(float x, float y) {
		this.position.setX(x);
		this.position.setY(y);
	}
	
	/**
	 * @return the position
	 */
	public Vector2f getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}


	/**
	 * @return the mAnimateFromLarge
	 */
	public boolean isAnimateFromLarge() {
		return mAnimateFromLarge;
	}
	
	/**
	 * @return the mAnimateFromSmall
	 */
	public boolean isAnimateFromSmall() {
		return mAnimateFromSmall;
	}

	/**
	 * @return the animationMillis
	 */
	public int getAnimationMillis() {
		return animationMillis;
	}

	/**
	 * @param mAnimateFromLarge the mAnimateFromLarge to set
	 */
	public void setAnimateFromLarge(boolean mAnimateFromLarge) {
		this.mAnimateFromLarge = mAnimateFromLarge;
	}

	/**
	 * @param mAnimateFromSmall the mAnimateFromSmall to set
	 */
	public void setAnimateFromSmall(boolean mAnimateFromSmall) {
		this.mAnimateFromSmall = mAnimateFromSmall;
	}

	/**
	 * @param animationMillis the animationMillis to set
	 */
	public void setAnimationMillis(int animationMillis) {
		this.animationMillis = animationMillis;
	}
	
	public void resetAnimation() {
		animatingDone = false;
		mStart = System.currentTimeMillis();
		if (mAnimateFromSmall) {
			currentAnimationSize = 0.0f;
		} else if (mAnimateFromLarge) {
			currentAnimationSize = 2.0f;
		} 
	}

}
