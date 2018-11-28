package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by cai.jia 2018/11/27 08:40
 */
public class BaseBridgeComponent {

    private static final int MIN_SCALE = 40;
    private PaintParams paintParams = new PaintParams();
    private Rect textBounds = new Rect();
    private Context context;
    protected Paint paint;
    /**
     * 刻度尺高度
     */
    protected int scaleSize;
    /**
     * 矩形到刻度之间的间隔
     */
    protected int rectToScaleSize;
    /**
     * 文字到刻度的间隔
     */
    protected int textToScaleSize;

    protected int minScale;

    protected int margin;

    protected int wScale;
    protected int hScale;

    protected int hStep = 1;
    protected int wStep = 1;

    protected float hCount;
    protected float wCount;

    public BaseBridgeComponent(Context context) {
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dpToPx(1));

        margin = (int) dpToPx(60);
        scaleSize = (int) dpToPx(4);
        rectToScaleSize = (int) dpToPx(8);
        textToScaleSize = (int) dpToPx(2);
        minScale = (int) dpToPx(MIN_SCALE);
        hScale = wScale = minScale;
    }

    public void restorePaintParams() {
        paint.setColor(paintParams.color);
        paint.setStrokeWidth(paintParams.strokeWidth);
        paint.setTextSize(paintParams.textSize);
        paint.setStyle(paintParams.style);
        paint.setTextAlign(paintParams.textAlign);
    }

    public void savePaintParams() {
        paintParams.color = paint.getColor();
        paintParams.strokeWidth = paint.getStrokeWidth();
        paintParams.textSize = paint.getTextSize();
        paintParams.style = paint.getStyle();
        paintParams.textAlign = paint.getTextAlign();
    }

    public int[] getTextBounds(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return new int[]{textBounds.width(), textBounds.height()};
    }

    public float spToPx(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    public float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 画笔常用属性
     */
    static class PaintParams {
        int color;
        float strokeWidth;
        float textSize;
        Paint.Style style;
        Paint.Align textAlign;
    }
}
