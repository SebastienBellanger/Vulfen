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
	private Paint mStrokePaint = new Paint(); // text border
	
	private int mStarMarginDp = 2;
	private int mStarMargin;
	
	private boolean mLocked;
	
	private float mScale;

	/** Text rect. */
	private Rect mTextRect;

	public LevelButton(Bitmap bitmap, boolean locked) {
		super(bitmap, !locked);
		this.mLocked = locked;
	}

	public void initValues(String levelName, Context context, int dpi,
			int width, int stars, float scale) {

		mScale = scale;
		setWidth(width);
		setHeight(width);

		mLevelName = levelName;

		mNbrOfStars = stars;

		mStarMargin = (int) GraphicsUtil.dpToPixels(mStarMarginDp, dpi);

		mStarPaint = new Paint();
		mStarPaint.setAntiAlias(true);

		this.mTextPaint = new Paint();
		this.mTextPaint.setColor(0xFFFFFFFF);
		this.mTextPaint.setAntiAlias(true);
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);
		this.mTextPaint.setTextSize((int) (40 * mScale));
		this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.mTextRect = new Rect();
		this.mTextPaint.getTextBounds(mLevelName, 0, mLevelName.length(),
				mTextRect);

		mStrokePaint = new Paint(mTextPaint);
		mStrokePaint.setColor(0x66000000);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setStrokeWidth(4 * mScale);
		
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);

		if (!mLocked) {
			
			float up = 5 * mScale;

			// Draw shadow text
			canvas.drawText(mLevelName, getPositionX() + getWidth() / 2,
					getPositionY() - up + getHeight() / 2 + mTextRect.height() / 2 - mStarMargin*2, mStrokePaint);

			// Draw text
			canvas.drawText(mLevelName, getPositionX() + getWidth() / 2,
					getPositionY() - up + getHeight() / 2 + mTextRect.height() / 2 - mStarMargin*2, mTextPaint);
			
			Bitmap darkStar = BitmapManager
					.getBitmap(Constants.BITMAP_STAR_DARK);
			Bitmap brightStar = BitmapManager
					.getBitmap(Constants.BITMAP_STAR);
			
			int textEndY = getPositionY() - (int)up + getHeight() / 2 + mTextRect.height() / 2;
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

	/**
	 * @param mNbrOfStars the mNbrOfStars to set
	 */
	public void setNbrOfStars(int nbrOfStars) {
		this.mNbrOfStars = nbrOfStars;
	}
	
	public int getNbrOfStars() {
		return this.mNbrOfStars;
	}
}
