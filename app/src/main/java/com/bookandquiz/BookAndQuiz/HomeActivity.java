package com.bookandquiz.BookAndQuiz;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView, username;
    CardView allsubject,quiz,stastic,qna;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findid();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setTitle("Home");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    private void showingwelcome(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        Date setMorningTime = null;
        Date setAfternoonTime = null;
        Date setEveningTime = null;
        Date setNightTime = null;
        Date currentTime = null;

        try {
            setMorningTime = dateFormat.parse("05:59");
            setAfternoonTime = dateFormat.parse("11:59");
            setEveningTime = dateFormat.parse("17:59");
            setNightTime = dateFormat.parse("19:59");
            currentTime = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if(currentTime.after(setMorningTime) || currentTime.before(setAfternoonTime))
        {
            textView.setText("Good Morning");
        }if(currentTime.after(setAfternoonTime)|| currentTime.before(setEveningTime)){
            textView.setText("Good Afternoon");
        }if(currentTime.after(setEveningTime) || currentTime.before(setNightTime)){
            textView.setText("Good Evening");
        }if(currentTime.after(setNightTime)||currentTime.before(setMorningTime)){
            textView.setText("Good Night");
        }
    }
    private void showUserName() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user = dataSnapshot.child("username").getValue(String.class);
                username.setText(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.loguot)
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

        if (item.getItemId()==android.R.id.home)
            finish();
return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
      showingwelcome();
        showUserName();
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.crd_allstudent:
                startActivity(new Intent(HomeActivity.this,AllStudentActivity.class));
                Toast.makeText(getApplicationContext(), "All Subject", Toast.LENGTH_SHORT).show();
                break;
            case R.id.crd_quiz:
                startActivity(new Intent(getApplicationContext(),QuizActivity.class));
                Toast.makeText(getApplicationContext(), "Quiz", Toast.LENGTH_SHORT).show();
                break;
            case R.id.crd_stat:
                startActivity(new Intent(getApplicationContext(),StasticActivity.class));
                Toast.makeText(getApplicationContext(), "Statistics", Toast.LENGTH_SHORT).show();
                break;
            case R.id.crd_qanda:
                startActivity(new Intent(getApplicationContext(),QandNActivity.class));
                Toast.makeText(getApplicationContext(), "Q / A", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void findid(){
        //init all id
        allsubject=findViewById(R.id.crd_allstudent);
        quiz=findViewById(R.id.crd_quiz);
        stastic=findViewById(R.id.crd_stat);
        qna=findViewById(R.id.crd_qanda);
        textView = findViewById(R.id.txt_welcome);
        username = findViewById(R.id.txt_username);

        // init onclick
        allsubject.setOnClickListener(this);
        quiz.setOnClickListener(this);
        qna.setOnClickListener(this);
        stastic.setOnClickListener(this);

    }

}