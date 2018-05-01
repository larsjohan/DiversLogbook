package no.ntnu.diverslogbook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.diverslogbook.fragments.DiveFragment;
import no.ntnu.diverslogbook.fragments.LogFragment;
import no.ntnu.diverslogbook.fragments.PlanFragment;
import no.ntnu.diverslogbook.fragments.ProfileFragment;
import no.ntnu.diverslogbook.util.DiveTable;


/**
 * Main activity, first activity launched.
 *
 * Used this example for tabs:
 * http://www.gadgetsaint.com/android/create-viewpager-tabs-android/#.WsXz84huZhF
 *
 */
public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;


    /**
     * Init tab icons from drawable folder.
     */
    private int[] tabIcons = {
            R.drawable.ic_profile,
            R.drawable.ic_plan,
            R.drawable.ic_dive,
            R.drawable.ic_log,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set listener on preferences.
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(new SettingsHandler(this));
        

        // Setup the tab navigation.
        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new ProfileFragment(),  "Profile");
        adapter.addFragment(new PlanFragment(),     "Plan");
        adapter.addFragment(new DiveFragment(),     "Dive");
        adapter.addFragment(new LogFragment(),      "Log");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }



    /**
     * Adding icons to tab.
     */
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }


    /**
     * Adds the Settings button to the action bar.
     *
     * @param menu the menu to add the button to
     * @return State of the operation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    /**
     * Called when an item in the action bar is pressed.
     *
     * @param item The pressed button
     * @return whether the operation is a success or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.action_preferences){
            try {
                Intent pref = new Intent(this, SettingsActivity.class);

                startActivity(pref);
                return true;
            } catch ( ActivityNotFoundException e) {
                e.printStackTrace();
            }

        }

        return false;
    }



    /**
     * Called when preferences change
     */
    public void updateMembersFromPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        this.url = pref.getString("rssurl", "");
        this.frequency = Integer.parseInt(pref.getString("frequency", getString(R.string.frequencyDefault)));
        this.feedSize = Integer.parseInt(pref.getString("feedSize", getString(R.string.itemsDefault)));
        */

    }


    /**
     * Override this function so that fragments can use it.
     *
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data Data from activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
