package com.vishnu.www.mystorage.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vishnu.www.mystorage.Helpers.FileListAdapter;
import com.vishnu.www.mystorage.Model.MyFile;
import com.vishnu.www.mystorage.R;

import java.util.ArrayList;

public class FileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILES = "myfiles";

    // TODO: Rename and change types of parameters
    private ArrayList<MyFile> myFiles;

    public FileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(ArrayList<MyFile> myFiles) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILES, myFiles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myFiles = (ArrayList<MyFile>) getArguments().getSerializable(ARG_FILES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        ListView lv_topFiles = (ListView)rootView.findViewById(R.id.lv_topFiles);
        FileListAdapter fileListAdapter = new FileListAdapter(container.getContext(),R.layout.listitem,myFiles);
        lv_topFiles.setAdapter(fileListAdapter);
        return rootView;
    }
}
