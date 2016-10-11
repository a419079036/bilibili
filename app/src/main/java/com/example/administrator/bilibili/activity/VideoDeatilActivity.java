package com.example.administrator.bilibili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.administrator.bilibili.R;

public class VideoDeatilActivity extends AppCompatActivity
{
    /**
     * 从推荐,分区进入视频详情中,视频的基本对象
     * 通常可以使用RecomBodyItem即可
     */
    public  static final String EXTRA_VIDEO_ITEM="video_item";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_deatil);
        Toolbar toolbar= (Toolbar) findViewById(R.id.Video_detail_Toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);


        }
        Intent intent=getIntent();
        Parcelable parcelableExtra = intent.getParcelableExtra(EXTRA_VIDEO_ITEM);


    }
}
