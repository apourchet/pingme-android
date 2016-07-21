package com.antoinepourchet.pingme;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PingsListView extends ListView implements Pingable {

    public static final String TAG = "PingsListView";

    private ArrayAdapter<String> adapter;

    public PingsListView(Context context) {
        super(context);
    }

    public PingsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PingsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListAdapter() {
        if (adapter == null) {
            adapter = new ArrayAdapter<>(getContext(), R.layout.pings_fragment_row);
            for (Ping p : PersistentDataManager.getLastPings(getContext())) {
                adapter.add(p.getChannelId() + " -> " + p.getMessage());
            }
        }
        setAdapter(adapter);
    }

    public void refreshAdapter() {
        adapter = null;
        setListAdapter();
    }

    @Override
    public void onPing(Ping ping) {
        Log.d(TAG, "GOT PING!");
        refreshAdapter();
    }

//    public class PingsAdapter extends ArrayAdapter<Ping> {
//
//        public PingsAdapter(Context context, int resource) {
//            super(context, resource);
//        }
//
//        public PingsAdapter(Context context, int resource, List<Ping> objects) {
//            super(context, resource, objects);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                LayoutInflater vi;
//                vi = LayoutInflater.from(getContext());
//                View v = vi.inflate(R.layout.pings_fragment_row, null);
//            }
//            return super.getView(position, convertView, parent);
//        }
//    }
}
