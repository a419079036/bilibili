package com.example.administrator.bilibili.client;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/9.
 */
//所有的接口请求,处理地址,参数
public class ClientAPI
{
    private static AbstractHttpUtil sHttpUtil;

    private ClientAPI()
    {
    }

    /**
     * 获取推荐列表
     *
     * @return JSONObject
     */
    public static JSONObject getRecommentList()
    {
        String url = "http://api.bilibili.cn/recommend";
        return sHttpUtil.doGet(url);
    }
}
