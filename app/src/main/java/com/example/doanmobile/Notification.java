package com.example.doanmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Notification extends AppCompatActivity {

    ImageView HomeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        HomeBtn = findViewById(R.id.homeBtn);
        // Sample data
        String[] namesArray = {"John Doe", "Jane Smith", "Sam Wilson"};
        String[] contentsArray = {
                "Liked your post",
                "Commented: 'Great job!'",
                "Shared your post"
        };
        int[] avatarsArray = {
                R.drawable.avthehe, // Replace with actual drawable resource IDs
                R.drawable.avthehe,
                R.drawable.avthehe
        };
        int[] postsArray = {
                R.drawable.sanpham1, // Replace with actual drawable resource IDs
                R.drawable.sanpham1,
                R.drawable.sanpham1
        };

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // Initialize ListView and Adapter
        ListView listView = findViewById(R.id.NotifList); // Replace with actual ID in XML
        NotificationAdapter adapter = new NotificationAdapter(this, namesArray, contentsArray, avatarsArray, postsArray);
        listView.setAdapter(adapter);
    }
}
