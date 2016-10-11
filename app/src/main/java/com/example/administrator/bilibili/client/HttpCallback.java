package com.example.administrator.bilibili.client;

/**
 * Created by Administrator on 2016/10/10.
 */

/**
 * 接口的设计原则:
 * 接口分散
 * 通常接口尽可能少包含方法
 *
 */
public interface HttpCallback
{
    /**
     * 网络访问成功,返回数据
     */
    void onSuccess(String url,int code,byte[] data);


}
