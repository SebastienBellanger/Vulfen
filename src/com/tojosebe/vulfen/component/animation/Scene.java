package com.tojosebe.vulfen.component.animation;

import android.graphics.Bitmap;

public class Scene {

	private Bitmap bitmap;
	
	private long duration;

	public Scene(Bitmap bitmap, long endTime) {
		this.bitmap = bitmap;
		this.duration = endTime;
	}
	
	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
}
