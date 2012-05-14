package com.tojosebe.vulfen.game;

import java.util.Iterator;
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
import com.tojosebe.vulfen.configuration.BowlConfiguration;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.configuration.LevelManager;
import com.tojosebe.vulfen.dialog.CanvasDialog;
import com.tojosebe.vulfen.dialog.CanvasDialogCounterString;
import com.tojosebe.vulfen.dialog.CanvasDialogRegularString;
import com.tojosebe.vulfen.dialog.CanvasDialogString;
import com.tojosebe.vulfen.dialog.CanvasDialogString.TextSize;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.tojosebe.vulfen.game.Pong.Type;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.math.Vector2f;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class BowlScreen2 extends Screen {

	private static final int SCORE_FOR_LIFE_LEFT = 1000;

	private static final String SCORE = "Score: ";

	private static final String TOP_SCORE = "Top Score: ";

	private float mScale;

	private Vibrator vibrator;

	private Bitmap mRed;
	private Bitmap mYellow0;
	private Bitmap mYellow1;
	private Bitmap mYellow2;
	private Bitmap mYellow3;
	private Bitmap mLaunchPad;

	private Paint mTextPaint = new Paint();
	private Paint mStrokePaint = new Paint(); // text border
	private Paint mTopPaint = new Paint();

	private List<Pong> mPongs = new Vector<Pong>();
	private List<BonusItem> mBonusItems = new Vector<BonusItem>();
	private Pong mLaunchPong = new Penguin();
	private boolean mHasLaunched = false;

	private boolean mMotionEnabled = true;
	private boolean mHasPong = false;
	private Vector2f mLastMotion = new Vector2f();
	private long mLastMotionTime = 0;

	private int mBonusItemDpWidth = 32;

	private int mLivesLeftDpWidth = 10;

	// Random bonus items variables.
	private long mRoundStartedTime;
	private int mBonusItemSuccessInt = 1;
	private boolean mBonusItemHasLaunched = false;
	private Random mBonusItemRandom = new Random(System.currentTimeMillis());
	private long mSecondsSinceLaunch = 0;
	private int mBounusItemCounter = 0;

	private int mTotalScore = 0;
	private int mRoundHits = 1;

	private int mTopScore = 0;
	private int mSavedTopScore = 0;

	// Game ended animation
	private long mGameEndedTime = 0;
	private long mLivesLeftAnimationStart = 0;
	private int mTimeStepsSinceGameEnded = 0;

	private List<Points> mPoints = new Vector<Points>();

	private boolean mVibrationEnabled = true;

	private int mLives;
	private boolean mGameOver = false;

	private RectF mDrawRect = new RectF();

	private BowlConfiguration mConfig;

	private int mDpi;

	private float mGameAreaHeight;

	public static final float GAME_AREA_ASPECT_RATIO = 1.333f;
	private float mLaunchPadHeight;

	private Level mLevelConfig;
	
	private CanvasDialogCounterString mCounterStringGameOver; 

	Paint mGenericPaint = new Paint();
	RectF mLaunchPadRect = new RectF();
	Rect mItemRect = new Rect();

	public BowlScreen2(Level levelConfiguration, int dpi, Activity activity) {
		mConfig = levelConfiguration.getBowlConfiguration();
		mLevelConfig = levelConfiguration;
		mDpi = dpi;
	}

	@Override
	protected void onTop() {
	}

	private BonusItem getRandomBonusItem() {

		BonusItem item = new BonusItem();
		Bitmap bitmap = null;

		item.getVelocity().setY(getWidth() / 5);

		int sequenceNumber = mBounusItemCounter % mLevelConfig.getBonusItemSequence().length;
		BonusItemType type = mLevelConfig.getBonusItemSequence()[sequenceNumber];
		if (type == BonusItemType.GROWER) {
			bitmap = ImageLoader.loadFromResource(mContext, R.drawable.fish);
			item.setItemType(BonusItemType.GROWER);
			item.setScore(500);
		} else if (type == BonusItemType.SHRINKER) {
			bitmap = ImageLoader.loadFromResource(mContext,
					R.drawable.fish_dead);
			item.setItemType(BonusItemType.SHRINKER);
			item.setScore(-500);
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

		vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mContext.getApplicationContext());
		mTopScore = mSavedTopScore = settings.getInt("mSavedTopScore", 0);

		mScale = getWidth() / 480.0f;

		float penguinPongLength = mLevelConfig.getPenguin().getWidth() * mScale;
		mLevelConfig.getPenguin().setWidth(penguinPongLength);
		mLevelConfig.getPenguin().setHeight(penguinPongLength);
		mLevelConfig.getPenguin().setRadius(penguinPongLength * 0.5f);

		mGameAreaHeight = (int) (getWidth() * GAME_AREA_ASPECT_RATIO);

		mLaunchPadHeight = getHeight() - mGameAreaHeight;
		if (mLaunchPadHeight < penguinPongLength * 1.2f) {
			// launchpad too small. We need to make width smaller. But what the
			// fuck. Make the launchpad large enough
			mLaunchPadHeight = penguinPongLength * 1.2f;
			mGameAreaHeight = getHeight() - mLaunchPadHeight;
		}

		mLevelConfig.getPenguin().getPosition()
				.setY(mGameAreaHeight + mLaunchPadHeight / 2.0f);

		mGenericPaint.setAntiAlias(true);

		mLaunchPadRect.set(0, getHeight() - mLaunchPadHeight, getWidth(),
				getHeight());

		float enemyLength = mLevelConfig.getEnemies().get(0).getWidth()
				* mScale;
		for (Pong enemy : mLevelConfig.getEnemies()) {
			enemy.setWidth(enemyLength);
			enemy.setHeight(enemyLength);
			enemy.setRadius(enemyLength * 0.5f);
		}

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
		mStrokePaint.setStrokeWidth(4);

		mLaunchPad = ImageLoader.loadFromResource(mContext,
				R.drawable.launcher3);
		
		 
		int enemyWidth = (int)enemyLength;
		
		mYellow0 = getEnemyPongBitmap(R.drawable.sebe_normal,enemyWidth,enemyWidth,0);
		enemyWidth *= 0.666666f;
		mYellow1 = getEnemyPongBitmap(R.drawable.sebe_2,enemyWidth,enemyWidth,0);
		enemyWidth *= 0.666666f;
		mYellow2 = getEnemyPongBitmap(R.drawable.sebe_3,enemyWidth,enemyWidth,0);
		enemyWidth *= 0.666666f;
		mYellow3 = getEnemyPongBitmap(R.drawable.sebe_4,enemyWidth,enemyWidth,0);
		mRed = getPongBitmap(mLevelConfig.getPenguin());

		reset();

		createBackground();

		createLifeLeftImage();
	}

	private Bitmap getPongBitmap(Pong pong) {
		int resource = pong.getImageResource();
		Bitmap bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), resource);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) pong.getHeight(),
				(int) pong.getWidth());
		return bitmap;
	}
	
	private Bitmap getEnemyPongBitmap(int resource, int height, int width, int timesShrinken) {
		Bitmap bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), resource);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, height,
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

			ImageComponent imageComp = null;

			Bitmap background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background4);
			imageComp = new ImageComponent(background, false);
			imageComp.setHeight(getHeight());
			imageComp.setWidth(getWidth());
			imageComp.resizeBitmap();
			BitmapManager.addBitmap(Constants.BITMAP_BACKGROUND_GAME,
					imageComp.getBitmap());
		}

	}

	private void createLifeLeftImage() {

		if (BitmapManager.getBitmap(Constants.BITMAP_PENGUIN_LIFE) == null) {

			Bitmap lifeBitmap = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.penguin_life);
			BitmapManager.addBitmap(Constants.BITMAP_PENGUIN_LIFE, lifeBitmap);
		}

	}

	private void reset() {
		mLives = mConfig.getLives();
		mGameOver = false;

		mTotalScore = 0;
		mBounusItemCounter = 0;

		mPoints.clear();
		mPongs.clear();
		mBonusItems.clear();

		for (Pong enemy : mLevelConfig.getEnemies()) {
			addInitialPong(new Vector2f(enemy.getPosition()),
					(int) enemy.getWidth(), (int) enemy.getHeight(),
					enemy.getImageResource());
		}

		mGameEndedTime = 0;
		mLivesLeftAnimationStart = 0;
		mTimeStepsSinceGameEnded = 0;

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
					mLaunchPong.position.set(x, y);
				}
			} else if (mHasPong
					&& motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				if (motionEvent.getY() > getHeight() - mLaunchPadHeight) {
					mHasPong = true;

					mLastMotion.set(x, y);
					mLastMotionTime = motionEvent.getEventTime();
					mLaunchPong.position.set(x, y);
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

						Pong redPong = new Penguin();
						redPong.position = new Vector2f(mLaunchPong.position);
						redPong.velocity = velocity;
						redPong.setHeight(mLevelConfig.getPenguin().getHeight());
						redPong.setWidth(mLevelConfig.getPenguin().getWidth());
						redPong.setRadius(mLevelConfig.getPenguin().getRadius());

						mPongs.add(redPong);

						mHasLaunched = true;
						mRoundStartedTime = System.currentTimeMillis();
						mBonusItemSuccessInt = 1;
						mBonusItemHasLaunched = false;
						mSecondsSinceLaunch = 0;
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
		return mScreenManager.removeScreen(this);
	}

	@Override
	public void update(float timeStep) {

		int moving = 0;

		// UPDATE POSITIONS AND VELOCITY FOR ALL PONGS.
		for (Pong pong : mPongs) {

			// First check if this pong has stopped.
			if (pong.velocity.getLength() == 0.0f) {
				continue;
			}

			// add one more pong to the move counter.
			moving++;

			// calculate new velocity
			Vector2f delta = pong.velocity.mul(timeStep);
			pong.position.addT(delta);

			// Check if pong is too far to the left
			if (pong.position.getX() < pong.getRadius()) {
				pong.position.setX(2.0f * pong.getRadius()
						- pong.position.getX());
				pong.velocity.setX(-pong.velocity.getX());
				// Check if pong is too far to the right
			} else if (pong.position.getX() > getWidth() - pong.getRadius()) {
				pong.position.setX(2.0f * (getWidth() - pong.getRadius())
						- pong.position.getX());
				pong.velocity.setX(-pong.velocity.getX());
			}

			// Check if pong is too far to the top
			if (pong.position.getY() < pong.getRadius()) {
				pong.position.setY(2.0f * pong.getRadius()
						- pong.position.getY());
				pong.velocity.setY(-pong.velocity.getY());
				// Check if pong is too far to the bottom
			} else if (pong.velocity.getY() > 0 /* if direction is down */
					&& pong.position.getY() > getHeight() - pong.getRadius()
							- mLaunchPadHeight) {
				pong.position.setY(2.0f
						* (getHeight() - pong.getRadius() - mLaunchPadHeight)
						- pong.position.getY());
				pong.velocity.setY(-pong.velocity.getY());
			}

			// Calculate velocity retardation
			Vector2f friction = pong.velocity.inv();
			friction.normalizeT();
			friction.mulT(timeStep * mConfig.getFriction() * mScale);

			// Set velocity to zero if friction has made our velocity negative.
			if (pong.velocity.getLength() < friction.getLength()) {
				pong.velocity.set(0.0f, 0.0f);
			} else {
				pong.velocity.addT(friction);
			}
		}

		// UPDATE POSITIONS AND VELOCITY FOR ALL BONUSITEMS.

		Iterator<BonusItem> iterator = mBonusItems.iterator();
		while (iterator.hasNext()) {

			BonusItem bonusItem = iterator.next();

			// calculate new velocity
			Vector2f delta = bonusItem.getVelocity().mul(timeStep);
			bonusItem.getPosition().addT(delta);

			// Check if bonusItem is too far to the bottom
			if (bonusItem.getVelocity().getY() > 0 /* if direction is down */
					&& bonusItem.getPosition().getY() > getHeight() - mLaunchPadHeight) {
				mBonusItems.remove(bonusItem);
			}
		}

		// CHECK IF GAME JUST ENDED
		if (!mGameOver && mHasLaunched && moving == 0) {
			
			if (checkGameFinished()) {
				mGameOver = true;
			} else {
				resetLauncher();
			}

			if (mTopScore > mSavedTopScore) {
				saveTopScore(); // TODO This should not be saved to disk in the loop!
			}
			
		}

		// CHECK FOR COLLISIONS
		for (int i = 0; i < mPongs.size(); i++) {

			// for each pong. Check for collision with the bonusitems.
			Pong pong = mPongs.get(i);
			if(pong.getType() == Type.PENGUIN) {
				for (int k = 0; k < mBonusItems.size(); k++) {
					BonusItem bonusItem = mBonusItems.get(k);
					
					Vector2f collision = pong.position.sub(bonusItem.getPosition());
					float length = collision.getLength();
					if (length < pong.getRadius() + bonusItem.getRadius()) {
						// PICK UP ITEM!
						if (bonusItem.getItemType() == BonusItemType.GROWER
								&& pong.getCurrentGroth() < pong.getMaxGroth()) {
							pong.setWidth(pong.getWidth() * 1.5f);
							pong.setRadius(pong.getRadius() * 1.5f);
							pong.setCurrentGroth(pong.getCurrentGroth() + 1);
							
							mTotalScore += bonusItem.getScore();
							
							Vector2f position = bonusItem.getPosition().sub(pong.position);
							position.mulT(0.5f);
							position.addT(pong.position);

							mPoints.add(new Points(bonusItem.getScore(), position, Color.GREEN, mScale));
							
							mBonusItems.remove(bonusItem);
							
							break;
						} else if (bonusItem.getItemType() == BonusItemType.SHRINKER
								& pong.getCurrentGroth() > -pong.getMaxShrink()) {
							pong.setWidth(pong.getWidth() * 0.666666f);
							pong.setRadius(pong.getRadius() * 0.666666f);
							pong.setCurrentGroth(pong.getCurrentGroth() - 1);
							
							mTotalScore += bonusItem.getScore();
							
							Vector2f position = bonusItem.getPosition().sub(pong.position);
							position.mulT(0.5f);
							position.addT(pong.position);

							mPoints.add(new Points(bonusItem.getScore(), position, Color.DKGRAY, mScale));
							
							mBonusItems.remove(bonusItem);
							
							break;
						}
					}
				}
			}

			for (int j = i + 1; j < mPongs.size(); j++) {

				Pong first = mPongs.get(i);
				Pong second = mPongs.get(j);

				Vector2f collision = first.position.sub(second.position);
				float length = collision.getLength();

				if (length >= first.getRadius() + second.getRadius()) {
					continue;
				}

				boolean wasKill = pongCollision(first, second);

				if (!wasKill) {
					Vector2f mtd = collision.mul((first.getRadius()
							+ second.getRadius() - length)
							/ length);
	
					first.position.addT(mtd.mul(0.505f));
					second.position.subT(mtd.mul(0.505f));
	
					collision.normalizeT();
	
					float aci = first.velocity.dot(collision);
					float bci = second.velocity.dot(collision);
	
					float acf = bci;
					float bcf = aci;
	
					first.velocity.addT(collision.mul((acf - aci) * 0.90f));
					second.velocity.addT(collision.mul((bcf - bci) * 0.90f));
				}
			}
		}

		// UPDATE FADING SCORES
		List<Points> pointsCopy = new Vector<Points>(mPoints); // TODO: kan man
																// undvika new
																// här?
		for (Points points : pointsCopy) {
			points.update(timeStep);
			if (points.isDone())
				mPoints.remove(points);
		}

		if (mHasLaunched && !mGameOver) {
			randomizeBonusItem();
		}

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

		Iterator<BonusItem> bonusItemsIterator = mBonusItems.iterator();
		while (bonusItemsIterator.hasNext()) {
			BonusItem bonusItem = bonusItemsIterator.next();
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

		for (Pong pong : mPongs) {

			float x = pong.position.getX() - pong.getRadius();
			float y = pong.position.getY() - pong.getRadius();

			mDrawRect.set(x, y, x + pong.getWidth(), y + pong.getWidth());

			if (pong.getType() == Type.PENGUIN) {
				canvas.drawBitmap(mRed, null, mDrawRect, mGenericPaint);
			} else if (pong.getType() == Type.COW) {
				if (pong.getTimesShrinken() == 0) {
					canvas.drawBitmap(mYellow0, null, mDrawRect, mGenericPaint);
				} else if (pong.getTimesShrinken() == 1) {
					canvas.drawBitmap(mYellow1, null, mDrawRect, mGenericPaint);
				} else if (pong.getTimesShrinken() == 2) {
					canvas.drawBitmap(mYellow2, null, mDrawRect, mGenericPaint);
				} else if (pong.getTimesShrinken() == 3) {
					canvas.drawBitmap(mYellow3, null, mDrawRect, mGenericPaint);
				}
			}
		}

		if (mMotionEnabled) {
			float x = mLaunchPong.position.getX() - mLaunchPong.getRadius();
			float y = mLaunchPong.position.getY() - mLaunchPong.getRadius();

			mDrawRect.set(x, y, x + mLevelConfig.getPenguin().getWidth(), y
					+ mLevelConfig.getPenguin().getWidth());

			canvas.drawBitmap(mRed, null, mDrawRect, mGenericPaint);
		}

		if (!mGameOver) {
			drawStatusBar(canvas);
		} else {
			addGameOverDialog(canvas);
		}

		for (Points points : mPoints) {
			points.draw(canvas);
		}
	}

	private void drawStatusBar(Canvas canvas) {
		String score = SCORE + mTotalScore;
		String top = TOP_SCORE + mTopScore;

		Rect scoreRect = new Rect();
		Rect topRect = new Rect();

		mTextPaint.getTextBounds(score, 0, score.length(), scoreRect);
		mTextPaint.getTextBounds(top, 0, top.length(), topRect);

		canvas.drawText(score, 10 * mScale, 10 * mScale + scoreRect.height(),
				mStrokePaint);
		canvas.drawText(score, 10 * mScale, 10 * mScale + scoreRect.height(),
				mTextPaint);

		canvas.drawText(top, 10 * mScale, 10 * mScale + scoreRect.height()
				+ topRect.height() * 1.3f, mStrokePaint);
		canvas.drawText(top, 10 * mScale, 10 * mScale + scoreRect.height()
				+ topRect.height() * 1.3f, mTextPaint);

		drawLifesLeft(canvas);
	}

	private void drawLifesLeft(Canvas canvas) {
		Bitmap lifePenguin = BitmapManager
				.getBitmap(Constants.BITMAP_PENGUIN_LIFE);
		for (int i = 0; i < mLives; i++) {

			float bitmapWidth = GraphicsUtil
					.dpToPixels(mLivesLeftDpWidth, mDpi);
			float rightMargin = bitmapWidth * 0.3f;

			mItemRect.set(
					(int) (getWidth() - bitmapWidth * (i + 1) - rightMargin),
					(int) (bitmapWidth * 0.5f), 
					(int) (getWidth() - bitmapWidth * (i) - rightMargin),
					(int) (bitmapWidth * 0.5f + bitmapWidth));
			canvas.drawBitmap(lifePenguin, null, mItemRect, mGenericPaint);
		}
	}
	
	private void loadNextLevel() {
		float scale = getWidth() / 480.0f;
		int gameAreaHeight = (int) (getWidth() * BowlScreen2.GAME_AREA_ASPECT_RATIO);
		LevelManager levelManager = LevelManager.getInstance(getWidth(), gameAreaHeight, scale);
		Level level = levelManager.getLevel(mLevelConfig.getWorldNumber(), mLevelConfig.getLevelNumber()+1);
		mLevelConfig = level;
		mConfig = mLevelConfig.getBowlConfiguration();
		initialize();
	}

	private void addGameOverDialog(Canvas canvas) {
		
		if (mLivesLeftAnimationStart == 0) {
			mLivesLeftAnimationStart = System.currentTimeMillis();
		}
		
		long timeSinceFinish = System.currentTimeMillis() - mLivesLeftAnimationStart;
		
		drawLifesLeft(canvas);
		
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
	
					mPoints.add(new Points(SCORE_FOR_LIFE_LEFT, position, Color.GREEN, mScale));
					mTotalScore += SCORE_FOR_LIFE_LEFT;
					mLives--;
				}
				mTimeStepsSinceGameEnded = (int)timeStepsSinceGameEnded;
			}
		} else if (mGameEndedTime == 0 && (timeSinceFinish) / 300 > mTimeStepsSinceGameEnded +3) {

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
					mScreenManager.removeTopScreen();
					mScreenManager.removeTopScreen();
					return true;
				}
			};
			
