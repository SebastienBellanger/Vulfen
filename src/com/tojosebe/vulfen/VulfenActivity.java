package com.tojosebe.vulfen;

import com.vulfox.GameActivity;

import android.app.Activity;
import android.os.Bundle;

public class VulfenActivity extends GameActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        addScreen(new GameScreen());
    }
}