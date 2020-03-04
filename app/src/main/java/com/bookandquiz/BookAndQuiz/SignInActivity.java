package com.bookandquiz.BookAndQuiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    TextView gotosignup,forgot;

    SignInButton google;

    Button signin,phone;
    EditText gmail,passwrod;

    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
public static final int RC_SIGN_IN=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findAllid();
        glesignin();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if (user!=null){
                    updateUI(user);
                }else {
                   updateUI(null);
                }

            }
        };



        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }




    private void updateUI(FirebaseUser user){
     final    String getname=user.getDisplayName();
        final String getemail=user.getEmail();
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).exists()))
                {

                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("username",getname);
                    userMap.put("gmail",getemail);
                    ref.child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void glesignin(){
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
     final    String getname=account.getDisplayName();
      final   String getgmail=account.getEmail();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    if(!(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).exists()))
                                    {

                                        Map<String,Object> userMap = new HashMap<>();
                                        userMap.put("username",getname);
                                        userMap.put("gmail",getgmail);
                                        ref.child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                                finish();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    private void findAllid() {
        google=findViewById(R.id.btn_googlesignin);
        phone=findViewById(R.id.btn_signinphone);
        gotosignup=findViewById(R.id.txt_signup);
        forgot=findViewById(R.id.txt_forgot);
        signin=findViewById(R.id.btn_signin);
        gmail=findViewById(R.id.edt_in_gamil);
        passwrod=findViewById(R.id.edt_in_password);
        gotosignup.setOnClickListener(this);
        forgot.setOnClickListener(this);
        signin.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.txt_signup){
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
        }
        if (v.getId()==R.id.txt_forgot){
          //  startActivity(new Intent(getApplicationContext(),ForgotActivity.class));
        }
        if (v.getId()==R.id.btn_signin) {
            final String getgmail = gmail.getText().toString();
            String getpassword = passwrod.getText().toString();
            if (TextUtils.isEmpty(getgmail)) {
                Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            }else
            if (TextUtils.isEmpty(getpassword)) {
                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
            }
           else if (TextUtils.isEmpty(getgmail) && TextUtils.isEmpty(getpassword)) {
                Toast.makeText(getApplicationContext(), "Input email and password", Toast.LENGTH_SHORT).show();
            } else {

                mAuth.signInWithEmailAndPassword(getgmail, getpassword)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    finish();
                                    Toast.makeText(SignInActivity.this, "SignIn successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed please try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }

    }

}
