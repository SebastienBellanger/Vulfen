package com.tojosebe.vulfen.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.util.StringUtil;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.GraphicsUtil;

public class AlienTalkScreen extends Screen {

	private long mStartTime;
	private long mBackgroundAnimationTimeMillis = 200;
	private long mAlienAnimationTimeMillis = 2000;
	Paint mPaint = new Paint();
	private int numberOfStars = 150;
	private float[] mStars;
	private Bitmap mAlienBitmap;
	private int mDpi;
	private float mScale;
	private int mAlienEndX = 30;
	private boolean mAlienAnimationDone = false;
	private boolean playButtonAdded = false;
	private String mBoubleText[];
	private int mLettersPerSecond = 40;
	private long mLettersStartTime;
	private int mLetterHeight = 24;
	private Rect dialogTextRect = new Rect();
	private int currentPage = 0;
	private int rowsPerPage = 4;
	private int totalPages = 0;
	private List<String> mRows = new ArrayList<String>();
	private Path wallpath = new Path();
	private Path morePath = new Path();

	private Paint mTextPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border

	// Bouble stuff
	private RectF mBoubleRect = new RectF();
	private float mPaddingRight = 20.0f;
	private float mPaddingShipAlien = 20.0f;
	private float mBoubleWidth = 300;
	private float mBoubleHeight = 250;
	private float mBoubleEdge = 20;
	private float mBoubleLeft;
	private float mBoubleTop;
	private float mBoubleMoreArrowWidth = 20;

	public AlienTalkScreen(int dpi, String[] boubleText) {
		mBoubleText = boubleText;
		mDpi = dpi;
	}

	@Override
	protected void initialize() {
		super.initialize();

		mScale = getWidth() / 480.0f;
		mAlienEndX *= mScale;
		mBoubleMoreArrowWidth *= mScale;

		mStartTime = System.currentTimeMillis();

		mPaint.setAntiAlias(true);

		// x,y for numberOfStars stars.
		mStars = new float[numberOfStars * 2];

		Random rand = new Random(mStartTime);

		for (int i = 0; i < numberOfStars; i += 2) {
			mStars[i] = rand.nextInt(getWidth() - 1);
			mStars[i + 1] = rand.nextInt(getHeight() - 1);
		}

		mAlienBitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.alienship);

		float width = getWidth() * 0.5f;
		float growFactor = width / mAlienBitmap.getWidth();

		mAlienBitmap = GraphicsUtil.resizeBitmap(mAlienBitmap,
				(int) (mAlienBitmap.getHeight() * growFactor), (int) width);

		mTextPaint.setARGB(255, 30, 30, 30);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(mLetterHeight * mScale);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mTextPaint.setTextAlign(Align.LEFT);

		mStrokePaint.setARGB(255, 255, 255, 255);
		mStrokePaint.setTextSize(mLetterHeight * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(4);
		mStrokePaint.setTextAlign(Align.LEFT);

		mPaddingRight *= mScale;
		mPaddingShipAlien *= mScale;
		mBoubleWidth *= mScale;
		mBoubleHeight *= mScale;
		mBoubleEdge *= mScale;
		mBoubleLeft = getWidth() - mBoubleWidth - mPaddingRight;
		mBoubleTop = getHeight() * 0.5f - mBoubleHeight - mPaddingShipAlien;
		mBoubleRect.set(mBoubleLeft, mBoubleTop, mBoubleLeft + mBoubleWidth,
				mBoubleTop + mBoubleHeight);

		for (int i = 0; i < mBoubleText.length; i++) {

			mTextPaint.getTextBounds(mBoubleText[i], 0,
					mBoubleText[i].length(), dialogTextRect);
			if (dialogTextRect.width() > mBoubleRect.width() - 2 * mBoubleEdge) {
				// Did not fit!
				List<String> partsThatFits = getPartsThatFits(
						mBoubleRect.width() - 2 * mBoubleEdge, mBoubleText[i]);
				mRows.addAll(partsThatFits);
			} else {
				mRows.add(mBoubleText[i]);
			}
		}

		totalPages = mRows.size() / rowsPerPage;
		if (totalPages * rowsPerPage < mRows.size()) {
			totalPages++;
		}
	}

