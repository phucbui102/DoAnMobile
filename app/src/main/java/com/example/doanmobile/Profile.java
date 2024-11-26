package com.example.doanmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView tvName, tvEmail;
    private Button btnEditProfile, btnLogout;
    private GridView gridView;
    private List<Post> postList;
    private Adapter_GridView Adapter_GridView;
    private SQLiteDatabase database;
    private int posselected = -1;

    private static final String DATABASE_NAME = "app_database.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_POSTS = "Post";

    public static void showBottomSheet(Context context) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Liên kết giao diện
        imgProfile = findViewById(R.id.img_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        gridView = findViewById(R.id.gridView);
        ImageView Back = findViewById(R.id.Back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Mở cơ sở dữ liệu
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTablesIfNotExist();

        loadUserData();
        setupGridView();

        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Chỉnh sửa profile hiện chưa được triển khai!", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Profile.this, Login.class));
            finish();
        });

        gridView.setOnItemLongClickListener((parent, view, position, id) -> {
            posselected = position;
            return false;
        });
        registerForContextMenu(gridView);
    }

    private void createTablesIfNotExist() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "email TEXT UNIQUE, " +
                "avatar TEXT, " +
                "password TEXT)";
        String createPostsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT, " +
                "image BLOB)";
        database.execSQL(createUsersTable);
        database.execSQL(createPostsTable);
    }

    private void loadUserData() {
        String query = "SELECT * FROM " + TABLE_USERS + " LIMIT 1";
        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                tvName.setText(cursor.getString(cursor.getColumnIndexOrThrow("fullname")));
                tvEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            } else {
                addUser("Nguyễn Văn A", "nguyenvana@example.com");
                loadUserData();
            }
        }
    }

    private void addUser(String fullname, String email) {
        String insertSQL = "INSERT INTO " + TABLE_USERS + " (fullname, email) VALUES (?, ?)";
        database.execSQL(insertSQL, new Object[]{fullname, email});
    }

    private void setupGridView() {
        postList = new ArrayList<>();
        fetchPostData();

        Adapter_GridView = new Adapter_GridView(this, postList);
        gridView.setAdapter(Adapter_GridView);
    }

    private void fetchPostData() {
        String selectQuery = "SELECT * FROM " + TABLE_POSTS + " ORDER BY _id DESC";
        try (Cursor cursor = database.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    postList.add(new Post(content, imageData, id));
                } while (cursor.moveToNext());
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_del_edit_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (posselected == -1) return false;

        if (item.getItemId() == R.id.Del_item) {
            confirmDelete();
            return true;
        } else if (item.getItemId() == R.id.Edit_item) {
            Toast.makeText(this, "Chức năng chỉnh sửa đang được phát triển", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn xoá?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            if (posselected != -1) {
                String id = postList.get(posselected).get_id();
                database.delete(TABLE_POSTS, "_id=?", new String[]{id});
                postList.remove(posselected);
                Adapter_GridView.notifyDataSetChanged();
                Toast.makeText(this, "Xoá thành công", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }
}
