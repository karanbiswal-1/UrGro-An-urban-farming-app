package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class exploreshow extends AppCompatActivity {

    private FloatingActionButton mBtnrun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploreshow);
        mBtnrun = findViewById(R.id.btn_run_explore);

        mBtnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(exploreshow.this,happyfarming.class));
                finish();
            }
        });
    }
}