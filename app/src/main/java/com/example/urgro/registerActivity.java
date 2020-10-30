package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

   private  EditText mEtusername;
    private EditText mEtname;
    private EditText mEtemail;
    private EditText mEtpassword;
    private TextView mEtsignin;
    private LottieAnimationView animationView;

    private DatabaseReference mRootref;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtusername = findViewById(R.id.et_reg_username);
        mEtname = findViewById(R.id.et_reg_name);
        mEtemail = findViewById(R.id.et_reg_email);
        mEtpassword = findViewById(R.id.et_reg_password);
        mEtsignin = findViewById(R.id.tv_reg_signin);

        pd = new ProgressDialog(this);

        mEtsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this,loginActivity.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mRootref = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton mBtnreg = findViewById(R.id.btn_reg);

        mBtnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = mEtname.getText().toString();
                String txt_username = mEtusername.getText().toString();
                String txt_email = mEtemail.getText().toString();
                String txt_password = mEtpassword.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_username) ){
                    Toast.makeText(registerActivity.this,"credentials are empty",Toast.LENGTH_LONG).show();
                }else if(txt_password.length() < 6){
                    Toast.makeText(registerActivity.this,"password is too short",Toast.LENGTH_LONG).show();
                }else{
                    registerUser(txt_name,txt_username,txt_email,txt_password);
                }
            }
        });


    }

    private void registerUser(final String name, final String username, final String email, String password) {

       pd.setMessage("please wait");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("NAME",name);
                map.put("USERNAME",username);
                map.put("EMAIL",email);
                map.put("ID",mAuth.getCurrentUser().getUid());
                map.put("PHONE","");
                map.put("PROFESSION","");
                map.put("RESIDENCE","");
                map.put("IMAGEURL","default");

                mRootref.child("users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     pd.dismiss();
                        if(task.isSuccessful()){

                            //Toast.makeText(registerActivity.this,"update your profile",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(registerActivity.this,urbanShow.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}