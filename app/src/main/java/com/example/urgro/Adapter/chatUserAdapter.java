package com.example.urgro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.messageActivity;
import com.example.urgro.model.usersInfo;

import java.util.List;

public class chatUserAdapter extends RecyclerView.Adapter<chatUserAdapter.viewHolder>{

    private Context context;
    private List<usersInfo> mUsers;

    public chatUserAdapter(Context context,List<usersInfo> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_display_chatuser,parent,false);
        return new chatUserAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final usersInfo user = mUsers.get(position);
        holder.mTvchatuser.setText(user.NAME);
        if(!user.IMAGEURL.equals("default")) {
            Glide.with(context).load(user.IMAGEURL).into(holder.mIvchatuser);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dmIntent = new Intent(context, messageActivity.class);
                dmIntent.putExtra("USERID", user.ID);
                context.startActivity(dmIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView mIvchatuser;
        private TextView mTvchatuser;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mIvchatuser = itemView.findViewById(R.id.iv_chatuser);
            mTvchatuser = itemView.findViewById(R.id.tv_charuser);
        }
    }
}
