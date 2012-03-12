package com.tojosebe.vulfen;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.tojosebe.vulfen.startscreen.StartScreen;
import com.vulfox.GameActivity;

public class VulfenActivity extends GameActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        super.onCreate(savedInstanceState);  
       
        mGameThread.setFixedTimeStep(false);
        
//        addScreen(new GameScreen());
        addScreen(new StartScreen(getDpi()));
    }

	private int getDpi() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH
		int dpiClassification = dm.densityDpi;
		return dpiClassification;
	}
}