package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.util.Vector;

public class settingFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        bindDeleteCategory(view);
        bindResetCurrentMoney(view);

        return view;
    }

    private void bindResetCurrentMoney(View view) {
    }

    private void bindDeleteCategory(View view) {
        ViewPager categoriesViewPager = view.findViewById(R.id.viewPagerCategories);
        final Vector<Fragment> viewPagerFragments = new Vector<>();
        viewPagerFragments.add(new deleteCategoryFragment());
        categoriesViewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return viewPagerFragments.get(i);
            }

            @Override
            public int getCount() {
                return viewPagerFragments.size();
            }
        });
    }
}
