package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent14 extends BaseBridgeComponent {

    private static final String TEXT_DIRECTION = "桩号增大方向";
    private float degree = 80;
    private int dWidth;
    private Path path = new Path();
    private double tanDegree;
    private float arrowWidth = dpToPx(100);
    private Path arrowPath = new Path();
    private float arrowX = dpToPx(8);
    private float arrowY = dpToPx(4);
    private float rectToArrowLine = dpToPx(10);
    private float arrowToText = dpToPx(4);

    public BridgeComponent14(Context context) {
        super(context);
    }

    public BridgeComponent14(Context context, float degree, String unit) {
        super(context);
        this.degree = degree;
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width, float height) {
        int freeHeight = viewHeight - margin * 2;
        float avgHeight = freeHeight / height;
        hCount = height;
        if (avgHeight < minScale) {
            int stepCount = (int) (freeHeight / minScale);
            hStep = (int) (height / stepCount);
            hScale = avgHeight;
            hCount = height / hStep;
        }

        float mapHeight = hCount * hStep * hScale;
        tanDegree = Math.tan(Math.toRadians(degree));
        dWidth = (int) (mapHeight / tanDegree);

        int freeWidth = viewWidth - margin * 2;
        float avgWidth = freeWidth / width;
        wCount = width;
        if (avgWidth < minScale) {
            int stepCount = (int) (freeWidth / minScale);
            wStep = (int) (width / stepCount);
            wScale = avgWidth;
            wCount = width / wStep;
        }
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width, float height,
                     String unit, Paint paint) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);

        //矩形宽度
        float mapWidth = wCount * wScale * wStep + dWidth;

        //矩形高度
        float mapHeight = hCount * hStep * hScale;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        canvas.drawLine(rectStartX + dWidth, rectStartY - rectToScaleSize,
                rectEndX, rectStartY - rectToScaleSize, paint);

        int floorW = (int) Math.floor(wCount);
        for (float k = 0; k <= floorW; k++) {
            //刻度线
            float i = k;
            if (k == floorW) {
                i = wCount;
            }

            float incrementWidth = i * wScale * wStep;
            canvas.drawLine(
                    rectStartX + incrementWidth + dWidth,
                    rectStartY + -rectToScaleSize,
                    rectStartX + incrementWidth + dWidth,
                    rectStartY - rectToScaleSize - scaleSize,
                    paint);

            String text = removeZero(i * wStep + "") + unit;
            drawText(canvas, Paint.Align.CENTER, text,
                    rectStartX + incrementWidth + dWidth,
                    rectStartY - rectToScaleSize - scaleSize - textToScaleSize,
                    false, paint);
        }

        //竖刻度
        float xOffset = (float) (mapHeight / tanDegree);
        canvas.drawLine(
                rectEndX + rectToScaleSize,
                rectStartY,
                rectEndX + rectToScaleSize - xOffset,
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
            float curOffsetX = (float) (curHeight / tanDegree);
            canvas.drawLine(
                    rectEndX + rectToScaleSize - curOffsetX,
                    rectStartY + curHeight,
                    rectEndX + rectToScaleSize - curOffsetX + scaleSize,
                    rectStartY + curHeight,
                    paint);

            String text = removeZero(i * hStep + "") + unit;
            drawText(canvas, Paint.Align.LEFT, text,
                    rectEndX + rectToScaleSize + scaleSize + textToScaleSize - curOffsetX,
                    rectStartY + curHeight, true, paint);
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

        //桩号增大方向
        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        arrowPath.reset();
        arrowPath.moveTo(rectStartX + (mapWidth - arrowWidth) / 2, rectEndY + rectToArrowLine);
        arrowPath.rLineTo(arrowWidth, 0);
        arrowPath.rLineTo(0, 0);
        arrowPath.setLastPoint(rectStartX + (mapWidth + arrowWidth) / 2 - arrowX,
                rectEndY + rectToArrowLine - arrowY);
        arrowPath.rLineTo(arrowX, arrowY);
        arrowPath.rLineTo(-arrowX, arrowY);
        canvas.drawPath(arrowPath, paint);
        restorePaintParams(paint);

        drawText(canvas, Paint.Align.CENTER, TEXT_DIRECTION,
                rectStartX + mapWidth / 2,
                rectEndY + rectToArrowLine + arrowY + arrowToText,
                true, paint);
    }

    @Override
    public float[] getBounds() {
        //矩形宽度
        float mapWidth = wCount * wScale * wStep + dWidth + 2 * margin;

        //矩形高度
        float mapHeight = hCount * hStep * hScale + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
