package com.example.administrator.bilibili.client;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/9.
 */

public   abstract  class AbstractHttpUtil
{
    public JSONObject doGet(String url)
    {
        JSONObject ret=null;
        byte[] bytes=doGetData(url);
        if (bytes!=null)
        {
            try
            {
                String str=new String(bytes,"UTF-8");
                ret=new JSONObject(str);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        return ret;
    }

    protected abstract byte[] doGetData(String url);

    /**
     * 异步的GET请求,请求在子线程中
     *
     * @param url
     * @return
     */
     public  abstract   void doGetDataAsync(String url,HttpCallback httpCallback);
    //接口回调,谁实现谁去调用
}
