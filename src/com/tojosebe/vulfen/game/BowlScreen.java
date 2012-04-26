package com.tojosebe.vulfen.game;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.game.Pong.Type;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.math.Vector2f;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class BowlScreen extends Screen {

	private float mScale;

	private Vibrator vibrator;

	private Bitmap mYellow;
	private Bitmap mRed;
	private Bitmap mLaunchPad;

	private Paint mTextPaint = new Paint();
	private Paint mTopPaint = new Paint();

	private List<Pong> mPongs = new Vector<Pong>();
	private Pong mLaunchPong = new Pong();
	private boolean mHasLaunched = false;

	private boolean mMotionEnabled = true;
	private boolean mHasPong = false;
	private Vector2f mLastMotion = new Vector2f();
	private long mLastMotionTime = 0;

	private int mTotalScore = 0;
	private int mRoundHits = 0;

	private int mTopScore = 0;
	private int mSavedTopScore = 0;

	private List<Points> mPoints = new Vector<Points>();

	private boolean mVibrationEnabled = true;

	private int mLives;
	private boolean mGameOver = false;

	private RectF mDrawRect = new RectF();

	private BowlConfiguration mConfig;

	private int mDpi;

	private float mGameAreaHeight;
	private float mGameAreaWidth;
	private float mGameAreaAspectRatio = 1.333f;
	private float mLaunchPadHeight;

	private Level mLevelConfig;

	Paint mLaunchPadPaint = new Paint();
	RectF mLaunchPadRect = new RectF();

	public BowlScreen(Level levelConfiguration, int dpi) {
		mConfig = levelConfiguration.getBowlConfiguration();
		mLevelConfig = levelConfiguration;
		mDpi = dpi;
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

		mGameAreaHeight = (int) (getWidth() * mGameAreaAspectRatio);
		mGameAreaWidth = getWidth();
		mLaunchPadHeight = getHeight() - mGameAreaHeight;
		if (mLaunchPadHeight < penguinPongLength * 1.2f) {
			// launchpad too small. We need to make width smaller. But what the
			// fuck. Make the launchpad large enough
			mLaunchPadHeight = penguinPongLength * 1.2f;
			mGameAreaHeight = getHeight() - mLaunchPadHeight;
		}

		mLevelConfig.getPenguin().getPosition()
				.setY(mGameAreaHeight + mLaunchPadHeight / 2.0f);

		mLaunchPadPaint.setAntiAlias(true);
		mLaunchPadRect.set(0, getHeight() - mLaunchPadHeight, getWidth(),
				getHeight());

		float enemyLength = mLevelConfig.getEnemies().get(0).getWidth()
				* mScale;
		for (Pong enemy : mLevelConfig.getEnemies()) {
			enemy.setWidth(enemyLength);
			enemy.setHeight(enemyLength);
			enemy.setRadius(enemyLength * 0.5f);
		}

		mTextPaint.setTextSize(32 * mScale);
		mTextPaint.setAntiAlias(true);
		mTopPaint.setTextSize(16 * mScale);
		mTopPaint.setAntiAlias(true);

		mLaunchPad = ImageLoader.loadFromResource(mContext,
				R.drawable.launcher3);
		mYellow = getPongBitmap(mLevelConfig.getEnemies().get(0));
		mRed = getPongBitmap(mLevelConfig.getPenguin());

		reset();
	}

	private Bitmap getPongBitmap(Pong pong) {
		int resource = pong.getImageResource();
		Bitmap bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), resource);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) pong.getHeight(),
				(int) pong.getWidth());
		return bitmap;
	}

	private void reset() {
		mLives = mConfig.getLives();
		mGameOver = false;

		mTotalScore = 0;

		mPoints.clear();
		mPongs.clear();

		for (Pong enemy : mLevelConfig.getEnemies()) {
			addInitialPong(new Vector2f(enemy.getPosition()),
					(int) enemy.getWidth(), (int) enemy.getHeight());
		}

		resetLauncher();
	}

	@Override
	public void handleInput(MotionEvent motionEvent) {

		if (mGameOver) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
				reset();
		}

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

						Pong redPong = new Pong();
						redPong.PongType = Type.PENGUIN;
						redPong.position = new Vector2f(mLaunchPong.position);
						redPong.velocity = velocity;
						redPong.setHeight(mLevelConfig.getPenguin().getHeight());
						redPong.setWidth(mLevelConfig.getPenguin().getWidth());
						redPong.setRadius(mLevelConfig.getPenguin().getRadius());

						mPongs.add(redPong);

						mHasLaunched = true;
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
			} else if (pong.velocity.getY() > 0 /*if direction is down */ &&
					pong.position.getY() > getHeight() - pong.getRadius()
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

		// CHECK IF GAME JUST ENDED
		if (!mGameOver && mHasLaunched && moving == 0) {
			resetLauncher();

			if (mTopScore > mSavedTopScore) {
				saveTopScore();
			}
		}

		// CHECK FOR COLLISIONS
		for (int i = 0; i < mPongs.size(); i++) {
			for (int j = i + 1; j < mPongs.size(); j++) {

				Pong first = mPongs.get(i);
				Pong second = mPongs.get(j);

				Vector2f collision = first.position.sub(second.position);
				float length = collision.getLength();

				if (length >= first.getRadius() + second.getRadius()) {
					continue;
				}

				pongCollision(first, second);

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

		// UPDATE FADING SCORES
		List<Points> pointsCopy = new Vector<Points>(mPoints);
		for (Points points : pointsCopy) {
			points.update(timeStep);
			if (points.isDone())
				mPoints.remove(points);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		Rect mBackgroundRect = new Rect();

		mBackgroundRect.set((int) ((getWidth() - mGameAreaWidth) / 2), 0,
				(int) (getWidth() - (getWidth() - mGameAreaWidth) / 2),
				getHeight());

		canvas.drawBitmap(BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND),
				0, 0, null);

		canvas.drawBitmap(mLaunchPad, null, mLaunchPadRect, mLaunchPadPaint);

		for (Pong pong : mPongs) {

			float x = pong.position.getX() - pong.getRadius();
			float y = pong.position.getY() - pong.getRadius();

			mDrawRect.set(x, y, x + pong.getWidth(), y + pong.getWidth());

			if (pong.PongType == Type.PENGUIN) {
				canvas.drawBitmap(mRed, null, mDrawRect, null);
			} else if (pong.PongType == Type.COW) {
				canvas.drawBitmap(mYellow, null, mDrawRect, null);
			}
		}

		if (mMotionEnabled) {
			float x = mLaunchPong.position.getX() - mLaunchPong.getRadius();
			float y = mLaunchPong.position.getY() - mLaunchPong.getRadius();

			mDrawRect.set(x, y, x + mLevelConfig.getPenguin().getWidth(), y
					+ mLevelConfig.getPenguin().getWidth());

			canvas.drawBitmap(mRed, null, mDrawRect, null);
		}

		if (!mGameOver) {
			String score = "Score: " + mTotalScore;
			String top = "Top Score: " + mTopScore;
			String livesLeft = "Left: " + mLives;

			Rect scoreRect = new Rect();
			Rect topRect = new Rect();
			Rect livesRect = new Rect();

			mTextPaint.getTextBounds(score, 0, score.length(), scoreRect);
			mTextPaint.getTextBounds(top, 0, top.length(), topRect);
			mTextPaint.getTextBounds(livesLeft, 0, livesLeft.length(),
					livesRect);

			canvas.drawText(score, 10 * mScale,
					10 * mScale + scoreRect.height(), mTextPaint);
			canvas.drawText(top, 10 * mScale, 10 * mScale + scoreRect.height()
					+ topRect.height(), mTopPaint);
			canvas.drawText(livesLeft, getWidth() - livesRect.width() - 10
					* mScale, 10 * mScale + livesRect.height(), mTextPaint);
		} else {
			String gameOver = "Game Over!";
			String score = "Final Score: " + mTotalScore;
			String tap = "Touch to restart";

			Rect gameOverRect = new Rect();
			Rect scoreRect = new Rect();
			Rect tapRect = new Rect();

			mTextPaint.getTextBounds(gameOver, 0, gameOver.length(),
					gameOverRect);
			mTextPaint.getTextBounds(score, 0, score.length(), scoreRect);
			mTextPaint.getTextBounds(tap, 0, tap.length(), tapRect);

			canvas.drawText(gameOver,
					getWidth() / 2 - gameOverRect.width() / 2, getHeight() / 2
							- gameOverRect.height(), mTextPaint);
			canvas.drawText(score, getWidth() / 2 - scoreRect.width() / 2,
					getHeight() / 2 + scoreRect.height(), mTextPaint);
			canvas.drawText(
					tap,
					getWidth() / 2 - tapRect.width() / 2,
					getHeight() / 2 + scoreRect.height() + tapRect.height() * 2,
					mTextPaint);
		}

		for (Points points : mPoints) {
			points.draw(canvas);
		}
	}

	private void pongCollision(Pong first, Pong second) {
		mRoundHits++;

		if (mVibrationEnabled) {
			// Vibrate for 30 milliseconds
			vibrator.vibrate(30);
		}

		if (first.PongType == Type.COW && second.PongType == Type.COW) {
			int value = mRoundHits * mConfig.getYellowYellowValue();
			mTotalScore += value;

			Vector2f position = second.position.sub(first.position);
			position.mulT(0.5f);
			position.addT(first.position);

			mPoints.add(new Points(value, position, Color.YELLOW, mScale));
		} else if ((first.PongType == Type.COW && second.PongType == Type.PENGUIN)
				|| (first.PongType == Type.PENGUIN && second.PongType == Type.COW)) {
			int value = mRoundHits * mConfig.getRedYellowValue();
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

		if (mTotalScore > mTopScore)
			mTopScore = mTotalScore;
	}

	private void resetLauncher() {
		mLives--;

		if (mLives < 0) {
			mGameOver = true;
			return;
		}

		mLaunchPong.PongType = Type.PENGUIN;

		mLaunchPong.position = new Vector2f(mLevelConfig.getPenguin()
				.getPosition());
		mLaunchPong.velocity = new Vector2f();
		mLaunchPong.setHeight(mLevelConfig.getPenguin().getHeight());
		mLaunchPong.setWidth(mLevelConfig.getPenguin().getWidth());
		mLaunchPong.setRadius(mLevelConfig.getPenguin().getRadius());

		mMotionEnabled = true;
		mHasLaunched = false;

		mRoundHits = 0;
	}

	/**
	 * Set up startpositions for the enemies.
	 */
	private void addInitialPong(Vector2f position, int width, int height) {
		Pong pong = new Pong();
		pong.PongType = Type.COW;
		pong.position = position;
		pong.velocity = new Vector2f();
		pong.setHeight(height);
		pong.setWidth(width);
		pong.setRadius(width * 0.5f);
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
}
