package com.mrg.sxpiyun.adpter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.mrg.sxpiyun.ui.ScheduleFragment;
import com.mrg.sxpiyun.ui.LianxirenFragment;
import com.mrg.sxpiyun.ui.XiaoXiListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2016-07-24.
 */
public class MainAdpter extends FragmentPagerAdapter {

    List<Fragment> list=new ArrayList<Fragment>();
    FragmentManager fm =null;

    public MainAdpter(FragmentManager fm) {
        super(fm);
        this.fm=fm;
        list.add(new XiaoXiListFragment());
//        list.add(new MainContentFragmemt());
        list.add(new LianxirenFragment());
        list.add(new ScheduleFragment());

    }

    @Override

    public Fragment getItem(int position) {
        Fragment fragment = null;
        fragment = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", "" + position);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        Fragment fragment = list.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }
}
