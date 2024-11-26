package com.example.doanmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPost extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView listView;
    private List<Post> postList;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        // Initialize ListView
        listView = findViewById(R.id.listView);

        // Initialize post list
        postList = new ArrayList<>();

        // Fetch posts from database
        fetchPostData();

        // Set up adapter for ListView
        postAdapter = new PostAdapter(this, postList);
        listView.setAdapter(postAdapter);
    }

    private void fetchPostData() {
        db = openOrCreateDatabase("app_database.db", MODE_PRIVATE, null);

        // Query to fetch all posts from the database
        String selectQuery = "SELECT * FROM Post ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get content and image data from cursor
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Create a new Post object and add it to the list
                    Post post = new Post(content, imageData);
                    postList.add(post);

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có bài đăng nào", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
