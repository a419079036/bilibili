package com.example.administrator.bilibili.client;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Administrator on 2016/10/10.
 */

public class OkHttpUtilIml extends AbstractHttpUtil
{
    private OkHttpClient mokHttpClient;

    public OkHttpUtilIml( )
    {
       mokHttpClient=new OkHttpClient();
    }

    /**
     * 同步的方法
     * @param url
     * @return
     */

    @Override
    protected byte[] doGetData(String url)
    {

        byte[] ret=null;
        if (url!=null)
        {
            try
            {

                Request.Builder builder=new Request.Builder();
                builder.url(url).get().addHeader("UserAgent ","BiLiBiLi WP Client/4.20.0 (orz@loli.my)");
                //创建请求
                Request request = builder.build();
                Response response = mokHttpClient.newCall(request).execute();
                //Response的获取操作,会直接读取网络数据并且关闭,使用只能提取一次,不能调用body的任何方法
                //因为流已经到末尾了
                ret = response.body().bytes();
                response=null;//关闭操作

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * 异步的请求没办法直接返回数据,所以返回值为空
     * @param url
     * @param httpCallback
     */
    @Override
    public void doGetDataAsync(final String url, final HttpCallback httpCallback)
    {

        if (url!=null)
        {
            Request.Builder builder=new Request.Builder();
            builder.url(url).get().addHeader("UserAgent ","BiLiBiLi WP Client/4.20.0 (orz@loli.my)");
            final Request request = builder.build();//编译请求

            mokHttpClient.newCall(request).enqueue(
                    //接口回调

                    new Callback()//匿名内部类
                    {
                        @Override
                        /**
                         * 失败的调用
                         */
                        public void onFailure(Call call, IOException e)
                        {

                        }

                        /**
                         * 成功时调用
                         * @param call
                         * @param response
                         * @throws IOException
                         */
                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            Log.e("自定义标签", "类名==OkHttpUtilIml" + "方法名==onResponse=====:" + "");
                            int code = response.code();
                            if (code==HTTP_OK)
                            {
                                byte[] data=response.body().bytes();
                                //得到数据
                                HttpUrl ur1 = call.request().url();
                                //得到网址
                                if (httpCallback!=null)
                                {
                                    //谁接收数据谁实现接口,谁发送数据谁使用接口
                                    httpCallback.onSuccess(url.toString(),code,data);

                                }


                            }

                        }
                    }

            );

        }


    }

}
