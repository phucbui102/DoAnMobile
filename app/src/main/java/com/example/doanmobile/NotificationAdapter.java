package com.example.doanmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {
    private final Context context;
    private final String[] names;
    private final String[] contents;
    private final int[] avatars;
    private final int[] posts;

    // Constructor to initialize data
    public NotificationAdapter(Context context, String[] names, String[] contents, int[] avatars, int[] posts) {
        this.context = context;
        this.names = names;
        this.contents = contents;
        this.avatars = avatars;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return names.length; // Assuming all arrays are of the same length
    }

    @Override
    public Object getItem(int position) {
        return names[position]; // Return name as the item
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Reuse convertView for better performance
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_notification, parent, false);
            holder = new ViewHolder();
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView); // Replace with actual ID
            holder.nameTextView = convertView.findViewById(R.id.nameTextView); // Replace with actual ID
            holder.contentTextView = convertView.findViewById(R.id.contentTextView); // Replace with actual ID
            holder.postImageView = convertView.findViewById(R.id.postImageView); // Replace with actual ID
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set data to views
        holder.avatarImageView.setImageResource(avatars[position]);
        holder.nameTextView.setText(names[position]);
        holder.contentTextView.setText(contents[position]);
        holder.postImageView.setImageResource(posts[position]);

        return convertView;
    }

    // ViewHolder to optimize performance
    private static class ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView contentTextView;
        ImageView postImageView;
    }
}
