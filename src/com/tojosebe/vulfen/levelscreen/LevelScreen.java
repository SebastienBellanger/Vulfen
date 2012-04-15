package com.tojosebe.vulfen.levelscreen;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.BowlScreen;
import com.tojosebe.vulfen.startscreen.Cloud;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class LevelScreen extends Screen {

	private final int mButtonUpperOffsetDp = 30;
	private final int mButtonUpperOffset;
	private int mListHeight;

	private float mScrollY = 0;
	private float mLastScrollLength = 0;
	
	private int mStartSizeDp = 10;

	private List<LevelButton> mButtons = new ArrayList<LevelButton>();

	private float mLastY;

	private int mDpi;

	private boolean mScrolling = false;

	private Cloud mCloud1;
	private Cloud mCloud2;

	private int mNbrOfLevels;
	private int mLevelHeight;
	private int mLevelWidth;

	private int mGridMinMarginDp = 15;
	private int mGridMargin;

	private int mLevelMarginDp = 4;
	private int mLevelWidthMargin;

	private int mGridCols;

	public LevelScreen(int dpi, Cloud cloud1, Cloud cloud2, int nbrOfLevels,
			int gridCols) {
		this.mDpi = dpi;
		this.mCloud1 = cloud1;
		this.mCloud2 = cloud2;
		this.mNbrOfLevels = nbrOfLevels;
		this.mGridCols = gridCols;

		mLevelWidthMargin = (int) GraphicsUtil.dpToPixels(mLevelMarginDp, mDpi);

		mButtonUpperOffset = (int) GraphicsUtil.dpToPixels(
				mButtonUpperOffsetDp, mDpi);
		mListHeight = mButtonUpperOffset;
	}

	@Override
	protected void initialize() {

		int minMargin = (int) GraphicsUtil.dpToPixels(mGridMinMarginDp, mDpi);
		mGridMargin = 0;

		int levelSpace = getWidth() - 2 * minMargin;

		mLevelWidth = (levelSpace / mGridCols) - mLevelWidthMargin * 2;
		mLevelHeight = mLevelWidth;

		mGridMargin = (getWidth() - (mLevelWidth + mLevelWidthMargin * 2)
				* mGridCols) / 2;

		// Background stuff
		addScreenComponent(createBackground());
		addScreenComponent(mCloud1.getImageComponent());
		addScreenComponent(mCloud2.getImageComponent());

		// Add level images.
		Bitmap levelbitmap = createLevelBitmap();

		addLevel("1", levelbitmap);

		for (int i = 1; i < mNbrOfLevels; ++i) {
			addLevel("" + (i + 1), levelbitmap);
		}
		
		createStars();
	}

	private Bitmap createLevelBitmap() {
		Bitmap levelbitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.level);
		levelbitmap = GraphicsUtil.resizeBitmap(levelbitmap, mLevelHeight,
				mLevelWidth);
		return levelbitmap;
	}
	
	private void createStars() {
		
		Bitmap darkStar = BitmapManager
				.getBitmap(Constants.BITMAP_STAR_DARK);
		
		int starSize = (int)GraphicsUtil.dpToPixels(mStartSizeDp, mDpi);

		if (darkStar == null) {
			darkStar = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.star_dark);
			darkStar = GraphicsUtil.resizeBitmap(darkStar, starSize, starSize);
			BitmapManager.addBitmap(Constants.BITMAP_STAR_DARK, darkStar);
		}
		
		Bitmap brightStar = BitmapManager
				.getBitmap(Constants.BITMAP_STAR);

		if (brightStar == null) {
			brightStar = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.star);
			brightStar = GraphicsUtil.resizeBitmap(brightStar, starSize,
					starSize);
			BitmapManager.addBitmap(Constants.BITMAP_STAR, brightStar);
		}

	}

	@Override
	protected void onTop() {
		// TODO: Reload scores and stuff.
	}

	private ImageComponent createBackground() {

		Bitmap background = BitmapManager
				.getBitmap(Constants.BITMAP_BACKGROUND);

		ImageComponent imageComp = null;

		if (background != null) {
			imageComp = new ImageComponent(background, false);
		} else {
			background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background);
			imageComp = new ImageComponent(background, false);
			imageComp.setHeight(getHeight());
			imageComp.setWidth(getWidth());
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
			} else if (mScrollY > mListHeight - getHeight()) {
				mScrollY = Math.max(0, mListHeight - getHeight());
			}

			recalculateButtons();
		} else if (action == MotionEvent.ACTION_UP && mScrolling) {
			mScrolling = false;
		}

	}

	private boolean isInsideScrollArea(float x) {
		boolean inside = true;
		return inside;
	}

	@Override
	public void update(float timeStep) {
		mCloud1.update(timeStep);
		mCloud2.update(timeStep);
	}

	@Override
	public void draw(Canvas canvas) {
		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		for (LevelButton button : mButtons) {
			button.draw(canvas);
		}
	}

	@Override
	public boolean handleBackPressed() {
		return mScreenManager.removeScreen(this);
	}

	private void recalculateButtons() {
		int yPosition = mButtonUpperOffset - Math.round(mScrollY);
		for (int i = 0; i < mButtons.size(); i++) {
			if (i != 0 && i % mGridCols == 0) {
				yPosition += mButtons.get(i).getHeight() + mButtonUpperOffset;
			}
			mButtons.get(i).setPositionY(yPosition);
		}
	}

	private void addLevel(String levelIndex, Bitmap levelBitmap) {
		LevelButton levelButton = null;

		EventListener listener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				if (Math.abs(mLastScrollLength) < GraphicsUtil.dpToPixels(10,
						mDpi)) {
					mScreenManager.addScreen(new BowlScreen(
							new BowlConfiguration()));
					return true;
				}
				return false;
			}
		};

		levelButton = new LevelButton(levelBitmap);
		levelButton.initValues(levelIndex, mContext, mDpi, mLevelWidth, 2);
		levelButton.setEventListener(listener);

		boolean newRow = false;
		if (mButtons.size() > 0) {
			newRow = mButtons.size() % mGridCols == 0;
		}
		int xCoord = mButtons.size() % mGridCols;

		int firstLevelPosition = mGridMargin + mLevelWidthMargin;

		if (xCoord == 0) {
			levelButton.setPositionX(firstLevelPosition);
		} else {
			levelButton.setPositionX(firstLevelPosition
					+ (mLevelWidth + mLevelWidthMargin * 2) * xCoord);
		}

		if (mButtons.size() == 0) {
			mListHeight += levelButton.getHeight() + mButtonUpperOffset;
		}

		if (newRow) {
			mListHeight += levelButton.getHeight() + mButtonUpperOffset;
			int positionY = mListHeight - Math.round(mScrollY)
					- levelButton.getHeight() - mButtonUpperOffset;
			levelButton.setPositionY(positionY);
		} else {
			int positionY = mListHeight - Math.round(mScrollY)
					- levelButton.getHeight() - mButtonUpperOffset;
			levelButton.setPositionY(positionY);
		}

		addScreenComponent(levelButton);
		mButtons.add(levelButton);
	}
}