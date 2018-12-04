package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent10 extends BaseBridgeComponent {

    private Path path = new Path();
    private float zWidth;
    private float zHeight;

    public BridgeComponent10(Context context) {
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

        zWidth = width / 8;
        zHeight = height / 8;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width, float height,
                     String unit) {
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
                rectStartX,
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

            float x = rectStartX + i * wScale * wStep;
            canvas.drawLine(
                    x, rectStartY - scaleSize - rectToScaleSize,
                    x, rectStartY - rectToScaleSize, paint);

            String text = removeZero(i * wStep + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text, x,
                    rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    false);
        }

        //竖刻度
        canvas.drawLine(
                rectEndX + rectToScaleSize,
                rectStartY,
                rectEndX + rectToScaleSize,
                rectEndY,
                paint);

        int floorHeight = (int) Math.floor(hCount) + 1;
        for (int k = 0; k < floorHeight; k++) {
            float i = k;
            if (k == floorHeight - 1) {
                i = hCount;
            }
            canvas.drawLine(
                    rectEndX + rectToScaleSize,
                    rectStartY + i * hScale * hStep,
                    rectEndX + rectToScaleSize + scaleSize,
                    rectStartY + i * hScale * hStep,
                    paint);

            String text = removeZero(i * hStep + "") + unit;
            drawText(canvas, Paint.Align.LEFT, text,
                    rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                    rectStartY + i * hScale * hStep,
                    true);
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        float rWidth = mapWidth * zWidth / width;
        float rHeight = mapHeight * zHeight / height;
        path.moveTo(rectStartX, rectStartY + rHeight);
        path.rLineTo(0, mapHeight - rHeight);
        path.rLineTo(mapWidth, 0);
        path.rLineTo(0, -(mapHeight - rHeight));
        path.rLineTo(-rWidth, -rHeight);
        path.rLineTo(-(mapWidth - 2 * rWidth), 0);
        path.close();
        canvas.drawPath(path, paint);
        restorePaintParams();
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
