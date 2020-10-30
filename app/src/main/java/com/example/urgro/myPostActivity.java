package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.urgro.Adapter.myPostAdapter;
import com.example.urgro.model.adPostInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myPostActivity extends AppCompatActivity {

    private RecyclerView mRcmyPost;
    private FirebaseUser fUser;
    private myPostAdapter myPostadapter;
    private List<adPostInfo> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mRcmyPost = findViewById(R.id.rcv_mypost);
        mPosts = new ArrayList<>();
        mRcmyPost.setLayoutManager(new LinearLayoutManager(myPostActivity.this,RecyclerView.VERTICAL,false));

        myPostadapter = new myPostAdapter(myPostActivity.this,mPosts);
        mRcmyPost.setAdapter(myPostadapter);

        readPost();

    }

    private void readPost() {
        FirebaseDatabase.getInstance().getReference().child("adPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPosts.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    adPostInfo post = snapshot.getValue(adPostInfo.class);
                    if(post.OWNER.equals(fUser.getUid())){
                        mPosts.add(post);
                    }
                }
                myPostadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}