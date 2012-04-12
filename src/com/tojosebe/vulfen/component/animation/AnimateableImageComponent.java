package com.tojosebe.vulfen.component.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.vulfox.component.ButtonComponent;

/**
 * Animates a sequence of images. The images need to be of the exact same size.
 */
public class AnimateableImageComponent extends ButtonComponent {

	private Bitmap[] mBitmap;

	private Rect mRect;

	private float aspectRatio;
	
	private Animation animation;

	public AnimateableImageComponent(Bitmap[] bitmap) {
		
		if (bitmap == null) {
			return;
		}
		
		animation = new Animation();
		mBitmap = bitmap;
		setHeight(mBitmap[0].getHeight());
		setWidth(mBitmap[0].getWidth());
		mRect = new Rect();
		aspectRatio = mBitmap[0].getWidth() / (float) mBitmap[0].getHeight();
	}

	@Override
	public void draw(Canvas canvas) {
		
		if (isVisible()) {
			mRect.set(getPositionX(), getPositionY(), getPositionX() + getWidth(),
					getPositionY() + getHeight());
	
			if (animation != null) {
				animation.draw(canvas, mRect);
			}
		}
	}

	/**
	 * Resize the bitmaps. This avoids ugly scaling effects when trying to draw a
	 * large image in a small rect.
	 */
	public void resizeBitmaps() {

		for (int i = 0; i < mBitmap.length; i++) {
			Bitmap bitmap = mBitmap[i];
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float scaleWidth = ((float) getWidth()) / width;
			float scaleHeight = ((float) getHeight()) / height;
	
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
	
			mBitmap[i] = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}
	}

	/**
	 * @param widthDp
	 *            Density-independent pixel
	 * @param deviceDpi
	 *            dots per inch for the device.
	 */
	public void setWidthInDpAutoSetHeight(int widthDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		setWidth((int) (widthDp * fraction));
		setHeight((int) (getWidth() / aspectRatio));
	}

	/**
	 * @param heightDp
	 *            Density-independent pixel
	 * @param deviceDpi
	 *            dots per inch for the device.
	 */
	public void setHeightInDpAutoSetWidth(int heightDp, int deviceDpi) {
		float fraction = deviceDpi / 160.0f;
		setHeight((int) (heightDp * fraction));
		setWidth((int) (getHeight() * aspectRatio));
	}
	
	/**
	 * Add a scene in the animation sequence.
	 * @param bitmapIndex the index 
	 * @param duration duration in millis for this scene.
	 */
	public void addScene(int bitmapIndex, long duration) {
		animation.addScene(new Scene(mBitmap[bitmapIndex], duration));
	}
	
	public void startAnimation() {
		animation.start();
	}
	
	public Bitmap[] getBitmaps() {
		return mBitmap;
	}
	
}
