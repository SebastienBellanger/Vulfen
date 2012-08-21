package com.tojosebe.vulfen.startscreen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.animation.AnimateableImageComponent;
import com.tojosebe.vulfen.dialog.DialogRegularString;
import com.tojosebe.vulfen.dialog.DialogScreen;
import com.tojosebe.vulfen.dialog.DialogString;
import com.tojosebe.vulfen.dialog.DialogString.TextSize;
import com.tojosebe.vulfen.game.StoryScreen;
import com.tojosebe.vulfen.util.Constants;
import com.tojosebe.vulfen.worldscreen.WorldScreen;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.BitmapManager;
import com.vulfox.util.GraphicsUtil;

public class StartScreen extends Screen {

	/** The dots per inch for the device. */
	private int mDpi;

	private Cloud mCloud1;
	private Cloud mCloud2;
	private Cow[] mCows;
	
	private float mScale;
	
	Paint mPaint = new Paint();
	private Rect mBackgroundRect;

	private boolean showExitDialog = false;
	private long mDialogShowStart = 0;
	
	private Activity mActivity;

	public StartScreen(int dpi, Activity activity) {
		this.mDpi = dpi;
		this.mActivity = activity;
	}
	
	@Override
	protected void onTop() {
		mDialogShowStart = 0;
		showExitDialog = false;
	}
	

	@Override
	protected void initialize() {

		mScale = getWidth() / 480.0f;
		
		mCloud1 = new Cloud(R.drawable.cloud1, mDpi,
				mContext.getApplicationContext(), 200, 70, getWidth(),
				Constants.BITMAP_CLOUD_1);
		mCloud2 = new Cloud(R.drawable.cloud2, mDpi,
				mContext.getApplicationContext(), 60, 130, getWidth(),
				Constants.BITMAP_CLOUD_2);
		mCows = new Cow[5];
		
		mPaint.setAntiAlias(true);

		createBackground();
		mBackgroundRect = new Rect();
		mBackgroundRect.set(0, 0, getWidth(), (int)(800*mScale));
		
		addScreenComponent(mCloud1.getImageComponent());
		addScreenComponent(mCloud2.getImageComponent());
		addCows();
		addScreenComponent(createPenguin());
		addScreenComponent(createTitle());
		addScreenComponent(createPlayButton());

	}

	private void addCows() {

		mCows[0] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 38, 145, 592, getWidth(),
				getHeight());
		addScreenComponent(mCows[0].getImageComponent());

