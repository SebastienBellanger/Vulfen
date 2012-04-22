package com.tojosebe.vulfen.levelscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.tojosebe.vulfen.util.Constants;
import com.vulfox.component.ImageComponent;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class LevelButton extends ImageComponent {

	private String mLevelName;

	private int mNbrOfStars;

	private Paint mStarPaint;

	/** Paints. */
	private Paint mTextPaint;
	private Paint mTextPaintShadow;

	private int mShadowOffsetDp = 3;
	private int mShadowOffset;
	
	private int mStarMarginDp = 2;
	private int mStarMargin;
	
	private boolean mLocked;

	/** Text rect. */
	private Rect mTextRect;

	public LevelButton(Bitmap bitmap, boolean locked) {
		super(bitmap, !locked);
		this.mLocked = locked;
	}

	public void initValues(String levelName, Context context, int dpi,
			int width, int stars) {

		setWidth(width);
		setHeight(width);

		mLevelName = levelName;

		mNbrOfStars = stars;

		mShadowOffset = (int) GraphicsUtil.dpToPixels(mShadowOffsetDp, dpi);
		mStarMargin = (int) GraphicsUtil.dpToPixels(mStarMarginDp, dpi);

		mStarPaint = new Paint();
		mStarPaint.setAntiAlias(true);

		this.mTextPaint = new Paint();
		this.mTextPaint.setColor(0xFFFFFFFF);
		this.mTextPaint.setAntiAlias(true);
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);
		this.mTextPaint.setTextSize((int) (30 * (dpi / 160.0f)));
		this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.mTextPaintShadow = new Paint(mTextPaint);
		this.mTextPaintShadow.setColor(0x44000000);
		this.mTextRect = new Rect();
		this.mTextPaint.getTextBounds(mLevelName, 0, mLevelName.length(),
				mTextRect);
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);

		if (!mLocked) {

			// Draw shadow text
			canvas.drawText(mLevelName, getPositionX() + getWidth() / 2 + mShadowOffset,
					getPositionY() + getHeight() / 2 + mTextRect.height() / 2 + mShadowOffset - mStarMargin*2, mTextPaintShadow);
		
			// Draw text
			canvas.drawText(mLevelName, getPositionX() + getWidth() / 2,
					getPositionY() + getHeight() / 2 + mTextRect.height() / 2 - mStarMargin*2, mTextPaint);
			
			Bitmap darkStar = BitmapManager
					.getBitmap(Constants.BITMAP_STAR_DARK);
			Bitmap brightStar = BitmapManager
					.getBitmap(Constants.BITMAP_STAR);
			
			int textEndY = getPositionY() + getHeight() / 2 + mTextRect.height() / 2;
			int starSize = darkStar.getWidth();
			int left = getPositionX() + getWidth() / 2 - starSize - starSize/2 - mStarMargin; 
			
			if (mNbrOfStars >= 1) {
				canvas.drawBitmap(brightStar, left, textEndY + mStarMargin, mStarPaint);
			} else {
				canvas.drawBitmap(darkStar, left, textEndY + mStarMargin, mStarPaint);
			}
			
			if (mNbrOfStars >= 2) {
				canvas.drawBitmap(brightStar, left+starSize+mStarMargin, textEndY + mStarMargin, mStarPaint);
			} else {
				canvas.drawBitmap(darkStar, left+starSize+mStarMargin, textEndY + mStarMargin, mStarPaint);
			}
			
			if (mNbrOfStars >= 3) {
				canvas.drawBitmap(brightStar, left+starSize+starSize+mStarMargin+mStarMargin, textEndY + mStarMargin, mStarPaint);
			} else {
				canvas.drawBitmap(darkStar, left+starSize+starSize+mStarMargin+mStarMargin, textEndY + mStarMargin, mStarPaint);
			}
		}
	}
}
