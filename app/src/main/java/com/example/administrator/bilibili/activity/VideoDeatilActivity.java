package com.example.administrator.bilibili.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.administrator.bilibili.R;
import com.example.administrator.bilibili.client.ClientAPI;
import com.example.administrator.bilibili.model.PlayUrl;
import com.example.administrator.bilibili.model.RecommendBodyItem;
import com.example.administrator.bilibili.model.VideoDetail;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoDeatilActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, View.OnTouchListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnBufferingUpdateListener
{
    /**
     * 从推荐,分区进入视频详情中,视频的基本对象
     * 通常可以使用RecomBodyItem即可
     */
    public static final String EXTRA_VIDEO_ITEM = "video_item";
    private RecommendBodyItem mrecommendBodyItem;
    /*
    * 视频详情数据
    * */

    private VideoDetail mVideoDetail;
    private SurfaceView mSurfaceView;
    private IjkMediaPlayer mPlayer;
    private GestureDetectorCompat mDetectorCompat;
    private View mMediaController;

    /**
     * 如果发生屏幕切换，进行播放
     */
    private boolean hasSwitchOrientation;
    private FloatingActionButton mFab;

    // 控制播放控制条的布局显示和隐藏
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else
        {
            View decorView = getWindow().getDecorView();
            int UiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(UiOption);


        }
        setContentView(R.layout.activity_video_deatil);
        if (savedInstanceState != null)
        {
            //发送切换
            hasSwitchOrientation = true;

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.video_detail_tool_bar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);


        }
        Intent intent = getIntent();
        mrecommendBodyItem = intent.getParcelableExtra(EXTRA_VIDEO_ITEM);
        EventBus.getDefault().register(this);//注册EventBus

        ImageView imageView = (ImageView) findViewById(R.id.video_detail_cover);
        if (imageView != null)
        {

            Picasso.with(this).load(mrecommendBodyItem.getCover())
                    .config(Bitmap.Config.RGB_565)
                    .resize(mrecommendBodyItem.getWidth(), mrecommendBodyItem.getHeight())
                    .into(imageView);


        }
        mFab = (FloatingActionButton) findViewById(R.id.video_detail_play_fab);
        if (mFab != null)
        {

            mFab.setOnClickListener(this);
        }
        mVideoDetail = null;

        mSurfaceView = (SurfaceView) findViewById(R.id.video_detail_play_view);
        if (mSurfaceView != null)
        {

            mSurfaceView.getHolder().addCallback(this);
            //兼容包下面的手势识别器,实现点击,拖拽的判断
            mDetectorCompat = new GestureDetectorCompat(this, new VideoGestureDetector());
            //手势可以在事件里面进行检查
            mSurfaceView.setOnTouchListener(this);//注册触摸事件


        }

        mPlayer = new IjkMediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        initView();
    }

    private void initView()
    {
        View btnSwitch = findViewById(R.id.video_controller_orientation_switch);
        btnSwitch.setOnClickListener(this);
        mMediaController = findViewById(R.id.video_controller);

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        mVideoDetail = null;

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ClientAPI.getVideoDetail(mrecommendBodyItem.getParam());


    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onReceiveDetail(VideoDetail detail)
    {
        mVideoDetail = detail;
        if (hasSwitchOrientation)
        {

            strartVideoPlay();


        }


    }

    private void strartVideoPlay()
    {
        if (mVideoDetail != null)
        {
            List<VideoDetail.Page> pages = mVideoDetail.getmPages();
            if (pages != null && pages.size() > 0)
            {
                VideoDetail.Page page = pages.get(0);
                long cid = page.getCid();
                ClientAPI.getPlayUrlAsync(Long.toString(cid), 1, "mp4");


            }


        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoPlayClickEvent(PlayUrl playUrl)
    {
        mSurfaceView.setVisibility(View.VISIBLE);

        //播放视频
        List<PlayUrl.Durl> durls=playUrl.getmDurl();
        if (durls!=null)
        {
            if (!durls.isEmpty())
            {
                PlayUrl.Durl durl=durls.get(0);
                String url = durl.getUrl();
                if (url!=null)
                {
                    try
                    {//设置视频音频的地址需要网络或者IO流的读取
                        mPlayer.setDataSource(url);
                        //异步子线程方式,开始准备视频,准备好就回调接口
                        mPlayer.prepareAsync();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }

            }

        }


    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.video_detail_play_fab:
                //视频播放
                strartVideoPlay();
                v.setVisibility(View.INVISIBLE);
                break;
            case  R.id.video_controller_orientation_switch:
                int requestedOrientation = getRequestedOrientation();
                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    // 垂直方向，那么切换成 水平方向
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mPlayer.setDisplay(holder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (mPlayer.isPlaying())
        {
            mPlayer.stop();
        }
        mPlayer.release();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer)
    {
        iMediaPlayer.start();

    }
    /**
     * 当缓存进度发生变化的时候，进行回调
     *
     * @param iMediaPlayer
     * @param progress
     */
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int progress)
    {
        Log.e("自定义标签", "类名==VideoDeatilActivity" + "方法名==onBufferingUpdate=====:" + "");

    }

    private class VideoGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        private static final String token="token";

        /**
         * 按下事件,返回true才可以处理手势
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        /**
         * 当快速点击 抬起的时候
         * @param e
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            mHandler.removeCallbacksAndMessages(token);
            mMediaController.setVisibility(View.VISIBLE);
            long time= SystemClock.uptimeMillis()+2000;
            //设置2秒后进度天自动消失
            mHandler.postAtTime(new Runnable()
            {
                @Override
                public void run()
                {
                  mMediaController.setVisibility(View.INVISIBLE);
                }
            },token,time);

            return super.onSingleTapUp(e);
        }
    }
}
