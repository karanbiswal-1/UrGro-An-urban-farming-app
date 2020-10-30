package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Animation topAnim,butAnim;
   ImageView appTitle;
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

       appTitle = findViewById(R.id.iv_apptitle);
        splashImg = findViewById(R.id.splashImg);

        splashImg.setAnimation(butAnim);
      appTitle.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            //    if(FirebaseAuth.getInstance().getCurrentUser() != null){
              //      startActivity(new Intent(MainActivity.this,dashBoard.class));
                //    finish();
                //}else {
                    startActivity(new Intent(MainActivity.this, loginActivity.class));
                    finish();
                //}
            }
        },3000);

    }
}