package com.bookandquiz.BookAndQuiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView textView,cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
//genarate fbsignin haskey
        try {
            PackageInfo info= getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_SIGNATURES);

            for (Signature signature:info.signatures){
                MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("keyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){

        }catch (NoSuchAlgorithmException e){

        }
*/

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }
    }

}
