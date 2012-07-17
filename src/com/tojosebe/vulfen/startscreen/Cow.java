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

	private int mDpi;
	
	private boolean mMoving;

	private int mScreenWidth;
	private int mScreenHeight;
	private int mYOffsetDp;
	private int mXOffsetDp;
	private int mStartY;

	public Cow(int resourceId, int dpi, Context context, int widthInDp,
			int xOffsetDp, int yOffsetDp, int screenWidth, int screenHeight) {
		this.mDpi = dpi;
		this.mScreenWidth = screenWidth;
		this.mScreenHeight = screenHeight;
		this.mXOffsetDp = xOffsetDp;
		this.mYOffsetDp = yOffsetDp;

		float fraction = dpi / 160.0f;

		Bitmap cow = ImageLoader.loadFromResource(context, resourceId);

		mImageComponent = new ImageComponent(cow, false);
		
		mImageComponent.setEventListener(new EventListener() {

			@Override
			public boolean handleButtonClicked() {
				
				if (!mMoving) {
					int scale = mScreenHeight / 2;
					mVelocity.setY(-scale);
					mMoving = true;
					return true;
				}
				return false;
			}
			
		});
		
		mImageComponent.setWidthInDpAutoSetHeight(widthInDp, dpi);

		mImageComponent.setPositionX(screenWidth / 2 - (screenWidth / 4)
				+ (int) (xOffsetDp * fraction));
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
		Random rand = new Random(System.currentTimeMillis() + (int)mPosition.getX() + (int)mPosition.getY());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	public void update(float timeStep) {
		
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

	private void reinitCow() {
		
		float fraction = mDpi / 160.0f;
		
		mImageComponent.setPositionX(mScreenWidth / 2 - (mScreenWidth / 4)
				+ (int) (mXOffsetDp * fraction));
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
