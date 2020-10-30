package com.example.urgro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.model.adPostInfo;
import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class adPostAdapter extends RecyclerView.Adapter<adPostAdapter.viewHolder>{

    private Context context;
    private List<adPostInfo> mAdpost;


    private FirebaseUser firebaseUser;

    public adPostAdapter(Context context, List<adPostInfo> mAdpost) {
        this.context = context;
        this.mAdpost = mAdpost;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_item,parent,false);
        return new adPostAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final adPostInfo post = mAdpost.get(position);

        holder.mTvexploreLocation.setText(post.LOCATION);
        holder.mTvexplorePrice.setText("Rs "+post.RENTPRICE);
        Glide.with(context).load(post.IMAGEURL).into(holder.mIVexploreImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previewIntent = new Intent(context,previewPost.class);
                previewIntent.putExtra("POSTID",post.POSTID);
                context.startActivity(previewIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAdpost.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public ImageView mIVexploreImage;
        public TextView mTvexploreLocation;
        public TextView mTvexplorePrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mIVexploreImage = itemView.findViewById(R.id.iv_explore_img);
            mTvexploreLocation = itemView.findViewById(R.id.tv_explore_location);
            mTvexplorePrice = itemView.findViewById(R.id.tv_explore_price);
        }


    }
}
