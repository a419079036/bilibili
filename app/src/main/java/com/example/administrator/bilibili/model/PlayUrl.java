package com.example.administrator.bilibili.model;

/**
 * Created by Administrator on 2016/10/12.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 视频播放时获取到的视频网站json解析
 *
 * @see com.example.administrator.bilibili.client.ClientAPI#getPlayUrlAsync(String, int, String)
 */
public class PlayUrl
{
    @SerializedName("from")
    private String from;
    @SerializedName("timelength")

    private long timeLength; //json timelength
    @SerializedName("format")

    private String format;

    @SerializedName("durl")

    private List<Durl> mDurl;

    public String getFrom()
    {
        return from;
    }

    public long getTimeLength()
    {
        return timeLength;
    }

    public String getFormat()
    {
        return format;
    }

    public List<Durl> getmDurl()
    {
        return mDurl;
    }

    public static class Durl
    {
        @SerializedName("order")
        private int order;

        @SerializedName("length")
        private long length;

        @SerializedName("size")
        private long size;

    /*
    * 视频网站,直接播放
    * */
        @SerializedName("url")
        private String url;

        @SerializedName("backup_Url")
        private List<String> backupUrls;

        public int getOrder()
        {
            return order;
        }

        public long getLength()
        {
            return length;
        }

        public long getSize()
        {
            return size;
        }

        public String getUrl()
        {
            return url;
        }

        public List<String> getBackupUrls()
        {
            return backupUrls;
        }
    }

}
