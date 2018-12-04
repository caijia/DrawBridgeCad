package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent8 extends BaseBridgeComponent {

    private float[] heights = new float[5];
    private String[] names = {"翼缘板", "腹板", "底板", "腹板", "翼缘板"};
    private float yibanHeight;
    private float fubanHeight;
    private float dibanHeight;
    public BridgeComponent8(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width,
                                     float yibanHeight, float fubanHeight, float dibanHeight) {
        int freeWidth = viewWidth - margin * 2;
        float pWidth = freeWidth / width;
        if (pWidth < minScale) {
            int stepCount = (int) (freeWidth / minScale);
            wStep = (int) (width / stepCount);
            wScale = pWidth;
        }
        wCount = width / wStep;

        int freeHeight = viewHeight - margin * 2;
        float pHeight = freeHeight / (yibanHeight * 2 + fubanHeight * 2 + dibanHeight);
        hCount = names.length;
        if (pHeight < minScale) {
            hScale = pHeight;
        }
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width,
                     float yibanHeight, float fubanHeight, float dibanHeight, String unit) {
        this.yibanHeight = yibanHeight;
        this.fubanHeight = fubanHeight;
        this.dibanHeight = dibanHeight;
        computeScaleAndStep(viewWidth, viewHeight, width, yibanHeight, fubanHeight, dibanHeight);

        for (int i = 0; i < heights.length; i++) {
            heights[0] = heights[4] = yibanHeight;
            heights[1] = heights[3] = fubanHeight;
            heights[2] = dibanHeight;
        }

        //矩形宽度
        float mapWidth = wCount * wScale * wStep;

        //矩形高度
        float mapHeight = (yibanHeight * 2 + fubanHeight * 2 + dibanHeight) * hScale;

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

            String text = removeZero(i * wStep + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text,
                    rectStartX + i * wScale * wStep,
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

        for (int i = 0; i <= hCount; i++) {
            float totalHeight = getTotalHeight(i) * hScale;
            canvas.drawLine(
                    rectEndX + rectToScaleSize,
                    rectStartY + totalHeight,
                    rectEndX + rectToScaleSize + scaleSize,
                    rectStartY + totalHeight,
                    paint);

            canvas.drawLine(
                    rectStartX,
                    rectStartY + totalHeight,
                    rectEndX,
                    rectStartY + totalHeight,
                    paint);

            if (i < hCount) {
                String text = heights[i] + unit;
                drawText(canvas, Paint.Align.LEFT, text,
                        rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                        rectStartY + totalHeight + heights[i] / 2 * hScale,
                        true);

                drawText(canvas, Paint.Align.RIGHT, names[i], rectStartX - rectToScaleSize,
                        rectStartY + totalHeight + heights[i] / 2 * hScale,
                        true);
            }
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint);
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

    @Override
    public float[] getBounds() {
        //矩形宽度
        float mapWidth = wCount * wScale * wStep + 2 * margin;

        //矩形高度
        float mapHeight = (yibanHeight * 2 + fubanHeight * 2 + dibanHeight) * hScale + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
