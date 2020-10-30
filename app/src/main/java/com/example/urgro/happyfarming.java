package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class happyfarming extends AppCompatActivity {

    private Button mBtnrun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happyfarming);
        mBtnrun = findViewById(R.id.btn_getstarted);
        mBtnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(happyfarming.this,dashBoard.class));
                finish();
            }
        });
    }
}