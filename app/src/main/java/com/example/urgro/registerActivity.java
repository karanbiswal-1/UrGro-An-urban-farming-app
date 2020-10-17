package com.example.urgro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {

    private EditText mEtname;
    private EditText mEtemail;
    private EditText mEtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtname = findViewById(R.id.et_name);
        mEtemail = findViewById(R.id.et_email);
        mEtpassword = findViewById(R.id.et_password);

        Button mBtnreg = findViewById(R.id.btn_reg);
        mBtnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = mEtemail.getText().toString();
                String txt_password = mEtpassword.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(registerActivity.this,"credentials are empty",Toast.LENGTH_LONG);
                }else if(txt_password.length() < 6){
                    Toast.makeText(registerActivity.this,"password is  too short",Toast.LENGTH_LONG);
                }else{
                    registerUser(txt_email,txt_password);
                }
            }
        });


    }

    private void registerUser(String email, String password) {

    }
}