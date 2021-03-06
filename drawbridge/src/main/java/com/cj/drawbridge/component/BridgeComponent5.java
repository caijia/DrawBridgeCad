package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent5 extends BaseBridgeComponent {

    private String hUnit;

    public BridgeComponent5(Context context) {
        super(context);
        minScale = (int) dpToPx(60);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width, int height) {
        hCount = height;
        hStep = 1;
        hScale = (int) dpToPx(120);

        int freeWidth = viewWidth - margin * 2;
        float percentWidth = freeWidth / hCount;
        if (percentWidth < minScale) {
            freeWidth = (int) (minScale * hCount);
        }
        wScale = freeWidth / width;
        if (minScale > wScale) {
            int stepCount = (int) (freeWidth / minScale);
            wStep = (int) (width / stepCount);
        }
        wCount = width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width, int height,
                     String direction, int dun, String wUnit, Paint paint) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
        //宽度
        float mapWidth = wCount * wScale * wStep;

        //高度
        float mapHeight = hScale * hStep;

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

            String text = removeZero(i * wStep + "") + wUnit;
            drawText(canvas, Paint.Align.CENTER, text,
                    rectStartX + i * wScale * wStep,
                    rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    false, paint);
        }

        //矩形
        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint);
        restorePaintParams(paint);

        float percentWidth = mapWidth / hCount;
        for (int k = 0; k < hCount; k++) {
            canvas.drawLine(
                    rectStartX + percentWidth * k + 0.5f,
                    rectStartY,
                    rectStartX + percentWidth * k,
                    rectEndY,
                    paint
            );
            hUnit = direction + dun + "-" + dun + "-";
            String text = hUnit + (int) (hCount - k) + "#";
            drawText(canvas, Paint.Align.LEFT, text,
                    rectStartX + percentWidth * k,
                    rectEndY + textToScaleSize,
                    1f, paint);
        }
    }

    @Override
    public float[] getBounds() {
        //宽度
        float mapWidth = wCount * wScale * wStep + 2 * margin;

        //高度
        float mapHeight = hScale * hStep + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
