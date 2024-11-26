package com.example.doanmobile;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class PostAdapter extends BaseAdapter {

    public int Status = 0;
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // Get current post
        Post post = posts.get(position);

        // Initialize views
        TextView contentTextView = convertView.findViewById(R.id.titletext);
        ImageView postImageView = convertView.findViewById(R.id.itemImage);
        ImageView likeBtn = convertView.findViewById(R.id.likeBtn);
        ImageView chatBtn = convertView.findViewById(R.id.chatBtn);
        TextView TextChat = convertView.findViewById(R.id.TextChat);
        ImageView menuImage = convertView.findViewById(R.id.menuImage);


        // Set the like button click listener for each item in the list
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hide the like button for this item

                MainActivity.incrementNotificationCount(context);
                if(Status==0)
                {
                    Status = 1;
                    likeBtn.setImageResource(R.drawable.red_heart);
                }
                else
                {
                    likeBtn.setImageResource(R.drawable.heart);
                    Status = 0;
                }


            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showBottomSheet(context);
            }
        });
        TextChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showBottomSheet(context);
            }
        });

        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showMenu(context,view);
            }
        });
        // Set content text
        contentTextView.setText(post.getContent());


        // Convert byte array to Bitmap and set image
        if (post.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
            postImageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
}
