package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.TypedValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia 2018/11/26 15:24
 */
public class BridgeComponent2 {

    private static final int MIN_SCALE = 40;
    private static final String REGEX = "(L\\d+-)(\\d+)";
    /**
     * 单位
     */
    private static final String H_UNIT = "m";
    private Pattern pattern = Pattern.compile(REGEX);
    private Paint paint;
    private String wUnit;
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
     * 高度刻度步长
     */
    private int hStep = 1;
    /**
     * 刻度最小的长度
     */
    private int minScale;
    private float newHeight;
    private float newWidth;

    public BridgeComponent2(Context context) {
        this.context = context;
        init();
    }

    /**
     * @param canvas     canvas
     * @param viewWidth  viewWidth
     * @param viewHeight viewHeight
     * @param widthStr   这个值必须是L1-1  这样的格式
     * @param height     height
     */
    public void draw(Canvas canvas, int viewWidth, int viewHeight, String widthStr, int height) {
        if (TextUtils.isEmpty(widthStr)) {
            throw new RuntimeException("widthStr is null");
        }
        boolean isMatcher = widthStr.matches(REGEX);
        if (!isMatcher) {
            throw new RuntimeException("widthStr format is error");
        }
        Matcher matcher = pattern.matcher(widthStr);
        if (matcher.matches()) {
            wUnit = matcher.group(1);
            int width = Integer.parseInt(matcher.group(2));
            draw(canvas, viewWidth, viewHeight, width, height);
        }
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
                hStep = height / stepCount;
            }

        } else {
            scale = freeWidth / width;
            if (minScale > scale) {
                int stepCount = (int) (freeWidth / dpToPx(MIN_SCALE));
                hStep = width / stepCount;
            }
        }

        newHeight = (float) height / hStep;
        newWidth = width;
    }

    private void draw(Canvas canvas, int viewWidth, int viewHeight, int width, int height) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
        //矩形宽度
        float mapWidth = newWidth * scale * hStep;

        //矩形高度
        float mapHeight = newHeight * scale * hStep;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        //横上部编号
        for (int i = 0; i < newWidth; i++) {
            canvas.drawLine(
                    rectStartX + i * scale,
                    rectStartY,
                    rectStartX + i * scale,
                    rectEndY,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(
                    wUnit + (i + 1),
                    rectStartX + i * scale + scale / 2,
                    rectStartY - space,
                    paint);

            if (i > 0 && i < newWidth) {
                int textHalfHeight = getTextBounds(wUnit + i, paint)[1] / 2;
                canvas.drawText(
                        wUnit + i,
                        rectStartX + i * scale,
                        rectEndY + space + textHalfHeight,
                        paint);
            }
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
            if (k == floorHeight - 1) {
                i = newHeight;
            }
            canvas.drawLine(
                    rectEndX + space,
                    rectStartY + i * scale * hStep,
                    rectEndX + space + scaleSize,
                    rectStartY + i * scale * hStep,
                    paint);


            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfHeight = getTextBounds((int) (i * hStep) + H_UNIT, paint)[1] / 2;
            canvas.drawText(
                    (int) (i * hStep) + H_UNIT,
                    rectEndX + space + scaleSize + textToScaleSize,
                    rectStartY + i * scale * hStep + textHalfHeight,
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
        paint.setTextAlign(paintParams.textAlign);
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
        paintParams.textAlign = paint.getTextAlign();
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
