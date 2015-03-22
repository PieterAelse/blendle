package com.piotapps.blendle.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piotapps.blendle.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularTodayFragment extends Fragment {

    public PopularTodayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_today, container, false);
    }
}
