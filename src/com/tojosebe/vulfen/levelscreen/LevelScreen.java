package com.tojosebe.vulfen.levelscreen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.configuration.LevelManager;
import com.tojosebe.vulfen.game.BowlScreen;
import com.tojosebe.vulfen.game.StoryScreen;
import com.tojosebe.vulfen.startscreen.Cloud;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class LevelScreen extends Screen {

	private final int mButtonUpperOffsetDp = 30;
	private final int mButtonUpperOffset;

	private static final String SAVED_TOP_STARS = "topStars";

	private int mListHeight;

	private float mScrollY = 0;
	private float mLastScrollLength = 0;

	private int mStarSize = 23;

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
	private int mNbrOfLockedLevels;

	private int mLevelMarginDp = 4;
	private int mLevelWidthMargin;

	private float mScale;
	Paint mPaint = new Paint();

	private int mGridCols;

	private int mWorldNumber;

	private Activity mActivity;

	public LevelScreen(int dpi, Cloud cloud1, Cloud cloud2, int nbrOfLevels,
			int nbrOfLockedLevels, int gridCols, int worldNumber,
			Activity activity) {
		this.mDpi = dpi;
		this.mCloud1 = cloud1;
		this.mCloud2 = cloud2;
		this.mNbrOfLevels = nbrOfLevels;
		this.mGridCols = gridCols;
		this.mNbrOfLockedLevels = nbrOfLockedLevels;
		this.mWorldNumber = worldNumber;
		this.mActivity = activity;

		mLevelWidthMargin = (int) GraphicsUtil.dpToPixels(mLevelMarginDp, mDpi);

		mButtonUpperOffset = (int) GraphicsUtil.dpToPixels(
				mButtonUpperOffsetDp, mDpi);
		mListHeight = mButtonUpperOffset;
	}

	@Override
	protected void initialize() {

		int minMargin = (int) GraphicsUtil.dpToPixels(mGridMinMarginDp, mDpi);
		mGridMargin = 0;

		mPaint.setAntiAlias(true);
		mScale = getWidth() / 480.0f;

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
		Bitmap lockedLevelbitmap = createLockedLevelBitmap();

		addLevel(1, levelbitmap, false);

		for (int i = 1; i < mNbrOfLevels; ++i) {
			boolean locked = mNbrOfLevels - mNbrOfLockedLevels <= i;
			if (locked) {
				addLevel((i + 1), lockedLevelbitmap, locked);
			} else {
				addLevel((i + 1), levelbitmap, locked);
			}

		}

		createStars();

		addScreenComponent(createBottomBackground());

	}

	private Bitmap createLockedLevelBitmap() {
		Bitmap levelbitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.level_lock);
		levelbitmap = GraphicsUtil.resizeBitmap(levelbitmap, mLevelHeight,
				mLevelWidth);
		return levelbitmap;
	}

	private Bitmap createLevelBitmap() {
		Bitmap levelbitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.level);
		levelbitmap = GraphicsUtil.resizeBitmap(levelbitmap, mLevelHeight,
				mLevelWidth);
		return levelbitmap;
	}

	public static String getStarsPrefsKey(int world, int level) {
		return SAVED_TOP_STARS + "_" + world + "_" + level;
	}

	private SharedPreferences getDefaultSharedPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(mContext
				.getApplicationContext());
	}

	private void createStars() {

		Bitmap darkStar = BitmapManager.getBitmap(Constants.BITMAP_STAR_DARK);

		int starSize = (int) (mStarSize * mScale);

		if (darkStar == null) {
			darkStar = ImageLoader.loadFromResource(
					mContext.getApplicationContext(),
					R.drawable.scorebar_star_dark2);
			darkStar = GraphicsUtil.resizeBitmap(darkStar, starSize, starSize);
			BitmapManager.addBitmap(Constants.BITMAP_STAR_DARK, darkStar);
		}

		Bitmap brightStar = BitmapManager.getBitmap(Constants.BITMAP_STAR);

		if (brightStar == null) {
			brightStar = ImageLoader
					.loadFromResource(mContext.getApplicationContext(),
							R.drawable.scorebar_star2);
			brightStar = GraphicsUtil.resizeBitmap(brightStar, starSize,
					starSize);
			BitmapManager.addBitmap(Constants.BITMAP_STAR, brightStar);
		}

	}

	@Override
	protected void onTop() {
		// TODO: Reload scores and stuff.
	}

	private ScreenComponent createBottomBackground() {

		StretchableImageButtonComponent bottom = new StretchableImageButtonComponent(
				mContext, R.drawable.screen_footer, "", 0, 0, 0,
				(int) GraphicsUtil.pixelsToDp(getWidth(), mDpi), 70, mDpi);

		bottom.setPositionX(0);
		bottom.setPositionY(getHeight()
				- (int) GraphicsUtil.dpToPixels(70, mDpi));

		return bottom;
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

		mPaint.setColor(0xFF0c4717);
		canvas.drawRect(0, (int) (800 * mScale), getWidth(), getHeight(),
				mPaint);

		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		for (LevelButton button : mButtons) {
			button.draw(canvas);
		}
	}

	@Override
	public boolean handleBackPressed() {
		return mScreenManager.removeScreenUI(this);
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

	private void addLevel(final int levelIndex, Bitmap levelBitmap,
			boolean locked) {
		LevelButton levelButton = null;

		EventListener listener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				if (Math.abs(mLastScrollLength) < GraphicsUtil.dpToPixels(10,
						mDpi)) {
					float scale = getWidth() / 480.0f;
					Level level = LevelManager.getLevel(mWorldNumber,
							levelIndex, scale, mActivity.getAssets());
					if (level.getLevelNumber() == 0
							&& level.getWorldNumber() == 0) {
						startStoryScreen(level);
					} else {
						startLevel(level);
					}
					return true;
				}
				return false;
			}

		};

		levelButton = new LevelButton(levelBitmap, locked);

		int stars = getDefaultSharedPrefs().getInt(
				getStarsPrefsKey(mWorldNumber, levelIndex), 0);

		levelButton.initValues("" + levelIndex, mContext, mDpi, mLevelWidth,
				stars, mScale);

		if (!locked) {
			levelButton.setEventListener(listener);
		}

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

	private void startStoryScreen(Level level) {
		mScreenManager.addScreenUI(new StoryScreen(mDpi, mCloud1, mCloud2,
				level, mActivity, this));
	}

	private void startLevel(Level level) {
		mScreenManager
				.addScreenUI(new BowlScreen(level, mDpi, mActivity, this));
	}

	public void updateLevelWithStars(int level, int stars) {
		mButtons.get(level).setNbrOfStars(stars);
	}

}
