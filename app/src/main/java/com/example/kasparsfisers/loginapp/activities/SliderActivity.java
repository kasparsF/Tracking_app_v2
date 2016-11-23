package com.example.kasparsfisers.loginapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kasparsfisers.loginapp.adapters.MyPagerAdapter;
import com.example.kasparsfisers.loginapp.R;
import com.example.kasparsfisers.loginapp.fragments.FirstFragment;
import com.example.kasparsfisers.loginapp.fragments.SecondFragment;
import com.example.kasparsfisers.loginapp.fragments.ThirdFragment;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class SliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set the content of the activity to use the login_activity.xmll layout file
        setContentView(R.layout.activity_slide);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);

        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new FirstFragment());
        fragments.add(new ThirdFragment());

        // Create an adapter that knows which fragment should be shown on each page
        MyPagerAdapter adapter = new MyPagerAdapter(this, getSupportFragmentManager(), fragments);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);


        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

    }

}
