package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.urgro.Adapter.chatAdapter;
import com.example.urgro.model.chatsInfo;
import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class messageActivity extends AppCompatActivity {

    private ImageView mProfIMG;
    private TextView mUsername;
    private EditText mTypemesage;
    private ImageView mSendmessage;
    private String userID;
    private RecyclerView mRcvtextmessage;
    private List<chatsInfo> mChats;
    private chatAdapter chatadapter;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mProfIMG = findViewById(R.id.iv_message_profimg);
        mUsername = findViewById(R.id.tv_message_username);
        mTypemesage = findViewById(R.id.et_type_message);
        mSendmessage = findViewById(R.id.iv_send_message);
        mRcvtextmessage = findViewById(R.id.rcv_textmessage);
        mChats = new ArrayList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRcvtextmessage.setLayoutManager(linearLayoutManager);

        Bundle data = getIntent().getExtras();
        userID = data.getString("USERID");

        FirebaseDatabase.getInstance().getReference().child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo user = dataSnapshot.getValue(usersInfo.class);
                Glide.with(messageActivity.this).load(user.IMAGEURL).into(mProfIMG);
                mUsername.setText(user.USERNAME);

                readMessage(fUser.getUid(),userID,user.IMAGEURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mTypemesage.getText().toString();
                if(message!= ""){
                    sendMessage(fUser.getUid(),userID,message);
                    mTypemesage.setText("");
                }else{
                    Toast.makeText(messageActivity.this,"U cant send empty message",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("SENDER",sender);
        map.put("RECEIVER",receiver);
        map.put("MESSAGE",message);

        FirebaseDatabase.getInstance().getReference().child("chats").push().setValue(map);
    }
    private void readMessage(final String myID, final String userID, final String imageURL){
        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    chatsInfo chat = snapshot.getValue(chatsInfo.class);
                    if(chat.SENDER.equals(myID) && chat.RECEIVER.equals(userID) || chat.SENDER.equals(userID) && chat.RECEIVER.equals(myID)){
                        mChats.add(chat);
                    }
                    chatadapter = new chatAdapter(messageActivity.this,mChats,imageURL);
                    mRcvtextmessage.setAdapter(chatadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}