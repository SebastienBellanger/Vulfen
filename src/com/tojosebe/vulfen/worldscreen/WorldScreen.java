package com.tojosebe.vulfen.worldscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.component.animation.AnimateableImageComponent;
import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.BowlScreen;
import com.tojosebe.vulfen.startscreen.Cloud;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ButtonComponent;
import com.vulfox.component.ImageComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class WorldScreen extends Screen {

	private final int BUTTON_OFFSET = 40;

	private float mScrollY = 0;
	private int mListHeight = BUTTON_OFFSET;
	private float mLastScrollLength = 0;

	private Pig mPig1;
	private Pig mPig2;

	private List<WorldButton> mButtons = new ArrayList<WorldButton>();

	private float mLastY;

	private int mDpi;

	private boolean mScrolling = false;

	private Cloud mCloud1;
	private Cloud mCloud2;

	private ButtonComponent mPenguinImageRight;
	private ButtonComponent mPenguinImageLeft;
	private boolean mPenguinRight = true;
	private int mPenguinTouches = 0;

	public WorldScreen(int dpi, Cloud cloud1, Cloud cloud2) {
		this.mDpi = dpi;
		this.mCloud1 = cloud1;
		this.mCloud2 = cloud2;
	}

	@Override
	protected void initialize() {

		addScreenComponent(createBackground());
		addScreenComponent(mCloud1.getImageComponent());
		addScreenComponent(mCloud2.getImageComponent());
		mPenguinImageRight = createPenguin(270);
		mPenguinImageRight.setEventListener(new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				if (mPenguinImageRight.isVisible()) {
					relocatePenguin();
				}
				return true;
			}
		});

		addPigs();
		addScreenComponent(mPenguinImageRight);

		WorldButton worldButton = addWorld("First Battle", 25, 10, null);
		addWorld("World 2", 25, 0, worldButton);
		addWorld("Extras", 25, 0, worldButton);
		addWorld("Custom Levels", 25, 0, worldButton);
		addWorld("Pinball world", 25, 0, worldButton);
		addWorld("Hello there :-)", 25, 0, worldButton);

	}

	@Override
	protected void onTop() {
		mPenguinTouches = 0;
		mPenguinImageRight.setVisible(true);
		mPig2.wimpPig();
		mPig1.wimpPig();
	}

	private void addPigs() {
		mPig1 = new Pig(R.drawable.pig_small, mDpi,
				mContext.getApplicationContext(), 25, 15, 0, mHeight);
		addScreenComponent(mPig1.getImageComponent());
		mPig2 = new Pig(R.drawable.pig_small, mDpi,
				mContext.getApplicationContext(), 30, 33, 5, mHeight);
		addScreenComponent(mPig2.getImageComponent());
	}

	private void relocatePenguin() {

		mPenguinTouches++;

		if (mPenguinTouches >= 3) {
			mPig2.superPig();
			mPig1.superPig();
			mPenguinImageRight.setVisible(false);
			mPenguinImageLeft.setVisible(false);
		} else {
			if (mPenguinRight) {
				if (mPenguinImageLeft == null) {
					mPenguinImageLeft = createPenguin(90);
					addScreenComponent(mPenguinImageLeft);
					mPenguinImageLeft.setEventListener(new EventListener() {
						@Override
						public boolean handleButtonClicked() {
							if (mPenguinImageLeft.isVisible()) {
								relocatePenguin();
							}
							return true;
						}
					});
				}
				mPenguinRight = false;
				mPenguinImageLeft.setPositionY(getRandom(0, mHeight
						- mPenguinImageLeft.getHeight()));
				mPenguinImageRight.setVisible(false);
				mPenguinImageLeft.setVisible(true);
			} else {
				mPenguinRight = true;
				mPenguinImageRight.setPositionY(getRandom(0, mHeight
						- mPenguinImageLeft.getHeight()));
				mPenguinImageRight.setVisible(true);
				mPenguinImageLeft.setVisible(false);
			}
		}

	}

	private int getRandom(int n, int m) {
		Random rand = new Random(System.currentTimeMillis());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	private ButtonComponent createPenguin(int rotation) {

		Bitmap penguin = BitmapManager.getBitmap(Constants.BITMAP_PENGUIN);
		if (penguin == null || penguin.isRecycled()) {
			penguin = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.penguin);
		}

		// Rotate the penguin.
		Bitmap targetBitmap = Bitmap.createBitmap(penguin.getWidth(),
				penguin.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Matrix matrix = new Matrix();
		matrix.setRotate(rotation, penguin.getWidth() / 2,
				penguin.getHeight() / 2);
		canvas.drawBitmap(penguin, matrix, new Paint());

		Bitmap penguinBlinking = BitmapManager
				.getBitmap(Constants.BITMAP_PENGUIN_BLINKING);
		if (penguinBlinking == null || penguinBlinking.isRecycled()) {
			penguinBlinking = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.penguin_blink);
		}

		// Rotate blinking penguin.
		Bitmap rotatedBitmapBlinking = Bitmap.createBitmap(
				penguinBlinking.getWidth(), penguinBlinking.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvasBlinking = new Canvas(rotatedBitmapBlinking);
		canvasBlinking.drawBitmap(penguinBlinking, matrix, new Paint());

		Bitmap[] bitmaps = new Bitmap[] { targetBitmap, rotatedBitmapBlinking };

		AnimateableImageComponent imageComp = new AnimateableImageComponent(
				bitmaps);

		int eyesOpen = 0;
		int eyesClosed = 1;

		imageComp.setWidthInDpAutoSetHeight(160, mDpi);
		if (rotation == 270) {
			imageComp.setPositionX(mWidth - (int) (imageComp.getWidth() / 2.5));
			imageComp.setPositionY(mHeight
					- (int) (imageComp.getHeight() * 1.5));
		} else {
			imageComp.setPositionX(-imageComp.getWidth()
					+ (int) (imageComp.getWidth() / 2.5));
			imageComp.setPositionY(mHeight
					- (int) (imageComp.getHeight() * 2.5));
		}

		// Resize the loaded bitmaps with nice algorithms so that they look
		// nice.
		imageComp.resizeBitmaps();

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

	private ImageComponent createBackground() {

		Bitmap background = BitmapManager
				.getBitmap(Constants.BITMAP_BACKGROUND);

		ImageComponent imageComp = null;

		if (background != null) {
			imageComp = new ImageComponent(background);
		} else {
			background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background);
			imageComp = new ImageComponent(background);
			imageComp.setHeight(mHeight);
			imageComp.setWidth(mWidth);
			imageComp.resizeBitmap();
			BitmapManager.addBitmap(Constants.BITMAP_BACKGROUND,
					imageComp.getBitmap());
		}

		return imageComp;
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {

		int action = motionEvent.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			mLastY = motionEvent.getY();
			if (isInsideScrollArea(motionEvent.getX())) {
				mScrolling = true;
				mLastScrollLength = 0;
			}
		} else if (action == MotionEvent.ACTION_MOVE && mScrolling) {

			float yDelta = motionEvent.getY() - mLastY;

			mLastY = motionEvent.getY();

			mScrollY -= yDelta;
			mLastScrollLength -= yDelta;
			Log.d("DELTA", "" + yDelta);
			Log.d("mLastScrollLength", "" + mLastScrollLength);

			if (mScrollY < 0) {
				mScrollY = 0;
			} else if (mScrollY > mListHeight - mHeight) {
				mScrollY = Math.max(0, mListHeight - mHeight);
			}

			recalulateButtons();
		} else if (action == MotionEvent.ACTION_UP && mScrolling) {
			mScrolling = false;
		}

	}

	private boolean isInsideScrollArea(float x) {
		boolean inside = false;
		if (mButtons != null && mButtons.size() > 0) {
			WorldButton button = mButtons.get(0);
			int w = button.getWidth();
			int areaLeftOfScrollArea = (mWidth - w) / 2;
			if (x > areaLeftOfScrollArea && x < areaLeftOfScrollArea + w) {
				inside = true;
			}
		}
		return inside;
	}

	@Override
	public void update(float timeStep) {
		mCloud1.update(timeStep);
		mCloud2.update(timeStep);
		mPig1.update(timeStep);
		mPig2.update(timeStep);
	}

	@Override
	public void draw(Canvas canvas) {
		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		mPig1.draw(canvas);
		mPig2.draw(canvas);
	}

	@Override
	public boolean handleBackPressed() {
		return mScreenManager.removeScreen(this);
	}

	private void recalulateButtons() {
		int yPosition = BUTTON_OFFSET - Math.round(mScrollY);
		for (int i = 0; i < mButtons.size(); i++) {
			mButtons.get(i).setPositionY(yPosition);
			yPosition += mButtons.get(i).getHeight() + BUTTON_OFFSET;
		}
	}

	private WorldButton addWorld(String worldName, int totalStages,
			int clearedStages, WorldButton worldButtonTemplate) {

		WorldButton worldButton = null;
		EventListener listener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				if (Math.abs(mLastScrollLength) < GraphicsUtil.dpToPixels(10, mDpi)) {
					mScreenManager.addScreen(new BowlScreen(
							new BowlConfiguration()));
					return true;
				}
				return false;
			}
		};

		if (worldButtonTemplate != null) {
			worldButton = new WorldButton(worldName, totalStages,
					clearedStages, mContext, mDpi, worldButtonTemplate);
			worldButton.setEventListener(listener);
		} else {
			worldButton = new WorldButton(worldName, totalStages,
					clearedStages, mContext, mDpi);
			worldButton.setEventListener(listener);
		}
		worldButton.setPositionX((mWidth - worldButton.getWidth()) / 2);

		int positionY = mListHeight - Math.round(mScrollY);
		worldButton.setPositionY(positionY);

		addScreenComponent(worldButton);
		mButtons.add(worldButton);

		mListHeight += worldButton.getHeight() + BUTTON_OFFSET;

		return worldButton;
	}
}
