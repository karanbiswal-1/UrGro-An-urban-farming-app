package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.urgro.Adapter.adPostAdapter;
import com.example.urgro.model.adPostInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    private RecyclerView mRcsearchMain;
    private List<adPostInfo> mAdpost;
    private adPostAdapter postAdapter;
    private EditText mEtsearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRcsearchMain = findViewById(R.id.rcv_search_main);
        mRcsearchMain.setLayoutManager(new LinearLayoutManager(searchActivity.this,RecyclerView.VERTICAL,false));

        mAdpost = new ArrayList<>();
        postAdapter = new adPostAdapter(searchActivity.this,mAdpost);
        mRcsearchMain.setAdapter(postAdapter);

        mEtsearchBar = findViewById(R.id.et_search_main);
        readPost();

        mEtsearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchPost(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void readPost() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("adPosts");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(mEtsearchBar.getText().toString())){
                    mAdpost.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        adPostInfo postInfo = snapshot.getValue(adPostInfo.class);
                        mAdpost.add(postInfo);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchPost(String s){
        Query query = FirebaseDatabase.getInstance().getReference().child("adPosts").orderByChild("LOCATION").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mAdpost.clear();
        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
        adPostInfo postInfo = snapshot.getValue(adPostInfo.class);
        mAdpost.add(postInfo);
        }
        postAdapter.notifyDataSetChanged();
        }

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });
        }
        }