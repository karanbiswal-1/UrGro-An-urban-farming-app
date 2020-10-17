package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginActivity extends AppCompatActivity {

     private  Button mBtnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnregister = findViewById(R.id.btn_register);

    }

    public void onRegisterClicked(View view){
        startActivity(new Intent(loginActivity.this,registerActivity.class));
    }
}