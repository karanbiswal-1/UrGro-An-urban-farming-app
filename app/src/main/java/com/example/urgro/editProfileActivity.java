package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.urgro.model.usersInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class editProfileActivity extends AppCompatActivity {

    private ImageView mIvback;
    private Button mBttnSave;
    private ImageView mIvprofIMG;
    private TextView mTvchangeProf;
    private EditText mEtusername;
    private EditText mEtname;
    private EditText mEtphone;
    private EditText mEtprofession;
    private EditText mEtaddress;
    private FirebaseUser fUser;
    private Uri mImageuri;
    private StorageTask uploadTask;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mIvback = findViewById(R.id.ic_editprofile_back);
        mBttnSave = findViewById(R.id.btn_save);
        mIvprofIMG = findViewById(R.id.iv_editprofile_profimg);
        mTvchangeProf = findViewById(R.id.tv_editprofile_change);
        mEtusername = findViewById(R.id.et_editprofile_username);
        mEtname = findViewById(R.id.et_editprofile_name);
        mEtphone = findViewById(R.id.et_editprofile_phone);
        mEtprofession = findViewById(R.id.et_editprofile_profession);
        mEtaddress = findViewById(R.id.et_editprofile_address);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("uploads");

        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersInfo user = dataSnapshot.getValue(usersInfo.class);
                mEtusername.setText(user.USERNAME);
                mEtname.setText(user.NAME);
                mEtaddress.setText(user.RESIDENCE);
                mEtphone.setText(user.PHONE);
                mEtprofession.setText(user.PROFESSION);
                Glide.with(editProfileActivity.this).load(user.IMAGEURL).into(mIvprofIMG);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mTvchangeProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(editProfileActivity.this);
            }
        });
        mIvprofIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(editProfileActivity.this);
            }
        });

        mBttnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    private void updateProfile() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("please wait");
        pd.show();

        HashMap<String,Object> map = new HashMap<>();
        map.put("USERNAME",mEtusername.getText().toString());
        map.put("NAME",mEtname.getText().toString());
        map.put("PHONE",mEtphone.getText().toString());
        map.put("PROFESSION",mEtprofession.getText().toString());
        map.put("RESIDENCE",mEtaddress.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    startActivity(new Intent(editProfileActivity.this,dashBoard.class));
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageuri = result.getUri();
        }else{
            Toast.makeText(editProfileActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
        }
        uploadImage();
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("uploading");
        pd.show();

        if(mImageuri != null){
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis()+".jpeg");
            uploadTask =  fileRef.putFile(mImageuri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri =  task.getResult();
                        String url = downloadUri.toString();
                        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).child("IMAGEURL").setValue(url);
                        pd.dismiss();
                    }else{
                        Toast.makeText(editProfileActivity.this,"upload failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            Toast.makeText(editProfileActivity.this,"No image selected",Toast.LENGTH_LONG).show();
        }
    }
}