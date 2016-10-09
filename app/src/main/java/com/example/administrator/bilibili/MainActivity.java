package com.example.administrator.bilibili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.administrator.bilibili.adapter.CommonFragmentAdapter;
import com.example.administrator.bilibili.fragment.BaseFragment;
import com.example.administrator.bilibili.fragment.CateGoryFragment;
import com.example.administrator.bilibili.fragment.DiscoveryFragment;
import com.example.administrator.bilibili.fragment.FollowFragment;
import com.example.administrator.bilibili.fragment.LiveFragment;
import com.example.administrator.bilibili.fragment.MovieFragment;
import com.example.administrator.bilibili.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.bilibili.R.id.main_header_navigationView;
import static com.example.administrator.bilibili.R.id.main_pager;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private ViewPager mPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(mtoolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(main_header_navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        mPager= (ViewPager) findViewById(main_pager);
        if (mPager!=null)
        {
            List<BaseFragment> mFragments=new ArrayList<>();
            mFragments.add(new LiveFragment());
            mFragments.add(new RecommendFragment());
            mFragments.add(new MovieFragment());
            mFragments.add(new CateGoryFragment());
            mFragments.add(new FollowFragment());
            mFragments.add(new DiscoveryFragment());

            CommonFragmentAdapter adapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
            mPager.setAdapter(adapter);

            mTabLayout= (TabLayout) findViewById(R.id.main_tab_layout);
            mTabLayout.setupWithViewPager(mPager);



        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_main_homepage:
                // TODO: 需要切换当前主界面为Home界面
                break;
            case R.id.action_main_offline:
                // TODO: 打开新的Activity
                break;
            case R.id.action_main_fav:
                // TODO: 打开新的Activity
                break;
            default:
                break;
        }
        // TODO: 考虑哪些是替换当前界面, 哪些是新开界面
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START,true);
                break;
        }


        return true;
    }
}
