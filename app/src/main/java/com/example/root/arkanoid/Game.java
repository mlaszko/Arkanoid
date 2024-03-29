package com.example.root.arkanoid;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import android.view.Window;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;

public class Game extends AppCompatActivity {

    ArkanoidView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Log.i("x", Integer.toString(size.x));
        Log.i("y", Integer.toString(size.y));

        view = new ArkanoidView(this, size.x, size.y);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }
}
