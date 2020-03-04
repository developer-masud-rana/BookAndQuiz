package com.bookandquiz.BookAndQuiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bookandquiz.BookAndQuiz.Common.Common;
import com.bookandquiz.BookAndQuiz.Interface.ItemClickListener;
import com.bookandquiz.BookAndQuiz.Model.Subject;
import com.bookandquiz.BookAndQuiz.ViewHolder.CategoryViewHolder;

public class AllSubjectActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseRecyclerOptions<Subject> options;
    FirebaseRecyclerAdapter<Subject,CategoryViewHolder> adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student);
        //toolbar setting

        Toolbar toolbar = findViewById(R.id.toolbara);
        setSupportActionBar(toolbar);
        toolbar.setTitle("All Subject");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView=findViewById(R.id.re);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllSubjectActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference(Common.STR_ALL_SUBJECT);


        options=new FirebaseRecyclerOptions.Builder<Subject>().setQuery(reference,Subject.class).build();
        adapter=new FirebaseRecyclerAdapter<Subject, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Subject model) {
                holder.category_name.setText(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(AllSubjectActivity.this, ChapterListActivity.class);
                        Common.CATEGORY_ID_SELECTED=adapter.getRef(position).getKey();
                        Common.CATEGORY_SELECTED=model.getName();
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.allstudent_item_layout,parent,false);
                
                return new CategoryViewHolder(v);
            }
        };

        setAdtapter();


    }

    private void setAdtapter() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter !=null)
            adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter !=null)
            adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
