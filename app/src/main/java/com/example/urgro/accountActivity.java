package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class accountActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mName;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mProfession;
    private TextView mAddress;
    private ImageView mBack;
   private FirebaseUser fUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mUsername = findViewById(R.id.tv_account_username);
        mName = findViewById(R.id.tv_account_name);
        mEmail = findViewById(R.id.tv_account_email);
        mPhone = findViewById(R.id.tv_account_phone);
        mProfession = findViewById(R.id.tv_account_profession);
        mAddress = findViewById(R.id.tv_account_address);
        mBack = findViewById(R.id.btn_account_back);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo user = dataSnapshot.getValue(usersInfo.class);
                mUsername.setText(user.USERNAME);
                mName.setText(user.NAME);
                mEmail.setText(user.EMAIL);
                mPhone.setText(user.PHONE);
                mProfession.setText(user.PROFESSION);
                mAddress.setText(user.RESIDENCE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}