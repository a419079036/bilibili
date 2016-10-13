package com.example.administrator.bilibili.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bilibili.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends BaseFragment implements SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener
{

    private IjkMediaPlayer mPlayer;

    public LiveFragment()
    {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle()
    {
        return "直播";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mPlayer = new IjkMediaPlayer();

        mPlayer.setOnPreparedListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer)
    {

    }
}
