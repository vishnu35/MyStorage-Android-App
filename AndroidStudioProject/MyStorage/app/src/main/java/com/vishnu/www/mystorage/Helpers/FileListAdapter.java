package com.vishnu.www.mystorage.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vishnu.www.mystorage.Model.MyFile;
import com.vishnu.www.mystorage.R;

import java.util.List;

/**
 * Created by Wishnu on 3/12/2016.
 */
    public class FileListAdapter extends ArrayAdapter<MyFile> {

    public FileListAdapter(Context context, int resource, List<MyFile> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyFile file = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_name);
        TextView size = (TextView) convertView.findViewById(R.id.list_size);

        name.setText(file.getName());
        size.setText(Helper.getSize(file.getSize()));
        return convertView;
    }

    @Override
    public MyFile getItem(int position) {
        return super.getItem(position);
    }
}