		mCows[1] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 40, 100, 614, getWidth(),
				getHeight());
		addScreenComponent(mCows[1].getImageComponent());

		mCows[2] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 40, 200, 610, getWidth(),
				getHeight());
		addScreenComponent(mCows[2].getImageComponent());

		mCows[3] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 35, 35, 570, getWidth(),
				getHeight());
		addScreenComponent(mCows[3].getImageComponent());

		mCows[4] = new Cow(R.drawable.cow_small, mScale,
				mContext.getApplicationContext(), 44, 60, 660, getWidth(),
				getHeight());
		addScreenComponent(mCows[4].getImageComponent());

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

		imageComp.setWidthInDpAutoSetHeight(160, mDpi);
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

	private ScreenComponent createTitle() {
		Bitmap title = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.title);
		ImageComponent imageComp = new ImageComponent(title, false);

		imageComp.setWidthInDpAutoSetHeight(250, mDpi);
		imageComp.setPositionX((getWidth() - imageComp.getWidth()) / 2);
		imageComp.setPositionYInDp(30, mDpi);

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		imageComp.resizeBitmap();

		return imageComp;
	}

	@Override
	public void update(float timeStep) {
		mCloud1.update(timeStep);
		mCloud2.update(timeStep);
		for (Cow cow : mCows) {
			cow.update(timeStep);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		
		mPaint.setColor(0xFF0c4717);
		canvas.drawRect(0, (int)(800*mScale), getWidth(), getHeight(), mPaint);
		
		canvas.drawBitmap(BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND),
				null, mBackgroundRect, mPaint);

		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		for (Cow cow : mCows) {
			cow.draw(canvas);
		}
		
		if (showExitDialog && mDialogShowStart == 0) {
			mDialogShowStart = System.currentTimeMillis();
			addExitDialog(canvas);
		}

	}

	public static boolean a = false;
	
	private ScreenComponent createPlayButton() {

		StretchableImageButtonComponent button = new StretchableImageButtonComponent(
				mContext.getApplicationContext(), R.drawable.button, "Play",
				0xFFFFFFFF, 0x44000000, 30, 150, 50, mDpi);

		button.setEventListener(new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				if (a) {
					mScreenManager.addScreenUI(new WorldScreen(mDpi, mCloud1,
							mCloud2, mActivity));
					a = false;
				} else {
					mScreenManager.addScreenUI(new StoryScreen(mDpi, mCloud1, mCloud2));
					a = true;
				}
				return true;
			}
		});

		button.setPositionX(getWidth() / 2 - button.getWidth() / 2);
		button.setPositionY(getHeight() / 2 - button.getHeight() / 2);

		return button;
	}

	private void createBackground() {
		if (BitmapManager.getBitmap(Constants.BITMAP_BACKGROUND) == null) {
			Bitmap background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), R.drawable.background);
			background = GraphicsUtil.resizeBitmap(background, (int)(800*mScale), getWidth());
			BitmapManager.addBitmap(Constants.BITMAP_BACKGROUND,
					background);
		}
	}

	private ImageComponent getImageComponent(int imageConstant, int resource) {
		Bitmap background = BitmapManager.getBitmap(imageConstant);

		ImageComponent imageComp = null;

		if (background != null) {
			imageComp = new ImageComponent(background, false);
		} else {
			background = ImageLoader.loadFromResource(
					mContext.getApplicationContext(), resource);
			imageComp = new ImageComponent(background, false);
			imageComp.setHeight(getHeight());
			imageComp.setWidth(getWidth());
			imageComp.resizeBitmap();
			BitmapManager.addBitmap(imageConstant, imageComp.getBitmap());
		}
		return imageComp;
	}

	@Override
	protected boolean handleBackPressed() {
		showExitDialog = true;
		return true;
	}

	private void addExitDialog(Canvas canvas) {

		EventListener exitListener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mActivity.finish();
				return true;
			}
		};
		EventListener cancelListener = new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mScreenManager.removeTopScreen();
				showExitDialog = false;
				mDialogShowStart = 0;
				return true;
			}
		};

		int numButtons = 2;
		DialogString[] dialogRows = new DialogString[1];

		dialogRows[0] = new DialogRegularString("EXIT GAME?",
				TextSize.MEDIUM, 0xFFffffff, new int[]{(int)(20*mScale),0,(int)(20*mScale),0});

		ImageComponent[] buttons = new ImageComponent[numButtons];

		Bitmap bitmapYes = ImageLoader.loadFromResource(mContext,
				R.drawable.button_yes);
		Bitmap bitmapNo = ImageLoader.loadFromResource(mContext,
				R.drawable.button_no);

		ImageComponent okButton = new ImageComponent(bitmapYes, true);
		okButton.setWidth((int) (70 * mScale));
		okButton.setHeight((int) (70 * mScale));
		okButton.setEventListener(exitListener);
		okButton.resizeBitmap();
		buttons[0] = okButton;
		ImageComponent cancelButton = new ImageComponent(bitmapNo,
				true);
		cancelButton.setWidth((int) (70 * mScale));
		cancelButton.setHeight((int) (70 * mScale));
		cancelButton.setEventListener(cancelListener);
		cancelButton.resizeBitmap();
		buttons[1] = cancelButton;

		DialogScreen exitDialog = new DialogScreen(getWidth(), getHeight(),
				mScale, mDialogShowStart, dialogRows, buttons, null,
				true);

		mScreenManager.addScreenUI(exitDialog);
	}

}
