package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.urgro.model.adPostInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class adPostActivity extends AppCompatActivity {

    private ImageView mIvback;
    private Button mBtnpost;
    private ImageView imageSelected;
    private EditText mEtpostlocation;
    private EditText mEtpostDescription;
    private EditText mEtpostprice;
    private EditText mEtpostAres;
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_post);

        mBtnpost = findViewById(R.id.btn_post);
        imageSelected = findViewById(R.id.iv_post_img);
        mEtpostlocation = findViewById(R.id.et_post_location);
        mEtpostDescription = findViewById(R.id.et_post_description);
        mEtpostprice = findViewById(R.id.et_post_price);
        mEtpostAres = findViewById(R.id.et_post_area);

        mIvback = findViewById(R.id.ic_post_back);
        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTask();
            }
        });

        Bundle data = getIntent().getExtras();
        if(data != null){
            String postID = data.getString("POSTID");
            FirebaseDatabase.getInstance().getReference().child("adPosts").child(postID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adPostInfo post = dataSnapshot.getValue(adPostInfo.class);
                    Glide.with(adPostActivity.this).load(post.IMAGEURL).into(imageSelected);
                    mEtpostDescription.setText(post.DESCRIPTION);
                    mEtpostlocation.setText(post.LOCATION);
                    mEtpostprice.setText(post.RENTPRICE);
                    mEtpostAres.setText(post.AREA);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            imageSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CropImage.activity().start(adPostActivity.this);
                }
            });
        }else {
            CropImage.activity().start(adPostActivity.this);
        }
    }

    private void uploadTask() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("adPosts").child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            StorageTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri dounloadUri = task.getResult();
                    imageUrl = dounloadUri.toString();

                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("adPosts");
                    String postID = mRef.push().getKey();

                    HashMap<String ,Object> map = new HashMap<>();
                    map.put("POSTID",postID);
                    map.put("IMAGEURL",imageUrl);
                    map.put("LOCATION",mEtpostlocation.getText().toString());
                    map.put("DESCRIPTION",mEtpostDescription.getText().toString());
                    map.put("RENTPRICE",mEtpostprice.getText().toString());
                    map.put("OWNER", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("AREA",mEtpostAres.getText().toString());

                    mRef.child(postID).setValue(map);

                    pd.dismiss();
                    startActivity(new Intent(adPostActivity.this,dashBoard.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(adPostActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this,"No image is selcted",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtention(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageSelected.setImageURI(imageUri);
        }else {
            Toast.makeText(adPostActivity.this,"try again",Toast.LENGTH_LONG).show();
            startActivity(new Intent(adPostActivity.this,dashBoard.class));
        }
    }
}