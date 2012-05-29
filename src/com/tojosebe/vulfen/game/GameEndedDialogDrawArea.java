package com.tojosebe.vulfen.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.dialog.DialogCounterString;
import com.tojosebe.vulfen.dialog.DialogDrawArea;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

/**
 * This class draws on the drawable area of a dialog. This particular class just
 * draws stars.
 * 
 */
public class GameEndedDialogDrawArea extends DialogDrawArea {

	private Bitmap mStarBitmap;
	private Paint mStarPaint;
	private Level mLevel;
	private DialogCounterString mCounterStringGameOver;

	private float starAnimationTime = 250.0f; // in milliseconds
	private long star1Started = 0;
	private long star2Started = 0;
	private long star3Started = 0;
	private float starSize = 100;


	public GameEndedDialogDrawArea(int height, Level level,
			Context context, DialogCounterString counterString,
			float scale) {
		setHeight((int)(height * scale));


		mLevel = level;
		mCounterStringGameOver = counterString;

		starSize *= scale;

		mStarBitmap = BitmapManager.getBitmap(Constants.BITMAP_STAR_2);
		if (mStarBitmap == null) {
			mStarBitmap = ImageLoader.loadFromResource(context,
					R.drawable.star_small);
			mStarBitmap = GraphicsUtil.resizeBitmap(mStarBitmap,
					(int) starSize, (int) starSize);
			BitmapManager.addBitmap(Constants.BITMAP_STAR_2, mStarBitmap);
		}
		mStarPaint = new Paint();
		mStarPaint.setAntiAlias(true);
	}

	@Override
	public void draw(Canvas canvas, int x, int y, int width) {

		int currentScore = mCounterStringGameOver.getCurrentValue();

		if (star1Started == 0 && currentScore >= mLevel.getOneStarScore()) {
			star1Started = System.currentTimeMillis();
		}
		if (star2Started == 0 && currentScore >= mLevel.getTwoStarsScore()) {
			star2Started = System.currentTimeMillis();
		}
		if (star3Started == 0 && currentScore >= mLevel.getThreeStarsScore()) {
			star3Started = System.currentTimeMillis();
		}

		long timeSinceStart1 = System.currentTimeMillis() - star1Started;
		float size1 = 0;

		if (star1Started == 0) {
			size1 = 0;
		} else if (timeSinceStart1 > starAnimationTime) {
			size1 = starSize;
		} else {
			size1 = starSize * (timeSinceStart1 / starAnimationTime);
		}

		long timeSinceStart2 = System.currentTimeMillis() - star2Started;
		float size2 = 0;

		if (star2Started == 0) {
			size2 = 0;
		} else if (timeSinceStart2 > starAnimationTime) {
			size2 = starSize;
		} else {
			size2 = starSize * (timeSinceStart2 / starAnimationTime);
		}

		long timeSinceStart3 = System.currentTimeMillis() - star3Started;
		float size3 = 0;

		if (star3Started == 0) {
			size3 = 0;
		} else if (timeSinceStart3 > starAnimationTime) {
			size3 = starSize;
		} else {
			size3 = starSize * (timeSinceStart3 / starAnimationTime);
		}

		drawStars(canvas, size1, size2, size3, width, x, y);

	}

	private void drawStars(Canvas canvas, float size1, float size2,
			float size3, int width, int x, int y) {

		float totalSizeOfStars = size1 + size2 + size3;
		
		float starDrawStartX = x + width * 0.5f - totalSizeOfStars * 0.5f;
		
		
		float yVal1 = y + getHeight() * 0.5f - size1 * 0.5f;

		Rect starRect = new Rect();
		starRect.set((int) starDrawStartX, (int) yVal1, (int) (starDrawStartX + size1),
				(int) (yVal1 + size1));

		if (star1Started != 0) {
			canvas.drawBitmap(mStarBitmap, null, starRect, mStarPaint);
		}

		float xVal2 = starDrawStartX + size1;
		float yVal2 = y + getHeight() * 0.5f - size2 * 0.5f;
		starRect = new Rect();
		starRect.set((int) xVal2, (int) yVal2, (int) (xVal2 + size2),
				(int) (yVal2 + size2));
		if (star2Started != 0) {
			canvas.drawBitmap(mStarBitmap, null, starRect, mStarPaint);
		}

		float xVal3 = starDrawStartX + size1 + size2;
		float yVal3 = y + getHeight() * 0.5f - size3 * 0.5f;
		starRect = new Rect();
		starRect.set((int) xVal3, (int) yVal3, (int) (xVal3 + size3),
				(int) (yVal3 + size3));
		if (star3Started != 0) {
			canvas.drawBitmap(mStarBitmap, null, starRect, mStarPaint);
		}

	}
}
