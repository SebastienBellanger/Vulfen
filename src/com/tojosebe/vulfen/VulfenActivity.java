package com.tojosebe.vulfen;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
        addScreen(new StartScreen());
    }
}