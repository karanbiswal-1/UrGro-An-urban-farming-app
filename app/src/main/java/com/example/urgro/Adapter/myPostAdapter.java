package com.example.urgro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urgro.R;
import com.example.urgro.adPostActivity;
import com.example.urgro.model.adPostInfo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static androidx.appcompat.widget.PopupMenu.*;

public  class myPostAdapter extends RecyclerView.Adapter<myPostAdapter.viewHolder> {

    private Context context;
    private List<adPostInfo> mPosts;

    public myPostAdapter(Context context, List<adPostInfo> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.searched_post_bg, parent, false);
        return new myPostAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        adPostInfo post = mPosts.get(position);
        if(post.IMAGEURL == "default"){
            Glide.with(context).load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fmoonvillageassociation.org%2Fdefault-profile-picture1%2F&psig=AOvVaw2K3UdaWBULdWQbBdY063wo&ust=1603728148827000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCMCGvYSP0OwCFQAAAAAdAAAAABAD").into(holder.mPostImage);
        }else {
            Glide.with(context).load(post.IMAGEURL).into(holder.mPostImage);
        }
        holder.mLocation.setText(post.LOCATION);
        holder.mRent.setText("Rs" + post.RENTPRICE);
        holder.mArea.setText(post.AREA+"sqm");
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private ImageView mMore;
        private ImageView mPostImage;
        private TextView mLocation;
        private TextView mRent;
        private TextView mArea;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mMore = itemView.findViewById(R.id.ic_more);
            mPostImage = itemView.findViewById(R.id.iv_searchedimg);
            mLocation = itemView.findViewById(R.id.tv_searched_location);
            mRent = itemView.findViewById(R.id.tv_searched_rentprice);
            mArea = itemView.findViewById(R.id.tv_searched_area);
            mMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    adPostInfo epost = mPosts.get(getAdapterPosition());
                    Intent editPostIntent = new Intent(context, adPostActivity.class);
                    editPostIntent.putExtra("POSTID",epost.POSTID);
                    context.startActivity(editPostIntent);
                    return true;
                case R.id.action_delete:
                    adPostInfo post = mPosts.get(getAdapterPosition());
                    FirebaseDatabase.getInstance().getReference().child("adPosts").child(post.POSTID).removeValue();
                    Toast.makeText(context, "adPost deleted", Toast.LENGTH_LONG).show();
                    return true;
                default:
                    return false;
            }
        }


        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_item);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }
    }

}
