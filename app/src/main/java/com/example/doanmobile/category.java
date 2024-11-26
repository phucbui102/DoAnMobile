package com.example.doanmobile;


import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class category extends AppCompatActivity {

    // Sample data for the GridView
    String[] itemNames = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
    int[] itemImages = {
            R.drawable.sanpham1, // Replace with your drawable resource IDs
            R.drawable.sanpham1,
            R.drawable.sanpham1,
            R.drawable.sanpham1,
            R.drawable.sanpham1,
            R.drawable.sanpham1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Find the GridView in the layout
        GridView gridView = findViewById(R.id.itemGrid);

        // Set up the adapter
        GridViewCategory adapter = new GridViewCategory(this, itemNames, itemImages);
        gridView.setAdapter(adapter);

        // Set an item click listener
        gridView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            // Display a toast with the clicked item's name
            Toast.makeText(this, "Clicked: " + itemNames[position], Toast.LENGTH_SHORT).show();
        });
    }
}
