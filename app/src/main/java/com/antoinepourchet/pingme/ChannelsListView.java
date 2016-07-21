package com.antoinepourchet.pingme;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;


public class ChannelsListView extends ListView {

    public static final String TAG = "ChannelsListView";

    private ChannelListAdapter adapter;

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
            adapter = new ChannelListAdapter(getContext(), R.layout.channels_fragment_row);
            adapter.addAll(PersistentDataManager.getChannels(getContext()));
        }
        setAdapter(adapter);
    }

    public void refreshListAdapter() {
        adapter = null;
        setListAdapter();
    }

    public class ChannelListAdapter extends ArrayAdapter<ChannelPreference> {

        public ChannelListAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (convertView == null) {
                LayoutInflater li = LayoutInflater.from(getContext());
                v = li.inflate(R.layout.channels_fragment_row, null);
            }
            final ChannelPreference channel = getItem(position);

            TextView tt1 = (TextView) v.findViewById(R.id.row_channelname);
            tt1.setText(channel.getId());

            ToggleButton tb = (ToggleButton) v.findViewById(R.id.channel_row_toggle);
            tb.setChecked(channel.doListen());

            ImageButton ib = (ImageButton) v.findViewById(R.id.channel_row_remove_button);

            ib.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChannelListAdapter.this.remove(channel);
                    ChannelListAdapter.this.notifyDataSetChanged();
                    PersistentDataManager.removeChannel(getContext(), channel);
                    ListenService.getInstance().stopListen(channel.getHost(), channel.getId());
                }
            });
            tb.setOnCheckedChangeListener(new ChannelToggleListener(channel));
            return v;
        }
    }

    public class ChannelToggleListener implements CompoundButton.OnCheckedChangeListener {

        public final ChannelPreference channel;

        public ChannelToggleListener(ChannelPreference channel) {
            this.channel = channel;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d(TAG, "onCheckChanged: " + b);
            channel.setListen(b).save(getContext());
            if (!b) {
                ListenService.getInstance().stopListen(channel.getHost(), channel.getId());
            } else {
                ListenService.getInstance().startListen(channel.getHost(), channel.getId());
            }
        }
    }
}
