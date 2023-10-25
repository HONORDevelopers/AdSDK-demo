package com.hihonor.adsdk.demo.external.splash;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hihonor.adsdk.demo.external.R;

/**
 * 功能描述
 *
 * @since 2023-06-07
 */
public class GlideUtils {
    public static void loadImage(Context context, String imgUrl, ImageView adImageView, int cornerRadius) {
        if (cornerRadius <= 0) {
            Glide.with(context).load(imgUrl).into(adImageView);
            return;
        }
        GradientDrawable defaultDrawable = new GradientDrawable();
        defaultDrawable.setColor(context.getResources().getColor(R.color.ads_app_magic_color_quaternary));
        defaultDrawable.setCornerRadius(cornerRadius);
        RequestOptions options = new RequestOptions()
                .transform(new RoundedCorners((int) cornerRadius));
        Glide.with(context).load(imgUrl).error(defaultDrawable).apply(options).into(adImageView);
    }
}
