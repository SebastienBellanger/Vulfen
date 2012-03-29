package com.tojosebe.vulfen.game;

import java.util.List;
import java.util.Vector;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.Pong.Type;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;

public class BowlScreen extends Screen {

	private float mScale;

	private float mRadius;

	private float mPongLength;

	private float mPongOffsetX;
	private float mPongOffsetY;

	private Bitmap mYellow;
	private Bitmap mRed;

	private Paint mBackgroundTop = new Paint();
	private Paint mBackgroundbottom = new Paint();

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

	private int mLives;
	private boolean mGameOver = false;

	private RectF mDrawRect = new RectF();

	private BowlConfiguration mConfig;

	public BowlScreen(BowlConfiguration configuration) {
		mConfig = configuration;
	}

	@Override
	protected void initialize() {
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
		mTopScore = mSavedTopScore = settings.getInt("mSavedTopScore", 0);
	

		mYellow = ImageLoader.loadFromResource(mContext.getApplicationContext(), R.drawable.sebe);
		mRed = ImageLoader.loadFromResource(mContext.getApplicationContext(), R.drawable.tojo);

		mScale = mWidth / 480.0f;
		mPongLength = 64 * mScale;
		mRadius = mPongLength * 0.5f;
		mPongOffsetX = mPongOffsetY = mPongLength * 0.5f;

		mBackgroundTop.setColor(Color.LTGRAY);
		mBackgroundbottom.setColor(Color.GRAY);

		mTextPaint.setTextSize(32 * mScale);
		mTopPaint.setTextSize(16 * mScale);

		reset();
	}

