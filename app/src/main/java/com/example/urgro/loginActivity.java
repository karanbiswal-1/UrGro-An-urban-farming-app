package com.example.urgro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class loginActivity extends AppCompatActivity {

     private EditText mEtloginEmail;
     private EditText mEtloginPassword;
     private FloatingActionButton mBtnlogin;
     private TextView mTvSignup;
     private TextView mForgot;
     private ImageView mGoogle;
     private GoogleSignInClient mGoogleClient;
     private static final int RC_SIGN_IN = 100;
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       mEtloginEmail = findViewById(R.id.et_log_email);
       mEtloginPassword = findViewById(R.id.et_log_password);
       mBtnlogin = findViewById(R.id.btn_login);
       mTvSignup = findViewById(R.id.tv_log_signup);
       mForgot = findViewById(R.id.tv_log_forgot);
       mGoogle = findViewById(R.id.iv_click_google);
       mAuth = FirebaseAuth.getInstance();

       if(mAuth.getCurrentUser()!=null){
           startActivity(new Intent(loginActivity.this,dashBoard.class));
           finish();
       }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
       mGoogleClient = GoogleSignIn.getClient(this,gso);
       
       mGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               signInGoogle();
           }
       });

       mForgot.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(loginActivity.this,forgot_password.class));
           }
       });

       mTvSignup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(loginActivity.this,registerActivity.class));
               finish();
           }
       });

       mBtnlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String txt_log_email = mEtloginEmail.getText().toString();
               String txt_log_password = mEtloginPassword.getText().toString();
               loginUser(txt_log_email,txt_log_password);
           }
       });

    }

    private void signInGoogle() {
        Intent gSigninintent = mGoogleClient.getSignInIntent();
        startActivityForResult(gSigninintent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(loginActivity.this,"signed In sucessfully",Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        }catch (ApiException e){
            Toast.makeText(loginActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
           // FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(loginActivity.this, "signed In sucessfully", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateuser(user);
                }else{
                    Toast.makeText(loginActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateuser(FirebaseUser user) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("please wait");
        pd.show();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String username = account.getDisplayName();
            String name = account.getGivenName();
            String email = account.getEmail();

            HashMap<String, Object> map = new HashMap<>();
            map.put("NAME", name);
            map.put("USERNAME", username);
            map.put("EMAIL", email);
            map.put("ID", mAuth.getCurrentUser().getUid());
            map.put("PHONE", "");
            map.put("PROFESSION", "");
            map.put("RESIDENCE", "");
            map.put("IMAGEURL", "default");

            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  pd.dismiss();
                    if(task.isSuccessful()){

                        //Toast.makeText(registerActivity.this,"update your profile",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(loginActivity.this,dashBoard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void loginUser(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("please wait");
        pd.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(loginActivity.this,"update your profile",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(loginActivity.this,dashBoard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}