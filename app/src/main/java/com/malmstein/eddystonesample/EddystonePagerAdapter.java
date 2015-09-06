package com.malmstein.eddystonesample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.malmstein.eddystonesample.account.AccountFragment;
import com.malmstein.eddystonesample.nearby.NearbyFragment;
import com.malmstein.eddystonesample.scan.ScanFragment;

public class EddystonePagerAdapter extends TaggedFragmentStatePagerAdapter {

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".EDDYSTONE_FRAGMENT#";

    public static final int POSITION_ACCOUNT = 0;
    public static final int POSITION_SCAN = 1;
    public static final int POSITION_NEARBY = 2;

    private String[] pages = {"Account", "Scan", "Nearby"};

    public EddystonePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public String getTag(int position) {
        return TAG_TEMPLATE + position;
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITION_ACCOUNT:
                return new AccountFragment();
            case POSITION_SCAN:
                return new ScanFragment();
            case POSITION_NEARBY:
                return new NearbyFragment();
            default:
                return new AccountFragment();
        }
    }

}