	private void reset() {
		mLives = mConfig.lives;
		mGameOver = false;

		mTotalScore = 0;

		mPoints.clear();
		mPongs.clear();

		float halfWidth = mWidth * 0.5f;
		float pongTop = mHeight * 0.2f;

		addInitialPong(new Vector2f(halfWidth - 2 * mPongLength, pongTop));
		addInitialPong(new Vector2f(halfWidth, pongTop));
		addInitialPong(new Vector2f(halfWidth + 2 * mPongLength, pongTop));

		addInitialPong(new Vector2f(halfWidth - mPongLength, pongTop + 2
				* mPongLength));
		addInitialPong(new Vector2f(halfWidth + mPongLength, pongTop + 2
				* mPongLength));

		addInitialPong(new Vector2f(halfWidth, pongTop + 4 * mPongLength));

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

				if (motionEvent.getY() > mHeight * 0.8f) {
					mHasPong = true;

					mLastMotion.set(x, y);
					mLastMotionTime = motionEvent.getEventTime();
					mLaunchPong.Position.set(x, y);
				}
			} else if (mHasPong
					&& motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				if (motionEvent.getY() > mHeight * 0.8f) {
					mHasPong = true;

					mLastMotion.set(x, y);
					mLastMotionTime = motionEvent.getEventTime();
					mLaunchPong.Position.set(x, y);
				} else {
					mHasPong = false;

					Vector2f velocity = new Vector2f(x, y);
					velocity.subT(mLastMotion);
					float length = velocity.getLength();
					velocity.normalizeT();

					float motionTime = (motionEvent.getEventTime() - mLastMotionTime) / 1000.0f;
					velocity.mulT(length / motionTime);

					if (velocity.getLength() > mConfig.maxSpeed * mScale)
						velocity.setLength(mConfig.maxSpeed * mScale);

					if (velocity.getLength() > 100.0f * mScale) {
						mMotionEnabled = false;

						Pong redPong = new Pong();
						redPong.PongType = Type.Red;
						redPong.Position = new Vector2f(mLaunchPong.Position);
						redPong.Velocity = velocity;

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

		for (Pong pong : mPongs) {

			if (pong.Velocity.getLength() == 0.0f)
				continue;

			moving++;

			Vector2f delta = pong.Velocity.mul(timeStep);
			pong.Position.addT(delta);

			if (pong.Position.getX() < mPongOffsetX) {
				pong.Position.setX(2.0f * mPongOffsetX - pong.Position.getX());
				pong.Velocity.setX(-pong.Velocity.getX());
			} else if (pong.Position.getX() > mWidth - mPongOffsetX) {
				pong.Position.setX(2.0f * (mWidth - mPongOffsetX)
						- pong.Position.getX());
				pong.Velocity.setX(-pong.Velocity.getX());
			}

			if (pong.Position.getY() < mPongOffsetY) {
				pong.Position.setY(2.0f * mPongOffsetY - pong.Position.getY());
				pong.Velocity.setY(-pong.Velocity.getY());
			} else if (pong.Position.getY() > mHeight - mPongOffsetY) {
				pong.Position.setY(2.0f * (mHeight - mPongOffsetY)
						- pong.Position.getY());
				pong.Velocity.setY(-pong.Velocity.getY());
			}

			Vector2f friction = pong.Velocity.inv();
			friction.normalizeT();
			friction.mulT(timeStep * mConfig.friction * mScale);

			if (pong.Velocity.getLength() < friction.getLength()) {
				pong.Velocity.set(0.0f, 0.0f);
			} else {
				pong.Velocity.addT(friction);
			}
		}

		if (!mGameOver && mHasLaunched && moving == 0) {
			resetLauncher();

			if(mTopScore > mSavedTopScore)
			{
				saveTopScore();
			}
		}

		for (int i = 0; i < mPongs.size(); i++) {
			for (int j = i + 1; j < mPongs.size(); j++) {

				Pong first = mPongs.get(i);
				Pong second = mPongs.get(j);

				Vector2f collision = first.Position.sub(second.Position);
				float length = collision.getLength();

				if (length >= mRadius * 2.0f)
					continue;

				pongCollision(first, second);

				Vector2f mtd = collision
						.mul((mRadius * 2.0f - length) / length);

				first.Position.addT(mtd.mul(0.505f));
				second.Position.subT(mtd.mul(0.505f));

				collision.normalizeT();

				float aci = first.Velocity.dot(collision);
				float bci = second.Velocity.dot(collision);

				float acf = bci;
				float bcf = aci;

				first.Velocity.addT(collision.mul((acf - aci) * 0.90f));
				second.Velocity.addT(collision.mul((bcf - bci) * 0.90f));
			}
		}

		List<Points> pointsCopy = new Vector<Points>(mPoints);
		for (Points points : pointsCopy) {
			points.update(timeStep);
			if (points.isDone())
				mPoints.remove(points);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawRect(0, 0, mWidth, mHeight * 0.8f, mBackgroundTop);
		canvas.drawRect(0, mHeight * 0.8f, mWidth, mHeight, mBackgroundbottom);

		for (Pong pong : mPongs) {

			float x = pong.Position.getX() - mPongOffsetX;
			float y = pong.Position.getY() - mPongOffsetY;

			mDrawRect.set(x, y, x + mPongLength, y + mPongLength);

			if (pong.PongType == Type.Red) {
				canvas.drawBitmap(mRed, null, mDrawRect, null);
			} else if (pong.PongType == Type.Yellow) {
				canvas.drawBitmap(mYellow, null, mDrawRect, null);
			}
		}

		if (mMotionEnabled) {
			float x = mLaunchPong.Position.getX() - mPongOffsetX;
			float y = mLaunchPong.Position.getY() - mPongOffsetY;

			mDrawRect.set(x, y, x + mPongLength, y + mPongLength);

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
			canvas.drawText(livesLeft,
					mWidth - livesRect.width() - 10 * mScale, 10 * mScale
							+ livesRect.height(), mTextPaint);
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

			canvas.drawText(gameOver, mWidth / 2 - gameOverRect.width() / 2,
					mHeight / 2 - gameOverRect.height(), mTextPaint);
			canvas.drawText(score, mWidth / 2 - scoreRect.width() / 2, mHeight
					/ 2 + scoreRect.height(), mTextPaint);
			canvas.drawText(tap, mWidth / 2 - tapRect.width() / 2, mHeight / 2
					+ scoreRect.height() + tapRect.height() * 2, mTextPaint);
		}

		for (Points points : mPoints) {
			points.draw(canvas);
		}
	}

	private void pongCollision(Pong first, Pong second) {
		mRoundHits++;

		if (first.PongType == Type.Yellow && second.PongType == Type.Yellow) {
			int value = mRoundHits * mConfig.yellowYellowValue;
			mTotalScore += value;

			Vector2f position = second.Position.sub(first.Position);
			position.mulT(0.5f);
			position.addT(first.Position);

			mPoints.add(new Points(value, position, Color.YELLOW, mScale));
		} else if ((first.PongType == Type.Yellow && second.PongType == Type.Red)
				|| (first.PongType == Type.Red && second.PongType == Type.Yellow)) {
			int value = mRoundHits * mConfig.redYellowValue;
			mTotalScore += value;

			Vector2f position = second.Position.sub(first.Position);
			position.mulT(0.5f);
			position.addT(first.Position);

			mPoints.add(new Points(value, position, Color.rgb(255, 180, 0),
					mScale));
		} else {
			int value = mRoundHits * mConfig.redRedValue;
			mTotalScore += value;

			Vector2f position = second.Position.sub(first.Position);
			position.mulT(0.5f);
			position.addT(first.Position);

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

		mLaunchPong.PongType = Type.Red;
		mLaunchPong.Position = new Vector2f(mWidth * 0.5f, mHeight * 0.9f);
		mLaunchPong.Velocity = new Vector2f();

		mMotionEnabled = true;
		mHasLaunched = false;

		mRoundHits = 0;
	}

	private void addInitialPong(Vector2f position) {
		Pong pong = new Pong();
		pong.PongType = Type.Yellow;
		pong.Position = position;
		pong.Velocity = new Vector2f();
		mPongs.add(pong);
	}

	private void saveTopScore() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
		Editor editor = settings.edit();		
		editor.putInt("mSavedTopScore", mTopScore);
		editor.commit();		
		
		mSavedTopScore = mTopScore;
	}
}
