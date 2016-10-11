package com.example.administrator.bilibili.model;

/**
 * Created by Administrator on 2016/10/10.
 */
//针对mvp模式,把数据模型进行接口抽象,包含数据的各种操作
public interface IRecommentItemModel
{
    String getType();
    void  setType(String type);

}
