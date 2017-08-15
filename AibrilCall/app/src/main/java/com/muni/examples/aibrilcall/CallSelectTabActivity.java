package com.muni.examples.aibrilcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Developer on 2017-08-12.
 */
public class CallSelectTabActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private  ViewPager viewPager;
    private CallViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.three_tablayout);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new CallViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment("긍정/부정", new fragment_page_one());
        adapter.addFragment("키워드", new fragment_page_two());
        adapter.addFragment("감정", new fragment_page_three());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }




}
