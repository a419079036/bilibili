package com.example.administrator.bilibili;

import android.app.Application;

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
        //初始化方法一:
     //   EventBus eventBus = EventBus.getDefault();

        //自定义初始化
        EventBusBuilder builder = EventBus.builder();
        builder.installDefaultEventBus();
    }
}
