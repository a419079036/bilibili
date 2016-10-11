package com.example.administrator.bilibili.client;

import android.util.Log;

import com.example.administrator.bilibili.model.RecommendItem;

import org.apache.commons.codec.digest.DigestUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * http://app.bilibili.com/x/show/old?appkey=1d8b6e7d45233436
 * Created by Administrator on 2016/10/9.
 */
//所有的接口请求,处理地址,参数
public class ClientAPI
{
    private static AbstractHttpUtil sHttpUtil;
    private static final String APP_KEY = "1d8b6e7d45233436";
    private static final String App_SECRET = "FamGR6I1OwSutCZ2CelYQTJznz42c7sBbJcC0YxGXToV8j14uk1+3VvTFEbyBZeW";


    static
    {
        // TODO: 创建特定的网络类库的支持
        sHttpUtil = new OkHttpUtilIml();
    }

    private ClientAPI()
    {
    }

    /**
     * 获取首页推荐列表
     *
     * @return JSONObject
     */
    public static JSONObject getRecommentList()
    {
        String url = "http://app.bilibili.com/x/show/old?appkey=1d8b6e7d45233436";
        return sHttpUtil.doGet(url);
    }

    public static void getRecommendListAsync()
    {
        String url = "http://app.bilibili.com/x/show/old?appkey=1d8b6e7d45233436";
        sHttpUtil.doGetDataAsync(url, new HttpCallback()
        {
            //实现接口方法,处理数据

            /**
             *
             * @param url   json地址
             * @param code 网络返回码
             * @param data json数据
             */
            @Override
            public void onSuccess(String url, int code, byte[] data)
            {
                //TODO:把数据发给上层UI
                if (code == 200 && data != null)
                {
                    try
                    {   //开始处理json数据
                        String str = new String(data, "UTF-8");
                        JSONObject json = new JSONObject(str);
                        //解析数据
                        int code1 = json.getInt("code");
                        //code1是json中带的返回码,0为正常返回
                        if (code1 != 0)
                        {
                            JSONArray array = json.getJSONArray("result");
                            int len = array.length();
                            List<RecommendItem> items = new ArrayList<RecommendItem>();
                            for (int i = 0; i < len; i++)
                            {
                                JSONObject obj = array.getJSONObject(i);
                                RecommendItem item = RecommendItem.createFromJson(obj);
                                items.add(item);

                            }
                            //TODO:发送给上层
                            //用eventbus发送数据
                            EventBus.getDefault().post(items);


                        }
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                }

            }
        });

    }

    public void getVideoDetail(String aid)
    {
        String url = "https://app.bilibili.com/x/view";
        //TODO:进行参数数字签名,访问网络,获取数据
        TreeMap<String, String> params = new TreeMap<>();
        params.put("aid", aid);
//        params.put("plat", "0");
        Log.d("VD", "getVideoDetail: " + url);
        url = appendParamsWithSign(url, params);
        sHttpUtil.doGetDataAsync(url, new HttpCallback()
        {
            @Override
            public void onSuccess(String url, int code, byte[] data)
            {
                if (code == 200)
                {
                    try
                    {
                        Log.d("VD", "url = : " + url);
                        String str = new String(data, "UTF-8");
                        JSONObject jsonObject = new JSONObject(str);
                        // json.toString(4); 带缩进打印
                        Log.d("VD", "result = : " + jsonObject.toString());
//                        System.out.println(jsonObject.toString());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private static String appendParamsWithSign(String url, TreeMap<String, String> params)
    {
        String ret = url;
        if (url != null && params != null)
        {
            params.put("appkey", APP_KEY);
            params.put("build", "420000");
            params.put("channel", "bili");
            params.put("mobi_app", "android");
            params.put("platform", "android");
            // TODO: 需要机型适配
            params.put("screen", "xxdhpi");
            params.put("ts", "1476113271000");

            String sign = sign(params, url);
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            Set<String> keySet = params.keySet();
            try
            {
                for (String key : keySet)
                {
                    sb.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"))
                            .append("&");
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            finally
            {
            }
            if (sb.length() > 0)
            {
                sb.append("sign=").append(sign);
            }
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * 计算sgin数值,进行数字签名
     *
     * @param params
     * @param appSecret 秘钥
     * @return
     */
    private static String sign(TreeMap<String, String> params, String appSecret)
    {
        String ret = null;
        if (params != null && appSecret != null)
        {
            StringBuilder sb = new StringBuilder();
            Set<String> keySet = params.keySet();
            for (String key : keySet)
            {
                try
                {
                    // key=value&key=value
                    sb.append(key)
                            .append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"))
                            .append("&");
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                }
            }
            if (sb.length() > 0)
            {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(App_SECRET);
            String s = sb.toString();
            ret = DigestUtils.md5Hex(s);
        }
        return ret;
    }
}
