package com.tojosebe.vulfen.dialog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.dialog.CanvasDialogString.TextSize;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;

public class CanvasDialog extends Screen {

	private int mWidth;
	private int mHeight;
	private float mScale;
	private int mGameOverFinalAlphaBackground = 100; //the alpha when animation is done.
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


	public CanvasDialog (int width, int height, float scale, long dialogStartTime, CanvasDialogString[] dialogText, ImageComponent[] buttons, boolean cancelable) {
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
		
		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(24 * mScale);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		mStrokePaint.setARGB(255, 30, 30, 30);
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
		
		float paddingLeft = (mWidth - mDialogWidth) * 0.5f;
		
		if (mDialogText != null) {
			
			float margin = 10 * mScale;
			mButtonsAreaHeight = mButtons == null ? 0 : mButtons[0].getHeight() * 1.2f;
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
			}
		}
		
		mDialogHeight = (int)(mButtonsAreaHeight + mTextAreaHeight);
		int dialogStartY = (mHeight - mDialogHeight) / 2;
		int dialogEnd = dialogStartY + mDialogHeight;
		
		if (mButtons != null) {
			int sizeBetweenButtons = mDialogWidth / mButtons.length;
			
			for (int i = 0; i < mButtons.length; i++) {
				
				ImageComponent button = mButtons[i];
				
				float x = sizeBetweenButtons * 0.5f - button.getWidth() * 0.5f;
				x += i*sizeBetweenButtons;
				
				button.setPositionX((int) (paddingLeft + x));
				button.setPositionY(dialogEnd - (int)(button.getHeight()*1.1f));
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
		mDialogRect.set(padding * 0.5f,
				dialogStartY,
				padding * 0.5f + mDialogWidth,
				dialogStartY + mDialogHeight);
		
		int colorBefore = mStrokePaint.getColor();
		float strokeWidthBefore = mStrokePaint.getStrokeWidth();
		
		int edgeRadius = (int)(10 * mScale);
		
		mStrokePaint.setStrokeWidth(10*mScale);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		mStrokePaint.setStrokeWidth(8*mScale);
		mStrokePaint.setColor(0xFFf49000);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		mStrokePaint.setStrokeWidth(3*mScale);
		mStrokePaint.setColor(0xFF444411);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, mStrokePaint);
		canvas.drawRoundRect(mDialogRect, edgeRadius, edgeRadius, paint);
		
		if (mButtons != null) {
			//DRAW DIVIDER
			mStrokePaint.setColor(0xFF885103);
			mStrokePaint.setStrokeWidth(2*mScale);
			canvas.drawLine(padding * 0.7f, 
					dialogStartY + mDialogHeight - mButtonsAreaHeight, 
					mWidth - padding * 0.7f, 
					dialogStartY + mDialogHeight - mButtonsAreaHeight, 
					mStrokePaint);
		}
		
		//Restore paint.
		mStrokePaint.setColor(colorBefore);
		mStrokePaint.setStrokeWidth(strokeWidthBefore);
		
		
		
		if (mDialogText != null) {
			
//			mButtonsAreaHeight;
//			mTextAreaHeight;
					
			for (int i = 0; i < mDialogText.length; i++) {
				
				CanvasDialogString canvasDialogString = mDialogText[i]; 
				
				if (canvasDialogString.getTextSize() == TextSize.LARGE) {
					mTextPaint.setTextSize(mLargeTextSize * mScale);
					mStrokePaint.setTextSize(mLargeTextSize * mScale);
				} else if (canvasDialogString.getTextSize() == TextSize.MEDIUM) {
					mTextPaint.setTextSize(mMediumTextSize * mScale);
					mStrokePaint.setTextSize(mMediumTextSize * mScale);
				} else if (canvasDialogString.getTextSize() == TextSize.SMALL) {
					mTextPaint.setTextSize(mSmallTextSize * mScale);
					mStrokePaint.setTextSize(mSmallTextSize * mScale);
				}
				
				mStrokePaint.setStrokeWidth(5*mScale);
				mTextPaint.getTextBounds(canvasDialogString.getContent(), 0, canvasDialogString.getContent().length(), dialogTextRect);
				
				canvas.drawText(canvasDialogString.getContent(), mWidth / 2 - dialogTextRect.width() / 2,
						mHeight / 2 + dialogTextRect.height(), mStrokePaint);
				canvas.drawText(canvasDialogString.getContent(), mWidth / 2 - dialogTextRect.width() / 2,
						mHeight / 2 + dialogTextRect.height(), mTextPaint);
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

		paint.setARGB(steps, 0,0,0);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
	}
	
}
