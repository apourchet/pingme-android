package com.antoinepourchet.pingme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        Log.d(TAG, "onCreateView");

        rootView = inflater.inflate(R.layout.pings_fragment, container, false);

        pingsListView = (PingsListView) rootView.findViewById(R.id.pings_lv);
        pingsListView.setListAdapter();
        new PingBroadcastReceiver(pingsListView).listen(getContext());

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.d(TAG, "onInflate");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onFocus() {
        if (getActivity() == null) return;
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        Log.d(TAG, "onFocus: " + fab);
        if (fab != null) {
            fab.hide();
        }
    }

    @Override
    public String getTabTitle() {
        return "PINGS";
    }
}
