package com.example.doanmobile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewCategory extends BaseAdapter {

    private final Context context;
    private final String[] itemNames;
    private final int[] itemImages;

    // Constructor
    public GridViewCategory(Context context, String[] itemNames, int[] itemImages) {
        this.context = context;
        this.itemNames = itemNames;
        this.itemImages = itemImages;
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public Object getItem(int position) {
        return itemNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate the grid item layout
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_gridview_vategory, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.itemImage);
            holder.textView = convertView.findViewById(R.id.itemName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set data for the current grid item
        holder.imageView.setImageResource(itemImages[position]);
        holder.textView.setText(itemNames[position]);

        return convertView;
    }

    // ViewHolder pattern for better performance
    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
