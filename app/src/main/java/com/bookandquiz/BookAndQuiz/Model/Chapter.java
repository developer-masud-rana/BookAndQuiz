package com.bookandquiz.BookAndQuiz.Model;

public class Chapter {
    public String pdfLink;
    public String subjectId;
    public String chapterName;
    public String chapterNo;

    public Chapter() {
    }

    public Chapter(String pdfLink, String subjectId, String chapterName, String chapterNo) {
        this.pdfLink = pdfLink;
        this.subjectId = subjectId;
        this.chapterName = chapterName;
        this.chapterNo = chapterNo;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }
}
