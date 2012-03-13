package com.tojosebe.vulfen.startscreen;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tojosebe.vulfen.GameScreen;
import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.WorldScreen;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ButtonComponent;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;
import com.vulfox.component.TextButtonComponent;

public class StartScreen extends Screen {

	/** The dots per inch for the device. */
	private int dpi;

	private Cloud cloud1;
	private Cloud cloud2;
	private Cow[] cows;


	public StartScreen(int dpi) {
		this.dpi = dpi;
	}

	@Override
	protected void initialize() {

		cloud1 = new Cloud(R.drawable.cloud1, dpi, mContext, 200, 70, mWidth);
		cloud2 = new Cloud(R.drawable.cloud2, dpi, mContext, 60, 130, mWidth);
		cows = new Cow[5];

		addScreenComponent(createBackground());
		addScreenComponent(createPlayButton());
		addScreenComponent(cloud1.getImageComponent());
		addScreenComponent(cloud2.getImageComponent());
		addCows();
		addScreenComponent(createPenguin());
		addScreenComponent(createTitle());
	}

	private void addCows() {

		cows[0] = new Cow(R.drawable.cow_small, dpi, mContext, 23, 0, 0, mWidth,
				mHeight);
		addScreenComponent(cows[0].getImageComponent());

		cows[1] = new Cow(R.drawable.cow_small, dpi, mContext, 25, -30, 14, mWidth,
				mHeight);
		addScreenComponent(cows[1].getImageComponent());

		cows[2] = new Cow(R.drawable.cow_small, dpi, mContext, 25, 35, 10, mWidth,
				mHeight);
		addScreenComponent(cows[2].getImageComponent());

		cows[3] = new Cow(R.drawable.cow_small, dpi, mContext, 29, -60, 40, mWidth,
				mHeight);
		addScreenComponent(cows[3].getImageComponent());

		cows[4] = new Cow(R.drawable.cow_small, dpi, mContext, 20, -65, -10, mWidth,
				mHeight);
		addScreenComponent(cows[4].getImageComponent());

	}

	private ScreenComponent createPenguin() {
		Bitmap title = ImageLoader.loadFromResource(mContext,
				R.drawable.penguin);
		ImageComponent imageComp = new ImageComponent(title);

		imageComp.setWidthInDpAutoSetHeight(180, dpi);
		imageComp.setPositionX(mWidth / 2);
		imageComp.setPositionY(mHeight - imageComp.getHeight());

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		imageComp.resizeBitmap();

		return imageComp;
	}

	private ScreenComponent createTitle() {
		Bitmap title = ImageLoader.loadFromResource(mContext, R.drawable.title);
		ImageComponent imageComp = new ImageComponent(title);

		imageComp.setWidthInDpAutoSetHeight(250, dpi);
		imageComp.setPositionX((mWidth - imageComp.getWidth()) / 2);
		imageComp.setPositionYInDp(40, dpi);

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		imageComp.resizeBitmap();

		return imageComp;
	}

	@Override
	public void update(float timeStep) {
		cloud1.update(timeStep);
		cloud2.update(timeStep);
		for (Cow cow : cows) {
			cow.update(timeStep);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		cloud1.draw(canvas);
		cloud2.draw(canvas);
		for (Cow cow : cows) {
			cow.draw(canvas);
		}
	}

	private ScreenComponent createPlayButton() {
		ButtonComponent button = new TextButtonComponent("Play", 0xFFFF7733,
				20, 0xFF221144, 0xFF226644, 250, 70, (mWidth - 250) / 2,
				(mHeight - 70) / 2) {
			@Override
			public void buttonClicked() {
				mScreenManager.addScreen(new WorldScreen());
			}
		};
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
