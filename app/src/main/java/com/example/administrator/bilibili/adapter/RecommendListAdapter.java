package com.example.administrator.bilibili.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bilibili.R;
import com.example.administrator.bilibili.model.RecommendBodyItem;
import com.example.administrator.bilibili.model.RecommendItem;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 * 推荐界面,列表适配器
 */

public class RecommendListAdapter extends RecyclerView.Adapter
{
    public static final int TYPE_RECOMMEND = 1;
    public static final int TYPE_LIVE = 2;
    public static final int TYPE_BANGUMI = 3;
    public static final int TYPE_REGION = 4;
    public static final int TYPE_WEB_LINK = 5;
    public static final int TYPE_HUDONG = 6;

    private Context mContext;
    private List<RecommendItem> mItems;

    public RecommendListAdapter(Context context, List<RecommendItem> items) {
        mContext = context;
        this.mItems = items;
    }

    /*
     * 1.RecycleView Adapter  以ViewHolder 为视图复用的标准
     * 2.RecycleView 不提供onItemClickListener 接口
     * 3.Adapter 同BaseAdapter , 都提供多布局的支持
     */

    @Override
    public int getItemViewType(int position) {
        int ret = 0;
        RecommendItem item = mItems.get(position);
        String type = item.getType();
        switch (type) {
            case "recommend":
                ret = 1;
                break;
            case "live":
                ret = 2;
                break;
            case "bangumi_2":
                ret = 3;
                break;
            case "region":
                ret = 4;
                break;
            case "weblink":
                ret = 5;
                break;
            case "activity":
                ret = 6;
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder ret = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;

        switch (viewType) {
            case TYPE_RECOMMEND:
                // TODO: 推荐布局
                itemView = inflater.inflate(R.layout.item_recommend_recommend, parent, false);
                ret = new RecommendViewViewHolder(itemView);
                break;
            case TYPE_LIVE:
                // TODO: 直播布局
            case TYPE_BANGUMI:
                // TODO:番剧布局
            case TYPE_REGION:
                // TODO:
            case TYPE_WEB_LINK:
                // TODO:
            case TYPE_HUDONG:
                // TODO:
            default:
                itemView = inflater.inflate(R.layout.item_recommend_simple, parent, false);
                ret = new SimpleViewHolder(itemView);
                break;
        }
        return ret;
    }

    /**
     * 把数据绑定到 ViewHolder 指向的View中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecommendItem recommendItem = mItems.get(position);
        if (holder instanceof SimpleViewHolder) {
            SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;
            simpleViewHolder.bindView(recommendItem);
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mItems != null) {
            ret = mItems.size();
        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    private static class SimpleViewHolder extends RecyclerView.ViewHolder {
        /**
         * 通用的View缓存功能
         */
        private SparseArrayCompat<View> mViews;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArrayCompat<>();
        }

        View getChildView(int rid) {
            View ret = null;
            ret = mViews.get(rid);
            if (ret == null) {
                ret = itemView.findViewById(rid);
                if (ret != null) {
                    mViews.put(rid, ret);
                }
            }
            return ret;
        }

        /**
         * 动态获取View的方法,
         * 利用反射, 获取视图
         *
         * @param name
         * @return
         */
        View getChildView(String name) {
            View ret = null;
            int id = 0;
            if (name != null) {
                // 使用反射动态获取多个控件的id
                Class aClass = R.id.class;
                try {
                    Field field = aClass.getDeclaredField(name);
                    field.setAccessible(true);
                    // 获取类成员的数值
                    id = field.getInt(aClass);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (id != 0) {
                ret = getChildView(id);
            }
            return ret;
        }

        /**
         * 用于绑定数据到视图
         *
         * @param recommendItem
         */
        void bindView(RecommendItem recommendItem) {
            TextView txt = (TextView) getChildView(R.id.item_recommend_text);
            txt.setText(recommendItem.getType());
        }


    }

    private static class RecommendViewViewHolder extends SimpleViewHolder implements View.OnClickListener {

        public RecommendViewViewHolder(View itemView) {
            super(itemView);
            for (int i = 0; i < 4; i++) {
                View view = getChildView("item_commend_card_view_" + i);
                if (view != null) {
                    // 每一个Body内部视频点击事件
                    view.setOnClickListener(this);
                }
            }
        }
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null) {
                if (tag instanceof RecommendBodyItem) {
                    RecommendBodyItem bodyItem = (RecommendBodyItem) tag;
                    // EventBus 传递对象给 Fragment , 有Fragment来跳转到详情
                    EventBus.getDefault().post(bodyItem);
                }
            }
        }

        @Override
        void bindView(RecommendItem recommendItem) {
            TextView txtTitle = ((TextView) getChildView(R.id.item_recommend_head_title));
            if (txtTitle != null) {
                txtTitle.setText(recommendItem.getHeadTitle());
            }

            //---------------------------
            // 条目 , 默认四个
            List<RecommendBodyItem> body = recommendItem.getBody();
            if (body.size() >= 4) {
                // 使用反射动态获取多个控件ID
                for (int i = 0; i < body.size(); i++) {
                    View view = getChildView("item_commend_card_view_" + i);
                    ImageView imageView = (ImageView) getChildView("item_commend_body_icon_" + i);
                    RecommendBodyItem bodyItem = body.get(i);
                    view.setTag(bodyItem);
                    String cover = bodyItem.getCover();
                    if (cover != null) {
                        // TODO: 显示图片
                        Context context = imageView.getContext();
                        Picasso.with(context)
                                .load(cover)
                                .config(Bitmap.Config.RGB_565)
//                                .noFade() // 滑动的效果
                                .resize(520, 350)
                                .into(imageView);
                    }


                    TextView txtBodyTitle = (TextView) getChildView("item_commend_body_title_" + i);
                    if (txtBodyTitle != null) {
                        String title = bodyItem.getTitle();
                        if (title != null) {
                            txtBodyTitle.setText(title);
                        }
                    }

                    TextView txtBodyCount = (TextView) getChildView("item_commend_body_count_" + i);
                    if (txtBodyTitle != null) {
                        String play = bodyItem.getPlay();
                        if (play != null) {
                            txtBodyCount.setText(play);
                        }
                    }

                    TextView txtBodyDanMuKu = (TextView) getChildView("item_commend_body_danmaku_" + i);
                    if (txtBodyTitle != null) {
                        String danMaKu = bodyItem.getDanmaku();
                        if (danMaKu != null) {
                            txtBodyDanMuKu.setText(danMaKu);
                        }
                    }
                }
            } else {
                // TODO: 代码动态添加布局

            }
        }

    }

    private static class LiveViewViewHolder extends SimpleViewHolder {

        public LiveViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class Bangumi2ViewViewHolder extends SimpleViewHolder {

        public Bangumi2ViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class RegionViewViewHolder extends SimpleViewHolder {

        public RegionViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class WebLinkViewViewHolder extends SimpleViewHolder {

        public WebLinkViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class HuodongViewViewHolder extends SimpleViewHolder {

        public HuodongViewViewHolder(View itemView) {
            super(itemView);
        }
    }


}
