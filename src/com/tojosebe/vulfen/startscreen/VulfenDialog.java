package com.tojosebe.vulfen.startscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.tojosebe.vulfen.R;
import com.vulfox.ImageLoader;
import com.vulfox.util.GraphicsUtil;

public class VulfenDialog extends Dialog {

	private Button positiveButton;
	private Button negativeButton;
	private Button neutralButton; 
	private Drawable positiveButtonDrawable;
	private Drawable negativeButtonDrawable;
	private Drawable neutralButtonDrawable;
	
	public VulfenDialog(Context context, int theme) {
		super(context, theme);
		
		setContentView(R.layout.dialog);
		
	}
	
	public void setPositiveButton(Button positiveButton) {
		this.positiveButton = positiveButton;
	}
	
	public void setNegativeButton(Button negativeButton) {
		this.negativeButton = negativeButton;
	}
	
	public void setNeutralButton(Button neutralButton) {
		this.neutralButton = neutralButton;
	}

	/**
	 * Call this after buttons have been set.
	 * @param activity
	 * @param buttonDrawableResource
	 * @param h
	 * @param w
	 */
	public void initDialog(Activity activity, int buttonDrawableResource, int h, int w) {
		
		Bitmap background = ImageLoader.loadFromResource(activity, buttonDrawableResource);
		background = GraphicsUtil.resizeBitmap9PatchStyle(background, h, w);
		
		boolean doCopy = false;
		
		if (positiveButton != null) {
			positiveButtonDrawable = new BitmapDrawable(background);
			positiveButton.setBackgroundDrawable(positiveButtonDrawable);
			doCopy = true;
		}
		
		if (negativeButton != null) {
			if (doCopy) {
				Bitmap backgroundCopy = background.copy(background.getConfig(), background.isMutable() ? true : false);
				negativeButtonDrawable = new BitmapDrawable(backgroundCopy);
			} else {
				negativeButtonDrawable = new BitmapDrawable(background);
				doCopy = true;
			}
			negativeButton.setBackgroundDrawable(negativeButtonDrawable);
		}
		
		//TODO: Neutral button not supported today
		if (neutralButton != null) {
			if (doCopy) {
				Bitmap backgroundCopy = background.copy(background.getConfig(), background.isMutable() ? true : false);
				neutralButtonDrawable = new BitmapDrawable(backgroundCopy);
			} else {
				negativeButtonDrawable = new BitmapDrawable(background);
			}
			neutralButton.setBackgroundDrawable(neutralButtonDrawable);
		}
		
		if (positiveButton != null) {
			positiveButton.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						positiveButtonDrawable.setColorFilter(0x88FFFFFF, PorterDuff.Mode.MULTIPLY);
						v.setBackgroundDrawable(positiveButtonDrawable);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						positiveButtonDrawable.clearColorFilter();
						v.setBackgroundDrawable(positiveButtonDrawable);
					}
					return false;
				}
			});
		}
		
		if (negativeButton != null) {
			negativeButton.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						negativeButtonDrawable.setColorFilter(0x88FFFFFF, PorterDuff.Mode.MULTIPLY);
						v.setBackgroundDrawable(negativeButtonDrawable);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						negativeButtonDrawable.clearColorFilter();
						v.setBackgroundDrawable(negativeButtonDrawable);
					}
					return false;
				}
			});
		}
	}


	
}
