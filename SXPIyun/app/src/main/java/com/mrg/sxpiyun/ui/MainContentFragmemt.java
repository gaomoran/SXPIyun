package com.mrg.sxpiyun.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxg.sxpi.ui.R;

/**
 * Created by MrG on 2016-07-24.
 */
public class MainContentFragmemt extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
    return view;
    }
}
