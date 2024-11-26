package com.example.doanmobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Edit_Profile extends AppCompatActivity {

    public EditText etFullname, etEmail;
    public Button btnSave;
    public ImageView Back;
    public SQLiteDatabase database;
    public static final String DATABASE_NAME = "app_database.db";
    public static final String TABLE_NAME = "users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Liên kết giao diện
        etFullname = findViewById(R.id.edt_username);
        etEmail = findViewById(R.id.edt_email);
        btnSave = findViewById(R.id.btn_save);
        Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Kết nối cơ sở dữ liệu
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Hiển thị thông tin hiện tại
        loadUserData();

        // Xử lý sự kiện lưu thông tin
        btnSave.setOnClickListener(v -> {
            String newFullname = etFullname.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            // Kiểm tra nếu thông tin không được nhập
            if (newFullname.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy thông tin hiện tại từ cơ sở dữ liệu (hoặc biến lưu trữ sẵn)
            Cursor cursor = database.rawQuery("SELECT fullname, email FROM " + TABLE_NAME + " WHERE id = 1", null);
            if (cursor.moveToFirst()) {
                String currentFullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
                String currentEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                // Kiểm tra nếu không có thay đổi
                if (newFullname.equals(currentFullname) && newEmail.equals(currentEmail)) {
                    Toast.makeText(this, "Không có thay đổi để lưu!", Toast.LENGTH_SHORT).show();
                    // Quay lại màn hình Profile
                    Intent intent = new Intent(this, Profile.class);
                    startActivity(intent);
                    cursor.close();
                    finish(); // Kết thúc Activity hiện tại
                    return;
                }
            }
            cursor.close();

            // Cập nhật thông tin nếu có thay đổi
            updateUser(newFullname, newEmail);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

            // Quay lại màn hình Profile
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            finish();
        });

//        ImageView imgFacebook = findViewById(R.id.imgFacebook);
//        ImageView imgInstagram = findViewById(R.id.imgInstagram);
//
//        // Liên kết Facebook
//        imgFacebook.setOnClickListener(v -> {
//            openSocialLink("https://www.facebook.com/profile.php?id=100024260026293");
//        });
//
//        // Liên kết Instagram
//        imgInstagram.setOnClickListener(v -> {
//            openSocialLink("https://www.instagram.com/yourprofile");
//        });

    }

    // Hàm mở liên kết mạng xã hội
    private void openSocialLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng để mở liên kết!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserData() {
        // Lấy dữ liệu từ bảng
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            // Hiển thị vào EditText
            etFullname.setText(fullname);
            etEmail.setText(email);
        }
        cursor.close();
    }

    private void updateUser(String fullname, String email) {
        // Cập nhật dữ liệu trong bảng
        String updateSQL = "UPDATE " + TABLE_NAME + " SET fullname = ?, email = ? WHERE id = 1";
        database.execSQL(updateSQL, new Object[]{fullname, email});
    }
}
