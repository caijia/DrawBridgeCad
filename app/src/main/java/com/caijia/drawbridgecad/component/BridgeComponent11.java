package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent11 extends BaseBridgeComponent {

    /**
     * 单位
     */
    private static final String UNIT = "m";
    private float[] heights = new float[3];
    private String[] names = {"翼缘板", "腹板", "底板"};
    private float degree = 80;
    private int dWidth;
    private Path path = new Path();
    private double tanDegree;

    public BridgeComponent11(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width,
                                     float yibanHeight, float fubanHeight, float dibanHeight) {
        int freeHeight = viewHeight - margin * 2;
        float pHeight = freeHeight / (yibanHeight + fubanHeight + dibanHeight);
        hCount = names.length;
        if (pHeight < minScale) {
            hScale = (int) pHeight;
        }

        float mapHeight = (yibanHeight + fubanHeight + dibanHeight) * hScale;
        tanDegree = Math.tan(Math.toRadians(degree));
        dWidth = (int) (mapHeight / tanDegree);
        int freeWidth = viewWidth - margin * 2 - 2 * dWidth;
        float pWidth = freeWidth / width;
        if (pWidth < minScale) {
            int stepCount = freeWidth / minScale;
            wStep = width / stepCount;
            wScale = (int) pWidth;
        }
        wCount = (float) width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, int width,
                     float yibanHeight, float fubanHeight, float dibanHeight) {
        computeScaleAndStep(viewWidth, viewHeight, width, yibanHeight, fubanHeight, dibanHeight);

        for (int i = 0; i < heights.length; i++) {
            heights[0] = yibanHeight;
            heights[1] = fubanHeight;
            heights[2] = dibanHeight;
        }

        //矩形宽度
        float mapWidth = wCount * wScale * wStep + dWidth;

        //矩形高度
        float mapHeight = (yibanHeight + fubanHeight + dibanHeight) * hScale;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        //横刻度
        canvas.drawLine(
                rectStartX + dWidth,
                rectStartY - rectToScaleSize,
                rectEndX,
                rectStartY - rectToScaleSize,
                paint);

        int floorWidth = (int) Math.floor(wCount) + 1;
        for (int j = 0; j < floorWidth; j++) {
            float i = j;
            if (j == floorWidth - 1) {
                i = wCount;
            }
            canvas.drawLine(
                    rectStartX + i * wScale * wStep + dWidth,
                    rectStartY - scaleSize - rectToScaleSize,
                    rectStartX + i * wScale * wStep + dWidth,
                    rectStartY - rectToScaleSize,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfWidth = getTextBounds((int) (i * wStep) + UNIT, paint)[0] / 2;
            canvas.drawText(
                    (int) (i * wStep) + UNIT,
                    rectStartX + i * wScale * wStep - textHalfWidth + dWidth,
                    rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    paint);
            restorePaintParams();
        }

        //竖刻度
        canvas.drawLine(
                rectEndX + rectToScaleSize,
                rectStartY,
                rectEndX + rectToScaleSize,
                rectEndY,
                paint);

        for (int i = 0; i <= hCount; i++) {
            float totalHeight = getTotalHeight(i) * hScale;
            //刻度线
            canvas.drawLine(
                    rectEndX + rectToScaleSize,
                    rectStartY + totalHeight,
                    rectEndX + rectToScaleSize + scaleSize,
                    rectStartY + totalHeight,
                    paint);

            //横线
            float tanX = (float) (totalHeight / tanDegree);
            if (i > 0 && i < hCount) {
                canvas.drawLine(
                        rectStartX + dWidth - tanX,
                        rectStartY + totalHeight,
                        rectEndX - tanX,
                        rectStartY + totalHeight,
                        paint);
            }

            if (i < hCount) {
                String text = heights[i] + UNIT;
                drawText(canvas, Paint.Align.LEFT, text,
                        rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                        rectStartY + totalHeight + heights[i] / 2 * hScale);

                float halfTanX = (float) ((totalHeight + heights[i] / 2 * hScale) / tanDegree);
                drawText(canvas, Paint.Align.RIGHT, names[i],
                        rectStartX - rectToScaleSize + dWidth - halfTanX,
                        rectStartY + totalHeight + heights[i] / 2 * hScale);
            }
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        path.moveTo(rectStartX + dWidth, rectStartY);
        path.rLineTo(-dWidth, mapHeight);
        path.rLineTo(mapWidth - dWidth, 0);
        path.rLineTo(dWidth, -mapHeight);
        path.close();
        canvas.drawPath(path, paint);
        restorePaintParams();
    }

    private void drawText(Canvas canvas, Paint.Align align, String text, float x, float y) {
        savePaintParams();
        paint.setTextSize(spToPx(10));
        paint.setStrokeWidth(0);
        paint.setTextAlign(align);
        int nameHalfHeight = getTextBounds(text, paint)[1] / 2;
        canvas.drawText(
                text,
                x,
                y + nameHalfHeight,
                paint);
        restorePaintParams();
    }

    private float getTotalHeight(int index) {
        float total = 0;
        for (int i = 0; i < heights.length; i++) {
            if (i < index) {
                total += heights[i];
            }
        }
        return total;
    }
}
