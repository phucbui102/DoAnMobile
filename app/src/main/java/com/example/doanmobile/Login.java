package com.example.doanmobile;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME = "app_database.db";
    SQLiteDatabase database;
    EditText edtUserName, edtPassword;
    Button btnLogin;

    // Hàm kiểm tra bảng tồn tại
    private boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = null;
        try {
            // Đảm bảo database đã mở
            if (database == null || !database.isOpen()) {
                database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            }

            // Thực hiện truy vấn kiểm tra sự tồn tại của bảng
            cursor = database.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ?", new String[]{tableName});

            // Kiểm tra xem bảng có tồn tại không
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception ex) {
            // Báo lỗi nếu có ngoại lệ
            Toast.makeText(this, "Lỗi kiểm tra bảng: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Đảm bảo đóng con trỏ sau khi sử dụng
            if (cursor != null) cursor.close();
        }

        return false; // Trả về false nếu không tìm thấy bảng hoặc gặp lỗi
    }


    // Hàm kiểm tra tài khoản người dùng
    private boolean isUser(String email, String password) {
        try {
            // Mở cơ sở dữ liệu nếu cần
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            // Thực hiện truy vấn tìm kiếm tài khoản
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

            // Kiểm tra kết quả truy vấn
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception ex) {
            // Báo lỗi nếu xảy ra
            Toast.makeText(this, "Lỗi kiểm tra tài khoản: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Không đóng database nếu dùng chung cho toàn bộ ứng dụng
            if (database != null && database.isOpen()) database.close();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc tài khoản không tồn tại
    }


    // Hàm khởi tạo cơ sở dữ liệu
    private void initDB() {
        try {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            if (!isTableExists(database, "users")) {
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL)";
                database.execSQL(sql);
                database.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'admin')");
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Lỗi khởi tạo cơ sở dữ liệu: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các view
        edtUserName                 = findViewById(R.id.edt_username);
        edtPassword                 = findViewById(R.id.edt_password);
        btnLogin                    = findViewById(R.id.btn_login);
        ImageView imgGoogle         = findViewById(R.id.img_google);
        TextView txtCreate_account  = findViewById(R.id.txt_create_account);

        // Mở cơ sở dữ liệu và khởi tạo bảng nếu cần
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        initDB();

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtUserName.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập tài khoản", Toast.LENGTH_SHORT).show();
                    edtUserName.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    edtPassword.requestFocus();
                } else if (isUser(email, password)) {
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện với Google (demo)
        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "Đăng nhập bằng Google chưa được triển khai", Toast.LENGTH_SHORT).show();
            }
        });

        txtCreate_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
