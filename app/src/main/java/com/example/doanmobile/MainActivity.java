package com.example.doanmobile;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.doanmobile.ListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static int count_notifi = 0;
    private List<Post> postList;
    private PostAdapter postAdapter;
    private SQLiteDatabase db;
    private ImageView addBtn,likeBtn,homeBtn,chatBtn;
    public static TextView notificationCountTextView;

    private static final String CHANNEL_ID = "example_channel_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View postView = getLayoutInflater().inflate(R.layout.list_item, null);

        // Find the likeBtn in the post layout (activity_post.xml)
        notificationCountTextView = findViewById(R.id.notification_icon_count);
        likeBtn = postView.findViewById(R.id.likeBtn);
        chatBtn = findViewById(R.id.chatBtn);
        ListView listView = findViewById(R.id.itemList);
        homeBtn = findViewById(R.id.homeBtn);
        postList = new ArrayList<>();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBtn.setVisibility(View.GONE);  // Ẩn nút likeBtn
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });
        fetchPostData();

        postAdapter = new PostAdapter(this, postList);
        listView.setAdapter(postAdapter);
        // ListView setup

//        String[] items = {"điện thoại cũ", "bàn phím cơ", "kệ để dép", "giày cũ", "hehe", "Item 6"};
//        int[] images = {R.drawable.sanpham1, R.drawable.sanpham1, R.drawable.sanpham1, R.drawable.menu, R.drawable.menu, R.drawable.menu};
//
//        ListAdapter adapter = new ListAdapter(this, items, images);
//        listView.setAdapter(adapter);

//        listView.setOnItemClickListener((adapterView, view, position, id) -> {
//            Toast.makeText(this, "Clicked: " + items[position], Toast.LENGTH_SHORT).show();
//        });

        ImageView notificationBtn = findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);
            }
        });

        ImageView Find = findViewById(R.id.searchBtn);
        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Find.class);
                startActivity(intent);
            }
        });

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPost.class);
                startActivity(intent);
            }
        });


        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });




        // Footer buttons
//        ImageView profileBtn = findViewById(R.id.profileBtn);
//        profileBtn.setOnClickListener(v -> Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show());
//
//        ImageView chatBtn = findViewById(R.id.chatBtn);
//        chatBtn.setOnClickListener(v -> Toast.makeText(this, "Chat Clicked", Toast.LENGTH_SHORT).show());
    }

    private void setupBottomSheetViews(BottomSheetDialog bottomSheetDialog, View bottomSheetView) {
        // Ánh xạ các View trong layout
        ImageButton btn1 = bottomSheetView.findViewById(R.id.btn1);
//        TextInputLayout textInputLayout = bottomSheetView.findViewById(R.id.textFieldLayout);
//        TextInputEditText editText = bottomSheetView.findViewById(R.id.editText);
//        MaterialButton dismissBtn = bottomSheetView.findViewById(R.id.dismiss);

        // Xử lý sự kiện cho nút "Dismiss"
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Được chưa", Toast.LENGTH_SHORT).show();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

//        dismissBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String inputText = Objects.requireNonNull(editText.getText()).toString().trim();
//
//                if (inputText.isEmpty()) {
//                    textInputLayout.setError("Please type some text"); // Hiển thị lỗi
//                } else {
//                    textInputLayout.setError(null); // Xóa lỗi nếu có
//                    Toast.makeText(MainActivity.this, inputText, Toast.LENGTH_SHORT).show();
//
//                    // Đóng Bottom Sheet
//                    bottomSheetDialog.dismiss();
//                }
//            }
//        });
    }

    public static void showMenu(Context context,View view)
    {
        MainActivity activity = (MainActivity) context;

        PopupMenu popupMenu = new PopupMenu(activity, view);

        // Gán menu resource vào PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.add_del_edit_menu, popupMenu.getMenu());

        // Xử lý sự kiện khi chọn menu item
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_edit:
//                        Toast.makeText(MainActivity.this, "Edit selected", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.action_delete:
//                        Toast.makeText(MainActivity.this, "Delete selected", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.action_share:
//                        Toast.makeText(MainActivity.this, "Share selected", Toast.LENGTH_SHORT).show();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });

        // Hiển thị PopupMenu
        popupMenu.show();

    }

    public static void showBottomSheet(Context context)
    {
        MainActivity activity = (MainActivity) context;

        activity.showBottomSheetDialog();

    }

    private void showBottomSheetDialog() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);

        // Inflate layout cho Bottom Sheet
        View bottomSheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottomsheet, null);

        // Thiết lập nội dung cho Bottom Sheet
        bottomSheetDialog.setContentView(bottomSheetView);

        // Thiết lập các sự kiện trong Bottom Sheet
        setupBottomSheetViews(bottomSheetDialog, bottomSheetView);

        // Hiển thị Bottom Sheet
        bottomSheetDialog.show();

        // Lắng nghe sự kiện khi Bottom Sheet bị dismiss
//        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                Toast.makeText(MainActivity.this, "Bottom sheet dismissed", Toast.LENGTH_SHORT).show();
//            }
//        });
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell) // Thay bằng icon của bạn
                .setContentTitle("Tiêu đề thông báo")
                .setContentText("Nội dung của thông báo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        // Kiểm tra quyền trước khi hiển thị thông báo (dành cho Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }

        // Hiển thị thông báo
        notificationManager.notify(1, builder.build());
    }

    public static void incrementNotificationCount(Context context) {
        count_notifi++;
        notificationCountTextView.setVisibility(View.VISIBLE);
        notificationCountTextView.setText(String.valueOf(count_notifi));

        // Create and show notification
        MainActivity activity = (MainActivity) context;
        activity.showNotification();
        activity.createNotificationChannel();

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