package com.example.urgro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView mTvusername = view.findViewById(R.id.tv_profile_name);
        final TextView mTvResidence = view.findViewById(R.id.tv_prof_des);
        final ImageView mIvprofPic = view.findViewById(R.id.iv_profile_Pic);
        RelativeLayout mRlaccount = view.findViewById(R.id.rl_account);
        RelativeLayout mRladposts = view.findViewById(R.id.rl_myads);
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
     ImageView mIveditProfile = view.findViewById(R.id.iv_edit_profile);



        mIveditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),editProfileActivity.class));
            }
        });

        mRlaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),accountActivity.class));
            }
        });

        mRladposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),myPostActivity.class));
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo user = dataSnapshot.getValue(usersInfo.class);
                if(!user.IMAGEURL.equals("default")) {
                    Glide.with(getActivity()).load(user.IMAGEURL).into(mIvprofPic);
                }
                mTvusername.setText(user.NAME);
                mTvResidence.setText(user.RESIDENCE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
