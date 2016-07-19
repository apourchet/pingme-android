package com.antoinepourchet.pingme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ChannelsListView extends ListView {

    public static final String TAG = "ChannelsListView";

    private ArrayAdapter<String> adapter;

    public ChannelsListView(Context context) {
        super(context);
    }

    public ChannelsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChannelsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListAdapter() {
        if (adapter == null) {
            adapter = new ArrayAdapter<>(getContext(), R.layout.channels_fragment_row);
            adapter.addAll(PersistentDataManager.getChannels(getContext()));
        }
        setAdapter(adapter);
    }

    public void refreshListAdapter() {
        adapter = null;
        setListAdapter();
    }
}
