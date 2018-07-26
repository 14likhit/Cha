package com.example.likhit.cha.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.likhit.cha.R;

public class ActivitySteps extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String appName;
    private int appId;
    private int questionId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        toolbar=(Toolbar)findViewById(R.id.toolbar_steps);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle appListQuestionData=getIntent().getExtras();

        if (appListQuestionData==null){
            return;
        }

        appName=appListQuestionData.getString("appName");
        appId=appListQuestionData.getInt("appId");
        questionId=appListQuestionData.getInt("questionId");

        getSupportActionBar().setTitle(appName);


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPageAdapter extends FragmentPagerAdapter{
        public ViewPageAdapter(FragmentManager fm) {super(fm);}

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Step " + (position+1);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Fragment_steps.newInstance(1, appName,appId,questionId);
                case 1: // Fragment # 0 - This will show SecondFragment
                    return Fragment_steps.newInstance(2, appName,appId,questionId);
                case 2: // Fragment # 1 - This will show ThirdFragment
                    return Fragment_steps.newInstance(3, appName,appId,questionId);
                case 3: // Fragment # 1 - This will show FourthFragment
                    return Fragment_steps.newInstance(4, appName,appId,questionId);
                case 4: // Fragment # 1 - This will show FifthFragment
                    return Fragment_steps.newInstance(5, appName,appId,questionId);
                // return FragmentSteps.newInstance(5, "Step # 5",app,appname);
                default:
                    return null;
            }
        }

        public int getCount(){
            return 5;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
