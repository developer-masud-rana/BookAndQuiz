package com.bookandquiz.BookAndQuiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.bookandquiz.BookAndQuiz.Common.Common;

import java.io.File;

public class PdfViewfromsdActivity extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewfromsd);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.select_background.getChapterName());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        pdfView = findViewById(R.id.pdfView);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("filelink");

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ITBangladesh/" + fileName + ".pdf");

        pdfView.fromFile(pdfFile)
                .defaultPage(1)
                .enableSwipe(true)
                .load();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
