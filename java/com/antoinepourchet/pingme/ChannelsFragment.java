package com.antoinepourchet.pingme;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChannelsFragment extends TabbedFragment
        implements View.OnClickListener, AddChannelDialog.Listener {

    public static final String TAG = "ChannelsFragment";

    private FloatingActionButton fab;
    private ChannelsListView channelsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.channels_fragment, container, false);

        channelsListView = (ChannelsListView) rootView.findViewById(R.id.channels_lv);
        channelsListView.setListAdapter();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onFocus() {
        if (getActivity() == null) return;
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.show();
        }
    }

    @Override
    public String getTabTitle() {
        return "CHANNELS";
    }

    public void processFabClick() {
        AddChannelDialog dialog = new AddChannelDialog();
        dialog.setListener(this);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        dialog.show(fragmentManager, "AddChannelDialog");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                processFabClick();
        }
    }

    @Override
    public void onPositiveClick(String host, String channelId) {
        Log.d(TAG, "onPositiveClick");
        ListenService.getInstance().startListen(host, channelId);
        PersistentDataManager.addChannel(getContext(), new ChannelPreference(host, channelId));
        channelsListView.refreshListAdapter();
    }
}
