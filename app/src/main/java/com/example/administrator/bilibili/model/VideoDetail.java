package com.example.administrator.bilibili.model;

/**
 * Created by Administrator on 2016/10/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 视频详情数据
 */
public class VideoDetail
{
    @SerializedName("aid")
    private long aid;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;
    @SerializedName("mPages")
    private List<Page> mPages;

    public long getAid()
    {
        return aid;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDesc()
    {
        return desc;
    }

    public List<Page> getmPages()
    {
        return mPages;
    }

    public static class Page
    {
        @SerializedName("cid")

        private long cid;
        @SerializedName("vid")

        private String vid;
        @SerializedName("part")

        private String part;

        public long getCid()
        {
            return cid;
        }

        public String getVid()
        {
            return vid;
        }

        public String getPart()
        {
            return part;
        }
    }

}
