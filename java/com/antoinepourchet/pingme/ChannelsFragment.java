package com.antoinepourchet.pingme;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class ChannelsFragment extends TabbedFragment implements View.OnClickListener {

    public static final String TAG = "ChannelsFragment";

    private FloatingActionButton fab;
    private ChannelsListView channelsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        View rootView = inflater.inflate(R.layout.channels_fragment, container, false);
        channelsListView = (ChannelsListView) rootView.findViewById(R.id.channels_lv);
        channelsListView.setListAdapter();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);

        return rootView;
    }

    // TODO This fab stuff doesnt work. Same for other fragments.
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        fab.hide();
    }

    @Override
    public void onFocus() {
        if (fab != null) {
            fab.show();
        }
    }

    @Override
    public String getTabTitle() {
        return "CHANNELS";
    }

    public void processFabClick() {
        String channelId = new Random().nextInt(10000) + "";
        ListenService.getInstance().startListen(channelId);
        PersistentDataManager.addChannel(getContext(), channelId);
        channelsListView.refreshListAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                processFabClick();
        }
    }
}
