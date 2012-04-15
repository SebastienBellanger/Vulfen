package com.tojosebe.vulfen.worldscreen;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.vulfox.ImageLoader;
import com.vulfox.component.ImageComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Pig {

	private ImageComponent mImageComponent;

	private Vector2f mVelocity;
	private Vector2f mPosition;

	private int mDpi;
	
	private boolean mMoving;
	
	private int upperRandomLimit = 1000;

	private int mScreenHeight;
	private int mYOffsetDp;
	private int mXOffsetDp;
	private int mStartY;

	public Pig(int resourceId, int dpi, Context context, int widthInDp,
			int xOffsetDp, int yOffsetDp, int screenHeight) {
		this.mDpi = dpi;
		this.mScreenHeight = screenHeight;
		this.mXOffsetDp = xOffsetDp;
		this.mYOffsetDp = yOffsetDp;

		float fraction = dpi / 160.0f;

		Bitmap pig = ImageLoader.loadFromResource(context, resourceId);

		mImageComponent = new ImageComponent(pig, false);
		mImageComponent.setWidthInDpAutoSetHeight(widthInDp, dpi);

		mImageComponent.setPositionX((int) (xOffsetDp * fraction));
		mImageComponent.setPositionY(screenHeight / 2 + (screenHeight / 4)
				+ (int) (yOffsetDp * fraction));

		mVelocity = new Vector2f(0, 0);
		mPosition = new Vector2f(mImageComponent.getPositionX(),
				mImageComponent.getPositionY());
		mStartY = mImageComponent.getPositionY();

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		mImageComponent.resizeBitmap();
	}

	private int getRandom(int n, int m) {
		Random rand = new Random(System.currentTimeMillis() + (int)mPosition.getX() + (int)mPosition.getY() + mImageComponent.getHeight());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	public void update(float timeStep) {
		
		if (mImageComponent.getPositionY() > mStartY) {
			reinitPig();
			mMoving = false;
		}
		
		if (mMoving) {

			Vector2f positionDelta = Vector2fPool.getInstance().aquire();
			positionDelta.set(mVelocity);
			positionDelta.mulT(timeStep);
	
			mPosition.addT(positionDelta);
			
			mVelocity.setY(mVelocity.getY()+(int)((1.0/mDpi)*350000*timeStep));
	
			Vector2fPool.getInstance().release(positionDelta);
			
		} else {
			int random = getRandom(1,upperRandomLimit);
			if (random == 1) {
				//Jump!
				mVelocity.setY(-500);
				mMoving = true;
			}
		}
	}
	
	public void superPig() {
		upperRandomLimit = 3;
	}
	
	public void wimpPig() {
		upperRandomLimit = 1000;
	}

	public void reinitPig() {
		
		float fraction = mDpi / 160.0f;
		
		mImageComponent.setPositionX((int) (mXOffsetDp * fraction));
		mImageComponent.setPositionY(mScreenHeight / 2 + (mScreenHeight / 4)
				+ (int) (mYOffsetDp * fraction));

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

}
