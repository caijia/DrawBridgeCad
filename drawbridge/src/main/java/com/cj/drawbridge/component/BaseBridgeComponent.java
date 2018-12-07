package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by cai.jia 2018/11/27 08:40
 */
public abstract class BaseBridgeComponent {

    private static final int MIN_SCALE = 40;
    protected int scaleSize;
    /**
     * 矩形到刻度之间的间隔
     */
    protected int rectToScaleSize;
    /**
     * 文字到刻度的间隔
     */
    protected int textToScaleSize;
    protected float minScale;
    protected int margin;
    protected float wScale;
    protected float hScale;
    protected int hStep = 1;
    protected int wStep = 1;
    protected float hCount;
    protected float wCount;
    private PaintParams paintParams = new PaintParams();
    private Rect textBounds = new Rect();
    private Context context;

    public BaseBridgeComponent(Context context) {
        this.context = context;
        margin = (int) dpToPx(60);
        scaleSize = (int) dpToPx(4);
        rectToScaleSize = (int) dpToPx(8);
        textToScaleSize = (int) dpToPx(2);
        minScale = (int) dpToPx(MIN_SCALE);
        hScale = wScale = minScale;
    }

    public void restorePaintParams(Paint paint) {
        paint.setColor(paintParams.color);
        paint.setStrokeWidth(paintParams.strokeWidth);
        paint.setTextSize(paintParams.textSize);
        paint.setStyle(paintParams.style);
        paint.setTextAlign(paintParams.textAlign);
    }

    public void savePaintParams(Paint paint) {
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

    public String removeZero(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return s;
    }

    public void drawText(Canvas canvas, Paint.Align align, String text, float x, float y,
                         boolean addSelfHalfHeight, Paint paint) {
        drawText(canvas, align, text, x, y, addSelfHalfHeight ? 0.5f : 0, paint);
    }

    public void drawText(Canvas canvas, Paint.Align align, String text, float x, float y,
                         float selfHeightPercent, Paint paint) {
        savePaintParams(paint);
        paint.setTextSize(spToPx(10));
        paint.setStrokeWidth(0);
        paint.setTextAlign(align);
        float nameHalfHeight = getTextBounds(text, paint)[1] * selfHeightPercent;
        canvas.drawText(
                text,
                x,
                y + nameHalfHeight,
                paint);
        restorePaintParams(paint);
    }

    public abstract float[] getBounds();

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
