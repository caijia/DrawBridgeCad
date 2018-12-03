package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.regex.Pattern;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent12 extends BaseBridgeComponent {

    private static final String UNIT = "m";
    private static final String REGEX = "(L\\d+-)(\\d+)";
    private static final String TEXT_KXB_CODE = "空心板编号";
    private static final String TEXT_JF_CODE = "铰缝编号";
    private float degree = 80;
    private int dWidth;
    private Path path = new Path();
    private double tanDegree;
    private String wUnit;
    private Pattern pattern = Pattern.compile(REGEX);

    public BridgeComponent12(Context context) {
        super(context);
    }

    public BridgeComponent12(Context context, float degree) {
        super(context);
        this.degree = degree;
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width, float height) {
        int freeHeight = viewHeight - margin * 2;
        float avgHeight = freeHeight / height;
        hCount = height;
        if (avgHeight < minScale) {
            int stepCount = freeHeight / minScale;
            hStep = (int) (height / stepCount);
            hScale = (int) avgHeight;
            hCount = height / hStep;
        }

        float mapHeight = hCount * hScale * hStep;
        tanDegree = Math.tan(Math.toRadians(degree));
        dWidth = (int) (mapHeight / tanDegree);
        wScale = minScale;
        wCount = width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, int width, float height,
                     String direction, int dun) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);

        //矩形宽度
        float mapWidth = wCount * wScale * wStep + dWidth;

        //矩形高度
        float mapHeight = hCount * hStep * hScale;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        float tanX = (float) (mapHeight / tanDegree);
        for (int i = 0; i < wCount; i++) {
            wUnit = direction + dun + "-";
            String text = wUnit + (i + 1);
            int incrementWidth = i * wScale * wStep;
            drawText(canvas, Paint.Align.CENTER, text,
                    rectStartX + dWidth + incrementWidth + wScale * wStep / 2,
                    rectStartY - rectToScaleSize, false);

            if (i < wCount - 1) {
                drawText(canvas, Paint.Align.CENTER, text,
                        rectStartX + incrementWidth + wScale * wStep,
                        rectEndY + rectToScaleSize, true);
            }

            canvas.drawLine(
                    rectStartX + dWidth + incrementWidth,
                    rectStartY,
                    rectStartX + dWidth - tanX + incrementWidth,
                    rectEndY,
                    paint
            );
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

            String text = removeZero(i * hStep + "") + UNIT;
            drawText(canvas, Paint.Align.LEFT, text,
                    rectEndX + rectToScaleSize + scaleSize + textToScaleSize - curOffsetX,
                    rectStartY + curHeight, true);
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

        drawText(canvas, Paint.Align.CENTER, TEXT_KXB_CODE,
                rectStartX + dWidth + (mapWidth - dWidth) / 2,
                rectStartY - dpToPx(20), false);

        drawText(canvas, Paint.Align.CENTER, TEXT_JF_CODE,
                rectStartX + (mapWidth - dWidth) / 2,
                rectEndY + dpToPx(20), true);
    }
}
