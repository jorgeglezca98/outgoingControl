package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Vector;

import javax.annotation.Nonnull;

public class mainPagerAdapter extends FragmentStatePagerAdapter {

    private Vector<Fragment> fragments;

    public mainPagerAdapter(FragmentManager fm,@Nonnull Vector<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
