package com.muni.examples.aibrilcall;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 2017-08-12.
 */

class CallViewPagerAdapter extends FragmentPagerAdapter {

        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CallViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String title, android.support.v4.app.Fragment fragment) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
}

