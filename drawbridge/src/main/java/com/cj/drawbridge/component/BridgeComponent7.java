package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent7 extends BaseBridgeComponent {

    private Path path = new Path();
    private float zHeight = 1;

    public BridgeComponent7(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width, float height) {
        int freeWidth = viewWidth - margin * 2;
        int freeHeight = viewHeight - margin * 2;
        if (viewWidth > viewHeight * width / height) {
            wScale = hScale = freeHeight / height;
            if (minScale > hScale) {
                int stepCount = (int) (freeHeight / minScale);
                wStep = hStep = (int) (height / stepCount);
            } else {
                wScale = hScale = minScale;
            }

        } else {
            hScale = wScale = freeWidth / width;
            if (minScale > wScale) {
                int stepCount = (int) (freeWidth / minScale);
                hStep = wStep = (int) (width / stepCount);
            } else {
                wScale = hScale = minScale;
            }
        }

        wCount = width / wStep;
        hCount = height / hStep;

        zHeight = height / 8;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width, float height,
                     String unit, Paint paint) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
        //矩形宽度
        float mapWidth = wCount * wScale * wStep;

        //矩形高度
        float mapHeight = hCount * hScale * hStep;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        //横刻度
        canvas.drawLine(
                rectStartX, rectStartY - rectToScaleSize,
                rectEndX, rectStartY - rectToScaleSize, paint);

        int floorWidth = (int) Math.floor(wCount) + 1;
        for (int j = 0; j < floorWidth; j++) {
            float i = j;
            if (j == floorWidth - 1) {
                i = wCount;
            }

            float x = rectStartX + i * wScale * wStep;
            canvas.drawLine(
                    x, rectStartY - scaleSize - rectToScaleSize,
                    x, rectStartY - rectToScaleSize, paint);

            String text = removeZero(i * wStep + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text, x,
                    rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    false, paint);
        }

        //竖刻度
        canvas.drawLine(
                rectStartX - rectToScaleSize,
                rectStartY,
                rectStartX - rectToScaleSize,
                rectEndY,
                paint);

        int floorHeight = (int) Math.floor(hCount) + 1;
        for (int k = 0; k < floorHeight; k++) {
            float i = k;
            if (k == floorHeight - 1) {
                i = hCount;
            }
            canvas.drawLine(
                    rectStartX - rectToScaleSize - scaleSize,
                    rectStartY + i * hScale * hStep,
                    rectStartX - rectToScaleSize,
                    rectStartY + i * hScale * hStep,
                    paint);

            String text = removeZero(i * hStep + "") + unit;
            drawText(canvas, Paint.Align.RIGHT, text,
                    rectStartX - rectToScaleSize - scaleSize - textToScaleSize,
                    rectStartY + i * hScale * hStep, true, paint);
        }

        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        float rHeight = mapHeight * zHeight / height;
        path.moveTo(rectStartX, rectStartY);
        path.rLineTo(0, mapHeight);
        path.rLineTo(mapWidth, -(mapHeight - rHeight));
        path.rLineTo(0, -rHeight);
        path.close();
        canvas.drawPath(path, paint);
        restorePaintParams(paint);
    }

    @Override
    public float[] getBounds() {
        //矩形宽度
        float mapWidth = wCount * wScale * wStep + 2 * margin;

        //矩形高度
        float mapHeight = hCount * hScale * hStep + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
