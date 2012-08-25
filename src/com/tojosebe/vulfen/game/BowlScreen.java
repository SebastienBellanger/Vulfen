package com.tojosebe.vulfen.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.component.ScoreBarComponent;
import com.tojosebe.vulfen.component.SpriteComponent;
import com.tojosebe.vulfen.configuration.BowlConfiguration;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.configuration.LevelManager;
import com.tojosebe.vulfen.contextmenu.ContextMenuScreen;
import com.tojosebe.vulfen.dialog.DialogCounterString;
import com.tojosebe.vulfen.dialog.DialogPulsingString;
import com.tojosebe.vulfen.dialog.DialogRegularString;
import com.tojosebe.vulfen.dialog.DialogScreen;
import com.tojosebe.vulfen.dialog.DialogString;
import com.tojosebe.vulfen.dialog.DialogString.TextSize;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.tojosebe.vulfen.game.Pong.Type;
import com.tojosebe.vulfen.levelscreen.LevelScreen;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.math.Vector2f;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class BowlScreen extends Screen {

	private static final int SCORE_FOR_LIFE_LEFT = 1000;
	private static final int SCORE_FOR_BRICK = 50;
	private static final int SCORE_FOR_GROWER = 500;
	private static final int SCORE_FOR_SHRINKER = -500;
	private static final int SCORE_FOR_ENEMY_KILL = 500;
	private static final int SCORE_FOR_ALIEN_KILL = 100;
	
	private static final String SAVED_TOP_SCORE = "TopScore";

	private static final String TOP_SCORE = "Top Score: ";

	private static final int ALIEN_DRIVE_BY_TIME_MILLIS = 5000; 
	
	private float mScale;

	private Vibrator vibrator;
	
	private Activity mActivity;
	
	private boolean paused = false;
	
	private ContextMenuScreen contextMenuScreen;
	
	//set to true means we will first check side collisions against all bricks.
	private boolean sidesHasPrecedence = true;
	
	/** Help vector. Saves memory by not allocating new every time. */
	private Vector2f collisionCheckVector = new Vector2f();
	
	/** Help array for scores that has faded away. */
	Score[] scoresToDelete = new Score[100];
	
	private AlienShip alien;
	private long mAlienStartTime;
	private long mAlienInterval; //millis between alien appearances.

	private Bitmap mPenguinBmpX005;
	private Bitmap mPenguinBmpX05;
	private Bitmap mPenguinBmpX1;
	private Bitmap mPenguinBmpX2;
	private Bitmap mPenguinBmpX3;
	
	private Bitmap mCow1;
	private Bitmap mCow2;
	private Bitmap mCow3;
		
	private Bitmap mLaunchPad;
	
	private Bitmap mBrickSoft;
	private Bitmap mBrickMedium;
	private Bitmap mBrickMediumBroken;
	private Bitmap mBrickHard;

	private Paint mTextPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border
	private Paint mTopPaint = new Paint();

	private List<Pong> mPongs = new Vector<Pong>();
	private List<Brick> mBricks = new Vector<Brick>();
	
	private LevelText mLevelText;
	private TopScoreText mTopScoreText;
	
	private List<BonusItem> mBonusItems = new Vector<BonusItem>();
	private Pong mLaunchPong;
	private boolean mHasLaunched = false;

	private boolean mMotionEnabled = true;
	private boolean mHasPong = false;
	private Vector2f mLastMotion = new Vector2f();
	private long mLastMotionTime = 0;
	
	private int mCowsKilled = 0;
	
	private int mBonusItemDpWidth = 32;

	private int mLivesLeftDpWidth = 13;
	private int mCowsTakenHolderWidth = 20;
	private int mCowsTakenWidth = 22;

	// Random bonus items variables.
	private long mRoundStartedTime;
	private int mBonusItemSuccessInt = 1;
	private Random mBonusItemRandom = new Random(System.currentTimeMillis());
	private long mSecondsSinceLaunch = 0;
	private int mBounusItemCounter = 0;
	private int mBounusItemTotalCounter = 0;

	private int mTotalScore = 0;
	private int mRoundHits = 1;

	private int mTopScore = 0;
	private int mSavedTopScore = 0;

	// Game ended animation
	private long mGameEndedTime = 0;
	private long mLivesLeftAnimationStart = 0;
	private int mTimeStepsSinceGameEnded = 0;

	private List<Score> mPoints = new Vector<Score>();
	
	private List<KillAnimation> mKillAnimations = new ArrayList<KillAnimation>();
	private List<KillAnimation> mKillAnimationsToDelete = new ArrayList<KillAnimation>();
	private List<SpriteComponent> mCollectedCows = new ArrayList<SpriteComponent>();
	
	private boolean mVibrationEnabled = true;

	private int mLives;
	private boolean mGameOver = false;

	private BowlConfiguration mConfig;

	private int mDpi;

	private float mGameAreaHeight;

	public static final float GAME_AREA_ASPECT_RATIO = 1.333f;
	private float mLaunchPadHeight;

	private Level mLevelConfig;
	
	private DialogCounterString mCounterStringGameOver; 

	Paint mGenericPaint = new Paint();
	RectF mLaunchPadRect = new RectF();
	Rect mItemRect = new Rect();
	
	private boolean mHightScore = false;
	
	Rect mScoreRect = new Rect();
	Rect mTopRect = new Rect();
	
	private LevelScreen mLevelScreen;
	
	private ScoreBarComponent mScoreBar;

	public BowlScreen(Level levelConfiguration, int dpi, Activity activity, LevelScreen levelScreen) {
		mConfig = levelConfiguration.getBowlConfiguration();
		mLevelConfig = levelConfiguration;
		mDpi = dpi;
		mActivity = activity;
		mLevelScreen = levelScreen;
	}

	@Override
	protected void onTop() {
	}

	private BonusItem getRandomBonusItem() {

		BonusItem item = new BonusItem();
		Bitmap bitmap = null;

		item.getVelocity().setY(getWidth() / 5);

		int sequenceNumber = mBounusItemTotalCounter % mLevelConfig.getBonusItemSequence().length;
		BonusItemType type = mLevelConfig.getBonusItemSequence()[sequenceNumber];
		if (type == BonusItemType.GROWER) {
			bitmap = ImageLoader.loadFromResource(mContext, R.drawable.fish);
			item.setItemType(BonusItemType.GROWER);
			item.setScore(SCORE_FOR_GROWER);
		} else if (type == BonusItemType.SHRINKER) {
			bitmap = ImageLoader.loadFromResource(mContext,
					R.drawable.fish_dead);
			item.setItemType(BonusItemType.SHRINKER);
			item.setScore(SCORE_FOR_SHRINKER);
		}

		float itemWidth = GraphicsUtil.dpToPixels(mBonusItemDpWidth, mDpi);
		int xPosition = mBonusItemRandom.nextInt(getWidth() - (int) itemWidth);
		xPosition += itemWidth * 0.5f;

		float multiplyer = itemWidth / bitmap.getWidth();
		item.setRadius(bitmap.getWidth() * 0.5f * multiplyer);
		item.setWidth(bitmap.getWidth() * multiplyer);
		item.setHeight(bitmap.getHeight() * multiplyer);

		item.setBitmap(bitmap);

		item.setPosition(new Vector2f(xPosition, -item.getHeight()));

		return item;
	}

	@Override
	protected void initialize() {

		mScale = getWidth() / 480.0f;
		
		vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);

		SharedPreferences settings = getDefaultSharedPrefs();
		mTopScore = mSavedTopScore = settings.getInt(getTopScorePrefsKey(), 0);

		float penguinPongLength = mLevelConfig.getPenguin().getWidth();

		mGameAreaHeight = (int) (getWidth() * GAME_AREA_ASPECT_RATIO);

		mLaunchPadHeight = getHeight() - mGameAreaHeight;
		if (mLaunchPadHeight < penguinPongLength * 1.2f) {
			// launchpad too small. We need to make width smaller. But what the
			// fuck. Make the launchpad large enough
			mLaunchPadHeight = penguinPongLength * 1.2f;
			mGameAreaHeight = getHeight() - mLaunchPadHeight;
		}

		mGenericPaint.setAntiAlias(true);

		mLaunchPadRect.set(0, getHeight() - mLaunchPadHeight, getWidth(),
				getHeight());

		float enemyLength = mLevelConfig.getEnemies().get(0).getWidth();

		mTopPaint.setTextSize(16 * mScale);
		mTopPaint.setAntiAlias(true);

		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(24 * mScale);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

		mStrokePaint.setARGB(255, 30, 30, 30);
		mStrokePaint.setTextSize(24 * mScale);
		mStrokePaint.setTypeface(Typeface.DEFAULT_BOLD);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setStrokeWidth(3 * mScale);

		mLaunchPad = ImageLoader.loadFromResource(mContext,
				R.drawable.launcher3);
		
		float enemyWidth = enemyLength;
		
		mCow1 = getEnemyPongBitmap(R.drawable.sebe_normal,enemyWidth,enemyWidth,0);
		enemyWidth *= 0.666666f;
		mCow2 = getEnemyPongBitmap(R.drawable.sebe_2,enemyWidth,enemyWidth,1);
		enemyWidth *= 0.666666f;
		mCow3 = getEnemyPongBitmap(R.drawable.sebe_3,enemyWidth,enemyWidth,2);
		mPenguinBmpX005 = getPongBitmap(mLevelConfig.getPenguin(), 0.66666f*0.66666f, R.drawable.penguin_mini);
		mPenguinBmpX05 = getPongBitmap(mLevelConfig.getPenguin(), 0.66666f, R.drawable.penguin_small);
		mPenguinBmpX1 = getPongBitmap(mLevelConfig.getPenguin(), 1.0f, R.drawable.penguin_small);
		mPenguinBmpX2 = getPongBitmap(mLevelConfig.getPenguin(), 1.5f, R.drawable.penguin_medium);
		mPenguinBmpX3 = getPongBitmap(mLevelConfig.getPenguin(), 1.5f * 1.5f, R.drawable.penguin_large);

		if (mLevelConfig.getBricks() != null && mLevelConfig.getBricks().size() > 0) {
			float h = mLevelConfig.getBricks().get(0).getHeight();
			float w = mLevelConfig.getBricks().get(0).getWidth();
			mBrickSoft = getBrickBitmap(Brick.Type.SOFT, w, h, R.drawable.brick_soft);
			mBrickMedium = getBrickBitmap(Brick.Type.MEDIUM, w, h, R.drawable.brick_medium);
			mBrickMediumBroken = getBrickBitmap(null, w, h, R.drawable.brick_medium_broken);
			mBrickHard = getBrickBitmap(Brick.Type.HARD, w, h, R.drawable.brick_hard);
		}
		
		mAlienInterval = 0;
		mAlienStartTime = 0;
		
		mAlienInterval = mLevelConfig.getAlienInterval();
		if (mAlienInterval != 0) {
			mAlienStartTime = System.currentTimeMillis();
			loadAlien();
		} else {
			alien = null;
		}
		
		createBackground();

		createLifeLeftImage();
		createCowTakenImages();
		
		mScoreBar = new ScoreBarComponent(mContext, mScale, mLevelConfig.getOneStarScore(), mLevelConfig.getTwoStarsScore(), mLevelConfig.getThreeStarsScore());
		addScreenComponent(mScoreBar);
		
		reset();
	}
	
	private void loadAlien() {
		if (alien == null) {
			Bitmap alienBitmap = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.alienship);
			float ratio = alienBitmap.getHeight()
					/ (float) alienBitmap.getWidth();
			alienBitmap = GraphicsUtil.resizeBitmap(alienBitmap,
					(int) (mCow1.getWidth() * 1.5f * ratio),
					(int) (mCow1.getWidth() * 1.5f));
			alien = new AlienShip(alienBitmap, true, false, false, 0, -alienBitmap.getWidth()*0.5f, (int)(mCow1.getHeight() * 1.5f),
					alienBitmap.getWidth(), alienBitmap.getHeight(), mScale, getWidth());
			mAlienStartTime = System.currentTimeMillis();
		}
	}

	private String getTopScorePrefsKey() {
		return SAVED_TOP_SCORE + "_" + mLevelConfig.getWorldNumber() + "_" + mLevelConfig.getLevelNumber();
	}

	private SharedPreferences getDefaultSharedPrefs() {
		return PreferenceManager
				.getDefaultSharedPreferences(mContext.getApplicationContext());
	}

	private Bitmap getPongBitmap(Pong pong, float growFactor, int drawable) {
		Bitmap bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), drawable);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) (pong.getHeight()* growFactor),
				(int) (pong.getWidth() * growFactor));
		return bitmap;
	}
	
	private Bitmap getBrickBitmap(Brick.Type type, float width, float height, int drawable) {
		
		Bitmap bitmap = null;
		if (type == Brick.Type.SOFT) {
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_BRICK_SOFT);
		} else if (type == Brick.Type.MEDIUM) {
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_BRICK_MEDIUM);
		} else if (type == Brick.Type.HARD) {
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_BRICK_HARD);
		}
		
		if (bitmap != null) {
			return bitmap;
		}
		
		bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), drawable);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) (height),
				(int) (width));
		
		if (type == Brick.Type.SOFT) {
			BitmapManager.addBitmap(Constants.BITMAP_BRICK_SOFT, bitmap);
		} else if (type == Brick.Type.MEDIUM) {
			BitmapManager.addBitmap(Constants.BITMAP_BRICK_MEDIUM, bitmap);
		} else if (type == Brick.Type.HARD) {
			BitmapManager.addBitmap(Constants.BITMAP_BRICK_HARD, bitmap);
		}
		
		
		
		return bitmap;
	}
	
	private Bitmap getEnemyPongBitmap(int resource, float height, float width, int timesShrinken) {
		
		Bitmap bitmap = null;
		switch (timesShrinken) {
		case 0:
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_COW_0);
			break;
		case 1:
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_COW_1);
			break;
		case 2:
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_COW_2);
			break;
		case 3:
			bitmap = BitmapManager.getBitmap(Constants.BITMAP_COW_3);
			break;
		}
		
		if (bitmap != null) {
			return bitmap;
		}
		
		bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), resource);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) height,
				(int) width);
		
		switch (timesShrinken) {
		case 0:
			BitmapManager.addBitmap(Constants.BITMAP_COW_0, bitmap);
			break;
		case 1:
			BitmapManager.addBitmap(Constants.BITMAP_COW_1, bitmap);
			break;
		case 2:
			BitmapManager.addBitmap(Constants.BITMAP_COW_2, bitmap);
			break;
		case 3:
			BitmapManager.addBitmap(Constants.BITMAP_COW_3, bitmap);
			break;
		}
		
		return bitmap;
	}

	private void createBackground() {
		if (BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND_GAME) == null) {
			Bitmap background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background4);
			background = GraphicsUtil.resizeBitmap(background, getHeight(), getWidth());
			BitmapManager.addBitmap(Constants.BITMAP_BACKGROUND_GAME,
					background);
		}
	}

	private void createLifeLeftImage() {

		if (BitmapManager.getBitmap(Constants.BITMAP_PENGUIN_LIFE) == null) {

			Bitmap lifeBitmap = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.penguin_life);
			float bitmapWidth = GraphicsUtil
					.dpToPixels(mLivesLeftDpWidth, mDpi);
			lifeBitmap = GraphicsUtil.resizeBitmap(lifeBitmap, (int)bitmapWidth, (int)bitmapWidth);
			BitmapManager.addBitmap(Constants.BITMAP_PENGUIN_LIFE, lifeBitmap);
		}
	}
	
	private void createCowTakenImages() {

		if (BitmapManager.getBitmap(Constants.BITMAP_COW_TAKEN_HOLDER) == null) {

			Bitmap cowTakenHolderBitmap = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.cow_taken_holder);
			int size = (int)((float)mCowsTakenHolderWidth * mScale);
			cowTakenHolderBitmap = GraphicsUtil.resizeBitmap(cowTakenHolderBitmap, size, size);
			BitmapManager.addBitmap(Constants.BITMAP_COW_TAKEN_HOLDER, cowTakenHolderBitmap);
		}
		
		if (BitmapManager.getBitmap(Constants.BITMAP_COW_TAKEN) == null) {

			Bitmap cowTakenBitmap = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.cow_taken);
			int size = (int)((float)mCowsTakenWidth * mScale);
			cowTakenBitmap = GraphicsUtil.resizeBitmap(cowTakenBitmap, size, size);
			BitmapManager.addBitmap(Constants.BITMAP_COW_TAKEN, cowTakenBitmap);
		}
	}

	private void reset() {
		mLives = mLevelConfig.getLives();
		mGameOver = false;
		
		mLaunchPong = new Pong(mLevelConfig.getPenguin());

		mTotalScore = 0;
		mScoreBar.reset();
		mBounusItemCounter = 0;
		mBounusItemTotalCounter = 0;
		mHightScore = false;
		paused = false;
		mCowsKilled = 0;

		mPoints.clear();
		mPongs.clear();
		mBonusItems.clear();
		mBricks.clear();
		mKillAnimations.clear();
		mKillAnimationsToDelete.clear();
		mCollectedCows.clear();
		
		if (alien != null) {
			alien.reset();
		}

		for (Pong enemy : mLevelConfig.getEnemies()) {
			addInitialPong(enemy);
		}
		
		addBricks(mLevelConfig.getBricks());

		mGameEndedTime = 0;
		mLivesLeftAnimationStart = 0;
		mTimeStepsSinceGameEnded = 0;
		
		mScoreBar.setVisible(true);
		
		mTopScore = mSavedTopScore = getDefaultSharedPrefs().getInt(getTopScorePrefsKey(), 0);

		mTopScoreText = new TopScoreText(TOP_SCORE + mTopScore , 0xFFFFFFFF, mScale);
		mLevelText = new LevelText("Level " + (mLevelConfig.getWorldNumber()+1) + "." + (mLevelConfig.getLevelNumber() + 1) , new Vector2f(getWidth()*0.5f, getHeight()*0.5f), 0xFFFFFFFF, mScale);
		resetLauncher();
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {

		if (mMotionEnabled) {
			float x = motionEvent.getX();
			float y = motionEvent.getY();

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				mHasPong = false;

				if (motionEvent.getY() > getHeight() - mLaunchPadHeight) {
					mHasPong = true;

					mLastMotion.set(x, y);
					mLastMotionTime = motionEvent.getEventTime();
					mLaunchPong.getPosition().set(x, y);
				}
			} else if (mHasPong
					&& motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				if (motionEvent.getY() > getHeight() - mLaunchPadHeight) {
					mHasPong = true;

					mLastMotion.set(x, y);
					mLastMotionTime = motionEvent.getEventTime();
					mLaunchPong.getPosition().set(x, y);
				} else {
					mHasPong = false;

					Vector2f velocity = new Vector2f(x, y);
					velocity.subT(mLastMotion);
					float length = velocity.getLength();
					velocity.normalizeT();

					float motionTime = (motionEvent.getEventTime() - mLastMotionTime) / 1000.0f;
					velocity.mulT(length / motionTime);

					if (velocity.getLength() > mConfig.getMaxSpeed() * mScale)
						velocity.setLength(mConfig.getMaxSpeed() * mScale);

					if (velocity.getLength() > 100.0f * mScale) {
						mMotionEnabled = false;

						Pong redPong = new Pong(mLevelConfig.getPenguin());
						redPong.setPosition(new Vector2f(mLaunchPong.getPosition()));
						redPong.velocity = velocity;
						redPong.setHeight(mLevelConfig.getPenguin().getHeight());
						redPong.setWidth(mLevelConfig.getPenguin().getWidth());
						redPong.setRadius(mLevelConfig.getPenguin().getRadius());

						mPongs.add(redPong);

						mHasLaunched = true;
						mRoundStartedTime = System.currentTimeMillis();
						mBonusItemSuccessInt = 1;
						mSecondsSinceLaunch = 0;
						mBounusItemCounter = 0;
					}
				}
			} else if (mHasPong
					&& motionEvent.getAction() == MotionEvent.ACTION_UP) {
				mHasPong = false;
			}
		}

	}

	@Override
	public boolean handleBackPressed() {
		
		showContextMenu();
		
		return true;

	}
	
	@Override
	public void handleShowOptionsMenu() {
		
		if (!paused) {
			showContextMenu();
		}
	}

	private void showContextMenu() {
		ImageComponent[] buttons = new ImageComponent[3];

		Bitmap bitmapMenu = ImageLoader.loadFromResource(mContext, R.drawable.button_menu);
		Bitmap bitmapReplay = ImageLoader.loadFromResource(mContext, R.drawable.button_replay);
		Bitmap bitmapCancel = ImageLoader.loadFromResource(mContext, R.drawable.button_no);
	
		EventListener cancelListener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mScreenManager.removeTopScreen();
				paused = false;
				return true;
			}
		};
		EventListener replayListener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mScreenManager.removeTopScreen();
				reset();
				paused = false;
				return true;
			}
		};
		EventListener menuListener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mScreenManager.removeTopScreens(2); // remove top 2 screens
				return true;
			}
		};
		buttons[0] = new ImageComponent(bitmapMenu, true);
		buttons[0].setWidth((int)(70*mScale));
		buttons[0].setHeight((int)(70*mScale));
		buttons[0].setEventListener(menuListener);
		buttons[0].resizeBitmap();
		
		buttons[1] = new ImageComponent(bitmapReplay, true);
		buttons[1].setWidth((int)(70*mScale));
		buttons[1].setHeight((int)(70*mScale));
		buttons[1].setEventListener(replayListener);
		buttons[1].resizeBitmap();
		
		buttons[2] = new ImageComponent(bitmapCancel, true);
		buttons[2].setWidth((int)(70*mScale));
		buttons[2].setHeight((int)(70*mScale));
		buttons[2].setEventListener(cancelListener);
		buttons[2].resizeBitmap();		
		
		contextMenuScreen = new ContextMenuScreen(getWidth(),
				getHeight(),
				mScale,
				buttons,
				true,
				this);

		mScreenManager.addScreenUI(contextMenuScreen);
		
		paused = true;
	}

	@Override
	public void update(float timeStep) {
		
//		System.out.println(Runtime.getRuntime().freeMemory());
		
		if (paused) {
			return;
		}
		
		int moving = 0;
		
		if (mMotionEnabled) {
			mLaunchPong.update(timeStep);
		}

		// UPDATE POSITIONS AND VELOCITY FOR ALL PONGS.
		for (Pong pong : mPongs) {

			pong.update(timeStep);
			
			// First check if this pong has stopped.
			if (pong.velocity.getLength() == 0.0f) {
				continue;
			}

			// add one more pong to the move counter.
			moving++;

			//calculate new velocity. avoids creating new Vector2f:
			pong.getPosition().setX(pong.getPosition().getX() + pong.velocity.getX() * timeStep);
			pong.getPosition().setY(pong.getPosition().getY() + pong.velocity.getY() * timeStep);

			// Calculate velocity retardation
			Vector2f friction = pong.velocity.inv(); //TODO: new används här. Undvika?
			friction.normalizeT(); //TODO: sqrt() kan undvikas.
			friction.mulT(timeStep * mConfig.getFriction() * mScale);

			// Set velocity to zero if friction has made our velocity negative.
			if (pong.velocity.getLengthSquared() < friction.getLengthSquared()) {
				pong.velocity.set(0.0f, 0.0f);
			} else {
				pong.velocity.addT(friction);
			}
		}

		// UPDATE POSITIONS AND VELOCITY FOR ALL BONUSITEMS.

		for (int i = 0; i < mBonusItems.size(); i++) {

			BonusItem bonusItem = mBonusItems.get(i);

			//calculate new velocity. avoids creating new Vector2f:
			bonusItem.getPosition().setX(bonusItem.getPosition().getX() + bonusItem.getVelocity().getX() * timeStep);
			bonusItem.getPosition().setY(bonusItem.getPosition().getY() + bonusItem.getVelocity().getY() * timeStep);		

			// Check if bonusItem is too far to the bottom
			if (bonusItem.getVelocity().getY() > 0 /* if direction is down */
					&& bonusItem.getPosition().getY() > getHeight() - mLaunchPadHeight) {
				mBonusItems.remove(bonusItem);
			}
		}

		// CHECK IF GAME JUST ENDED
		boolean levelCompleted = false;
		if (!mGameOver && mHasLaunched && moving == 0) {
			levelCompleted = checkGameFinished();
			if (levelCompleted) {
				mGameOver = true;
				mBonusItems.clear();
				if (alien != null) {
					alien.reset();
				}
			} else {
				resetLauncher();
			}

		}
		
		if (alien != null && !alien.isMoving() && !mGameOver && mHasLaunched) {
			long timeSinceCheckPoint = System.currentTimeMillis() - mAlienStartTime;
			if (timeSinceCheckPoint > mAlienInterval) {
				if (!alien.isDead()) {
					alien.reset();
				}
				alien.start();
				mAlienStartTime = System.currentTimeMillis() + ALIEN_DRIVE_BY_TIME_MILLIS;
			}
		}
		
		if (alien != null) {
			alien.update(timeStep);
			if (alien.canShoot()) {
				for (Pong pong : mPongs) {
					if (pong.getType() == Pong.Type.COW) {
						if (pong.getTimesShrinken() > 0) {
							if (alien.isShooting() == false) {
								pong.setTimesShrinken(pong.getTimesShrinken()-1);
								pong.setWidth(pong.getWidth() * 1.5f);
								pong.setHeight(pong.getHeight() * 1.5f);
								pong.setRadius(pong.getRadius() * 1.5f);
								alien.shoot(pong);
								if (mVibrationEnabled) {
									vibrator.vibrate(20);
								}
							}
							break;
						}
					}
				}
			}
		}

		// CHECK FOR COLLISIONS
		for (int i = 0; i < mPongs.size(); i++) {

			// for each pong. Check for collision with the bonusitems and alien.
			Pong pong = mPongs.get(i);
			if(pong.getType() == Type.PENGUIN) {
				
				if (alien != null && alien.isMoving()) {
					if (CollisionCalculator.circleCircleCollision(pong.getPosition(), alien.getPosition(), pong.getRadius(), alien.getRadius())) {
						setTotalScore(mTotalScore + SCORE_FOR_ALIEN_KILL);
						
						Vector2f position = alien.getPosition().sub(pong.getPosition());
						position.mulT(0.5f);
						position.addT(pong.getPosition());

						mPoints.add(new Score(SCORE_FOR_ALIEN_KILL, position, Color.GREEN, mScale));
						
						alien.setDead(true);
					}
				}
				
				for (int k = 0; k < mBonusItems.size(); k++) {
					BonusItem bonusItem = mBonusItems.get(k);
					
					if (CollisionCalculator.circleCircleCollision(pong.getPosition(), bonusItem.getPosition(), pong.getRadius(), bonusItem.getRadius())) {
						
						int color = 0;
						
						// PICK UP ITEM!
						if (bonusItem.getItemType() == BonusItemType.GROWER) {
							
							if (pong.getCurrentGroth() < pong.getMaxGroth() && canGrow(pong, pong.getRadius() * 1.5f)) {
								pong.setWidth(pong.getWidth() * 1.5f);
								pong.setHeight(pong.getHeight() * 1.5f);
								pong.setRadius(pong.getRadius() * 1.5f);
								pong.setCurrentGroth(pong.getCurrentGroth() + 1);
							}
							color = Color.GREEN;

						} else if (bonusItem.getItemType() == BonusItemType.SHRINKER) {
							
							if (pong.getCurrentGroth() > -pong.getMaxShrink()) {
								pong.setWidth(pong.getWidth() * 0.666666f);
								pong.setHeight(pong.getHeight() * 0.666666f);
								pong.setRadius(pong.getRadius() * 0.666666f);
								pong.setCurrentGroth(pong.getCurrentGroth() - 1);
							}
							color = Color.DKGRAY;
						}
						
						setTotalScore(mTotalScore + bonusItem.getScore());
						
						Vector2f position = bonusItem.getPosition().sub(pong.getPosition()); //TODO: new. Inte så farligt dock.
						position.mulT(0.5f);
						position.addT(pong.getPosition());

						mPoints.add(new Score(bonusItem.getScore(), position, color, mScale));//TODO: new. Inte så farligt dock.
						
						mBonusItems.remove(bonusItem);
						
						break;
					}
				}
			}
			
			//TODO: denna kod kan få en stilla stående pong som tar en bonusitem att hamna i en annan pong.
			// Check if pong is too far to the left
			if (pong.getPosition().getX() < pong.getRadius()) {
				pong.getPosition().setX(pong.getRadius());
				pong.velocity.setX(-pong.velocity.getX());
				// Check if pong is too far to the right
			} else if (pong.getPosition().getX() > getWidth() - pong.getRadius()) {
				pong.getPosition().setX(getWidth() - pong.getRadius());
				pong.velocity.setX(-pong.velocity.getX());
			}

			// Check if pong is too far to the top
			if (pong.getPosition().getY() < pong.getRadius()) {
				pong.getPosition().setY(pong.getRadius());
				pong.velocity.setY(-pong.velocity.getY());
				// Check if pong is too far to the bottom
			} else if (pong.velocity.getY() > -0.000001 /* if direction is down or no velocity at all. */
					&& pong.getPosition().getY() > getHeight() - pong.getRadius()
							- mLaunchPadHeight) {
				pong.getPosition().setY(getHeight() - pong.getRadius() - mLaunchPadHeight);
				pong.velocity.setY(-pong.velocity.getY());
			}

			for (int j = i + 1; j < mPongs.size(); j++) {

				Pong first = mPongs.get(i);
				Pong second = mPongs.get(j);

				first.getPosition().sub(second.getPosition(), collisionCheckVector);

				if (!CollisionCalculator.circleCircleCollision(first.getPosition(), second.getPosition(), first.getRadius(), second.getRadius())) {
					continue;
				}
				
				boolean wasKill = pongCollision(first, second);

				if (!wasKill) {
					
					//Calculate new velocity vectors.
					
					float length = collisionCheckVector.getLength(); //TODO: sqrt

					Vector2f mtd = collisionCheckVector.mul((first.getRadius()
							+ second.getRadius() - length)
							/ length);
	
					first.getPosition().addT(mtd.mul(0.505f));
					second.getPosition().subT(mtd.mul(0.505f));
	
					//samma som normalizeT:
					collisionCheckVector.divT(length);
	
					float aci = first.velocity.dot(collisionCheckVector);
					float bci = second.velocity.dot(collisionCheckVector);

					first.velocity.addT(collisionCheckVector.mul((bci - aci) * 0.90f));
					second.velocity.addT(collisionCheckVector.mul((aci - bci) * 0.90f));
				} else {
					if (first.getType() == Type.COW) {
						mKillAnimations.add(new KillAnimation(first.getPosition().getX(), first.getPosition().getY(), 7*mScale + (int)(5* mScale)*mCowsKilled + (int)(mCowsTakenHolderWidth * mScale)*mCowsKilled, getHeight() - 25*mScale));
						mCowsKilled++;
					} else {
						mKillAnimations.add(new KillAnimation(first.getPosition().getX(), first.getPosition().getY(), 7*mScale + (int)(5* mScale)*mCowsKilled + (int)(mCowsTakenHolderWidth * mScale)*mCowsKilled, getHeight() - 25*mScale));
						mCowsKilled++;
					}
				}
			}
			
			//Check for collision with bricks
			checkBrickCollision(pong, timeStep);
		}

		// UPDATE FADING SCORES							
		updateScores(timeStep);
		
		// UPDATE POSITIONS FOR KILL ANIMATIONS
		updateKillAnimations();
		for (SpriteComponent cow : mCollectedCows) {
			cow.update(timeStep);
		}	
		
		if (!mLevelText.isDone()) {
			mLevelText.update(timeStep);
		}
		if (!mTopScoreText.isDone()) {
			mTopScoreText.update(timeStep);
		}

		if (mHasLaunched && !mGameOver) {
			randomizeBonusItem();
		}
		
		if (mBricks.size() > 0) {
			for (Brick brick : mBricks) {
				brick.update(timeStep);
			}
		}
		
		if (mGameOver && checkGameFinished()) {
			saveTopScore();
		}

	}

	private void updateKillAnimations() {
		for (KillAnimation killAnimation : mKillAnimations) {
			killAnimation.update();
			if (killAnimation.isDone()) {
				mKillAnimationsToDelete.add(killAnimation);
				float width = mCowsTakenWidth * mScale;
				mCollectedCows.add(new SpriteComponent(BitmapManager.getBitmap(Constants.BITMAP_COW_TAKEN), true, true, false, 200, killAnimation.getCurrentX() + width*0.5f, killAnimation.getCurrentY() + width*0.5f, width, width));
			}
		}
		mKillAnimations.removeAll(mKillAnimationsToDelete);
		mKillAnimationsToDelete.clear();
	}

	private boolean canGrow(Pong pong, float newRadius) {
		boolean canGrow = true;
		
		if (mBricks != null && mBricks.size() > 0) {
			for (Brick brick : mBricks) {
				if (brick.getType() == Brick.Type.HARD) {
					boolean collision = CollisionCalculator.circleOrtogonalSquareCollisionWithNewRadius(pong, brick, newRadius);
					if (collision) {
						canGrow = false;	
						break;
					}
				}
			}
		}
		
		return canGrow;
	}

	private void checkBrickCollision(Pong pong, float timeStep) {
		if (mBricks.size() > 0) {
			Brick brickCollidedWith = null;
			boolean sideHit = false;
			boolean cornerHit = false;
			boolean collision = false;
			boolean collisionExist = false;
			
			if (sidesHasPrecedence) {

				//First check collision against sides.
				for (int j = 0; j < mBricks.size(); j++) {
					
					Brick brick = mBricks.get(j);

					boolean singleSideCollision = false;
					
					collision = CollisionCalculator
							.circleOrtogonalSquareCollision(pong, brick);
					
					collisionExist = collision ? collision : collisionExist;
	
					if (collision) {
						
						// Ok there was a collision. See if it was against side.
						singleSideCollision = CollisionCalculator.circleOrtogonalSquareSingleSideCollision(pong, brick);
						
						if (singleSideCollision) {
							sideHit = true;
							if (brickCollidedWith == null) {
								brickCollidedWith = brick;
							} else {
								//Pong is colliding with more than one brick. 
								//Check which one is closest.
								
								pong.getPosition().sub(brickCollidedWith.getPosition(), collisionCheckVector);
								float lengthBetweenPongAndPreviousBrickSquared = collisionCheckVector.getLengthSquared();
								
								pong.getPosition().sub(brick.getPosition(), collisionCheckVector);
								float lengthBetweenPongAndCurrentBrickSquared = collisionCheckVector.getLengthSquared();
								
								if (lengthBetweenPongAndPreviousBrickSquared > lengthBetweenPongAndCurrentBrickSquared) {
									brickCollidedWith = brick;
								}
							}
							if (brick.getType() == Brick.Type.SOFT) {
								mPoints.add(new Score(SCORE_FOR_BRICK, new Vector2f(brick 
										.getPosition()), Color.YELLOW, mScale));
								setTotalScore(mTotalScore + SCORE_FOR_BRICK);
							}
						}
	
					}
	
				}
				
				if (brickCollidedWith == null && collisionExist) {
					
					
					//There was no hit against any single side. So check the corners.
					for (int j = 0; j < mBricks.size(); j++) {
						Brick brick = mBricks.get(j);
						if (CollisionCalculator.circleOrtogonalSquareCollision(pong, brick) && 
							CollisionCalculator.circleOrtogonalSquareCornerCollision(pong, brick)) {
							cornerHit = true;

							brickCollidedWith = brick;
	//							Logger.log("CORNER HIT against" + brick.toString());
							if (brick.getType() == Brick.Type.SOFT) {
	//								brick.fadeOut(250);
								mPoints.add(new Score(SCORE_FOR_BRICK, new Vector2f(brick
										.getPosition()), Color.YELLOW, mScale));
								setTotalScore(mTotalScore + SCORE_FOR_BRICK);
							}
							break;
						}
					}
				}
			} else {
				
				//check sides and corners for one brick at a time.
				for (int j = 0; j < mBricks.size(); j++) {
					
					Brick brick = mBricks.get(j);

					collision = CollisionCalculator
							.circleOrtogonalSquareCollision(pong, brick);
	
					if (collision) {
						
						sideHit = true; //we don't care so set both to true
						cornerHit = true;//we don't care so set both to true
						
						brickCollidedWith = brick;
						if (brick.getType() != Brick.Type.HARD) {
							mPoints.add(new Score(SCORE_FOR_BRICK, new Vector2f(brick
									.getPosition()), Color.YELLOW, mScale));
							setTotalScore(mTotalScore + SCORE_FOR_BRICK);
						}
						break;
					}
				}
			}
			
			if (brickCollidedWith != null) {
				if (brickCollidedWith.getType() == Brick.Type.SOFT){
					mBricks.remove(brickCollidedWith);
				} else if (brickCollidedWith.getType() == Brick.Type.MEDIUM) {
					brickCollidedWith.setType(Brick.Type.SOFT);
					brickCollidedWith.setBitmap(mBrickMediumBroken);
				}
				CollisionCalculator
						.calculateCollisionVectorCircleVsSolidSquare(pong,
								brickCollidedWith, cornerHit, sideHit, timeStep);
			}
		}
	}

	private void updateScores(float timeStep) {
		int i = 0;
		for (Score score : mPoints) {
			score.update(timeStep);
			if (score.isDone()) {
				scoresToDelete[i] = score;
				i++;
			}
		}
		if (i > 0) {
			for (int k = 0; k < i; k++) {
				mPoints.remove(scoresToDelete[k]);
			}
		}
	}

	private void setTotalScore(int score) {
		mTotalScore = score;
		
		float star1progress = 0;
		float star2progress = 0;
		float star3progress = 0;
		
		if (mTotalScore >= mLevelConfig.getOneStarScore()) {
			star1progress = 1.0f;
			if (mTotalScore >= mLevelConfig.getTwoStarsScore()) {
				star2progress = 1.0f;
				if (mTotalScore >= mLevelConfig.getThreeStarsScore()) {
					star3progress = 1.0f;
				} else {
					star3progress = (mTotalScore  - mLevelConfig.getTwoStarsScore()) / (float)(mLevelConfig.getThreeStarsScore() - mLevelConfig.getTwoStarsScore());
				}
			} else {
				star2progress = (mTotalScore - mLevelConfig.getOneStarScore()) / (float)(mLevelConfig.getTwoStarsScore() - mLevelConfig.getOneStarScore());
			}
		} else {
			star1progress = mTotalScore / (float)mLevelConfig.getOneStarScore();
		}
		
		mScoreBar.setScoreProgress(star1progress, star2progress, star3progress);
	}

	private boolean checkGameFinished() {
		boolean finished = true;
		for (int i = 0; i < mPongs.size(); i++) {
			if (mPongs.get(i).getType() == Type.COW) {
				finished = false;
				break;
			}
		}
		return finished;
	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawBitmap(
				BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND_GAME), 0,
				0, null);

		for (int i = 0; i < mBonusItems.size(); i++) {
			BonusItem bonusItem = mBonusItems.get(i);
			mItemRect.set((int) (bonusItem.getPosition().getX() - bonusItem
					.getWidth() * 0.5f),
					(int) (bonusItem.getPosition().getY() - bonusItem
							.getHeight() * 0.5f),
					(int) (bonusItem.getPosition().getX() + bonusItem
							.getWidth() * 0.5f), (int) (bonusItem.getPosition()
							.getY() + bonusItem.getHeight() * 0.5f));
			canvas.drawBitmap(bonusItem.getBitmap(), null, mItemRect,
					mGenericPaint);
		}

		canvas.drawBitmap(mLaunchPad, null, mLaunchPadRect, mGenericPaint);
		
		int numberOfCows = mLevelConfig.getEnemies().size();
		for (int i = 0; i < numberOfCows; i++) {
			int spaceBetweenCows = (int)(5* mScale);
			canvas.drawBitmap(BitmapManager.getBitmap(Constants.BITMAP_COW_TAKEN_HOLDER), 7*mScale + spaceBetweenCows*i + (int)(mCowsTakenHolderWidth * mScale)*i,
					getHeight() - 25*mScale, mGenericPaint);
		}

		if (mBricks.size() > 0) {
			for (Brick brick : mBricks) {
				brick.draw(canvas);
			}
		}
		
		for (Pong pong : mPongs) {

			if (pong.getType() == Type.PENGUIN) {
				if (pong.getCurrentGroth() < -1) {
					pong.setSpriteBitmap(mPenguinBmpX005);
				} else if (pong.getCurrentGroth() == -1) {
					pong.setSpriteBitmap(mPenguinBmpX05);
				} else if (pong.getCurrentGroth() == 0) {
					pong.setSpriteBitmap(mPenguinBmpX1);
				} else if (pong.getCurrentGroth() == 1) {
					pong.setSpriteBitmap(mPenguinBmpX2);
				} else if (pong.getCurrentGroth() == 2) {
					pong.setSpriteBitmap(mPenguinBmpX3);
				}
			} else if (pong.getType() == Type.COW) {
				if (pong.getTimesShrinken() == 0) {
					pong.setSpriteBitmap(mCow1);
				} else if (pong.getTimesShrinken() == 1) {
					pong.setSpriteBitmap(mCow2);
				} else if (pong.getTimesShrinken() == 2) {
					pong.setSpriteBitmap(mCow3);
				}
			}
			
			pong.draw(canvas);
		}
		
		if (alien != null) {
			alien.draw(canvas);
		}
		
		if (mMotionEnabled) {
			
			mLaunchPong.setSpriteBitmap(mPenguinBmpX1);
			mLaunchPong.draw(canvas);

		}

		
		if (!mGameOver) {
			drawStatusBar(canvas);
		} else {
			addGameOverDialog(canvas);
		}

		for (Score points : mPoints) {
			points.draw(canvas);
		}
		
		for (KillAnimation killAnimation : mKillAnimations) {
			canvas.drawBitmap(BitmapManager.getBitmap(Constants.BITMAP_COW_TAKEN_HOLDER), 
					killAnimation.getCurrentX(),
					killAnimation.getCurrentY(),
					mGenericPaint);
		}
		
		for (SpriteComponent cow : mCollectedCows) {
			cow.draw(canvas);
		}	
		
		if (!mLevelText.isDone()) {
			mLevelText.draw(canvas);
		}
		if (!mTopScoreText.isDone()) {
			mTopScoreText.draw(canvas);
		}
	}

	private void drawStatusBar(Canvas canvas) {
		
		if (!mScoreBar.isVisible()) {
			return;
		}
		
		String score = "" + mTotalScore;
		String top = TOP_SCORE + mTopScore;

		mTextPaint.getTextBounds(score, 0, score.length(), mScoreRect);
		mTextPaint.getTextBounds(top, 0, top.length(), mTopRect);

		canvas.drawText(score, mScoreBar.getDrawRect().right + 10 * mScale, 10 * mScale + mScoreRect.height(),
				mStrokePaint);
		canvas.drawText(score, mScoreBar.getDrawRect().right + 10 * mScale, 10 * mScale + mScoreRect.height(),
				mTextPaint);

		mTopScoreText.setPosition(getWidth()*0.5f, getHeight() *0.5f + mLevelText.getTextHeight() + 10 * mScale);

		drawLifesLeft(canvas);
	}

	private void drawLifesLeft(Canvas canvas) {
		Bitmap lifePenguin = BitmapManager
				.getBitmap(Constants.BITMAP_PENGUIN_LIFE);
		for (int i = 0; i < mLives; i++) {

			float bitmapWidth = GraphicsUtil
					.dpToPixels(mLivesLeftDpWidth, mDpi);
			float rightMargin = 10* mScale;

			mItemRect.set(
					(int) (getWidth() - bitmapWidth * (i + 1) - rightMargin),
					(int) (bitmapWidth * 0.5f), 
					(int) (getWidth() - bitmapWidth * (i) - rightMargin),
					(int) (bitmapWidth * 0.5f + bitmapWidth));
			canvas.drawBitmap(lifePenguin, null, mItemRect, mGenericPaint);
		}
	}
	
	private void loadNextLevel() {
		
		if (mLevelConfig.getWorldNumber() == 0 && mLevelConfig.getLevelNumber() == 9) {
			//After level 1.10 is completed:
			mScreenManager.addScreenUI(new AlienTalkScreen(mDpi, new String[] { "Heey!",
			"Stop messing with my cows! Don't be so rude!" }));
		}
		
		Level level = LevelManager.getLevel(mLevelConfig.getWorldNumber()+1, mLevelConfig.getLevelNumber()+2, mScale, mActivity.getAssets());
		mLevelConfig = level;
		mConfig = mLevelConfig.getBowlConfiguration();
		initialize();
	}

	private void addGameOverDialog(Canvas canvas) {
		
		if (paused) {
			paused = false;
			mScreenManager.removeScreenUI(contextMenuScreen);
		}
		
		if (mLivesLeftAnimationStart == 0) {
			mLivesLeftAnimationStart = System.currentTimeMillis();
		}
		
		long timeSinceFinish = System.currentTimeMillis() - mLivesLeftAnimationStart;
		
		drawLifesLeft(canvas);
		drawStatusBar(canvas);
		
		if (mLives > 0 && timeSinceFinish > 1000) {

			long timeStepsSinceGameEnded = (timeSinceFinish) / 300;
			
			if (timeStepsSinceGameEnded > mTimeStepsSinceGameEnded) {
				//new timestep!
				if (mLives > 0) {
					float bitmapWidth = GraphicsUtil
							.dpToPixels(mLivesLeftDpWidth, mDpi);
					float rightMargin = bitmapWidth * 0.3f;
	
					Vector2f position = new Vector2f(getWidth() - bitmapWidth * (mLives + 1) - rightMargin*2.0f, 
							bitmapWidth * 4.0f);
	
					mPoints.add(new Score(SCORE_FOR_LIFE_LEFT, position, Color.GREEN, mScale));
					setTotalScore(mTotalScore + SCORE_FOR_LIFE_LEFT);
					mLives--;
				}
				mTimeStepsSinceGameEnded = (int)timeStepsSinceGameEnded;
			}
		} else if (mGameEndedTime == 0 && (timeSinceFinish) / 300 > mTimeStepsSinceGameEnded +3) {

			mScoreBar.setVisible(false);
			
			mGameEndedTime = System.currentTimeMillis();
			
			EventListener nextLevelListener = new EventListener() {
				@Override
				public boolean handleButtonClicked() {
					mScreenManager.removeTopScreen();
					loadNextLevel();
					return true;
				}
			};
			EventListener replayListener = new EventListener() {
				@Override
				public boolean handleButtonClicked() {
					mScreenManager.removeTopScreen();
					reset();
					return true;
				}
			};
			EventListener menuListener = new EventListener() {
				@Override
				public boolean handleButtonClicked() {
					mScreenManager.removeTopScreens(2); // remove top 2 screens
					return true;
				}
			};
			
			boolean success = checkGameFinished();
			
			int numButtons = 3;
			int numRows = 2;
			if (mHightScore && success) {
				numRows = 3;
			}
			
			DialogString[] dialogRows = new DialogString[numRows];
			
			GameEndedDialogDrawArea drawArea = null;
			
			if (success) {
				dialogRows[0] = new DialogRegularString("LEVEL COMPLETED!",
						TextSize.SMALL, 0xFFffffff, null);
				mCounterStringGameOver = new DialogCounterString(mTotalScore,
						TextSize.LARGE, 0xFFffffff, new int[] { 20, 0, 0, 0 });
				dialogRows[1] = mCounterStringGameOver;
				drawArea = new GameEndedDialogDrawArea(100, mLevelConfig,
						mContext, mCounterStringGameOver, mScale);
				if (mHightScore) {
					dialogRows[2] = new DialogPulsingString("NEW HIGHSCORE!",
							TextSize.SMALL, 0xFFff0000,
							new int[] { 0, 0, 10, 0 });
				}
			} else {
				numButtons = 2;
				dialogRows[0] = new DialogRegularString("LEVEL FAILED!", TextSize.SMALL, 0xFFffffff, null);
				dialogRows[1] = new DialogRegularString(""+mTotalScore, TextSize.LARGE, 0xFFffffff, null);
			}
			
			ImageComponent[] buttons = new ImageComponent[numButtons];

			Bitmap bitmapNext = ImageLoader.loadFromResource(mContext, R.drawable.button_next);
			Bitmap bitmapReplay = ImageLoader.loadFromResource(mContext, R.drawable.button_replay);
			Bitmap bitmapMenu = ImageLoader.loadFromResource(mContext, R.drawable.button_menu);
			
			if (success) {
				ImageComponent nextLevelButton = new ImageComponent(bitmapNext, true);
				nextLevelButton.setWidth((int)(70*mScale));
				nextLevelButton.setHeight((int)(70*mScale));
				nextLevelButton.setEventListener(nextLevelListener);
				nextLevelButton.resizeBitmap();
				buttons[2] = nextLevelButton;
			}
			
			ImageComponent replayLevelButton = new ImageComponent(bitmapReplay, true);
			replayLevelButton.setWidth((int)(70*mScale));
			replayLevelButton.setHeight((int)(70*mScale));
			replayLevelButton.setEventListener(replayListener);
			replayLevelButton.resizeBitmap();
			buttons[1] = replayLevelButton;
			
			ImageComponent menuButton = new ImageComponent(bitmapMenu, true);
			menuButton.setWidth((int)(70*mScale));
			menuButton.setHeight((int)(70*mScale));
			menuButton.setEventListener(menuListener);
			menuButton.resizeBitmap();
			buttons[0] = menuButton;
			

			DialogScreen gameOverDialog = new DialogScreen(getWidth(),
					getHeight(),
					mScale,
					mGameEndedTime,
					dialogRows,
					buttons,
					drawArea, 
					false);

			mScreenManager.addScreenUI(gameOverDialog);
		}
	}

	private boolean pongCollision(Pong first, Pong second) {
		boolean wasKill = false;

		if (mVibrationEnabled) {
			vibrator.vibrate(20);
		}

		if (first.getType() == Type.COW && second.getType() == Type.COW) {
			int value = mRoundHits * mConfig.getEnemyEnemyValue();
			setTotalScore(mTotalScore + value);

			Vector2f position = second.getPosition().sub(first.getPosition());
			position.mulT(0.5f);
			position.addT(first.getPosition());

			mPoints.add(new Score(value, position, Color.YELLOW, mScale));
		} else if ((first.getType() == Type.COW && second.getType() == Type.PENGUIN)
				|| (first.getType() == Type.PENGUIN && second.getType() == Type.COW)) {
			
			if (first.getType() == Type.COW) {
				if (first.getTimesShrinken() == first.getShrinkSteps()) {
					mPongs.remove(first);
					wasKill = true;
				}
				first.setWidth(first.getWidth() * 0.666666f);
				first.setHeight(first.getHeight() * 0.666666f);
				first.setRadius(first.getRadius() * 0.666666f);
				first.setTimesShrinken(first.getTimesShrinken()+1);
			} else {
				if (second.getTimesShrinken() == second.getShrinkSteps()) {
					mPongs.remove(second);
					wasKill = true;
				}
				second.setWidth(second.getWidth() * 0.666666f);
				second.setHeight(second.getHeight() * 0.666666f);
				second.setRadius(second.getRadius() * 0.666666f);
				second.setTimesShrinken(second.getTimesShrinken()+1);
			}
			
			int value = mRoundHits * mConfig.getPenguinEnemyValue();
			if (wasKill) {
				value = SCORE_FOR_ENEMY_KILL;
			}
			setTotalScore(mTotalScore + value);

			Vector2f position = second.getPosition().sub(first.getPosition());
			position.mulT(0.5f);
			position.addT(first.getPosition());

			mPoints.add(new Score(value, position, Color.YELLOW,
					mScale));

		} else {
			int value = mRoundHits * mConfig.getPenguinPenguinValue();
			setTotalScore(mTotalScore + value);

			Vector2f position = second.getPosition().sub(first.getPosition());
			position.mulT(0.5f);
			position.addT(first.getPosition());

			mPoints.add(new Score(value, position, Color.YELLOW, mScale));
		}
		
		return wasKill;
	}

	private void resetLauncher() {
		mLives--;

		if (mLives < 0) {
			mGameOver = true;
			mBonusItems.clear();
			if (alien != null) {
				alien.reset();
			}
			return;
		}

		mLaunchPong.setPosition(new Vector2f(mLevelConfig.getPenguin()
				.getPosition()));
		mLaunchPong.velocity = new Vector2f();
		mLaunchPong.setHeight(mLevelConfig.getPenguin().getHeight());
		mLaunchPong.setWidth(mLevelConfig.getPenguin().getWidth());
		mLaunchPong.setRadius(mLevelConfig.getPenguin().getRadius());
		mLaunchPong.setAnimateFromSmall(true);
		mLaunchPong.setAnimationMillis(120);
		mLaunchPong.resetAnimation();

		mMotionEnabled = true;
		mHasLaunched = false;

	}

	/**
	 * Set up startpositions for the enemies.
	 * 
	 * @param i
	 */
	private void addInitialPong(Pong enemy) {
		Pong pong = new Pong(enemy);
		pong.velocity = new Vector2f();
		mPongs.add(pong);
	}
	
	private void addBricks(List<Brick> bricks) {
		
		if (bricks != null && bricks.size() > 0) {
			for (Brick brick : bricks) {
				Brick brickCopy = new Brick(brick);
				if (brickCopy.getType() == Brick.Type.SOFT) {
					brickCopy.setBitmap(mBrickSoft);
				} else if (brickCopy.getType() == Brick.Type.MEDIUM) {
					brickCopy.setBitmap(mBrickMedium);
				} else if (brickCopy.getType() == Brick.Type.HARD) {
					brickCopy.setBitmap(mBrickHard);
				}
				mBricks.add(brickCopy);
			}
		}
	}

	private void saveTopScore() {
		SharedPreferences settings = getDefaultSharedPrefs();

		String starsKey = LevelScreen.getStarsPrefsKey(mLevelConfig.getWorldNumber()+1, mLevelConfig.getLevelNumber()+1);
		int stars = settings.getInt(starsKey, 0);
		
		if (mScoreBar.getNumberOfStarsShowing() > stars) {
			Editor editor = settings.edit();
			editor.putInt(starsKey, mScoreBar.getNumberOfStarsShowing());
			mLevelScreen.updateLevelWithStars(mLevelConfig.getLevelNumber(), mScoreBar.getNumberOfStarsShowing());
			editor.commit();
		}
		
		if (mTotalScore > mSavedTopScore) {
			Editor editor = settings.edit();
			editor.putInt(getTopScorePrefsKey(), mTotalScore);
			mHightScore = true;
			mSavedTopScore = mTotalScore;
			editor.commit();
		}
		
			
	}

	private void randomizeBonusItem() {

		if (mLevelConfig.getBonusItemSequence() == null) {
			return;
		}
		
		long secondsSinceLaunch = (System.currentTimeMillis() - mRoundStartedTime) / 1000;

		if (mSecondsSinceLaunch == secondsSinceLaunch || mBounusItemCounter >= mLevelConfig.getBonusItemsPerRound()) {
			return;
		} else {
			mSecondsSinceLaunch = secondsSinceLaunch;
			int randomInt = mBonusItemRandom.nextInt(mLevelConfig.getBonusItemPropability());
			if (randomInt <= mBonusItemSuccessInt) {
				mBonusItems.add(getRandomBonusItem());
				mBounusItemCounter++;
				mBounusItemTotalCounter++;
			} else {
				mBonusItemSuccessInt++;
			}
		}
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
