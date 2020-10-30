package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.urgro.model.usersInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashBoard extends AppCompatActivity {

   private Toolbar mToolbar;
   private DrawerLayout mDrawer;
   private NavigationView mNavDrawer;
   private ImageView ham;
   private ImageView mIvdashProf;
   private MeowBottomNavigation mButtomNav;
   private FirebaseUser fUser;
   private FirebaseAuth mAuth;
   private static final int ID_HOME=1;
   private static final int ID_EXPLORE=2;
   private static final int ID_ADD=3;
   private static final int ID_CHAT=4;
   private static final int ID_PROFILE=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mDrawer = findViewById(R.id.dl_dash);
        mToolbar = findViewById(R.id.tlb_dash);
        setSupportActionBar(mToolbar);
        ham = findViewById(R.id.ic_ham);
        mNavDrawer = findViewById(R.id.navView);
        mButtomNav = (MeowBottomNavigation) findViewById(R.id.btm_nav);
        mIvdashProf = findViewById(R.id.prof_dash_img);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        mButtomNav.add(new MeowBottomNavigation.Model(1,R.drawable.ic_dashboard));
        mButtomNav.add(new MeowBottomNavigation.Model(2,R.drawable.ic_explore));
        mButtomNav.add(new MeowBottomNavigation.Model(3,R.drawable.ic_add));
        mButtomNav.add(new MeowBottomNavigation.Model(4,R.drawable.ic_chat_dark));
        mButtomNav.add(new MeowBottomNavigation.Model(5,R.drawable.ic_profile));


        final TextView txtProfileName = (TextView)mNavDrawer.getHeaderView(0).findViewById(R.id.tv_navName);
        final TextView txtprofileEmail = (TextView)mNavDrawer.getHeaderView(0).findViewById(R.id.nav_email);
        final ImageView navProfImg = (ImageView)mNavDrawer.getHeaderView(0).findViewById(R.id.iv_header_profimg);

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo user = dataSnapshot.getValue(usersInfo.class);

                if(!user.IMAGEURL.equals("default")) {
                    Glide.with(dashBoard.this).load(user.IMAGEURL).into(mIvdashProf);
                    Glide.with(dashBoard.this).load(user.IMAGEURL).into(navProfImg);
                }
                txtProfileName.setText(user.NAME);
                txtprofileEmail.setText(user.EMAIL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  Glide.with(dashBoard.this).load(imageurl).into(mIvdashProf);

        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(dashBoard.this,"ham clicked",Toast.LENGTH_LONG).show();
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });

        mButtomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
               // Toast.makeText(getApplicationContext(),"clicked item"+item.getId(),Toast.LENGTH_LONG).show();
           }
        });

        mButtomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                switch (item.getId()){
                    case ID_HOME:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new homeFragment()).commit();
                        break;
                    case ID_EXPLORE:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new exploreFragment()).commit();
                        break;
                    case ID_ADD:
                        startActivity(new Intent(dashBoard.this,adPostActivity.class));
                        break;
                    case ID_CHAT:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new chatFragment()).commit();
                        break;
                    case ID_PROFILE:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new profileFragment()).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new homeFragment()).commit();
                        break;
                }

            }
        });
        mButtomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });




        mNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  switch (item.getItemId()){
                      case R.id.nav_profile:
                          startActivity(new Intent(dashBoard.this,accountActivity.class));
                          mDrawer.closeDrawer(Gravity.LEFT);
                          break;
                      case R.id.nav_logout:
                          userlogout();
                          break;
                      case R.id.nav_manage:
                          startActivity(new Intent(dashBoard.this,editProfileActivity.class));
                          mDrawer.closeDrawer(Gravity.LEFT);
                  }
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container,new homeFragment()).commit();


    }

    private void userlogout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(dashBoard.this,loginActivity.class));
        finish();
    }


}