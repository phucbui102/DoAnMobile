package com.example.doanmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Find extends AppCompatActivity {

    private SearchView searchView;
    private ImageView BackBtn;
    private PostAdapter postAdapter;
    private ListView findListView;
    private CustomListFind findAdapter;
    private List<Post> postList;

    private static final String[] items = {"điện thoại cũ", "bàn phím cơ", "kệ để dép", "giày cũ", "hehe"};
    private static final int[] images = {R.drawable.ic_access_time, R.drawable.ic_access_time, R.drawable.ic_access_time, R.drawable.ic_access_time, R.drawable.ic_access_time};
    private static final int[] imagesdel = {R.drawable.ic_delete, R.drawable.ic_delete, R.drawable.ic_delete, R.drawable.ic_delete, R.drawable.ic_delete};

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        // Initialize views
        searchView = findViewById(R.id.searchView);
        findListView = findViewById(R.id.FindList);
        BackBtn = findViewById(R.id.BackBtn);

        // Set up back button listener
        BackBtn.setOnClickListener(view -> finish());

        // Initialize post list
        postList = new ArrayList<>();

        // Initialize adapters
        findAdapter = new CustomListFind(this, items);
        postAdapter = new PostAdapter(this, postList);

        // Set the default adapter to display predefined items
        findListView.setAdapter(findAdapter);
        searchView.requestFocus();
        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    // Fetch filtered data from the database
                    fetchPostData(query);

                    // Update the ListView with the filtered posts
                    findListView.setAdapter(postAdapter);
                } else {
                    Toast.makeText(Find.this, "Vui lòng nhập sản phẩm muốn tìm", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText); // Filter predefined list in real time
                return true;
            }
        });
    }

    private void fetchPostData(String query) {
        db = openOrCreateDatabase("app_database.db", MODE_PRIVATE, null);

        // Query to fetch filtered posts from the database
        String selectQuery = "SELECT * FROM Post WHERE content LIKE ? ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});

        postList.clear(); // Clear the list before fetching new data

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get content and image data from cursor
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));

                    // Create a new Post object and add it to the list
                    postList.add(new Post(content, imageData));

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có bài đăng nào phù hợp", Toast.LENGTH_SHORT).show();
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

        // Notify the adapter about data changes
        postAdapter.notifyDataSetChanged();
    }

    private void filterList(String query) {
        List<String> filteredItems = new ArrayList<>();
        List<Integer> filteredImages = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            if (items[i].toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(items[i]);
//                filteredImages.add(images[i]);
            }
        }

        // Update the adapter with the filtered data
        findAdapter = new CustomListFind(
                this,
                filteredItems.toArray(new String[0])
//                filteredImages.stream().mapToInt(i -> i).toArray()
        );
        findListView.setAdapter(findAdapter);
    }
}
