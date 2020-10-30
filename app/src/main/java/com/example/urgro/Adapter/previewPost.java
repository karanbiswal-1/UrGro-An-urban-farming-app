package com.example.urgro.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.messageActivity;
import com.example.urgro.model.adPostInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class previewPost extends AppCompatActivity {

    private ImageView mBack;
    private  ImageView mPostImage;
    private TextView  mLocation;
    private  TextView mArea;
    private  TextView mRent;
    private TextView mDES;
    private Button mChat;
    private String  postID;
    private String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_post);
        mBack = findViewById(R.id.ic_back_normal);
        mPostImage = findViewById(R.id.iv_previewimg);
        mLocation = findViewById(R.id.tv_location_preview);
        mDES = findViewById(R.id.tv_des_preview);
        mArea = findViewById(R.id.tv_area_preview);
        mRent = findViewById(R.id.tv_rent_preview);
        mChat = findViewById(R.id.btn_chat_preview);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle data = getIntent().getExtras();
        postID = data.getString("POSTID");

        FirebaseDatabase.getInstance().getReference().child("adPosts").child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adPostInfo post = dataSnapshot.getValue(adPostInfo.class);
                Glide.with(previewPost.this).load(post.IMAGEURL).into(mPostImage);
                mLocation.setText(post.LOCATION);
                mRent.setText(post.RENTPRICE);
                mArea.setText(post.AREA);
                mDES.setText(post.DESCRIPTION);
                ownerID = post.OWNER;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ownerID)){
            mChat.setVisibility(View.INVISIBLE);
            mChat.setAlpha(0);
        }

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ownerID)) {
                    Intent chatIntent = new Intent(previewPost.this, messageActivity.class);
                    chatIntent.putExtra("USERID", ownerID);
                    startActivity(chatIntent);
                }else{
                    Toast.makeText(previewPost.this,"its your own AdPost",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}