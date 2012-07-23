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
	
	private long starAnimationTime = 200;
	private long star1animationStartTime = 0;
	private long star2animationStartTime = 0;
	private long star3animationStartTime = 0;
	private float currentAnimationSizeStar1 = 1.0f; //100 percent i.e. full size
	private float currentAnimationSizeStar2 = 1.0f; //100 percent i.e. full size
	private float currentAnimationSizeStar3 = 1.0f; //100 percent i.e. full size
	
	private float mPaddingRight;
	private float mTotalBarWidth;
	
	private float positionStar1;
	private float positionStar2;
	
	private float fractionStar1;
	private float fractionStar2;
	
	private int starTop;
	private int starBottom;
	
			
	

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
		
		starTop = mDrawRect.top;
		starBottom = mDrawRect.bottom;
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
		
			int diffX = 0;
			int diffY = 0;
			if (currentAnimationSizeStar1 != 1.0f) {
				int left = (int) (10 * mScale) + (int)positionStar1;
				int right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar1);
				int widthStar1 = (right - left);
				int adjustedWidthStar1 = (int)(currentAnimationSizeStar1 * widthStar1);
				diffX = (int) ((adjustedWidthStar1 - widthStar1) / 2.0f);
				int heightStar1 = (starBottom - starTop);
				int adjustedHeightStar1 = (int)(currentAnimationSizeStar1 * heightStar1);
				diffY = (int) ((adjustedHeightStar1 - heightStar1) / 2.0f);
			}
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)positionStar1 - diffX;
			mStarDrawRect.right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar1 + diffX);
			mStarDrawRect.top = starTop - diffY;
			mStarDrawRect.bottom = starBottom + diffY;
			
			if (star1showing) {
				canvas.drawBitmap(
						mBitmapStar,
						null, mStarDrawRect, paint);
			} else {
				canvas.drawBitmap(
						mBitmapStarDark,
						null, mStarDrawRect, paint);
			}
			
			
			diffX = 0;
			diffY = 0;
			if (currentAnimationSizeStar2 != 1.0f) {
				int left = (int) (10 * mScale) + (int)positionStar2;
				int right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar2);
				int widthStar2 = (right - left);
				int adjustedWidthStar2 = (int)(currentAnimationSizeStar2 * widthStar2);
				diffX = (int) ((adjustedWidthStar2 - widthStar2) / 2.0f);
				int heightStar2 = (starBottom - starTop);
				int adjustedHeightStar2 = (int)(currentAnimationSizeStar2 * heightStar2);
				diffY = (int) ((adjustedHeightStar2 - heightStar2) / 2.0f);
			}
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)positionStar2 - diffX;
			mStarDrawRect.right = (int)(10 * mScale + mBitmapStarDark.getWidth() + (int)positionStar2 + diffX);
			mStarDrawRect.top = starTop - diffY;
			mStarDrawRect.bottom = starBottom + diffY;
			
			if (star2showing) {
				canvas.drawBitmap(
						mBitmapStar,
						null, mStarDrawRect, paint);
			} else {
				canvas.drawBitmap(
						mBitmapStarDark,
						null, mStarDrawRect, paint);
			}
			
			diffX = 0;
			diffY = 0;
			if (currentAnimationSizeStar3 != 1.0f) {
				int left = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth() - mBitmapStar.getWidth();
				int right = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth();
				int widthStar3 = (right - left);
				int adjustedWidthStar3 = (int)(currentAnimationSizeStar3 * widthStar3);
				diffX = (int) ((adjustedWidthStar3 - widthStar3) / 2.0f);
				int heightStar3 = (starBottom - starTop);
				int adjustedHeightStar3 = (int)(currentAnimationSizeStar3 * heightStar3);
				diffY = (int) ((adjustedHeightStar3 - heightStar3) / 2.0f);
			}
			
			mStarDrawRect.left = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth() - mBitmapStar.getWidth() - diffX;
			mStarDrawRect.right = (int) (10 * mScale) + (int)mBitmapProgressBackground.getWidth() + diffX;
			mStarDrawRect.top = starTop - diffY;
			mStarDrawRect.bottom = starBottom + diffY;
			
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
			if (!star1showing) {
				star1showing = true;
				star1animationStartTime = System.currentTimeMillis();
			}
			star2showing = false;
			star3showing = false;
			currentBarWidth = mTotalBarWidth * fractionStar1 + star2Progress * (mTotalBarWidth * fractionStar2 - mTotalBarWidth * fractionStar1);
		} else if (star3Progress < 1.0) {
			star1showing = true;
			if (!star2showing) {
				star2showing = true;
				star2animationStartTime = System.currentTimeMillis();
			}
			star3showing = false;
			currentBarWidth = mTotalBarWidth * fractionStar2 + star3Progress * (mTotalBarWidth * (1 - fractionStar2));
		} else {
			star1showing = true;
			star2showing = true;
			if (!star3showing) {
				star3showing = true;
				star3animationStartTime = System.currentTimeMillis();
			}
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
		
		if (star1animationStartTime > 0) {
			long timeSinceStart = System.currentTimeMillis() - star1animationStartTime;
			if (timeSinceStart < starAnimationTime) {
				float fraction = timeSinceStart / (float)starAnimationTime;
				if (fraction > 1.0f) {
					fraction = 1.0f;
				}
				currentAnimationSizeStar1 = 1.0f + (1.0f - fraction);
			} else {
				star1animationStartTime = 0;
				currentAnimationSizeStar1 = 1.0f;
			}
		}
		
		if (star2animationStartTime > 0) {
			long timeSinceStart = System.currentTimeMillis() - star2animationStartTime;
			if (timeSinceStart < starAnimationTime) {
				float fraction = timeSinceStart / (float)starAnimationTime;
				if (fraction > 1.0f) {
					fraction = 1.0f;
				}
				currentAnimationSizeStar2 = 1.0f + (1.0f - fraction);
			} else {
				star2animationStartTime = 0;
				currentAnimationSizeStar2 = 1.0f;
			}
		}
		
		if (star3animationStartTime > 0) {
			long timeSinceStart = System.currentTimeMillis() - star3animationStartTime;
			if (timeSinceStart < starAnimationTime) {
				float fraction = timeSinceStart / (float)starAnimationTime;
				if (fraction > 1.0f) {
					fraction = 1.0f;
				}
				currentAnimationSizeStar3 = 1.0f + (1.0f - fraction);
			} else {
				star3animationStartTime = 0;
				currentAnimationSizeStar3 = 1.0f;
			}
		}
		
		
	}

}
