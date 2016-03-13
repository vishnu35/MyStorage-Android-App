package com.vishnu.www.mystorage.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vishnu.www.mystorage.Model.MyExt;
import com.vishnu.www.mystorage.Model.MyFile;
import com.vishnu.www.mystorage.R;

import java.util.List;

/**
 * Created by Wishnu on 3/12/2016.
 */
public class ExtListAdapter extends ArrayAdapter<MyExt> {
    public ExtListAdapter(Context context, int resource, List<MyExt> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyExt ext = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_name);
        TextView size = (TextView) convertView.findViewById(R.id.list_size);

        name.setText(ext.getExtension());
        size.setText(ext.getCount() + "");
        return convertView;
    }

    @Override
    public MyExt getItem(int position) {
        return super.getItem(position);
    }
}
