package com.tojosebe.vulfen.startscreen;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.VulfenActivity;
import com.tojosebe.vulfen.component.animation.AnimateableImageComponent;
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

	private Activity mActivity;

	public StartScreen(int dpi, Activity activity) {
		this.mDpi = dpi;
		this.mActivity = activity;
	}

	@Override
	protected void initialize() {

		mCloud1 = new Cloud(R.drawable.cloud1, mDpi,
				mContext.getApplicationContext(), 200, 70, getWidth(), Constants.BITMAP_CLOUD_1);
		mCloud2 = new Cloud(R.drawable.cloud2, mDpi,
				mContext.getApplicationContext(), 60, 130, getWidth(), Constants.BITMAP_CLOUD_2);
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
				mContext.getApplicationContext(), 23, 0, 0, getWidth(), getHeight());
		addScreenComponent(mCows[0].getImageComponent());

		mCows[1] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 25, -30, 14, getWidth(), getHeight());
		addScreenComponent(mCows[1].getImageComponent());

		mCows[2] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 25, 35, 10, getWidth(), getHeight());
		addScreenComponent(mCows[2].getImageComponent());

		mCows[3] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 20, -65, -10, getWidth(), getHeight());
		addScreenComponent(mCows[3].getImageComponent());

		mCows[4] = new Cow(R.drawable.cow_small, mDpi,
				mContext.getApplicationContext(), 29, -60, 40, getWidth(), getHeight());
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
		BitmapManager.addBitmap(Constants.BITMAP_PENGUIN,rezisedBitmaps[0]);
		BitmapManager.addBitmap(Constants.BITMAP_PENGUIN_BLINKING, rezisedBitmaps[1]);

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
		mCloud1.draw(canvas);
		mCloud2.draw(canvas);
		for (Cow cow : mCows) {
			cow.draw(canvas);
		}
	}

	private ScreenComponent createPlayButton() {

		StretchableImageButtonComponent button = new StretchableImageButtonComponent(
				mContext.getApplicationContext(), R.drawable.button,
				"Play", 0xFFFFFFFF, 0x44000000, 30,
				150, 50, mDpi);

		button.setEventListener(new EventListener() {
			@Override
			public boolean handleButtonClicked() {
				mScreenManager.addScreen(new WorldScreen(mDpi, mCloud1, mCloud2, mActivity));
				return true;
			}
		});

		button.setPositionX(getWidth() / 2 - button.getWidth() / 2);
		button.setPositionY(getHeight() / 2 - button.getHeight() / 2);

		return button;
	}

	private ImageComponent createBackground() {
		return getImageComponent(Constants.BITMAP_BACKGROUND,
				R.drawable.background);
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

		mActivity.showDialog(VulfenActivity.DIALOG_REALLY_EXIT);

		return true;
	}
	

	@Override
	protected Dialog onCreateDialog(int id, Dialog inDialog, Bundle args) {

		switch (id) {
		case VulfenActivity.DIALOG_REALLY_EXIT:

			final VulfenDialog dialog;
			
			if (inDialog == null) {
				dialog = new VulfenDialog(mActivity,
						R.style.CustomDialogTheme);
			} else {
				dialog = (VulfenDialog)inDialog;
			}

			// INIT DIALOG BUTTONS
			Button noButton = (Button) dialog
					.findViewById(R.id.button_negative);
			Button yesButton = (Button) dialog
					.findViewById(R.id.button_positive);
			noButton.setText(R.string.no);
			yesButton.setText(R.string.yes);
			dialog.setPositiveButton(yesButton);
			dialog.setNegativeButton(noButton);

			// SET DIALOG HEADER
			((TextView) dialog.findViewById(R.id.dialog_header_text))
					.setText(R.string.quit);

			// SET DIALOG CONTENT
			((TextView) dialog.findViewById(R.id.dialog_content))
					.setText(R.string.really_quit);
			((TextView) dialog.findViewById(R.id.dialog_content)).setVisibility(View.VISIBLE);

			if (inDialog == null) {
				// INIT DIALOG SIZES
				int h = (int) GraphicsUtil.dpToPixels(70, mDpi);
				int w = (int) GraphicsUtil.dpToPixels(120, mDpi);
				dialog.initDialog(mActivity, R.drawable.button, h, w);
			}

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
