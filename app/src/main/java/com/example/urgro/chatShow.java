package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class chatShow extends AppCompatActivity {

    private FloatingActionButton mBtnrun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_show);
        mBtnrun = findViewById(R.id.btn_run_chat);

        mBtnrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(chatShow.this,exploreshow.class));
                finish();
            }
        });
    }
}