package com.malmstein.eddystonesample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.malmstein.eddystonesample.account.AccountFragment;
import com.malmstein.eddystonesample.nearby.NearbyFragment;
import com.malmstein.eddystonesample.scan.ScanFragment;

public class EddystonePagerAdapter extends FragmentStatePagerAdapter {

    private String[] pages = {"Account", "Scan", "Nearby"};

    public EddystonePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
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
            case 0:
                return new AccountFragment();
            case 1:
                return new ScanFragment();
            case 2:
                return new NearbyFragment();
            default:
                return new AccountFragment();
        }
    }

}
