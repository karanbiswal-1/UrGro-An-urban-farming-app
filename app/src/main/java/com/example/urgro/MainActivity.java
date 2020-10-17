package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topAnim,butAnim;
    TextView appname1,appname2,tagline;
    ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        butAnim = AnimationUtils.loadAnimation(this,R.anim.buttom_animation);
        topAnim.setDuration(1200);
        butAnim.setDuration(1200);

        appname1 = findViewById(R.id.splash_title);
        appname2 = findViewById(R.id.splashGro);
        tagline = findViewById(R.id.splash_tagline);
        splashImg = findViewById(R.id.splashImg);

        splashImg.setAnimation(butAnim);
        appname1.setAnimation(topAnim);
        appname2.setAnimation(topAnim);
        tagline.setAnimation(topAnim);

    }
}