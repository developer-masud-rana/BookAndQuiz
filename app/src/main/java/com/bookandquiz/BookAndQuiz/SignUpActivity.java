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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    TextView signin;
    Button signup;
    EditText name,email,password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signin=findViewById(R.id.txt_signin);
        signup=findViewById(R.id.btn_signup);
        name=findViewById(R.id.edt_up_username);
        email=findViewById(R.id.edt_up_useremail);
        password=findViewById(R.id.edt_up_userpassword);
        auth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              final String getgmail=email.getText().toString();
               final String getname=name.getText().toString();
               final String getpassword=password.getText().toString();
               if (TextUtils.isEmpty(getname)){
                   Toast.makeText(getApplicationContext(), "Invalid User name", Toast.LENGTH_SHORT).show();
               }if (TextUtils.isEmpty(getgmail)){
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                }
               if (TextUtils.isEmpty(getpassword)){
                   Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
               }
               if (TextUtils.isEmpty(getname)&&TextUtils.isEmpty(getgmail)&&TextUtils.isEmpty(getpassword)){
                   Toast.makeText(SignUpActivity.this, "Please input yor information for signup", Toast.LENGTH_SHORT).show();
               }
               else{

                auth.createUserWithEmailAndPassword(getgmail,getpassword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
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

                                    finish();
                                    Toast.makeText(SignUpActivity.this, "Your Account Created Successfully", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(SignUpActivity.this, "sorry your account not created, try again later", Toast.LENGTH_SHORT).show();
                                }
                            }});
            }}
        });

    }


/*    private void RegisterUser(final String name, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final DatabaseReference RootRef;
                    RootRef = FirebaseDatabase.getInstance().getReference();
                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!(dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).exists())) {

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("email", email);
                                userMap.put("name", name);
                                userMap.put("password", password);

                                RootRef.child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Network Error.., please Try again", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "This " + email + " already exists", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }*/
}
