package com.example.doanmobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    public static final String DATABASE_NAME = "app_database.db";
    EditText edtFullName, edtUserorEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    SQLiteDatabase database;

    // Thêm người dùng vào bảng
    public void addUser(String fullname, String email, String password) {
        try {
            SQLiteDatabase database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            // Truy vấn SQL để thêm người dùng
            String insertQuery = "INSERT INTO users (fullname, email, password) VALUES (?, ?, ?)";
            database.execSQL(insertQuery, new Object[]{fullname, email, password});

            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            database.close();
        } catch (Exception e) {
            // Xử lý lỗi nếu xảy ra
            Toast.makeText(this, "Đăng ký thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Kiểm tra email trùng lặp
    public boolean isEmailExists(String email) {
        SQLiteDatabase database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        boolean exists = false;

        try {
            // Truy vấn SQL để kiểm tra email
            String query = "SELECT * FROM users WHERE email = ?";
            cursor = database.rawQuery(query, new String[]{email});

            exists = cursor.getCount() > 0;
        } finally {
            // Đảm bảo đóng Cursor và database
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return exists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các view
        edtFullName = findViewById(R.id.edt_full_name);
        edtUserorEmail = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        TextView tvLogin = findViewById(R.id.txt_login);

        // Tạo hoặc kết nối cơ sở dữ liệu
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname TEXT, " +
                "email TEXT UNIQUE, " +
                "avata TEXT, " +
                "password TEXT)";
        database.execSQL(createTableQuery);

        // Xử lý sự kiện đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = edtFullName.getText().toString().trim();
                String email = edtUserorEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                // Kiểm tra thông tin nhập liệu
                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra email trùng lặp
                if (isEmailExists(email)) {
                    Toast.makeText(Register.this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thêm người dùng mới
                addUser(fullName, email, password);

                // Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
