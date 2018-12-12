package com.cj.drawbridge.helper;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.cj.drawbridge.R;

public class GlideImageLoader {

    private static volatile GlideImageLoader instance;

    private GlideImageLoader() {

    }

    public static GlideImageLoader getInstance() {
        if (instance == null) {
            synchronized (GlideImageLoader.class) {
                if (instance == null) {
                    instance = new GlideImageLoader();
                }
            }
        }
        return instance;
    }

    public void loadImage(ImageView imageView, Integer res, @DrawableRes int defaultResId) {
        loadImage(imageView, res, defaultResId, 0, 0);
    }

    public void loadImage(ImageView imageView, Integer res, @DrawableRes int defaultResId,
                          int width, int height) {
        RequestOptions options = new RequestOptions()
                .priority(Priority.HIGH)
                .error(defaultResId)
                .placeholder(defaultResId);

        if (width > 0 && height > 0) {
            options = options.override(width, height);
        }

        RequestBuilder<Bitmap> builder = Glide.with(imageView.getContext())
                .asBitmap()
                .load(res)
                .apply(options);
        builder.into(imageView);
    }

    public void loadImage(String url, ImageView imageView, RequestOptions options) {
        RequestBuilder<Bitmap> builder = Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(options);
        builder.into(imageView);
    }

    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, R.drawable.shape_white);
    }

    public void loadImage(String url, ImageView imageView, @DrawableRes int defaultResId) {
        loadImage(url, imageView, null, null, defaultResId);
    }

    public void loadImage(String url, ImageView imageView, RequestListener<Bitmap> listener,
                          @DrawableRes int defaultResId) {
        loadImage(url, imageView, null, listener, defaultResId);
    }

    public void loadImage(String url, ImageView imageView, Transformation<Bitmap> transformation,
                          RequestListener<Bitmap> listener,
                          @DrawableRes int defaultResId) {
        RequestBuilder<Bitmap> builder = Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .listener(listener)
                .apply(new RequestOptions()
                        .priority(Priority.HIGH)
                        .error(defaultResId)
                        .placeholder(defaultResId));
        if (transformation != null) {
            builder.apply(RequestOptions.bitmapTransform(transformation));
        }
        builder.into(imageView);
    }
}