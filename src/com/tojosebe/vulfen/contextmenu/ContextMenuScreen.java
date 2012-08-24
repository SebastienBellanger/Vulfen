package com.tojosebe.vulfen.contextmenu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.tojosebe.vulfen.game.BowlScreen;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;

public class ContextMenuScreen extends Screen {

	private int mWidth;
	private int mHeight;
	private float mScale;
	private int mGameOverFinalAlphaBackground = 150; // the alpha when animation
														// is done.
	private Paint mTextPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border
	private RectF mDialogRect = new RectF();
	private long mDialogStartTime;
	private int mDialogHeight;
	private ImageComponent[] mButtons;
	private boolean mCancelable;
	private int mDialogWidth;
	private float mButtonsAreaHeight;
	private float mPaddingLeft;
	private int dialogStartY;
	private long animationFromBottomTime = 150;
	private BowlScreen mBowlScreen;

	public ContextMenuScreen(int width, int height, float scale, ImageComponent[] buttons, 
			boolean cancelable, BowlScreen bowlScreen) {
		mBowlScreen = bowlScreen;
		mWidth = width;
		mHeight = height;
		mScale = scale;
		mDialogStartTime = System.currentTimeMillis();
		mButtons = buttons;
		mCancelable = cancelable;
		mDialogWidth = mWidth - (int) (mWidth * 0.2f);

		mTextPaint.setARGB(255, 30, 30, 30);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(24 * mScale);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		mStrokePaint.setARGB(255, 255, 255, 255);
		mStrokePaint.setTextSize(24 * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(4 * mScale);

		setCoversWholeScreen(false);
	}

	@Override
	public boolean handleBackPressed() {
		if (mCancelable) {
			mScreenManager.removeScreenUI(this);
			mBowlScreen.setPaused(false);
		}
		return true;
	}

	@Override
	protected void initialize() {

		mPaddingLeft = (mWidth - mDialogWidth) * 0.5f;
		
		mButtonsAreaHeight = mButtons == null ? 0
				: mButtons[0].getHeight() * 1.2f;

		mDialogHeight = (int) (mButtonsAreaHeight * 1.2f);
		dialogStartY = mHeight;

		if (mButtons != null) {
			int sizeBetweenButtons = mDialogWidth / mButtons.length;

			for (int i = 0; i < mButtons.length; i++) {

				ImageComponent button = mButtons[i];

				float x = sizeBetweenButtons * 0.5f - button.getWidth() * 0.5f;
				x += i * sizeBetweenButtons;
				
				button.setPositionX((int) (mPaddingLeft + x));
				button.setPositionY((int) (dialogStartY + mDialogHeight*0.5f - button.getHeight()*0.5f));
				addScreenComponent(button);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		
		Paint paint = new Paint();
		
		updateDialogY();

		animateBackground(canvas, paint);

		int padding = (int) (mWidth * 0.2f);
		
		paint.setColor(0xFFFFFFAA);
		mDialogRect.set(padding * 0.5f, dialogStartY, padding * 0.5f
				+ mDialogWidth, dialogStartY + mDialogHeight);

		int colorBefore = mStrokePaint.getColor();
		float strokeWidthBefore = mStrokePaint.getStrokeWidth();

		int edgeRadius = (int) (10 * mScale);

		mStrokePaint.setARGB(255, 30, 30, 30);
		mStrokePaint.setStrokeWidth(10 * mScale);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		mStrokePaint.setStrokeWidth(8 * mScale);
		mStrokePaint.setColor(0xFFf49000);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		mStrokePaint.setStrokeWidth(3 * mScale);
		mStrokePaint.setColor(0xFF444411);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, paint);

		// Restore paint.
		mStrokePaint.setColor(colorBefore);
		mStrokePaint.setStrokeWidth(strokeWidthBefore);

	}

	private void updateDialogY() {
		long timeSinceStart = System.currentTimeMillis() - mDialogStartTime;
		
		float percentageDone = 0.0f;
		
		if (timeSinceStart > animationFromBottomTime) {
			percentageDone = 1.0f;
		} else {
			percentageDone = timeSinceStart / (float)animationFromBottomTime;
		}
		
		int targetY = (mHeight - mDialogHeight) + (int) (mButtonsAreaHeight * 0.1f);
		int startY = mHeight;
		
		int diff = startY - targetY;
		
		float currentDiff = diff * percentageDone;
		
		dialogStartY = mHeight - (int)currentDiff;
		
		if (mButtons != null) {
			int sizeBetweenButtons = mDialogWidth / mButtons.length;

			for (int i = 0; i < mButtons.length; i++) {

				ImageComponent button = mButtons[i];

				float x = sizeBetweenButtons * 0.5f - button.getWidth() * 0.5f;
				x += i * sizeBetweenButtons;
				
				button.setPositionX((int) (mPaddingLeft + x));
				button.setPositionY((int) (dialogStartY + mDialogHeight*0.5f - button.getHeight()*0.5f));
			}
		}
	}

	private void animateBackground(Canvas canvas, Paint paint) {
		int alphaStepsPerSecond = 400;
		long timeSinceEnd = System.currentTimeMillis() - mDialogStartTime;
		int steps = (int) ((timeSinceEnd / 1000.0f) * alphaStepsPerSecond);

		if (steps > mGameOverFinalAlphaBackground) {
			steps = mGameOverFinalAlphaBackground;
		}

		paint.setARGB(steps, 0, 0, 0);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
	}

}
