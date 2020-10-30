package com.example.urgro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.model.chatsInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.viewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<chatsInfo> mChats;
    private String imageUrl;
    private FirebaseUser fUser;

    public chatAdapter(Context context,List<chatsInfo> mChats,String imageUrl) {
        this.context = context;
        this.mChats = mChats;
        this.imageUrl = imageUrl;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_right,parent,false);
            return new chatAdapter.viewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.message_left,parent,false);
            return new chatAdapter.viewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        chatsInfo chat = mChats.get(position);
        holder.mText.setText(chat.MESSAGE);
        if(imageUrl != null) {
            Glide.with(context).load(imageUrl).into(holder.mImg);
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
           if(mChats.get(position).SENDER.equals(fUser.getUid())){
               return  MSG_TYPE_RIGHT;
           }else{
               return MSG_TYPE_LEFT;
           }
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView mText;
        public ImageView mImg;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.tv_message);
            mImg = itemView.findViewById(R.id.iv_message);
        }
    }
}
