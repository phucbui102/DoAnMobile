package com.example.doanmobile;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class AddPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker
    private ImageView selectedImageView,BackBtn;
    private EditText contentEditText;
    private ImageButton addImageButton;
    private Button postButton;
    private Uri selectedImageUri;
    private TextView Text_add_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initialize views
        addImageButton = findViewById(R.id.Add_image);
        selectedImageView = findViewById(R.id.Post_Image);
        contentEditText = findViewById(R.id.Content);
        postButton = findViewById(R.id.post);
        BackBtn = findViewById(R.id.BackBtn);
        Text_add_image = findViewById(R.id.Text_add_image);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPost.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Set click listener on Add Image Button
        addImageButton.setOnClickListener(view -> openImagePicker());

        // Set click listener on Post Button
        postButton.setOnClickListener(view -> {
            if (contentEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mô tả!", Toast.LENGTH_SHORT).show();
            } else {
                saveImageToDatabase();
                Intent intent = new Intent(AddPost.this , MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openImagePicker() {
        // Create an intent to open image gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Filter for images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Get the URI of the selected image
            if (selectedImageUri != null) {

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentEditText.getLayoutParams();

// Đặt chiều cao mới (ví dụ: 200dp)
                params.height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

// Áp dụng LayoutParams mới
                contentEditText.setLayoutParams(params);
                selectedImageView.setVisibility(View.VISIBLE);
                Text_add_image.setText("Ảnh khác");

                selectedImageView.setImageURI(selectedImageUri); // Display the image in the ImageView
            } else {
                Toast.makeText(this, "Không thể chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToDatabase() {
        SQLiteDatabase db = null;
        try {
            // Get ContentResolver
            ContentResolver contentResolver = getContentResolver();

            // Check if image was selected
            if (selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get description from EditText
            String content = contentEditText.getText().toString();

            // Get image name from URI
            String fileName = getFileNameFromUri(selectedImageUri);
            if (fileName == null) {
                fileName = "default_image.jpg"; // Fallback if name is not found
            }

            // Create InputStream from the selected image URI
            InputStream inputStream = contentResolver.openInputStream(selectedImageUri);
            byte[] imageData = new byte[inputStream.available()];
            inputStream.read(imageData);
            inputStream.close();

            // Create or open the database
            db = openOrCreateDatabase("app_database.db", MODE_PRIVATE, null);

            // Create table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Post (_id INTEGER PRIMARY KEY AUTOINCREMENT, image BLOB, content TEXT)";
            db.execSQL(createTableQuery);

            // Insert image and description into the database
            String insertQuery = "INSERT INTO Post (image, content) VALUES (?, ?)";
            db.execSQL(insertQuery, new Object[]{imageData, content});

            // Notify the user
            Toast.makeText(this, "Ảnh và mô tả đã được lưu vào cơ sở dữ liệu", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lưu ảnh và mô tả thất bại!", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();  // Close the database
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment(); // Fallback to last path segment if name is not found
        }

        return result;
    }
}
