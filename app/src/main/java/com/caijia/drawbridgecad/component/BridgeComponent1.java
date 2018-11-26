package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent1 {

    /**
     * 刻度最小的长度dp
     */
    private static final int MIN_SCALE = 40;
    private Paint paint;
    /**
     * 单位
     */
    private String unit = "m";
    /**
     * 刻度高度
     */
    private int scaleSize;
    /**
     * 矩形到刻度之间的间隔
     */
    private int space;
    /**
     * 文字到刻度的间隔
     */
    private int textToScaleSize;
    /**
     * 绘制图形的总区域
     */
    private RectF drawRect = new RectF();
    private Rect textBounds = new Rect();
    private PaintParams paintParams = new PaintParams();
    private Context context;
    private int margin;
    /**
     * 一个刻度多长
     */
    private int scale;
    /**
     * 刻度步长
     */
    private int step = 1;
    /**
     * 刻度最小的长度
     */
    private int minScale;
    private float newHeight;
    private float newWidth;

    public BridgeComponent1(Context context) {
        this.context = context;
        init();
    }

    private Context getContext() {
        return context;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dpToPx(1));

        margin = (int) dpToPx(30);
        scaleSize = (int) dpToPx(4);
        space = (int) dpToPx(8);
        textToScaleSize = (int) dpToPx(2);
        minScale = (int) dpToPx(MIN_SCALE);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private int[] getTextBounds(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return new int[]{textBounds.width(), textBounds.height()};
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width, int height) {
        int freeWidth = viewWidth - margin * 2;
        int freeHeight = viewHeight - margin * 2;
        if (viewWidth > viewHeight * width / height) {
            scale = freeHeight / height;
            if (minScale > scale) {
                int stepCount = (int) (freeHeight / dpToPx(MIN_SCALE));
                step = height / stepCount;
            }

        } else {
            scale = freeWidth / width;
            if (minScale > scale) {
                int stepCount = (int) (freeWidth / dpToPx(MIN_SCALE));
                step = width / stepCount;
            }
        }

        newHeight = (float) height / step;
        newWidth = (float) width / step;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, int width, int height) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
        //矩形宽度
        float mapWidth = newWidth * scale * step;

        //矩形高度
        float mapHeight = newHeight * scale * step;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        //横刻度
        canvas.drawLine(
                rectStartX,
                rectStartY - space,
                rectEndX,
                rectStartY - space,
                paint);

        int floorWidth = (int) Math.floor(newWidth) + 1;
        for (int j = 0; j < floorWidth; j++) {
            float i = j;
            if (j == floorWidth - 1) {
                i = newWidth;
            }
            canvas.drawLine(
                    rectStartX + i * scale * step,
                    rectStartY - scaleSize - space,
                    rectStartX + i * scale * step,
                    rectStartY - space,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfWidth = getTextBounds((int) (i * step) + unit, paint)[0] / 2;
            canvas.drawText(
                    (int) (i * step) + unit,
                    rectStartX + i * scale * step - textHalfWidth,
                    rectStartY - scaleSize - space - textToScaleSize,
                    paint);
            restorePaintParams();
        }

        //竖刻度
        canvas.drawLine(
                rectEndX + space,
                rectStartY,
                rectEndX + space,
                rectEndY,
                paint);

        int floorHeight = (int) Math.floor(newHeight) + 1;
        for (int k = 0; k < floorHeight; k++) {
            float i = k;
            if (k == floorHeight -1) {
                i = newHeight;
            }
            canvas.drawLine(
                    rectEndX + space,
                    rectStartY + i * scale * step,
                    rectEndX + space + scaleSize,
                    rectStartY + i * scale * step,
                    paint);


            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfHeight = getTextBounds((int) (i * step) + unit, paint)[1] / 2;
            canvas.drawText(
                    (int) (i * step) + unit,
                    rectEndX + space + scaleSize + textToScaleSize,
                    rectStartY + i * scale * step + textHalfHeight,
                    paint);
            restorePaintParams();
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        drawRect.set(rectStartX, rectStartY, rectEndX, rectEndY);
        canvas.drawRect(drawRect, paint);
        restorePaintParams();
    }

    private void restorePaintParams() {
        paint.setColor(paintParams.color);
        paint.setStrokeWidth(paintParams.strokeWidth);
        paint.setTextSize(paintParams.textSize);
        paint.setStyle(paintParams.style);
    }

    private float spToPx(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    private void savePaintParams() {
        paintParams.color = paint.getColor();
        paintParams.strokeWidth = paint.getStrokeWidth();
        paintParams.textSize = paint.getTextSize();
        paintParams.style = paint.getStyle();
    }

    /**
     * 画笔常用属性
     */
    static class PaintParams {
        int color;
        float strokeWidth;
        float textSize;
        Paint.Style style;
    }
}
