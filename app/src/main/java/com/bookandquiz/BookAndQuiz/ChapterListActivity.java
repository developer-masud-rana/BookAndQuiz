package com.bookandquiz.BookAndQuiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.bookandquiz.BookAndQuiz.Common.Common;
import com.bookandquiz.BookAndQuiz.Model.Chapter;
import com.bookandquiz.BookAndQuiz.ViewHolder.ChapterViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChapterListActivity extends AppCompatActivity {
    Query query;
    FirebaseRecyclerOptions<Chapter> options;
    FirebaseRecyclerAdapter<Chapter, ChapterViewHolder> adapter;


    RecyclerView recyclerView;


    private static final String TAG="Download Task";
    private String downloadUr1="",downloadFileName="";

    File pdfFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(Common.CATEGORY_SELECTED);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView=findViewById(R.id.recycler_list_chapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChapterListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadChapterlist();
    }

    private void loadChapterlist() {
        query= FirebaseDatabase.getInstance().getReference(Common.STR_ALL_CHAPTER)
                .orderByChild("subjectId").equalTo(Common.CATEGORY_ID_SELECTED);
        options=new FirebaseRecyclerOptions.Builder<Chapter>().setQuery(query,Chapter.class).build();

        adapter=new FirebaseRecyclerAdapter<Chapter, ChapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChapterViewHolder holder, int position, @NonNull final Chapter model) {
                holder.chapterno.setText(model.getChapterNo());
                holder.chaptername.setText(model.getChapterName());
                downloadUr1=(model.getPdfLink());

                holder.btnview.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String n = (model.getChapterName());
                        pdfFile = new File(Environment.getExternalStorageDirectory() + "/ITBangladesh/" + n + ".pdf");

                        if(pdfFile.exists()){
                            Intent intent = new Intent(ChapterListActivity.this, PdfViewfromsdActivity.class);
                            intent.putExtra("filelink", n);
                            startActivity(intent);
                            Toast.makeText(ChapterListActivity.this, "Open Pdf file", Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent = new Intent(ChapterListActivity.this, PdfViewActivity.class);
                            Common.select_background = model;
                            startActivity(intent);
                            Toast.makeText(ChapterListActivity.this, "loading", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.download.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        downloadUr1 = (model.getPdfLink());
                        String  name = (model.getChapterName());
                        downloadToPdf(name);

                    }
                });

            }

            @NonNull
            @Override
            public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapterlist_item_layout,parent,false);
                int height = parent.getMeasuredHeight();
                itemView.setMinimumHeight(height);
                return new ChapterViewHolder(itemView);
            }
        };
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
    protected void onResume() {
        super.onResume();
        if (adapter !=null)
            adapter.startListening();
    }
    private void downloadToPdf(String name)
    {

        downloadFileName = name;
        DownloadTaskPdf();
    }
    private void DownloadTaskPdf()
    {


        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {


        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                File newFile = new File(Environment.getExternalStorageDirectory() + "/" + "ITBangladesh");
                File oFile = new File(newFile, downloadFileName+".pdf");
                if(!(oFile.exists()))
                {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUr1));//file url
                    request.allowScanningByMediaScanner();
                    request.setDescription(downloadFileName)//file name
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .setDestinationUri(Uri.fromFile(oFile))//location
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setVisibleInDownloadsUi(true)
                            .setTitle("Downloading");
                    DownloadManager manager = (DownloadManager) ChapterListActivity.this.getSystemService(DOWNLOAD_SERVICE);
                    assert manager != null;
                    long DownLoadID = manager.enqueue(request);
                    Toast.makeText(ChapterListActivity.this, "Downloading Start", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChapterListActivity.this, "pdf already downloaded", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception e){
                Toast.makeText(ChapterListActivity.this, "Pdf Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void result)
        {

            //Toast.makeText(ListChapterActivity.this, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUr1);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }

                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "ITBangladesh");
                } else
                    Toast.makeText(ChapterListActivity.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName+".pdf");//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

}
