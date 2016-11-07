package com.example.kasparsfisers.loginapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public  class MyPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private ArrayList<Fragment> fragmentList;


//    public MyPagerAdapter(Context context, FragmentManager fm) {
//        super(fm);
//        mContext = context;
//    }


    public MyPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        if(fragmentList == null){
            return  null;
        }
        if(position < fragmentList.size()){
            return  fragmentList.get(position);
        }
        else {
            return fragmentList.get(0);
        }
    }

    public  void  addNewItem(Fragment f){
        if(f !=  null) {
            this.fragmentList.add(f);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
