package com.locon.withu.android.ui.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.locon.withu.Constants;
import com.locon.withu.android.ui.ChannelsFragment;
import com.locon.withu.android.ui.LocationsFragment;
import com.locon.withu.android.ui.StoriesFragment;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private final List<String> mFragmentNames;

    public MyPagerAdapter(FragmentManager fm, List<String> fragmentNames) {
        super(fm);
        mFragmentNames = fragmentNames;
    }

    @Override
    public Fragment getItem(int position) {
        String fragmentName = mFragmentNames.get(position);
        switch (fragmentName) {
            case Constants.FragmentNames.STORIES_FRAGMENT:
                return new StoriesFragment();

            case Constants.FragmentNames.CHANNELS_FRAGMENT:
                return new ChannelsFragment();

            case Constants.FragmentNames.LOCATIONS_FRAGMENT:
                return new LocationsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNames.get(position);
    }
}
