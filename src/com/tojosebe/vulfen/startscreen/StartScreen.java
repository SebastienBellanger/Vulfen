package com.tojosebe.vulfen.startscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.VulfenActivity;
import com.tojosebe.vulfen.component.animation.AnimateableImageComponent;
import com.tojosebe.vulfen.worldscreen.WorldScreen;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;
import com.vulfox.util.GraphicsUtil;

public class StartScreen extends Screen {

	/** The dots per inch for the device. */
	private int mDpi;

	private Cloud mCloud1;
	private Cloud mCloud2;
	private Cow[] mCows;

	private int mOrientation;
	
	private Activity mActivity;

	public StartScreen(int dpi, Activity activity) {
		this.mDpi = dpi;
		this.mActivity = activity;
	}

	@Override
	protected void initialize() {

		mCloud1 = new Cloud(R.drawable.cloud1, mDpi,
				mContext.getApplicationContext(), 200, 70, mWidth);
		mCloud2 = new Cloud(R.drawable.cloud2, mDpi,
				mContext.getApplicationContext(), 60, 130, mWidth);
		mCows = new Cow[5];

		addScreenComponent(createBackground());
		addScreenComponent(mCloud1.getImageComponent());
		addScreenComponent(mCloud2.getImageComponent());
		addCows();
		addScreenComponent(createPenguin());
		addScreenComponent(createTitle());
		addScreenComponent(createPlayButton());

	}

	private void addCows() {

		mCows[0] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 23, 0, 0, mWidth, mHeight);
		addScreenComponent(mCows[0].getImageComponent());

		mCows[1] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 25, -30, 14, mWidth, mHeight);
		addScreenComponent(mCows[1].getImageComponent());

		mCows[2] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 25, 35, 10, mWidth, mHeight);
		addScreenComponent(mCows[2].getImageComponent());

		mCows[3] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 20, -65, -10, mWidth,
				mHeight);
		addScreenComponent(mCows[3].getImageComponent());

		mCows[4] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 29, -60, 40, mWidth, mHeight);
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
		imageComp.setPositionX(mWidth - imageComp.getWidth());
		imageComp.setPositionY(mHeight - imageComp.getHeight());

		// Resize the loaded bitmaps with nice algorithms so that they looks
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

	private ScreenComponent createTitle() {
		Bitmap title = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.title);
		ImageComponent imageComp = new ImageComponent(title);

		imageComp.setWidthInDpAutoSetHeight(250, mDpi);
		imageComp.setPositionX((mWidth - imageComp.getWidth()) / 2);
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
		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		for (Cow cow : mCows) {
			cow.draw(canvas);
		}
	}

	private ScreenComponent createPlayButton() {

		StretchableImageButtonComponent button = new StretchableImageButtonComponent(
				mContext.getApplicationContext(), R.drawable.button,
				R.drawable.button_pressed, "Play", 0xFFFFFFFF, 0x44000000, 30,
				150, 50, mDpi);

		button.setEventListener(new EventListener() {
			@Override
			public void handleButtonClicked() {
				mScreenManager.addScreen(new WorldScreen());
			}
		});

		button.setPositionX(mWidth / 2 - button.getWidth() / 2);

		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			button.setPositionY(mHeight / 2 - button.getHeight() / 2);
		} else {
			button.setPositionY(mHeight / 2 - button.getHeight() / 2);
		}

		return button;
	}

	private ImageComponent createBackground() {
		Bitmap background = ImageLoader.loadFromResource(
				mContext.getApplicationContext(), R.drawable.background);
		ImageComponent imageComp = new ImageComponent(background);
		imageComp.setHeight(mHeight);
		imageComp.setWidth(mWidth);
		imageComp.resizeBitmap();
		return imageComp;
	}

	@Override
	protected boolean handleBackPressed() {

		mActivity.showDialog(VulfenActivity.DIALOG_REALLY_EXIT);

		return true;
	}

	public Dialog onCreateDialog(int id) {

		switch (id) {
		case VulfenActivity.DIALOG_REALLY_EXIT:

			//CREATE DIALOG
			final VulfenDialog dialog = new VulfenDialog(mActivity, R.style.CustomDialogTheme);
			
			//INIT DIALOG BUTTONS
			Button noButton = (Button) dialog.findViewById(R.id.button_negative);
			Button yesButton = (Button) dialog.findViewById(R.id.button_positive);
			noButton.setText(R.string.no);
			yesButton.setText(R.string.yes);
			dialog.setPositiveButton(yesButton);
			dialog.setNegativeButton(noButton);
			
			//SET DIALOG HEADER
			((TextView)dialog.findViewById(R.id.dialog_header_text)).setText(R.string.quit);
			
			//SET DIALOG CONTENT
			((TextView)dialog.findViewById(R.id.dialog_content)).setText(R.string.really_quit);

			//INIT DIALOG SIZES
			int h = (int)GraphicsUtil.dpToPixels(70, mDpi);
			int w = (int)GraphicsUtil.dpToPixels(120, mDpi);
			dialog.initDialog(mActivity, R.drawable.button, h, w);
			
			noButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			yesButton.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					mActivity.finish();
				}
			});
			
			return dialog;
		}
		return null;
	}


}
