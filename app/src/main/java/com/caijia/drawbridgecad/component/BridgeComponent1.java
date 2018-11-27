package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent1 extends BaseBridgeComponent {

    /**
     * 单位
     */
    private static final String UNIT = "m";

    public BridgeComponent1(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width, int height) {
        int freeWidth = viewWidth - margin * 2;
        int freeHeight = viewHeight - margin * 2;
        if (viewWidth > viewHeight * width / height) {
            wScale = hScale = freeHeight / height;
            if (minScale > hScale) {
                int stepCount = freeHeight / minScale;
                wStep = hStep = height / stepCount;
            }

        } else {
            hScale = wScale = freeWidth / width;
            if (minScale > wScale) {
                int stepCount = freeWidth / minScale;
                hStep = wStep = width / stepCount;
            }
        }

        hCount = (float) height / hStep;
        wCount = (float) width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, int width, int height) {
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
            canvas.drawLine(
                    rectStartX + i * wScale * wStep,
                    rectStartY - scaleSize - rectToScaleSize,
                    rectStartX + i * wScale * wStep,
                    rectStartY - rectToScaleSize,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfWidth = getTextBounds((int) (i * wStep) + UNIT, paint)[0] / 2;
            canvas.drawText(
                    (int) (i * wStep) + UNIT,
                    rectStartX + i * wScale * wStep - textHalfWidth,
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


            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfHeight = getTextBounds((int) (i * hStep) + UNIT, paint)[1] / 2;
            canvas.drawText(
                    (int) (i * hStep) + UNIT,
                    rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                    rectStartY + i * hScale * hStep + textHalfHeight,
                    paint);
            restorePaintParams();
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint);
        restorePaintParams();
    }
}
