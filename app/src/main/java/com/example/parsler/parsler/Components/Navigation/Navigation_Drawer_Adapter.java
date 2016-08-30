package com.example.parsler.parsler.Components.Navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parsler.parsler.Models.NavigationDrawerObject;
import com.example.parsler.parsler.R;

public class Navigation_Drawer_Adapter extends ArrayAdapter<NavigationDrawerObject> {
    private static Context context;
    private final int layoutResourceId;
    private NavigationDrawerObject data[] = null;

    public Navigation_Drawer_Adapter(Context context, int layoutResourceId, NavigationDrawerObject[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.navDrawerImageView);
        TextView textView = (TextView) v.findViewById(R.id.navDrawerTextView);

        NavigationDrawerObject choice = data[position];

        imageView.setImageResource(choice.icon);
        textView.setText(choice.name);

        return v;
    }
}