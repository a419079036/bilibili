package com.example.administrator.bilibili.presenters;

import com.example.administrator.bilibili.fragment.IRecommentView;
import com.example.administrator.bilibili.model.IRecommentItemModel;

/**
 * Created by Administrator on 2016/10/10.
 */

public class RecommendPresenter
{
    private IRecommentView miRecommentView;
    private IRecommentItemModel miRecommentItemModel;

    public RecommendPresenter(IRecommentView miRecommentView)
    {
        this.miRecommentView = miRecommentView;
    }

    public  void setModel(IRecommentItemModel model)
    {
        miRecommentItemModel=model;
    }

    public void  updataTypeText(){
        if (miRecommentItemModel!=null)
        {

            miRecommentView.setTypeText(miRecommentItemModel.getType());
        }

    }
}
