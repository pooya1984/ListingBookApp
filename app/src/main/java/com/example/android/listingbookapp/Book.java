package com.example.android.listingbookapp;


public class Book {


    private String mBookTitl;
    private String mUrl;


    public Book(String BookTitl,String Url) {
        mBookTitl= BookTitl;
        mUrl=Url;
    }



    public String getBookTitle() {
        return mBookTitl;
    }


    public String getUrl() {
        return mUrl;
    }
}