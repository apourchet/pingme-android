package com.antoinepourchet.pingme;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PingsFragment extends TabbedFragment {

    public static final String TAG = "PingsFragment";

    private View rootView;
    private PingsListView pingsListView;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        rootView = inflater.inflate(R.layout.pings_fragment, container, false);
        pingsListView = (PingsListView) rootView.findViewById(R.id.pings_lv);
        pingsListView.setListAdapter();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        new PingBroadcastReceiver(pingsListView).listen(getContext());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        fab.hide();
    }

    @Override
    public void onFocus() {
        if (fab != null) {
            fab.hide();
        }
    }

    @Override
    public String getTabTitle() {
        return "PINGS";
    }
}
