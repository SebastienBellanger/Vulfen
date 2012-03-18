package com.tojosebe.vulfen.startscreen;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.WorldScreen;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.StretchableImageButtonComponent;
import com.vulfox.listener.EventListener;

public class StartScreen extends Screen {

	/** The dots per inch for the device. */
	private int mDpi;

	private Cloud mCloud1;
	private Cloud mCloud2;
	private Cow[] mCows;


	public StartScreen(int dpi) {
		this.mDpi = dpi;
	}

	@Override
	protected void initialize() {

		mCloud1 = new Cloud(R.drawable.cloud1, mDpi, mContext, 200, 70, mWidth);
		mCloud2 = new Cloud(R.drawable.cloud2, mDpi, mContext, 60, 130, mWidth);
		mCows = new Cow[5];

		addScreenComponent(createBackground());
		addScreenComponent(createPlayButton());
		addScreenComponent(mCloud1.getImageComponent());
		addScreenComponent(mCloud2.getImageComponent());
		addCows();
		addScreenComponent(createPenguin());
		addScreenComponent(createTitle());
	}

	private void addCows() {

		mCows[0] = new Cow(R.drawable.cow_small, mDpi, mContext, 23, 0, 0, mWidth,
				mHeight);
		addScreenComponent(mCows[0].getImageComponent());

		mCows[1] = new Cow(R.drawable.cow_small, mDpi, mContext, 25, -30, 14, mWidth,
				mHeight);
		addScreenComponent(mCows[1].getImageComponent());

		mCows[2] = new Cow(R.drawable.cow_small, mDpi, mContext, 25, 35, 10, mWidth,
				mHeight);
		addScreenComponent(mCows[2].getImageComponent());

		mCows[3] = new Cow(R.drawable.cow_small, mDpi, mContext, 20, -65, -10, mWidth,
				mHeight);
		addScreenComponent(mCows[3].getImageComponent());
		
		mCows[4] = new Cow(R.drawable.cow_small, mDpi, mContext, 29, -60, 40, mWidth,
				mHeight);
		addScreenComponent(mCows[4].getImageComponent());

	}

	private ScreenComponent createPenguin() {
		Bitmap title = ImageLoader.loadFromResource(mContext,
				R.drawable.penguin);
		ImageComponent imageComp = new ImageComponent(title);

		imageComp.setWidthInDpAutoSetHeight(180, mDpi);
		imageComp.setPositionX(mWidth / 2);
		imageComp.setPositionY(mHeight - imageComp.getHeight());

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		imageComp.resizeBitmap();

		return imageComp;
	}

	private ScreenComponent createTitle() {
		Bitmap title = ImageLoader.loadFromResource(mContext, R.drawable.title);
		ImageComponent imageComp = new ImageComponent(title);

		imageComp.setWidthInDpAutoSetHeight(250, mDpi);
		imageComp.setPositionX((mWidth - imageComp.getWidth()) / 2);
		imageComp.setPositionYInDp(40, mDpi);

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
		
		StretchableImageButtonComponent button = new StretchableImageButtonComponent(mContext,
				R.drawable.button, R.drawable.button_pressed, "Play", 0xFFFFFFFF, 0x44000000,30,150,50,mDpi);
		
		button.setEventListener(new EventListener() {
			@Override
			public void handleButtonClicked() {
				mScreenManager.addScreen(new WorldScreen());
			}
		});
		
		button.setPositionX(mWidth/2 - button.getWidth()/2);
		button.setPositionY(mHeight/2 - button.getHeight()/2);
		
		return button;
	}

	private ImageComponent createBackground() {
		Bitmap background = ImageLoader.loadFromResource(mContext,
				R.drawable.background);
		ImageComponent imageComp = new ImageComponent(background);
		imageComp.setHeight(mHeight);
		imageComp.setWidth(mWidth);
		return imageComp;
	}
}
