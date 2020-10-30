package com.example.urgro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

public class exploreFragment extends Fragment {

    private EditText mEtexplorebar;
    private ImageView mIvsearchimg;
    private List<adPostInfo> mAdpost;
    private adPostAdapter postAdapter;
    private RecyclerView mRcexplore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtexplorebar = view.findViewById(R.id.et_explore);
        mIvsearchimg = view.findViewById(R.id.iv_explore_search);
        mRcexplore = view.findViewById(R.id.rcv_exploremain);

        mRcexplore.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        mAdpost = new ArrayList<>();
        postAdapter = new adPostAdapter(getActivity(),mAdpost);
        mRcexplore.setAdapter(postAdapter);

        readPost();

        mEtexplorebar.addTextChangedListener(new TextWatcher() {
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

    private void searchPost(String s) {
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

    private void readPost() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("adPosts");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(mEtexplorebar.getText().toString())){
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
}
