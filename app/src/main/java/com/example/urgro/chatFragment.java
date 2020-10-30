package com.example.urgro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urgro.Adapter.chatUserAdapter;
import com.example.urgro.model.chatsInfo;
import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chatFragment extends Fragment {

   private RecyclerView mRcvchatusers;
   private chatUserAdapter chatUseradapter;
   private List<usersInfo> mUsers;
   private FirebaseUser fUser;
   private List<String> userList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvchatusers = view.findViewById(R.id.rcv_chatuserlist);
        mRcvchatusers.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    chatsInfo chat = snapshot.getValue(chatsInfo.class);
                    if(chat.SENDER.equals(fUser.getUid())){
                        userList.add(chat.RECEIVER);
                    }
                    if(chat.RECEIVER.equals(fUser.getUid())){
                        userList.add(chat.SENDER);
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    usersInfo user = snapshot.getValue(usersInfo.class);
                    int flag =0;
                    for(String id:userList){
                        if(user.ID.equals(id)){
                            if(mUsers.size()!= 0){
                                for(usersInfo user1:mUsers){
                                    if(user.ID.equals(user1.ID)){
                                        flag = 1;
                                    }
                                }
                                if(flag==0){
                                    mUsers.add(user);
                                }
                            }else{
                                mUsers.add(user);
                            }
                        }
                    }
                }
                chatUseradapter = new chatUserAdapter(getContext(),mUsers);
                mRcvchatusers.setAdapter(chatUseradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
