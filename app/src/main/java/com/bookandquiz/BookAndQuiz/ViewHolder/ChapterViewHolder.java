package com.bookandquiz.BookAndQuiz.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookandquiz.BookAndQuiz.Interface.ItemClickListener;
import com.bookandquiz.BookAndQuiz.R;

public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView chapterno;
    public TextView chaptername;
    public Button download;
    public TextView btnview;
    ItemClickListener itemClickListener;

    public ChapterViewHolder(@NonNull View itemView) {

        super(itemView);
        chapterno=itemView.findViewById(R.id.row_subject_chapterNo);
        chaptername=itemView.findViewById(R.id.row_subject_chapterName);
        download=itemView.findViewById(R.id.row_item_download);
        btnview=itemView.findViewById(R.id.row_item_view);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
