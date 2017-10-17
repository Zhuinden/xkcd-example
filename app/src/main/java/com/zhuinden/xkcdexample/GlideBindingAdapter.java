package com.zhuinden.xkcdexample;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Owner on 2017. 10. 17..
 */

public class GlideBindingAdapter {
    private GlideBindingAdapter() {
    }

    @BindingAdapter("glideImageSrc")
    public static void setGlideImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}
