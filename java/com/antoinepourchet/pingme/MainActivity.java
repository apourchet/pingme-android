package com.antoinepourchet.pingme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(getApplicationContext(), ListenService.class);
        getApplicationContext().startService(serviceIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(mSectionsPagerAdapter);

        LogPrefsOnStart();
    }

    private void LogPrefsOnStart() {
        Log.d(TAG, "CHECKING STORAGE");
        Set<ChannelPreference> channels = PersistentDataManager.getChannels(this);
        for (ChannelPreference c : channels) {
            Log.d(TAG, c.toString());
        }
        List<Ping> pings = PersistentDataManager.getLastPings(this);
        for (Ping p : pings) {
            Log.d(TAG, p.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mViewPager.setCurrentItem(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
            implements TabLayout.OnTabSelectedListener {

        public ArrayList<TabbedFragment> tabbedFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabbedFragments = new ArrayList<>();
            tabbedFragments.add(new ChannelsFragment());
            tabbedFragments.add(new PingsFragment());
            tabbedFragments.add(new SettingsFragment());
        }

        @Override
        public TabbedFragment getItem(int position) {
            return tabbedFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            tabbedFragments.get(position).onFocus();
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return tabbedFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabbedFragments.get(position).getTabTitle();
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            for (TabbedFragment tf : tabbedFragments) {
                if (tf.getTabTitle().equals(tab.getText())) {
                    tf.onFocus();
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            for (TabbedFragment tf : tabbedFragments) {
                if (tf.getTabTitle().equals(tab.getText())) {
                    tf.onUnfocus();
                }
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            onTabSelected(tab);
        }
    }
}
