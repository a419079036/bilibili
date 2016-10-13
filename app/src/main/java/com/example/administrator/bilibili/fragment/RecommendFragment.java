package com.example.administrator.bilibili.fragment;


import android.content.Intent;
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
import android.widget.Toast;

import com.example.administrator.bilibili.R;
import com.example.administrator.bilibili.activity.VideoDeatilActivity;
import com.example.administrator.bilibili.adapter.RecommendListAdapter;
import com.example.administrator.bilibili.client.ClientAPI;
import com.example.administrator.bilibili.model.RecommendBodyItem;
import com.example.administrator.bilibili.model.RecommendItem;
import com.example.administrator.bilibili.presenters.RecommendPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends BaseFragment implements IRecommentView, SwipeRefreshLayout.OnRefreshListener
{
    RecommendPresenter mRecommendPresenter;
    private List<RecommendItem> mItems;
    private RecommendListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mItems=new ArrayList<>();
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
}
