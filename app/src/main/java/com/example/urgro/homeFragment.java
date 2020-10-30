package com.example.urgro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgro.Adapter.homeAdapter;
import com.example.urgro.model.adPostInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {

    private RecyclerView mRchomeView;
    private List<adPostInfo> postInfos;
    private homeAdapter homeadapter;
    private FirebaseUser fUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRchomeView = view.findViewById(R.id.rcv_home);
        mRchomeView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));

        postInfos = new ArrayList<>();
        homeadapter = new homeAdapter(getActivity(),postInfos);
        mRchomeView.setAdapter(homeadapter);
        readPost();

    }

    private void readPost() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("adPosts");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    postInfos.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        adPostInfo postInfo = snapshot.getValue(adPostInfo.class);
                        postInfos.add(postInfo);
                    }
                    homeadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
