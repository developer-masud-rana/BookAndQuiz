package com.bookandquiz.BookAndQuiz.Interface;

public interface Downloader {

    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
