package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent16 extends BaseBridgeComponent {

    private float mapWidth;
    private Path path = new Path();

    public BridgeComponent16(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float[] hanJieArray, float height) {
        int freeHeight = viewHeight - margin * 2;
        float avgHeight = freeHeight / height;
        hCount = height;
        if (avgHeight < minScale) {
            int stepCount = (int) (freeHeight / minScale);
            hStep = (int) (height / stepCount);
            hScale = (int) avgHeight;
            hCount = height / hStep;
        }

        int freeWidth = viewWidth - margin * 2;
        float totalWidth = getTotalWidth(hanJieArray, hanJieArray.length);
        float avgWidth = freeWidth / totalWidth;
        if (avgWidth < minScale) {
            wScale = avgWidth;
        }
    }

    private float getTotalWidth(float[] hanJieArray, int size) {
        float total = 0;
        for (int i = 0; i < hanJieArray.length; i++) {
            if (i < size) {
                total += hanJieArray[i];
            } else {
                break;
            }
        }
        return total;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float[] hanJieArray,
                     float height, String direction, int hanTai, String unit, Paint paint) {
        computeScaleAndStep(viewWidth, viewHeight, hanJieArray, height);

        //矩形宽度
        mapWidth = getTotalWidth(hanJieArray, hanJieArray.length) * wScale;

        //矩形高度
        float mapHeight = hCount * hStep * hScale;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        canvas.drawLine(rectStartX, rectStartY - rectToScaleSize,
                rectEndX, rectStartY - rectToScaleSize, paint);

        for (int i = 0; i <= hanJieArray.length; i++) {
            float totalWidth = getTotalWidth(hanJieArray, i);
            float incrementWidth = totalWidth * wScale;
            canvas.drawLine(
                    rectStartX + incrementWidth,
                    rectStartY + -rectToScaleSize,
                    rectStartX + incrementWidth,
                    rectStartY - rectToScaleSize - scaleSize,
                    paint);

            String text = removeZero(totalWidth + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text,
                    rectStartX + incrementWidth,
                    rectStartY - rectToScaleSize - scaleSize - textToScaleSize,
                    false, paint);

            //虚线
            if (i > 0 && i < hanJieArray.length) {
                savePaintParams(paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setPathEffect(new DashPathEffect(new float[]{15, 5}, 0));
                path.reset();
                path.moveTo(rectStartX + incrementWidth,
                        rectStartY);
                path.rLineTo(0, mapHeight);
                canvas.drawPath(path, paint);
                restorePaintParams(paint);
            }

            //底部文字
            if (i >= 0 && i < hanJieArray.length) {
                String bottomText;
                if (hanTai < 0) {
                    bottomText = direction + i;
                } else {
                    bottomText = direction + hanTai + "-" + i;
                }
                drawText(canvas, Paint.Align.CENTER, bottomText,
                        rectStartX + incrementWidth + hanJieArray[i] * wScale / 2,
                        rectEndY + textToScaleSize + rectToScaleSize,
                        1f, paint);
            }
        }

        //竖刻度
        canvas.drawLine(
                rectEndX + rectToScaleSize,
                rectStartY,
                rectEndX + rectToScaleSize,
                rectEndY,
                paint);

        //hCount = 8.2  ( 0 - 7 , 8.2 ) 满足条件
        int floorH = (int) Math.floor(hCount);
        for (float k = 0; k <= floorH; k++) {
            //刻度线
            float i = k;
            if (k == floorH) {
                i = hCount;
            }
            float curHeight = i * hStep * hScale;
            canvas.drawLine(
                    rectEndX + rectToScaleSize,
                    rectStartY + curHeight,
                    rectEndX + rectToScaleSize + scaleSize,
                    rectStartY + curHeight,
                    paint);

            String text = removeZero(i * hStep + "") + unit;
            drawText(canvas, Paint.Align.LEFT, text,
                    rectEndX + rectToScaleSize + scaleSize + textToScaleSize,
                    rectStartY + curHeight, true, paint);
        }

        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint);
        restorePaintParams(paint);
    }

    @Override
    public float[] getBounds() {
        //矩形宽度
        float mapWidth = this.mapWidth + 2 * margin;

        //矩形高度
        float mapHeight = hCount * hStep * hScale + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
