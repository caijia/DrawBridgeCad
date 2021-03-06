package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent11 extends BaseBridgeComponent {

    private float[] heights = new float[3];
    private String[] names = {"翼缘板", "腹板", "底板"};
    private float degree = 80;
    private int dWidth;
    private Path path = new Path();
    private double tanDegree;
    private float yibanHeight;
    private float fubanHeight;
    private float dibanHeight;

    public BridgeComponent11(Context context) {
        super(context);
    }
    public BridgeComponent11(Context context, float degree) {
        super(context);
        this.degree = degree;
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width,
                                     float yibanHeight, float fubanHeight, float dibanHeight) {
        int freeHeight = viewHeight - margin * 2;
        float pHeight = freeHeight / (yibanHeight + fubanHeight + dibanHeight);
        hCount = names.length;
        if (pHeight < minScale) {
            hScale = pHeight;
        }

        float mapHeight = (yibanHeight + fubanHeight + dibanHeight) * hScale;
        tanDegree = Math.tan(Math.toRadians(degree));
        dWidth = (int) (mapHeight / tanDegree);
        int freeWidth = viewWidth - margin * 2 - 2 * dWidth;
        float pWidth = freeWidth / width;
        if (pWidth < minScale) {
            int stepCount = (int) (freeWidth / minScale);
            wStep = (int) (width / stepCount);
            wScale = pWidth;
        }
        wCount = width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width,
                     float yibanHeight, float fubanHeight, float dibanHeight, String unit,
                     Paint paint) {
        this.yibanHeight = yibanHeight;
        this.fubanHeight = fubanHeight;
        this.dibanHeight = dibanHeight;
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

        //hCount = 8.2  ( 0 - 7 , 8.2 ) 满足条件
        int floorW = (int) Math.floor(wCount);
        for (float k = 0; k <= floorW; k++) {
            float i = k;
            if (k == floorW) {
                i = wCount;
            }

            float x = rectStartX + i * wScale * wStep + dWidth;
            canvas.drawLine(
                    x, rectStartY - scaleSize - rectToScaleSize,
                    x, rectStartY - rectToScaleSize, paint);

            String text = removeZero(i * wStep + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text,
                    x, rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    false, paint);
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
                String text = heights[i] + unit;
                drawText(canvas, Paint.Align.LEFT, text,
                        rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                        rectStartY + totalHeight + heights[i] / 2 * hScale,
                        true, paint);

                float halfTanX = (float) ((totalHeight + heights[i] / 2 * hScale) / tanDegree);
                drawText(canvas, Paint.Align.RIGHT, names[i],
                        rectStartX - rectToScaleSize + dWidth - halfTanX,
                        rectStartY + totalHeight + heights[i] / 2 * hScale,
                        true, paint);
            }
        }

        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        path.moveTo(rectStartX + dWidth, rectStartY);
        path.rLineTo(-dWidth, mapHeight);
        path.rLineTo(mapWidth - dWidth, 0);
        path.rLineTo(dWidth, -mapHeight);
        path.close();
        canvas.drawPath(path, paint);
        restorePaintParams(paint);
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

    @Override
    public float[] getBounds() {
        //矩形宽度
        float mapWidth = wCount * wScale * wStep + dWidth + 2 * margin;

        //矩形高度
        float mapHeight = (yibanHeight + fubanHeight + dibanHeight) * hScale + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
