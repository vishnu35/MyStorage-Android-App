package com.vishnu.www.mystorage.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vishnu.www.mystorage.Helpers.ExtListAdapter;
import com.vishnu.www.mystorage.Helpers.FileListAdapter;
import com.vishnu.www.mystorage.Model.MyExt;
import com.vishnu.www.mystorage.R;

import java.util.ArrayList;

public class ExtFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_EXT = "myExts";
    ArrayList<MyExt> myExts;


    public ExtFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static ExtFragment newInstance(ArrayList<MyExt> myExts) {
        ExtFragment fragment = new ExtFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXT, myExts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myExts = (ArrayList<MyExt>) getArguments().getSerializable(ARG_EXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ext, container, false);
        ListView lv_topExts = (ListView)rootView.findViewById(R.id.lv_topExts);
        ExtListAdapter extListAdapter = new ExtListAdapter(container.getContext(),R.layout.listitem,myExts);
        lv_topExts.setAdapter(extListAdapter);

        return rootView;
    }
}
