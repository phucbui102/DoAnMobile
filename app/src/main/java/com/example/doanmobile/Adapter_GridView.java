package com.example.doanmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class Adapter_GridView extends BaseAdapter {

    private Context context;
    private List<Post> posts;

    public Adapter_GridView(Context context, List<Post> posts) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_post, parent, false);
            holder = new ViewHolder();
            holder.postImageView = convertView.findViewById(R.id.itemImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Post post = posts.get(position);

        // Set image
        if (post.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length);
            holder.postImageView.setImageBitmap(bitmap);
        }

        // Set menu button click listener
       ;

        return convertView;
    }

    static class ViewHolder {
        ImageView postImageView;
        ImageView menuImage;
    }
}
