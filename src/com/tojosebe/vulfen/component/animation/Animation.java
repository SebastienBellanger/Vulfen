package com.tojosebe.vulfen.component.animation;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

	private ArrayList<Scene> mSceneList;
	
	private Paint mImagePaint;
	
	private long mCurrentTime = 0;
	private long mTotalTime = 0;
	private long mStartTime = 0;
	
	private boolean mStarted = false;
	
	public Animation() {
		mImagePaint = new Paint();
		mImagePaint.setAntiAlias(true);
	}
	
	public synchronized void addScene(Scene scene) {
		if (mSceneList == null) {
			mSceneList = new ArrayList<Scene>();
		}
		
		mTotalTime += scene.getDuration();
		mSceneList.add(scene);
	}
	
	public void start() {
		mStartTime = System.currentTimeMillis();
		mCurrentTime = 0;
		mStarted = true;
	}
	
	public void draw(Canvas canvas, Rect rect) {
		if (mStarted) {		
			updateTime();
			if (mSceneList != null) {
				Bitmap bitmap = getCurrentBitmap();
				if (bitmap != null) {
					canvas.drawBitmap(bitmap, null, rect, mImagePaint);
				}
			}
		}
	} 

	private void updateTime() {
		mCurrentTime = System.currentTimeMillis() - mStartTime;
		
		if (mCurrentTime > mTotalTime) {
			//Start "movie" from the beginning!
			start();
		}
	}

	/**
	 * This is inefficient. Consider using faster way to get the correct bitmap than go through the entire list.
	 * @return
	 */
	private synchronized Bitmap getCurrentBitmap() {
		long sum = 0;
		for (Scene scene: mSceneList) {
			if (mCurrentTime <= sum + scene.getDuration()) {
				return scene.getBitmap();
			}
			sum += scene.getDuration();
		}
		return null;
	}
	
	
}
