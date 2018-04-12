package no.ntnu.diverslogbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.diverslogbook.fragments.DiveFragment;
import no.ntnu.diverslogbook.fragments.LogFragment;
import no.ntnu.diverslogbook.fragments.PlanFragment;
import no.ntnu.diverslogbook.fragments.ProfileFragment;


/**
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
            R.drawable.ic_profile_black_24dp,
            R.drawable.ic_plan_black_24dp,
            R.drawable.ic_dive_24dp,
            R.drawable.ic_log_black_24dp,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
