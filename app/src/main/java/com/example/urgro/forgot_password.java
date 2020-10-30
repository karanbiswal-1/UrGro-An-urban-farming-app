package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    private EditText mEtfpEmail;
    private Button mBtnreset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEtfpEmail = findViewById(R.id.et_fp_email);
        mBtnreset = findViewById(R.id.btn_reset);
        firebaseAuth = FirebaseAuth.getInstance();

        mBtnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEtfpEmail.getText().toString();

                if(email.equals("")){
                    Toast.makeText(forgot_password.this,"please enter your email",Toast.LENGTH_LONG).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgot_password.this,"please check your email",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(forgot_password.this,loginActivity.class));
                                finish();
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(forgot_password.this,error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}