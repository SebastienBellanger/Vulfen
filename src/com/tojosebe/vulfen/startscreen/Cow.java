package com.tojosebe.vulfen.startscreen;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.vulfox.ImageLoader;
import com.vulfox.component.ImageComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Cow {

	private ImageComponent mImageComponent;

	private Vector2f mVelocity;
	private Vector2f mPosition;

	private boolean mMoving;	
	private boolean mCanJump = true;	
	private int mScreenHeight;
	private float mY;
	private float mX;
	private int mStartY;
	
	private float mScale;

	public Cow(int resourceId, float scale, Context context, int width,
			int x, int y, int screenWidth, int screenHeight) {

		this.mScale = scale;
		this.mScreenHeight = screenHeight;
		this.mX = x;
		this.mY = y;

		Bitmap cow = ImageLoader.loadFromResource(context, resourceId);

		mImageComponent = new ImageComponent(cow, false);
		
		mImageComponent.setEventListener(new EventListener() {

			@Override
			public boolean handleButtonClicked() {
				
				if (!mMoving && mCanJump) {
					int scale = mScreenHeight / 2;
					mVelocity.setY(-scale);
					mMoving = true;
					return true;
				}
				return false;
			}
			
		});
		
		mImageComponent.setWidth((int)(width * mScale));
		
		float height = (cow.getHeight() / (float)cow.getWidth()) * width;
		
		mImageComponent.setHeight((int)(height * mScale));

		mImageComponent.setPositionX((int)(mX * mScale));
		mImageComponent.setPositionY((int)(mY * mScale));

		mVelocity = new Vector2f(0, 0);
		mPosition = new Vector2f(mImageComponent.getPositionX(),
				mImageComponent.getPositionY());
		mStartY = mImageComponent.getPositionY();

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		mImageComponent.resizeBitmap();
	}

	private int getRandom(int n, int m) {
		Random rand = new Random(System.currentTimeMillis() + (int)mPosition.getX() + (int)mPosition.getY());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	public void update(float timeStep) {
		
		if (mCanJump) {
		if (mImageComponent.getPositionY() > mStartY) {
			reinitCow();
			mMoving = false;
		}
		
		if (mMoving) {

			Vector2f positionDelta = Vector2fPool.getInstance().aquire();
			positionDelta.set(mVelocity);
			positionDelta.mulT(timeStep);
	
			mPosition.addT(positionDelta);
			
			int scale = mScreenHeight / 2;
			mVelocity.setY(mVelocity.getY()+(int)(3*timeStep*scale));
	
			Vector2fPool.getInstance().release(positionDelta);
			
		} else {
			int random = getRandom(0,1000);
			
			if (random == 42) {
				//Jump!
				int scale = mScreenHeight / 2;
				mVelocity.setY(-scale);
				mMoving = true;
			}
		}
		}
	}

	private void reinitCow() {
		
		mImageComponent.setPositionX((int)(mX * mScale));
		mImageComponent.setPositionY((int)(mY * mScale));

		mVelocity = new Vector2f(0, 0);
		mPosition = new Vector2f(mImageComponent.getPositionX(),
				mImageComponent.getPositionY());
	}

	public void draw(Canvas canvas) {
		mImageComponent.setPositionX((int) mPosition.getX());
		mImageComponent.setPositionY((int) mPosition.getY());
	}

	/**
	 * @return the imageComponent
	 */
	public ImageComponent getImageComponent() {
		return mImageComponent;
	}
	
	public void freezeAndReset() {
		reinitCow();
		mCanJump = false;
	}

	/**
	 * @param mPosition the mPosition to set
	 */
	public void setPosition(float x, float y) {
		this.mPosition.set(x, y);
	}

}
