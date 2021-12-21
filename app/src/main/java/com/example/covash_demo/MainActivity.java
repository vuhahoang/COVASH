package com.example.covash_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;

    Animation topAni , botAni;
    ImageView img;
    TextView name , dgn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAni = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAni = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img = (ImageView) findViewById(R.id.imageView);
        dgn = (TextView) findViewById(R.id.textView2);


        img.setAnimation(topAni);

        dgn.setAnimation(botAni);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,loginform.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);
    }


}