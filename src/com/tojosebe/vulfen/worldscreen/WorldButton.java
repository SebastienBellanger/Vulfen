package com.tojosebe.vulfen.worldscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.util.BitmapManager;

public class WorldButton extends StretchableImageButtonComponent {

	private String mWorldName;
	private String mStagesText;
	
	private Rect mTextRect;
	private Rect mScoreTextRect;
	
	private Paint mTextPaint;
	private Paint mStrokePaint = new Paint(); // text border
	
	private Paint mScorePaint;
	private Paint mScorePaintShadow;
	
	private Paint mStarPaint;
	
	private float mScale;
	
	private Rect mStarsClearedRect = new Rect();
	
	private int mClearedStars;
	
	private int mTotalStars;
	
	
	public WorldButton(String worldName, int totalStars, int clearedStars, Context context, int dpi, float scale) {
		super(context, R.drawable.world, "", 0xFFFF0000,
				0xFF000000, 20, 175, 152, dpi, scale);
		mScale = scale;
		mTotalStars = totalStars;
		mClearedStars = clearedStars;
		init(worldName, clearedStars, totalStars);
	}
	
	public WorldButton(String worldName, int totalStars, int clearedStars, Context context, int dpi, WorldButton buttonTemplate, float scale) {
		super((StretchableImageButtonComponent)buttonTemplate);
		mScale = scale;
		mTotalStars = totalStars;
		mClearedStars = clearedStars;
		init(worldName, totalStars, clearedStars);
	}
	
	private void init(String worldName, int clearedStars, int totalStars) {
		
		mWorldName = worldName;
		
		this.mTextPaint = new Paint();
		this.mTextPaint.setColor(0xFFFFFFFF);
		this.mTextPaint.setAntiAlias(true);
		this.mTextPaint.setTextAlign(Paint.Align.CENTER);
		this.mTextPaint.setTextSize((int) 30 * mScale);
		this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.mTextRect = new Rect();
		this.mTextPaint.getTextBounds(worldName, 0, worldName.length(),
				mTextRect);
		
		mStrokePaint = new Paint(mTextPaint);
		mStrokePaint.setColor(0x66000000);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setStrokeWidth(4 * mScale);
		
		this.mStagesText = String.format("%d/%d", clearedStars, totalStars);
		
		this.mScorePaint = new Paint();
		this.mScorePaint.setColor(0xFFFFFFFF);
		this.mScorePaint.setAntiAlias(true);
		this.mScorePaint.setTextSize((int) 24 * mScale);
		this.mScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
		this.mScoreTextRect = new Rect();
		
		
		mScorePaintShadow = new Paint(mScorePaint);
		mScorePaintShadow.setColor(0x66000000);
		mScorePaintShadow.setStyle(Paint.Style.STROKE);
		mScorePaintShadow.setStrokeWidth(4 * mScale);
		
		this.mScorePaint.getTextBounds(mStagesText, 0, mStagesText.length(),
				mScoreTextRect);
		
		mStarPaint = new Paint();
		mStarPaint.setAntiAlias(true);
		
		
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		// Draw mWorldName shadow text
		canvas.drawText(mWorldName, getPositionX() + getWidth() / 2,
				getPositionY() + getHeight() / 4 + mTextRect.height() / 2, mStrokePaint);
		// Draw mWorldName text
		canvas.drawText(mWorldName, getPositionX() + getWidth() / 2,
				getPositionY() + getHeight() / 4 + mTextRect.height() / 2, mTextPaint);
			
		int bottomEdge = getPositionY() + getHeight() - getHeight()/5;
		
		this.mStagesText = String.format("%d/%d", mClearedStars, mTotalStars);
		
		Bitmap brightStar = BitmapManager.getBitmap(Constants.BITMAP_STAR_BIG);
		
		int starSize = brightStar.getWidth();
		
		mScorePaint.getTextBounds(mStagesText, 0, mStagesText.length(), mStarsClearedRect);

		int width = getWidth();
		float spaceBetweenStarAndText = 5 * mScale;
		float widthStarsCleared = starSize + mStarsClearedRect.width() + spaceBetweenStarAndText;
		
		int starLeft = getPositionX()
				+ (int) ((width - widthStarsCleared) / 2.0f);

		canvas.drawBitmap(brightStar, starLeft + spaceBetweenStarAndText
				+ mStarsClearedRect.width(), bottomEdge - 20 * mScale - brightStar.getHeight() + 10 * mScale, mStarPaint);
	
		// Draw mStagesText shadow text
		canvas.drawText(mStagesText, starLeft,
				bottomEdge - 20 * mScale, mScorePaintShadow);
		// Draw mStagesText text
		canvas.drawText(mStagesText, starLeft,
				bottomEdge - 20 * mScale, mScorePaint);
	}

	/**
	 * @return the mClearedStars
	 */
	public int getClearedStars() {
		return mClearedStars;
	}

	/**
	 * @param mClearedStars the mClearedStars to set
	 */
	public void setClearedStars(int clearedStars) {
		this.mClearedStars = clearedStars;
	}

	
}
