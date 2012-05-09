package com.tojosebe.vulfen.dialog;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.tojosebe.vulfen.dialog.CanvasDialogString.TextSize;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;

public class CanvasDialog extends Screen {

	private int mWidth;
	private int mHeight;
	private float mScale;
	private int mGameOverFinalAlphaBackground = 150; // the alpha when animation
														// is done.
	private Paint mTextPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border
	private RectF mDialogRect = new RectF();
	private Rect dialogTextRect = new Rect();
	private long mDialogStartTime;
	private int mDialogHeight;
	private ImageComponent[] mButtons;
	private boolean mCancelable;
	private CanvasDialogString[] mDialogText;
	private int mDialogWidth;
	private int mLargeTextSize = 40;
	private int mMediumTextSize = 30;
	private int mSmallTextSize = 20;
	private float mButtonsAreaHeight;
	private float mTextAreaHeight;
	private CanvasDialogDrawArea mDrawArea;
	private float mPaddingLeft;

	public CanvasDialog(int width, int height, float scale,
			long dialogStartTime, CanvasDialogString[] dialogText,
			ImageComponent[] buttons, CanvasDialogDrawArea drawArea,
			boolean cancelable) {
		mWidth = width;
		mHeight = height;
		mScale = scale;
		mDialogStartTime = dialogStartTime;
		mDialogText = dialogText;
		mButtons = buttons;
		mCancelable = cancelable;
		mDialogWidth = mWidth - (int) (mWidth * 0.2f);
		mLargeTextSize *= scale;
		mMediumTextSize *= scale;
		mSmallTextSize *= scale;
		mDrawArea = drawArea;

		mStrokePaint.setARGB(255, 255, 255, 255);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(24 * mScale);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

		mTextPaint.setARGB(255, 30, 30, 30);
		mStrokePaint.setTextSize(24 * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(4);

		setCoversWholeScreen(false);
	}

	@Override
	public boolean handleBackPressed() {
		if (mCancelable) {
			mScreenManager.removeScreen(this);
		}
		return true;
	}

	@Override
	protected void initialize() {

		mPaddingLeft = (mWidth - mDialogWidth) * 0.5f;

		if (mDialogText != null) {

			float margin = 10 * mScale;
			mButtonsAreaHeight = mButtons == null ? 0
					: mButtons[0].getHeight() * 1.2f;
			mTextAreaHeight = margin + margin;

			for (int i = 0; i < mDialogText.length; i++) {
				CanvasDialogString canvasDialogString = mDialogText[i];
				if (canvasDialogString.getTextSize() == TextSize.LARGE) {
					mTextAreaHeight += mLargeTextSize * 1.5f;
				} else if (canvasDialogString.getTextSize() == TextSize.MEDIUM) {
					mTextAreaHeight += mMediumTextSize * 1.5f;
				} else if (canvasDialogString.getTextSize() == TextSize.SMALL) {
					mTextAreaHeight += mSmallTextSize * 1.5f;
				}
				mTextAreaHeight += canvasDialogString.getPadding()[0] * mScale;
				mTextAreaHeight += canvasDialogString.getPadding()[2] * mScale;
				//TODO: we don't care about left and right padding at the moment.
			}
		}

		int drawAreaHeight = mDrawArea != null ? mDrawArea.getHeight() : 0;

		mDialogHeight = (int) (mButtonsAreaHeight + mTextAreaHeight + drawAreaHeight);
		int dialogStartY = (mHeight - mDialogHeight) / 2;
		int dialogEnd = dialogStartY + mDialogHeight;

		if (mButtons != null) {
			int sizeBetweenButtons = mDialogWidth / mButtons.length;

			for (int i = 0; i < mButtons.length; i++) {

				ImageComponent button = mButtons[i];

				float x = sizeBetweenButtons * 0.5f - button.getWidth() * 0.5f;
				x += i * sizeBetweenButtons;

				button.setPositionX((int) (mPaddingLeft + x));
				button.setPositionY(dialogEnd
						- (int) (button.getHeight() * 1.1f));
				addScreenComponent(button);
			}
		}

	}

	@Override
	public void draw(Canvas canvas) {

		Paint paint = new Paint();

		animateBackground(canvas, paint);

		int padding = (int) (mWidth * 0.2f);
		int dialogStartY = (mHeight - mDialogHeight) / 2;
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

		if (mButtons != null) {
			// DRAW DIVIDER
			// mStrokePaint.setColor(0xFF885103);
			mStrokePaint.setColor(0xFF303030);

			mStrokePaint.setStrokeWidth(2 * mScale);
			canvas.drawLine(padding * 0.7f, dialogStartY + mDialogHeight
					- mButtonsAreaHeight, mWidth - padding * 0.7f, dialogStartY
					+ mDialogHeight - mButtonsAreaHeight, mStrokePaint);
		}

		if (mDialogText != null) {

			float currentY = dialogStartY;
			for (int i = 0; i < mDialogText.length; i++) {

				CanvasDialogString canvasDialogString = mDialogText[i];

				if (canvasDialogString.getTextSize() == TextSize.LARGE) {
					mTextPaint.setTextSize(mLargeTextSize);
					mStrokePaint.setTextSize(mLargeTextSize);
				} else if (canvasDialogString.getTextSize() == TextSize.MEDIUM) {
					mTextPaint.setTextSize(mMediumTextSize);
					mStrokePaint.setTextSize(mMediumTextSize);
				} else if (canvasDialogString.getTextSize() == TextSize.SMALL) {
					mTextPaint.setTextSize(mSmallTextSize);
					mStrokePaint.setTextSize(mSmallTextSize);
				}

				if (canvasDialogString instanceof CanvasDialogCounterString) {
					CanvasDialogCounterString canvasDialogCounter = (CanvasDialogCounterString) canvasDialogString;
					if (canvasDialogCounter.getCurrentValue() != canvasDialogCounter
							.getEndValue()) {
						animateCounter(canvasDialogCounter);
					}
				}

				currentY += (mTextPaint.getTextSize() * 1.25f);
				currentY += canvasDialogString.getPadding()[0] * mScale; //upper padding
				
				mTextPaint.getTextBounds(canvasDialogString.getContent(), 0,
						canvasDialogString.getContent().length(),
						dialogTextRect);

				mStrokePaint.setColor(0xFF303030);
				mStrokePaint.setStrokeWidth(5 * mScale);
				canvas.drawText(canvasDialogString.getContent(), mWidth / 2
						- dialogTextRect.width() / 2, currentY, mStrokePaint);
				mTextPaint.setColor(canvasDialogString.getColor());
				canvas.drawText(canvasDialogString.getContent(), mWidth / 2
						- dialogTextRect.width() / 2, currentY, mTextPaint);
				currentY += (mTextPaint.getTextSize() * 0.25f);
				currentY += canvasDialogString.getPadding()[0] * mScale; //bottom padding
			}
		}

		// Restore paint.
		mStrokePaint.setColor(colorBefore);
		mStrokePaint.setStrokeWidth(strokeWidthBefore);

		if (mDrawArea != null) {
			mDrawArea.draw(canvas, (int) mPaddingLeft,
					(int) (dialogStartY + mTextAreaHeight), mDialogWidth);
		}

	}

	private void animateCounter(CanvasDialogCounterString canvasDialogCounter) {

		long timeSinceStart = System.currentTimeMillis() - mDialogStartTime;

		if (timeSinceStart < canvasDialogCounter.getCounterAnimationDelay()) {
			return;
		}

		int endValue = canvasDialogCounter.getEndValue();
		int stepsPerSecond = (int) (endValue / canvasDialogCounter
				.getSecondsToAnimate());
		int steps = (int) ((timeSinceStart / 1000.0f) * stepsPerSecond);

		if (steps > endValue) {
			steps = endValue;
		}

		canvasDialogCounter.setCurrentAnimationValue(steps);
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
