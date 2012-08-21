package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.animation.AnimateableImageComponent;
import com.tojosebe.vulfen.startscreen.Cloud;
import com.tojosebe.vulfen.startscreen.Cow;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class StoryScreen extends Screen {

	private static final int ALIEN_SHIP_BOTTOM_Y = 88;
	private static float TELEPORT_COW_POSITION_X = 0;
	private static float TELEPORT_COW_POSITION_Y = 0;
	private static float TELEPORT_COW_HEIGHT = 0;
	private static float TELEPORT_COW_WIDTH = 0;
	
	private Bitmap mAlienBitmap;
	private Bitmap mFarmHouseBitmap;
	private Rect mFarmHouseRect;
	private Bitmap mFenceBitmap;
	private Rect mFenceRect;
	private Rect mBackgroundRect;
	private int mAlienY = 100;
	private long mStartTime;
	private long mAlienAnimationTimeMillis = 4000;
	Paint mPaint = new Paint();
	private Cloud mCloud1;
	private Cloud mCloud2;
	private Cow[] mCows;
	private ScreenComponent mPenguin;

	private Path mTeleportPath = new Path();
	private long mCowTeleportDelay = 2000;
	private long mCowTeleportTime = 5000;
	private boolean mTeleporting = false;

	private int mDpi;
	private float mScale;
	private int mAlienEndX = 30;
	private boolean mAlienAnimationDone = false;
	private boolean playButtonAdded = false;

	public StoryScreen(int dpi, Cloud cloud1, Cloud cloud2) {
		mDpi = dpi;
		this.mCloud1 = cloud1;
		this.mCloud2 = cloud2;
	}

	@Override
	public void update(float timeStep) {
		mCloud1.update(timeStep);
		mCloud2.update(timeStep);
		for (Cow cow : mCows) {
			cow.update(timeStep);
		}

		if (mAlienAnimationDone) {
			long timeSinceStart = System.currentTimeMillis() - mStartTime;
			if (timeSinceStart > mAlienAnimationTimeMillis + mCowTeleportDelay) {
				
				mTeleporting = true;
				
				float start = TELEPORT_COW_POSITION_Y;
				float end = mAlienY + ALIEN_SHIP_BOTTOM_Y * mScale;
				float wayToGo = (start - end) * 1.0f;
				float wayToGoX = getWidth()*0.5f - TELEPORT_COW_POSITION_X;
				
				long timeSinceTeleportStart = timeSinceStart - mAlienAnimationTimeMillis - mCowTeleportDelay;
				float percentageComplete = timeSinceTeleportStart / (float) mCowTeleportTime;
				
				if (percentageComplete >= 1.0) {
					mCows[2].setPosition((int)(TELEPORT_COW_POSITION_X + wayToGoX), (int)(start - wayToGo));
				} else {
					mCows[2].setPosition((int)(TELEPORT_COW_POSITION_X + percentageComplete * wayToGoX), (int)(start - percentageComplete * wayToGo));
					mCows[2].getImageComponent().setWidth((int)(TELEPORT_COW_WIDTH* (1.0-percentageComplete)));
					mCows[2].getImageComponent().setHeight((int)(TELEPORT_COW_HEIGHT* (1.0-percentageComplete)));
				}
			}

		}
	}

	@Override
	protected void initialize() {
		super.initialize();

		mScale = getWidth() / 480.0f;

		createBackground();

		mStartTime = System.currentTimeMillis();

		mPaint.setAntiAlias(true);

		mCows = new Cow[4];

		addCows();

		mAlienBitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.alienship);

		mFarmHouseBitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.farmhouse);

		mFenceBitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.fence);

		int fenceX = (int) (0 * mScale);
		int fenceY = (int) (550 * mScale);
		int fenceW = (int) (mFenceBitmap.getWidth() * mScale);
		int fenceH = (int) (mFenceBitmap.getHeight() * mScale);
		mFenceRect = new Rect(fenceX, fenceY, fenceX + fenceW, fenceY + fenceH);

		int farmhouseX = (int) (260 * mScale);
		int farmHouseY = (int) (380 * mScale);
		int farmHouseW = (int) (mFarmHouseBitmap.getWidth() * mScale);
		int farmHouseH = (int) (mFarmHouseBitmap.getHeight() * mScale);
		mFarmHouseRect = new Rect(farmhouseX, farmHouseY, farmhouseX
				+ farmHouseW, farmHouseY + farmHouseH);

		float width = getWidth() * 0.4f;
		float growFactor = width / mAlienBitmap.getWidth();

		mAlienBitmap = GraphicsUtil.resizeBitmap(mAlienBitmap,
				(int) (mAlienBitmap.getHeight() * growFactor), (int) width);

		mAlienY *= mScale;
		mAlienEndX = (int) (getWidth() * 0.5f - mAlienBitmap.getWidth() * 0.5f);

		mBackgroundRect = new Rect();
		mBackgroundRect.set(0, 0, getWidth(), (int) (800 * mScale));

		mPenguin = createPenguin();
	}

	private void addCows() {

		mCows[0] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 38, 145, 580, getWidth(),
				getHeight());
		addScreenComponent(mCows[0].getImageComponent());

		mCows[1] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 40, 100, 595, getWidth(),
				getHeight());
		addScreenComponent(mCows[1].getImageComponent());

		mCows[2] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 40, 200, 590, getWidth(),
				getHeight());
		addScreenComponent(mCows[2].getImageComponent());

		mCows[3] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 35, 35, 560, getWidth(),
				getHeight());
		addScreenComponent(mCows[3].getImageComponent());

		TELEPORT_COW_POSITION_X = mCows[2].getImageComponent().getPositionX();
		TELEPORT_COW_POSITION_Y = mCows[2].getImageComponent().getPositionY();
		TELEPORT_COW_HEIGHT = mCows[2].getImageComponent().getHeight();
		TELEPORT_COW_WIDTH = mCows[2].getImageComponent().getWidth();
	}

	private void createBackground() {
		if (BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND) == null) {
			Bitmap background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background);
			background = GraphicsUtil.resizeBitmap(background,
					(int) (800 * mScale), getWidth());
			BitmapManager.addBitmap(Constants.BITMAP_BACKGROUND, background);
		}
	}

	private ScreenComponent createPenguin() {
		Bitmap penguin = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.penguin);
		Bitmap penguinBlinking = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.penguin_blink);

		Bitmap[] bitmaps = new Bitmap[] { penguin, penguinBlinking };

		AnimateableImageComponent imageComp = new AnimateableImageComponent(
				bitmaps);

		int eyesOpen = 0;
		int eyesClosed = 1;

		imageComp.setWidthInDpAutoSetHeight(80, mDpi);
		imageComp.setPositionX(getWidth() - imageComp.getWidth());
		imageComp.setPositionY(getHeight() - imageComp.getHeight());

		// Resize the loaded bitmaps with nice algorithms so that they looks
		// nice.
		imageComp.resizeBitmaps();

		Bitmap[] rezisedBitmaps = imageComp.getBitmaps();
		BitmapManager.addBitmap(Constants.BITMAP_PENGUIN, rezisedBitmaps[0]);
		BitmapManager.addBitmap(Constants.BITMAP_PENGUIN_BLINKING,
				rezisedBitmaps[1]);

		// blink sequence
		imageComp.addScene(eyesOpen, 1000);
		imageComp.addScene(eyesClosed, 300);
		imageComp.addScene(eyesOpen, 3000);
		imageComp.addScene(eyesClosed, 200);
		imageComp.addScene(eyesOpen, 2500);
		imageComp.addScene(eyesClosed, 300);

		imageComp.startAnimation();

		return imageComp;
	}

	@Override
	public void draw(Canvas canvas) {

		mPaint.setColor(0xFF0c4717);
		canvas.drawRect(0, (int) (800 * mScale), getWidth(), getHeight(),
				mPaint);

		canvas.drawBitmap(BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND),
				null, mBackgroundRect, mPaint);

		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		mCloud1.getImageComponent().draw(canvas);
		mCloud2.getImageComponent().draw(canvas);

		for (Cow cow : mCows) {
			cow.draw(canvas);
			cow.getImageComponent().draw(canvas);
		}

		mPenguin.draw(canvas);

		canvas.drawBitmap(mFarmHouseBitmap, null, mFarmHouseRect, mPaint);
		canvas.drawBitmap(mFenceBitmap, null, mFenceRect, mPaint);

		drawAlien(canvas);

		if (mAlienAnimationDone && !mTeleporting) {
			// addScreenComponent(createPlayButton());
			// playButtonAdded = true;
			for (Cow cow : mCows) {
				cow.freezeAndReset();
			}
		}

		if (mTeleporting) {
			mPaint.setColor(Constants.ALIEN_TELEPORT_COLOR);
			mPaint.setStyle(Style.FILL);
			mTeleportPath.moveTo(getWidth() * 0.5f, mAlienY
					+ ALIEN_SHIP_BOTTOM_Y * mScale);
			mTeleportPath.lineTo(TELEPORT_COW_POSITION_X - 5 * mScale,
					TELEPORT_COW_POSITION_Y + TELEPORT_COW_HEIGHT);
			mTeleportPath
					.lineTo(TELEPORT_COW_POSITION_X + TELEPORT_COW_WIDTH + 5
							* mScale, TELEPORT_COW_POSITION_Y
							+ TELEPORT_COW_HEIGHT);
			mTeleportPath.lineTo(getWidth() * 0.5f, mAlienY + 88 * mScale);
			canvas.drawPath(mTeleportPath, mPaint);
		}
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

		canvas.drawBitmap(mAlienBitmap, currentStep, mAlienY, mPaint);
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