	private List<String> getPartsThatFits(float textAreaWidth,
			String stringToFit) {

		List<String> parts = new ArrayList<String>();

		String[] words = stringToFit.split(" ");

		String currentString = "";

		int index = 0;
		for (String word : words) {
			currentString += " " + word;
			currentString = currentString.trim();

			mTextPaint.getTextBounds(currentString, 0, currentString.length(),
					dialogTextRect);

			if (dialogTextRect.width() > mBoubleRect.width() - 2 * mBoubleEdge
					|| index == words.length - 1) {

				if (dialogTextRect.width() <= mBoubleRect.width() - 2
						* mBoubleEdge) {
					parts.add(currentString);
				} else {
					parts.add(StringUtil
							.substringBeforeLast(currentString, " "));
					currentString = word;
					if (index == words.length - 1) {
						parts.add(currentString.trim());
					}
				}
			}
			index++;
		}

		return parts;
	}

	@Override
	public void update(float timeStep) {

	}

	@Override
	protected void onTop() {

	}

	public void handleInput(MotionEvent motionEvent) {
		if (currentPage < totalPages - 1 && mAlienAnimationDone) {
			currentPage++;
			mLettersStartTime = System.currentTimeMillis();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		animateBackground(canvas);

		drawStars(canvas);

		drawAlien(canvas);

		if (mAlienAnimationDone && !playButtonAdded
				&& currentPage == totalPages - 1) {
			addScreenComponent(createPlayButton());
			playButtonAdded = true;
		}

		if (mAlienAnimationDone) {
			if (mLettersStartTime == 0) {
				mLettersStartTime = System.currentTimeMillis();
			}
			drawBouble(canvas);
		}

	}

	private void drawBouble(Canvas canvas) {

		int oldColor = mPaint.getColor();

		mPaint.setColor(0xFFFFFFFF);

		canvas.drawRoundRect(mBoubleRect, mBoubleEdge, mBoubleEdge, mPaint);

		mPaint.setStyle(Style.FILL);
		wallpath.moveTo(mBoubleLeft, mBoubleTop + mBoubleHeight - mBoubleEdge);
		wallpath.lineTo(mAlienEndX + mAlienBitmap.getWidth() * 0.5f,
				getHeight() * 0.5f);
		wallpath.lineTo(mBoubleLeft + mBoubleEdge, mBoubleTop + mBoubleHeight);
		wallpath.lineTo(mBoubleLeft, mBoubleTop + mBoubleHeight - mBoubleEdge);
		canvas.drawPath(wallpath, mPaint);

		float currentY = mBoubleTop + mBoubleEdge * 2;

		int loops = mRows.size() > rowsPerPage ? rowsPerPage : mRows.size();
		if (currentPage * rowsPerPage + rowsPerPage >= mRows.size()) {
			loops = mRows.size() % rowsPerPage;
			if (mRows.size() <= rowsPerPage) {
				loops = mRows.size();
			}
		}
		boolean morePages = (rowsPerPage * (currentPage + 1)) < mRows.size();
		
		
		long timeSinceStart = (System.currentTimeMillis() - mLettersStartTime);
		int totalLettersToDrawNow = (int)((timeSinceStart/(float)1000) * mLettersPerSecond);		
		
		int totalLettersDrawn = 0;

		for (int i = 0; i < loops; i++) {

			String textToDraw = mRows.get(currentPage * rowsPerPage + i);
			boolean breakOut = false;

			int lettersToDrawThisLoop = textToDraw.length();
			if (totalLettersDrawn + lettersToDrawThisLoop > totalLettersToDrawNow) {
				textToDraw = textToDraw.substring(0,totalLettersToDrawNow - totalLettersDrawn);
				breakOut = true;
			}

			mTextPaint.getTextBounds(textToDraw, 0, textToDraw.length(),
					dialogTextRect);

			mStrokePaint.setColor(0xFF303030);
			mStrokePaint.setStrokeWidth(5 * mScale);
			canvas.drawText(textToDraw, mBoubleLeft + mBoubleEdge, currentY,
					mStrokePaint);
			mTextPaint.setColor(0xFFBAC0E3);
			canvas.drawText(textToDraw, mBoubleLeft + mBoubleEdge, currentY,
					mTextPaint);

			currentY += (mTextPaint.getTextSize() * 2.0f);
			
			totalLettersDrawn += textToDraw.length();
			
			if (breakOut) {
				break;
			}
		}

		// Draw more arrow
		if (morePages && (System.currentTimeMillis() - mStartTime) % 1000 > 500) {

			mPaint.setColor(0xFF000000);

			float boubleCenter = mBoubleRect.left + mBoubleRect.width() * 0.5f;

			morePath.moveTo(boubleCenter, mBoubleTop + mBoubleHeight
					- mBoubleEdge);
			morePath.lineTo(boubleCenter - mBoubleMoreArrowWidth * 0.5f,
					mBoubleTop + mBoubleHeight - mBoubleEdge * 2);
			morePath.lineTo(boubleCenter + mBoubleMoreArrowWidth * 0.5f,
					mBoubleTop + mBoubleHeight - mBoubleEdge * 2);
			morePath.lineTo(boubleCenter, mBoubleTop + mBoubleHeight
					- mBoubleEdge);
			canvas.drawPath(morePath, mPaint);
		}

		mPaint.setColor(oldColor);
	}

	private void drawAlien(Canvas canvas) {

		int startX = -(int) (getWidth() * 0.5f);

		int totalSteps = Math.abs(startX - mAlienEndX);

		long timeSinceStart = System.currentTimeMillis() - mStartTime;

		int currentStep = mAlienEndX;

		if (timeSinceStart < mAlienAnimationTimeMillis) {
			currentStep = startX
					+ (int) (totalSteps * (timeSinceStart / (float) mAlienAnimationTimeMillis));
		} else {
			mAlienAnimationDone = true;
		}

		canvas.drawBitmap(mAlienBitmap, currentStep, getHeight() * 0.5f, mPaint);
	}

	private void drawStars(Canvas canvas) {

		int oldColor = mPaint.getColor();
		mPaint.setColor(0xFFFFFF00);

		for (int i = 0; i < numberOfStars; i++) {
			canvas.drawPoints(mStars, mPaint);
		}

		mPaint.setColor(oldColor);

	}

	private void animateBackground(Canvas canvas) {
		int alphaStepsPerSecond = 400;

		long timeSinceStart = System.currentTimeMillis() - mStartTime;

		int steps = 255;
		if (timeSinceStart < mBackgroundAnimationTimeMillis) {
			steps = (int) ((timeSinceStart / 1000.0f) * alphaStepsPerSecond);
		}

		mPaint.setARGB(steps, 0, 0, 40);
		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
	}

	private ScreenComponent createPlayButton() {

		StretchableImageButtonComponent button = new StretchableImageButtonComponent(
				mContext.getApplicationContext(), R.drawable.button,
				"Continue", 0xFFFFFFFF, 0x44000000, 30, 170, 50, mDpi);

		button.setEventListener(new EventListener() {
			@Override
			public boolean handleButtonClicked() {

				mScreenManager.removeTopScreen();

				return true;
			}
		});

		float bottomPadding = 30.0f;
		bottomPadding *= mScale;

		button.setPositionX(getWidth() / 2 - button.getWidth() / 2);
		button.setPositionY((int) (getHeight() - button.getHeight() - bottomPadding));

		return button;
	}

}
