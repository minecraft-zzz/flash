package com.google.mlkit.vision.demo.java;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter1 extends FragmentStateAdapter {
    private final List<Fragment> fragments;

    public MyPagerAdapter1(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new ArrayList<>();
        fragments.add(new DiaryFragment());
        fragments.add(new ChartFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    public void setBundle(Bundle bundle) {
        DiaryFragment diaryFragment = (DiaryFragment) fragments.get(0);
        diaryFragment.setArguments(bundle);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
