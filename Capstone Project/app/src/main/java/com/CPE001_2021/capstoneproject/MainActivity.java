package com.CPE001_2021.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    FragmentAdapter adapter;
    public static LoadingDialog ld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ld = new LoadingDialog(this,"Loading");
        SetUp_TabLayout();
    }

    private void SetUp_TabLayout(){
        Toolbar toolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // SETUP VIEW PAGER
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm,getLifecycle());
        viewPager.setAdapter(adapter);

        // ADD ALL THE FRAGMENTS
        adapter.addFragment(new fragmentFridge(),"Fridge");
        adapter.addFragment(new fragmentRecipe(),"Recipe");
        adapter.addFragment(new fragmentProfile(),"Profile");

        // WHEN TAB IS SELECTED, CHANGE VIEW PAGE
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // WHEN VIEW PAGE WAS CHANGED, UPDATE TAB
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }



}

