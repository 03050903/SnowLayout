package com.licrafter.snowlayout.demo;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.licrafter.snowlayout.library.SnowLayout;

public class MainActivity extends AppCompatActivity {

    private Drawable[] mDrawables = new Drawable[6];
    private SnowLayout mSnowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSnowLayout = (SnowLayout) findViewById(R.id.snowLayout);
        mDrawables[0] = ContextCompat.getDrawable(this, R.mipmap.ic_snow1);
        mDrawables[1] = ContextCompat.getDrawable(this, R.mipmap.ic_snow2);
        mDrawables[2] = ContextCompat.getDrawable(this, R.mipmap.ic_snow3);
        mDrawables[3] = ContextCompat.getDrawable(this, R.mipmap.ic_snow4);
        mDrawables[4] = ContextCompat.getDrawable(this, R.mipmap.ic_snow5);
        mDrawables[5] = ContextCompat.getDrawable(this, R.mipmap.ic_snow6);
        mSnowLayout.setDrawables(mDrawables);
        mSnowLayout.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSnowLayout.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnowLayout.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSnowLayout.stop();
    }
}