//			boolean fail = mTotalScore < mLevelConfig.getOneStarScore();
			boolean success = checkGameFinished();
			
			int numButtons = 3;
			CanvasDialogString[] dialogRows = new CanvasDialogString[2];
			
			GameEndedCanvasDialogDrawArea drawArea = null;
			
			if (success) {
				dialogRows[0] = new CanvasDialogRegularString("LEVEL COMPLETED!", TextSize.SMALL, 0xFFffffff, null);
				mCounterStringGameOver = new CanvasDialogCounterString(mTotalScore, TextSize.LARGE, 0xFFffffff, new int[]{20,0,0,0});
				dialogRows[1] = mCounterStringGameOver;
				drawArea = new GameEndedCanvasDialogDrawArea(100, mLevelConfig, mContext, mCounterStringGameOver, mScale);
			} else {
				numButtons = 2;
				dialogRows[0] = new CanvasDialogRegularString("LEVEL FAILED!", TextSize.SMALL, 0xFFffffff, null);
				dialogRows[1] = new CanvasDialogRegularString(""+mTotalScore, TextSize.LARGE, 0xFFffffff, null);
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
			

			CanvasDialog gameOverDialog = new CanvasDialog(getWidth(),
					getHeight(),
					mScale,
					mGameEndedTime,
					dialogRows,
					buttons,
					drawArea, 
					false);

			mScreenManager.addScreen(gameOverDialog);
		}
	}

	private boolean pongCollision(Pong first, Pong second) {
//		mRoundHits++;
		boolean wasKill = false;

		if (mVibrationEnabled) {
			// Vibrate for 30 milliseconds
			vibrator.vibrate(30);
		}

		if (first.getType() == Type.COW && second.getType() == Type.COW) {
			int value = mRoundHits * mConfig.getYellowYellowValue();
			mTotalScore += value;

			Vector2f position = second.position.sub(first.position);
			position.mulT(0.5f);
			position.addT(first.position);

			mPoints.add(new Points(value, position, Color.YELLOW, mScale));
		} else if ((first.getType() == Type.COW && second.getType() == Type.PENGUIN)
				|| (first.getType() == Type.PENGUIN && second.getType() == Type.COW)) {
			
			if (first.getType() == Type.COW) {
				if (first.getTimesShrinken() == first.getShrinkSteps()) {
					mPongs.remove(first);
					wasKill = true;
				}
				first.setWidth(first.getWidth() * 0.666666f);
				first.setRadius(first.getRadius() * 0.666666f);
				first.setTimesShrinken(first.getTimesShrinken()+1);
			} else {
				if (second.getTimesShrinken() == second.getShrinkSteps()) {
					mPongs.remove(second);
					wasKill = true;
				}
				second.setWidth(second.getWidth() * 0.666666f);
				second.setRadius(second.getRadius() * 0.666666f);
				second.setTimesShrinken(second.getTimesShrinken()+1);
			}
			
			int value = mRoundHits * mConfig.getRedYellowValue();
			if (wasKill) {
				value = 500;
			}
			mTotalScore += value;

			Vector2f position = second.position.sub(first.position);
			position.mulT(0.5f);
			position.addT(first.position);

			mPoints.add(new Points(value, position, Color.rgb(255, 180, 0),
					mScale));

		} else {
			int value = mRoundHits * mConfig.getRedRedValue();
			mTotalScore += value;

			Vector2f position = second.position.sub(first.position);
			position.mulT(0.5f);
			position.addT(first.position);

			mPoints.add(new Points(value, position, Color.RED, mScale));
		}

		if (mTotalScore > mTopScore) {
			mTopScore = mTotalScore;
		}
		
		return wasKill;
	}

	private void resetLauncher() {
		mLives--;

		if (mLives < 0) {
			mGameOver = true;
			mBonusItems.clear();
			return;
		}

		mLaunchPong.position = new Vector2f(mLevelConfig.getPenguin()
				.getPosition());
		mLaunchPong.velocity = new Vector2f();
		mLaunchPong.setHeight(mLevelConfig.getPenguin().getHeight());
		mLaunchPong.setWidth(mLevelConfig.getPenguin().getWidth());
		mLaunchPong.setRadius(mLevelConfig.getPenguin().getRadius());

		mMotionEnabled = true;
		mHasLaunched = false;

//		mRoundHits = 0;

	}

	/**
	 * Set up startpositions for the enemies.
	 * 
	 * @param i
	 */
	private void addInitialPong(Vector2f position, int width, int height,
			int imageResource) {
		Pong pong = new Cow();
		pong.position = position;
		pong.velocity = new Vector2f();
		pong.setHeight(height);
		pong.setWidth(width);
		pong.setRadius(width * 0.5f);
		pong.setImageResource(imageResource);
		mPongs.add(pong);
	}

	private void saveTopScore() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mContext.getApplicationContext());
		Editor editor = settings.edit();
		editor.putInt("mSavedTopScore", mTopScore);
		editor.commit();

		mSavedTopScore = mTopScore;
	}

	private void randomizeBonusItem() {

		if (mLevelConfig.getBonusItemSequence() == null) {
			return;
		}
		
		long secondsSinceLaunch = (System.currentTimeMillis() - mRoundStartedTime) / 1000;

		if (mSecondsSinceLaunch == secondsSinceLaunch || mBonusItemHasLaunched) {
			return;
		} else {
			mSecondsSinceLaunch = secondsSinceLaunch;
			int randomInt = mBonusItemRandom.nextInt(5);
			if (randomInt <= mBonusItemSuccessInt) {
				mBonusItems.add(getRandomBonusItem());
				mBonusItemHasLaunched = true;
				mBounusItemCounter++;
			} else {
				mBonusItemSuccessInt++;
			}
		}
	}
	
}
