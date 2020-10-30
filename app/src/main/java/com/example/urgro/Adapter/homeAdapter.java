package com.example.urgro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.messageActivity;
import com.example.urgro.model.adPostInfo;
import com.example.urgro.model.usersInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class homeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
     private  int lastPosition = -1;

    private Context context;
    private List<adPostInfo> adposts;

    public homeAdapter(Context context,List<adPostInfo> adposts) {
        this.context = context;
        this.adposts = adposts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
         View view = LayoutInflater.from(context).inflate(R.layout.cell_home_header,parent,false);
         return new homeAdapter.headerViewHolder(view);
        }else if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.cell_home_item,parent,false);
            return new homeAdapter.viewHolder(view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  headerViewHolder){
            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersInfo user = dataSnapshot.getValue(usersInfo.class);
                    ((headerViewHolder) holder).mTvuserNameHeader.setText(user.NAME);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else if(holder instanceof viewHolder){
            final adPostInfo post = adposts.get(position-1);

            FirebaseDatabase.getInstance().getReference().child("users").child(post.OWNER).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersInfo user = dataSnapshot.getValue(usersInfo.class);
                    ((viewHolder) holder).mTvprofileUsername.setText(user.USERNAME);
                    if(!user.IMAGEURL.equals("default")) {
                        Glide.with(context).load(user.IMAGEURL).into(((viewHolder) holder).mIvprofileimg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ((viewHolder) holder).mTvpostLocation.setText(post.LOCATION);
            ((viewHolder) holder).priceTag.setText("RS "+post.RENTPRICE);
            ((viewHolder) holder).mArea.setText(post.AREA+" sqm");
            Glide.with(context).load(post.IMAGEURL).into(((viewHolder) holder).mIvpostImage);
            final String userid = post.OWNER;
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userid)){
                ((viewHolder) holder).mDirect.setVisibility(View.GONE);
            }

            ((viewHolder) holder).mDirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userid)) {
                        Intent dmIntent = new Intent(context, messageActivity.class);
                        dmIntent.putExtra("USERID", userid);
                        context.startActivity(dmIntent);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent previewIntent = new Intent(context,previewPost.class);
                    previewIntent.putExtra("POSTID",post.POSTID);
                    context.startActivity(previewIntent);
                }
            });
        }
        int lastPosition = -1;
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;



    }
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return adposts.size()+1;
    }

    public class headerViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvhello;
        private TextView mTvuserNameHeader;

        public headerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvhello = itemView.findViewById(R.id.tv_hello);
            mTvuserNameHeader = itemView.findViewById(R.id.tv_username_home_header);
        }
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView mIvprofileimg;
        private TextView mTvprofileUsername;
        private TextView mTvpostLocation;
        private  ImageView mIvpostImage;
        private TextView priceTag;
        private TextView mArea;
        private ImageView mDirect;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mIvprofileimg = itemView.findViewById(R.id.profile_image);
            mIvpostImage = itemView.findViewById(R.id.roundedImageView);
            mTvpostLocation = itemView.findViewById(R.id.tv_post_location_home);
            mTvprofileUsername = itemView.findViewById(R.id.profile_username);
            priceTag = itemView.findViewById(R.id.tv_post_rent_home);
            mDirect = itemView.findViewById(R.id.iv_dm);
            mArea = itemView.findViewById(R.id.tv_home_area);
        }
    }
}
