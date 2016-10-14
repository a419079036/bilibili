package com.example.administrator.bilibili.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.bilibili.R;
import com.example.administrator.bilibili.activity.RecommendWebViewActivity;
import com.example.administrator.bilibili.activity.VideoDeatilActivity;
import com.example.administrator.bilibili.adapter.RecommendListAdapter;
import com.example.administrator.bilibili.client.ClientAPI;
import com.example.administrator.bilibili.model.RecommendBodyItem;
import com.example.administrator.bilibili.model.RecommendItem;
import com.example.administrator.bilibili.presenters.RecommendPresenter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseFragment implements IRecommentView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{
    private RollPagerView mRollViewPager;
    RecommendPresenter mRecommendPresenter;
    private List<RecommendItem> mItems;
    private RecommendListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList<>();
        mAdapter = new RecommendListAdapter(getContext(), mItems);
        //注册Eventbus 事件订阅者
        EventBus.getDefault().register(this);
    }

    /**
     * EventBus 当事件发送过来的时候,调用,
     * 根据threadMode 来设置这个方法在哪个线程中执行
     *
     * @param items
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<RecommendItem> items)
    {
        Log.e("自定义标签", "类名==RecommendFragment" + "方法名==onEvent=====:" + "");
        if (items != null)
        {
            Toast.makeText(getContext(), "收到推荐", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
            mItems.clear();
            mItems.addAll(items);
            mAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onDestroy()
    {
        //注销
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public RecommendFragment()
    {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle()
    {
        return "推荐";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View ret = inflater.inflate(R.layout.fragment_recommend, container, false);
        RecyclerView recyclerView = (RecyclerView) ret.findViewById(R.id.recommend_list);
        mRollViewPager = (RollPagerView) ret.findViewById(R.id.roll_view_pager);
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(2000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        mRollViewPager.setOnClickListener(this);

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getContext(), Color.YELLOW, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);


        if (recyclerView != null)
        {
            /*布局管理器,能够对item进行排版*/
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(lm);
            recyclerView.setAdapter(mAdapter);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) ret.findViewById(R.id.recommend_refresh_layout);
        if (mSwipeRefreshLayout != null)
        {
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        ClientAPI.getRecommendListAsync();
        return ret;

    }


    @Override
    public void setTypeText(String type)
    {

    }

    @Subscribe
    public void OnRecoommendBodyClickEvent(RecommendBodyItem bodyItem)
    {
        //根据goto
        String aGoto = bodyItem.getGoto();

        //av 视频详情 推荐 区域
        //live 直播
        //weblink 网页 weblink,activity
        //bangumi_list 番剧

        Intent intent = null;

        switch (aGoto)
        {
            case "av":
                intent = new Intent(getContext(), VideoDeatilActivity.class);
                intent.putExtra(VideoDeatilActivity.EXTRA_VIDEO_ITEM, bodyItem);
                break;
            case "live":
                break;
            case "weblink":
                break;
            case "bangumi_list":
                break;
            default:
                break;
        }
        if (intent != null)
        {
            startActivity(intent);

        }


    }

    ///////////////////////////////////////////////////////////////////////////
    // 下拉刷新接口回调
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onRefresh()
    {
        ClientAPI.getRecommendListAsync();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.roll_view_pager:
                Toast.makeText(getContext(), "点击了图片", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private class TestNormalAdapter extends StaticPagerAdapter
    {
        private int[] imgs = {
                R.drawable.topweb1,
                R.drawable.topweb2,
                R.drawable.topweb3,
        };


        @Override
        public View getView(ViewGroup container, final int position)
        {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v)
                {
                    String webUrl = "http://www.bilibili.com/blackboard/activity-cm03.html";
                    switch (position)
                    {
                        case 0:
                            Log.e("自定义标签", "类名==TestNormalAdapter" + "方法名==onClick=====:" + "点击0");
                            break;
                        case 1:
                            Log.e("自定义标签", "类名==TestNormalAdapter" + "方法名==onClick=====:" + "点击1");
                            break;
                        case 2:
                            Log.e("自定义标签", "类名==TestNormalAdapter" + "方法名==onClick=====:" + "点击2");
                            webUrl="http://bangumi.bilibili.com/anime/5550";


                            break;

                    }

                    Intent intentWeb=new Intent(getActivity(),RecommendWebViewActivity.class);
                    intentWeb.putExtra("url",webUrl);
                    startActivity(intentWeb);



                }

            });


            return view;
        }


        @Override
        public int getCount()
        {
            return imgs.length;
        }
    }
}
