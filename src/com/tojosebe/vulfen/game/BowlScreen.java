package com.tojosebe.vulfen.game;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.VulfenActivity;
import com.tojosebe.vulfen.configuration.Level;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.tojosebe.vulfen.game.Pong.Type;
import com.tojosebe.vulfen.startscreen.VulfenDialog;
import com.tojosebe.vulfen.util.Constants;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class BowlScreen extends Screen {

	private float mScale;

	private Vibrator vibrator;

	private Bitmap mRed;
	private Bitmap mYellow;
	private Bitmap mLaunchPad;

	private Paint mTextPaint = new Paint();
	private Paint mTopPaint = new Paint();

	private List<Pong> mPongs = new Vector<Pong>();
	private List<BonusItem> mBonusItems = new Vector<BonusItem>();
	private Pong mLaunchPong = new Penguin();
	private boolean mHasLaunched = false;

	private boolean mMotionEnabled = true;
	private boolean mHasPong = false;
	private Vector2f mLastMotion = new Vector2f();
	private long mLastMotionTime = 0;

	private int bonusItemDpWidth = 32;

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

	private float mGameAreaAspectRatio = 1.333f;
	private float mLaunchPadHeight;

	private Level mLevelConfig;

	private boolean mBallsAreMoving;

	private BonusItemRandomizer mBonusItemRandomizer;

	Paint mGenericPaint = new Paint();
	RectF mLaunchPadRect = new RectF();
	Rect mItemRect = new Rect();
	
	private Activity mActivity;

	public BowlScreen(Level levelConfiguration, int dpi, Activity activity) {
		mConfig = levelConfiguration.getBowlConfiguration();
		mLevelConfig = levelConfiguration;
		mDpi = dpi;
		mActivity = activity;
	}
	
	@Override
	protected void onTop() {
//		mActivity.showDialog(VulfenActivity.DIALOG_SCORE_TO_BEAT); TODO: fixa till dialogen. gör en layout som inflatas.
	}

	private BonusItem getRandomBonusItem() {

		BonusItem item = new BonusItem();
		Bitmap bitmap = null;

		Random rand = new Random(System.currentTimeMillis());
		int randomNumber = rand.nextInt(2);

		if (randomNumber == 0) {
			bitmap = ImageLoader.loadFromResource(mContext, R.drawable.fish);
			item.setItemType(BonusItemType.GROWER);
		} else if (randomNumber == 1) {
			bitmap = ImageLoader.loadFromResource(mContext,
					R.drawable.fish_dead);
			item.setItemType(BonusItemType.SHRINKER);
		}

		float itemWidth = GraphicsUtil.dpToPixels(bonusItemDpWidth, mDpi);
		int xPosition = rand.nextInt(getWidth() - (int) itemWidth);
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

		mGameAreaHeight = (int) (getWidth() * mGameAreaAspectRatio);

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

		mTextPaint.setTextSize(32 * mScale);
		mTextPaint.setAntiAlias(true);
		mTopPaint.setTextSize(16 * mScale);
		mTopPaint.setAntiAlias(true);

		mLaunchPad = ImageLoader.loadFromResource(mContext,
				R.drawable.launcher4);
		mYellow = getPongBitmap(mLevelConfig.getEnemies().get(0));
		mRed = getPongBitmap(mLevelConfig.getPenguin());

		reset();

		createBackground();
		
	}
	
	public Dialog onCreateDialog(int id) {

		switch (id) {
		case VulfenActivity.DIALOG_SCORE_TO_BEAT:

			// CREATE DIALOG
			final VulfenDialog dialog = new VulfenDialog(mActivity,
					R.style.CustomDialogTheme);

			// INIT DIALOG BUTTONS
			Button yesButton = (Button) dialog
					.findViewById(R.id.button_positive);
			yesButton.setText(android.R.string.ok);
			dialog.setPositiveButton(yesButton);

			// SET DIALOG HEADER
			((TextView) dialog.findViewById(R.id.dialog_header_text))
					.setText(mActivity.getString(R.string.quit) + " " + mLevelConfig.getLevelNumber());

			// SET DIALOG CONTENT
//			((TextView) dialog.findViewById(R.id.dialog_content))
//					.setText(R.string.);

			// INIT DIALOG SIZES
			int h = (int) GraphicsUtil.dpToPixels(70, mDpi);
			int w = (int) GraphicsUtil.dpToPixels(120, mDpi);
			dialog.initDialog(mActivity, R.drawable.button, h, w);

			yesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			return dialog;
		}
		return null;
	}

	private Bitmap getPongBitmap(Pong pong) {
		int resource = pong.getImageResource();
		Bitmap bitmap = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), resource);
		bitmap = GraphicsUtil.resizeBitmap(bitmap, (int) pong.getHeight(),
				(int) pong.getWidth());
		if (pong.getType() == Type.COW) {
			switch (pong.getCurrentGroth()) {
			case -1:
				BitmapManager.addBitmap(Constants.BITMAP_COW_MINUS_1, bitmap); break;
			case 0:
				BitmapManager.addBitmap(Constants.BITMAP_COW_0, bitmap); break;
			case 1:
				BitmapManager.addBitmap(Constants.BITMAP_COW_1, bitmap); break;
			case 2:
				BitmapManager.addBitmap(Constants.BITMAP_COW_2, bitmap); break;
			}
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

	private void reset() {
		mLives = mConfig.getLives();
		mGameOver = false;

		mTotalScore = 0;

		mPoints.clear();
		mPongs.clear();

		for (Pong enemy : mLevelConfig.getEnemies()) {
			addInitialPong(new Vector2f(enemy.getPosition()),
					(int) enemy.getWidth(), (int) enemy.getHeight(), enemy.getImageResource());
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

						Pong redPong = new Penguin();
						redPong.position = new Vector2f(mLaunchPong.position);
						redPong.velocity = velocity;
						redPong.setHeight(mLevelConfig.getPenguin().getHeight());
						redPong.setWidth(mLevelConfig.getPenguin().getWidth());
						redPong.setRadius(mLevelConfig.getPenguin().getRadius());

						mPongs.add(redPong);

						mHasLaunched = true;
						mBallsAreMoving = true;

						mBonusItemRandomizer = new BonusItemRandomizer(1);
						mBonusItemRandomizer.start();
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
		stopMovement();
		return mScreenManager.removeScreen(this);
	}

	private void stopMovement() {
		if (mBonusItemRandomizer != null && mBallsAreMoving) {
			mBonusItemRandomizer.stopThread();
			mBonusItemRandomizer = null;
			mBallsAreMoving = false;
		}
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
					&& bonusItem.getPosition().getY() > getHeight()) {
				mBonusItems.remove(bonusItem);
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

			// for each pong. Check for collision with the bonusitems.
			for (int k = 0; k < mBonusItems.size(); k++) {
				BonusItem bonusItem = mBonusItems.get(k);
				Pong pong = mPongs.get(i);
				Vector2f collision = pong.position.sub(bonusItem.getPosition());
				float length = collision.getLength();
				if (length < pong.getRadius() + bonusItem.getRadius()) {
					// PICK UP ITEM!
					if (bonusItem.getItemType() == BonusItemType.GROWER && pong.getCurrentGroth() < pong.getMaxGroth()) {
						pong.setWidth(pong.getWidth() * 1.5f);
						pong.setRadius(pong.getRadius() * 1.5f);
						mBonusItems.remove(bonusItem);
						pong.setCurrentGroth(pong.getCurrentGroth()+1);
						break;
					} else if (bonusItem.getItemType() == BonusItemType.SHRINKER & pong.getCurrentGroth() > -pong.getMaxShrink()) {
						pong.setWidth(pong.getWidth() * 0.666666f);
						pong.setRadius(pong.getRadius() * 0.666666f);
						mBonusItems.remove(bonusItem);
						pong.setCurrentGroth(pong.getCurrentGroth()-1);
						break;
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

		if (moving == 0) {
			stopMovement();
		}
	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawBitmap(
				BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND_GAME), 0,
				0, null);

		for (BonusItem bonusItem : mBonusItems) {
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
				canvas.drawBitmap(mRed, null, mDrawRect, null);
			} else if (pong.getType() == Type.COW) {
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

		if (first.getType() == Type.COW && second.getType() == Type.COW) {
			int value = mRoundHits * mConfig.getYellowYellowValue();
			mTotalScore += value;

			Vector2f position = second.position.sub(first.position);
			position.mulT(0.5f);
			position.addT(first.position);

			mPoints.add(new Points(value, position, Color.YELLOW, mScale));
		} else if ((first.getType() == Type.COW && second.getType() == Type.PENGUIN)
				|| (first.getType() == Type.PENGUIN && second.getType() == Type.COW)) {
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

		mRoundHits = 0;

	}

	/**
	 * Set up startpositions for the enemies.
	 * @param i 
	 */
	private void addInitialPong(Vector2f position, int width, int height, int imageResource) {
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

	private class BonusItemRandomizer extends Thread {

		private boolean mRun = true;
		private int mItemsToRandomize = 1;
		private int mSuccesInt = 1;

		public BonusItemRandomizer(int itemsToRandomize) {
			mItemsToRandomize = itemsToRandomize;
		}

		public void run() {

			Random rand = new Random(System.currentTimeMillis());

			while (mRun) {
				try {
					Thread.sleep(1000);

					int randomInt = rand.nextInt(10);

					if (randomInt <= mSuccesInt) {
						if (mItemsToRandomize == 1) {
							mRun = false;
						}
						mItemsToRandomize--;
						mBonusItems.add(getRandomBonusItem());
					} else {
						mSuccesInt++;
					}

				} catch (InterruptedException e) {

				}
			}
		}

		public void stopThread() {
			mRun = false;
			this.interrupt();
		}

	}

}
