package com.bookandquiz.BookAndQuiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;

import com.bookandquiz.BookAndQuiz.Common.Common;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
public class PdfViewActivity extends AppCompatActivity {
    PDFView pdfView;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(Common.select_background.getChapterName());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfView=findViewById(R.id.pdfView);
        progressBar=findViewById(R.id.chapter_show_progressbar);



        new PdfViewActivity.RetrievePDFSteam().execute(Common.select_background.getPdfLink());


    }
    class RetrievePDFBytes extends AsyncTask<String,Void, byte[]>{

        @Override
        protected byte[] doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            pdfView.fromBytes(bytes).load();
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    class RetrievePDFSteam extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            progressBar.setVisibility(View.GONE);
        }
    }
}
