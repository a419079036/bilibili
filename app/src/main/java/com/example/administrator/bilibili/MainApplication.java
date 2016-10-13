package com.example.administrator.bilibili;

import android.app.Application;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

/**
 * Created by Administrator on 2016/10/10.
 */

/**
 * 全局初始化部分
 */
public class MainApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("自定义标签", "类名==MainApplication" + "方法名==onCreate=====:" + "");
        EventBusBuilder builder = EventBus.builder();
        builder.installDefaultEventBus();
    }
}
