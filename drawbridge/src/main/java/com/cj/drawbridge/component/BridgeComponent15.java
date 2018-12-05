package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class BridgeComponent15 extends BaseBridgeComponent {

    private static final String TEXT = "桩号增大方向";
    private int[] widths = {0, 90, 180, 270, 0};
    private float radius = dpToPx(40);
    private float rectToRadiusSpace = dpToPx(40);
    private float arrowHeight = dpToPx(80);
    private Path arrowPath = new Path();
    private float arrowX = dpToPx(4);
    private float arrowY = dpToPx(8);
    private float radiusToArrowLine = dpToPx(40);

    public BridgeComponent15(Context context) {
        super(context);
    }

    private void computeScaleAndStep(int viewHeight, float height) {
        int freeHeight = viewHeight - margin * 2;
        float avgHeight = freeHeight / height;
        hCount = height;
        if (avgHeight < minScale) {
            int stepCount = (int) (freeHeight / minScale);
            hStep = (int) (height / stepCount);
            hScale = avgHeight;
            hCount = height / hStep;
        }

        wStep = 1;
        wScale = minScale;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float height, String unit) {
        computeScaleAndStep(viewHeight, height);

        float mapWidth = wScale * (widths.length - 1) + 2 * radius + rectToRadiusSpace + radiusToArrowLine;
        float mapHeight = hScale * hStep * hCount;

        float startX = (viewWidth - mapWidth) / 2;
        float startY = (viewHeight - mapHeight) / 2;

        float endX = startX + mapWidth;
        float endY = startY + mapHeight;

        int save = canvas.save();
        float textCenterY = viewHeight / 2;
        float textCenterX = startX - arrowX - rectToScaleSize;
        canvas.rotate(90, textCenterX, textCenterY);
        drawText(canvas, Paint.Align.CENTER, TEXT, textCenterX, textCenterY, true);
        canvas.restoreToCount(save);

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        arrowPath.reset();
        arrowPath.moveTo(startX, textCenterY - arrowHeight / 2);
        arrowPath.rLineTo(0, arrowHeight);
        arrowPath.rLineTo(0, 0);
        arrowPath.setLastPoint(startX + arrowX, textCenterY + arrowHeight / 2 - arrowY);
        arrowPath.rLineTo(-arrowX, arrowY);
        arrowPath.rLineTo(-arrowX, -arrowY);
        canvas.drawPath(arrowPath, paint);

        float circleStartX = startX + radiusToArrowLine;
        canvas.drawCircle(circleStartX + radius, textCenterY, radius, paint);

        float rectStartX = circleStartX + 2 * radius + rectToRadiusSpace;
        float rectWidth = wScale * (widths.length - 1);
        canvas.drawRect(rectStartX, startY, rectStartX + rectWidth,
                startY + mapHeight, paint);

        restorePaintParams();

        //圆上的文字
        drawText(canvas, Paint.Align.RIGHT, "270'",
                startX + radiusToArrowLine - rectToScaleSize,
                textCenterY, true);

        drawText(canvas, Paint.Align.LEFT, "90'",
                startX + radiusToArrowLine + 2 * radius + rectToScaleSize,
                textCenterY, true);

        drawText(canvas, Paint.Align.CENTER, "0'",
                startX + radiusToArrowLine + radius,
                textCenterY - radius - rectToScaleSize, false);

        drawText(canvas, Paint.Align.CENTER, "180'",
                startX + radiusToArrowLine + radius,
                textCenterY + radius + rectToScaleSize, 1f);

        //横刻度
        canvas.drawLine(rectStartX,
                startY - rectToScaleSize,
                rectStartX + rectWidth,
                startY - rectToScaleSize,
                paint);

        for (int i = 0; i < widths.length; i++) {
            float x = rectStartX + i * wScale * wStep;
            canvas.drawLine(x, startY - rectToScaleSize - scaleSize, x,
                    startY - rectToScaleSize, paint);

            drawText(canvas, Paint.Align.CENTER, widths[i] + "'", x,
                    startY - rectToScaleSize - scaleSize - textToScaleSize,
                    false);
        }

        //竖刻度
        canvas.drawLine(endX + rectToScaleSize,
                startY, endX + rectToScaleSize, startY + mapHeight, paint);

        //hCount = 8.2  ( 0 - 7 , 8.2 ) 满足条件
        int floorH = (int) Math.floor(hCount);
        for (float k = 0; k <= floorH; k++) {
            float i = k;
            if (k == floorH) {
                i = hCount;
            }

            float y = startY + i * hStep * hScale;
            canvas.drawLine(
                    endX + rectToScaleSize, y,
                    endX + rectToScaleSize + scaleSize, y, paint);

            String text = removeZero(i * hStep + "") + unit;
            drawText(canvas, Paint.Align.LEFT, text,
                    endX + rectToScaleSize + scaleSize + textToScaleSize,
                    y, true);
        }
    }

    @Override
    public float[] getBounds() {
        float mapWidth = wScale * (widths.length - 1) + 2 * radius + rectToRadiusSpace + radiusToArrowLine + 2 * margin;
        float mapHeight = hScale * hStep * hCount + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
