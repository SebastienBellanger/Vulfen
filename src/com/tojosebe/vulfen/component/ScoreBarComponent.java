package com.tojosebe.vulfen.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.component.ScreenComponent;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class ScoreBarComponent extends ScreenComponent {

	private Context mContext;
	private float mScale;
	private Rect mDrawRect;
	private Rect mStarDrawRect;
	private Rect mDrawRectOverlay;
	private Rect mDrawRectMarker;
	private Paint paint;
	private Bitmap mBitmapProgressBackground;
	private Bitmap mBitmapStarsOverlay;
	private Bitmap mBitmapGrayOverlay;
	private Bitmap mBitmapScoreMarker;
	private Bitmap mBitmapStar;
	private Bitmap mBitmapStarDark;
	
	private boolean star1showing = false;
	private boolean star2showing = false;
	private boolean star3showing = false;
	
	private float mPaddingRight;
	private float mTotalBarWidth;
	
	private float positionStar1;
	private float positionStar2;
	
	private float fractionStar1;
	private float fractionStar2;
	
	

	public ScoreBarComponent(Context context, float scale, int scoreStar1, int scoreStar2, int scoreStar3) {
		mScale = scale;
		mContext = context;
		paint = new Paint();
		paint.setAntiAlias(true);

		mBitmapProgressBackground = createScoreBarBackground();
		mBitmapGrayOverlay = createScoreBarGrayOverlay();
		mBitmapStarsOverlay = createScoreBarStarsOverlay();
		mBitmapScoreMarker = ImageLoader.loadFromResource(
				mContext.getApplicationContext(),
				R.drawable.scorebar_marker);
		mBitmapStar = ImageLoader.loadFromResource(
				mContext.getApplicationContext(),
				R.drawable.scorebar_star);
		mBitmapStar = GraphicsUtil.resizeBitmap(mBitmapStar,
				(int) (mScale * mBitmapStar.getHeight() * 0.5f),
				(int) (mScale * mBitmapStar.getWidth() * 0.5f));
		mBitmapStarDark = ImageLoader.loadFromResource(
				mContext.getApplicationContext(),
				R.drawable.scorebar_star_dark);
		mBitmapStarDark = GraphicsUtil.resizeBitmap(mBitmapStarDark,
				(int) (mScale * mBitmapStarDark.getHeight() * 0.5f),
				(int) (mScale * mBitmapStarDark.getWidth() * 0.5f));

		
		mPaddingRight = mBitmapProgressBackground.getWidth() * 0.0658f; //Beacause I drew it this way i.e 6.58% padding.
		mTotalBarWidth = mBitmapProgressBackground.getWidth() - mPaddingRight;
		
		int halfStarWidth = (int) (mBitmapStarDark.getHeight()*0.5f);
		
		fractionStar1 = ((float)scoreStar1 / (float)scoreStar3);
		fractionStar2 = ((float)scoreStar2 / (float)scoreStar3);
		
		positionStar1 = fractionStar1 * mTotalBarWidth - halfStarWidth;
		positionStar2 = fractionStar2 * mTotalBarWidth - halfStarWidth;

		mDrawRect = new Rect();
		mStarDrawRect = new Rect();
		mDrawRectOverlay = new Rect();
		mDrawRectMarker = new Rect();

		// TODO: stoppa in höjden på texten här på något sätt så det blir
		// snyggt.
		mDrawRect.set((int) (10 * mScale), (int) (5 * mScale),
				(int) (10 * mScale + mBitmapProgressBackground.getWidth()),
				(int) (5 * mScale + mBitmapProgressBackground.getHeight()));
		
		mStarDrawRect.set((int) (10 * mScale), (int) (5 * mScale),
				(int) (10 * mScale + mBitmapStarDark.getWidth()),
				(int) (5 * mScale + mBitmapStarDark.getHeight()));
	}
	
	public void reset() {
		star1showing = false;
		star2showing = false;
		star3showing = false;
		setScoreProgress(0,0,0);
	}
	
	private Bitmap createScoreBarStarsOverlay() {

		if (BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_STARS_OVERLAY) == null) {

			Bitmap scoreBarBackground = ImageLoader.loadFromResource(
					mContext.getApplicationContext(),
					R.drawable.scorebar_stars_overlay);

			scoreBarBackground = GraphicsUtil.resizeBitmap(scoreBarBackground,
					(int) (mScale * scoreBarBackground.getHeight() * 0.5f),
					(int) (mScale * scoreBarBackground.getWidth() * 0.5f));

			BitmapManager.addBitmap(Constants.BITMAP_SCORE_BAR_STARS_OVERLAY,
					scoreBarBackground);
		}
		return BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_STARS_OVERLAY);
	}

	private Bitmap createScoreBarGrayOverlay() {

		if (BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_GRAY_OVERLAY) == null) {

			Bitmap scoreBarBackground = ImageLoader.loadFromResource(
					mContext.getApplicationContext(),
					R.drawable.scorebar_gray_overlay);

			scoreBarBackground = GraphicsUtil.resizeBitmap(scoreBarBackground,
					(int) (mScale * scoreBarBackground.getHeight() * 0.5f),
					(int) (mScale * scoreBarBackground.getWidth() * 0.5f));

			BitmapManager.addBitmap(Constants.BITMAP_SCORE_BAR_GRAY_OVERLAY,
					scoreBarBackground);
		}
		return BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_GRAY_OVERLAY);
	}

	private Bitmap createScoreBarBackground() {

		if (BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_BACK) == null) {

			Bitmap scoreBarBackground = ImageLoader.loadFromResource(
					mContext.getApplicationContext(),
					R.drawable.scorebar_progress);

			scoreBarBackground = GraphicsUtil.resizeBitmap(scoreBarBackground,
					(int) (mScale * scoreBarBackground.getHeight() * 0.5f),
					(int) (mScale * scoreBarBackground.getWidth() * 0.5f));

			BitmapManager.addBitmap(Constants.BITMAP_SCORE_BAR_BACK,
					scoreBarBackground);
		}
		return BitmapManager.getBitmap(Constants.BITMAP_SCORE_BAR_BACK);
	}

	@Override
	public void draw(Canvas canvas) {
		
		if (isVisible()) {
			canvas.drawBitmap(
					mBitmapProgressBackground,
					null, mDrawRect, paint);
			canvas.drawBitmap(
					mBitmapGrayOverlay,
					null, mDrawRectOverlay, paint);
			canvas.drawBitmap(
					mBitmapScoreMarker,
					null, mDrawRectMarker, paint);
			canvas.drawBitmap(
					mBitmapStarsOverlay,
					null, mDrawRect, paint);
		
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)positionStar1;
			mStarDrawRect.right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar1);
			
			if (star1showing) {
				canvas.drawBitmap(
						mBitmapStar,
						null, mStarDrawRect, paint);
			} else {
				canvas.drawBitmap(
						mBitmapStarDark,
						null, mStarDrawRect, paint);
			}
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)positionStar2;
			mStarDrawRect.right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar2);
			
			if (star2showing) {
				canvas.drawBitmap(
						mBitmapStar,
						null, mStarDrawRect, paint);
			} else {
				canvas.drawBitmap(
						mBitmapStarDark,
						null, mStarDrawRect, paint);
			}
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth() - mBitmapStar.getWidth();
			mStarDrawRect.right = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth();
			
			if (star3showing) {
				canvas.drawBitmap(
						mBitmapStar,
						null, mStarDrawRect, paint);
			}
		}
	}

	@Override
	public void handleActionDown(MotionEvent motionEvent,
			boolean insideConponent) {
	}

	@Override
	public boolean handleActionUp(MotionEvent motionEvent,
			boolean insideConponent) {
		return false;
	}

	@Override
	public void handleActionMove(MotionEvent motionEvent,
			boolean insideConponent) {
	}
	
	
	public void setScoreProgress(float star1Progress, float star2Progress, float star3Progress) {
		
		
		float currentBarWidth = 0f;
		
		
		if (star1Progress < 1.0) {
			currentBarWidth = star1Progress * mTotalBarWidth * fractionStar1;
			star1showing = false;
			star2showing = false;
			star3showing = false;
		} else if (star2Progress < 1.0) {
			star1showing = true;
			star2showing = false;
			star3showing = false;
			currentBarWidth = mTotalBarWidth * fractionStar1 + star2Progress * (mTotalBarWidth * fractionStar2 - mTotalBarWidth * fractionStar1);
		} else if (star3Progress < 1.0) {
			star1showing = true;
			star2showing = true;
			star3showing = false;
			currentBarWidth = mTotalBarWidth * fractionStar2 + star3Progress * (mTotalBarWidth * (1 - fractionStar2));
		} else {
			star1showing = true;
			star2showing = true;
			star3showing = true;
			currentBarWidth = mTotalBarWidth;
		}
		
		mDrawRectOverlay.set((int) ((10 * mScale) + currentBarWidth), (int) (5 * mScale),
				(int) (10 * mScale + mTotalBarWidth),
				(int) (5 * mScale + mBitmapProgressBackground.getHeight()));
		if (star1Progress < 0.05) {
			mDrawRectMarker.set(0,0,0,0);
		} else {
			mDrawRectMarker.set((int) ((10 * mScale) + currentBarWidth), (int) (5 * mScale),
					(int) (10 * mScale  + currentBarWidth + 2 * mScale),
					(int) (5 * mScale + mBitmapProgressBackground.getHeight()));
		}
	}
	

	/**
	 * @return the mDrawRect
	 */
	public Rect getDrawRect() {
		return mDrawRect;
	}

	@Override
	public void update(float timeStep) {
	}

}
