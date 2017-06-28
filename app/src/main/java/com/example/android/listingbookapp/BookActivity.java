package com.example.android.listingbookapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {


    //Variable to log errors as they occur
    public static final String LOG_TAG = BookActivity.class.getName();
    //Google Books API URL
    private static String GOOGLE_BOOKS_URL = "  https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";
    //Variable for the user inputs (text to search for and search button)
    private EditText editText;
    private Button searchButton;

    //Variable for the ListView to populate
    private ListView bookListView;
    private BookListAdapter mAdapter;
    private String userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //Find the EditText and Button objects
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.text_edit);
                userInput = editText.getText().toString().replace(" ", "+");
                BookAsyncTask task = new BookAsyncTask();
                task.execute();
            }
        });


        //Find the list_view
        bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookListAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);
            }
        });

        // Start the AsyncTask to fetch the Book data
        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_BOOKS_URL);
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {


        @Override
        protected List<Book> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> result = QueryUtils.fetchBookData(urls[0], GOOGLE_BOOKS_URL);
            return result;
        }


        @Override
        protected void onPostExecute(List<Book> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
