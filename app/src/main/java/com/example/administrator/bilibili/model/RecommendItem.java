package com.example.administrator.bilibili.model;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class RecommendItem implements IRecommentItemModel
{
    private String type;
    private String headParam;
    private String headGoto;
    private String headStyle;
    private String headTitle;
    private long headCount;

    private List<RecommendBodyItem> body;

    public static RecommendItem createFromJson(JSONObject json) throws JSONException
    {
        RecommendItem ret = null;
        if (json != null)
        {
            ret = new RecommendItem();
            //  ret.type=js.
            ret.type = json.getString("type");
            JSONObject head = json.getJSONObject("head");
            ret.headParam = head.getString("param");
            ret.headGoto = head.getString("goto");
            ret.headStyle = head.getString("style");
            ret.headTitle = head.getString("title");
            ret.headCount = head.optLong("count");

            JSONArray body = json.getJSONArray("body");
            int size = body.length();
            if (size > 0)
            {
                ret.body = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < size; i++)
                {
                    //  RecommendBodyItem bodyItem=new RecommendBodyItem();
                    JSONObject bi = body.getJSONObject(i);
                    RecommendBodyItem bodyItem = gson.fromJson(bi.toString(), RecommendBodyItem.class);
                    //TODO:解析body内部的内容
                    ret.body.add(bodyItem);

                }


            }


        }
        return ret;

    }

    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }

    public String getHeadParam()
    {
        return headParam;
    }

    public String getHeadGoto()
    {
        return headGoto;
    }

    public String getHeadStyle()
    {
        return headStyle;
    }

    public String getHeadTitle()
    {
        return headTitle;
    }

    public long getHeadCount()
    {
        return headCount;
    }

    public List<RecommendBodyItem> getBody()
    {
        return body;
    }
}
