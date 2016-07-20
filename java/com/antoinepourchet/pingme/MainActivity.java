package com.antoinepourchet.pingme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

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
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(mSectionsPagerAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            tabbedFragments.add(new PingsFragment());
            tabbedFragments.add(new ChannelsFragment());
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
