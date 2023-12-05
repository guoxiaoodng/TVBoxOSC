package com.github.tvbox.osc.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author: 徐州彭于晏
 * @date: 2020-12-30 2:14 PM
 * @desc: 请添加描述
 */
public class ImageAdapter extends BannerAdapter<Integer, ImageAdapter.BannerViewHolder> {

    private boolean round = false;

    private Context mContext;

    public ImageAdapter(List<Integer> urlList, Context context) {
        super(urlList);
        this.mContext = context;
    }

    public ImageAdapter(List<Integer> urlList, boolean round, Context context) {
        super(urlList);
        this.round = round;
        this.mContext = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Integer data, int position, int size) {
        holder.imageView.setImageResource(data);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }

}
