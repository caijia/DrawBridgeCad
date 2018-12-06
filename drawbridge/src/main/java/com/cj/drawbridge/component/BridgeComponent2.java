package com.cj.drawbridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent2 extends BaseBridgeComponent {

    PathMeasure pathMeasure = new PathMeasure();
    private String hUnit;
    //弧形部分
    private Path arcPath = new Path();
    private int initHeight;
    private float[] pos = new float[2];
    private float[] tan = new float[2];

    public BridgeComponent2(Context context) {
        super(context);
        initHeight = (int) dpToPx(26);
        minScale = (int) dpToPx(50);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, float width, int height) {
        hCount = height;
        hStep = 1;
        hScale = (int) dpToPx(10);

        int freeWidth = viewWidth - margin * 2;
        float percentWidth = freeWidth / (hCount + 1);
        if (percentWidth < minScale) {
            freeWidth = (int) (minScale * (hCount + 1));
        }
        wScale = freeWidth / width;
        if (minScale > wScale) {
            int stepCount = (int) (freeWidth / minScale);
            wStep = (int) (width / stepCount);
        }
        wCount = width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, float width, int dunShu,
                     String direction, int zuoDun, int youDun, String wUnit) {
        computeScaleAndStep(viewWidth, viewHeight, width, dunShu);
        //宽度
        float mapWidth = wCount * wScale * wStep;

        //高度
        float mapHeight = initHeight + (hCount - 1) * hScale * hStep;

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
                    false);
        }

        //横线
        canvas.drawLine(rectStartX, rectStartY, rectEndX, rectStartY, paint);

        //圆弧 从第二个 - 倒数第二个 画弧
        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        float percentWidth = mapWidth / (hCount + 1);
        arcPath.reset();
        arcPath.moveTo(rectStartX, rectEndY);
        arcPath.rLineTo(percentWidth, 0);
        arcPath.quadTo(
                rectStartX + (rectEndX - rectStartX) / 2,
                rectStartY + initHeight,
                rectEndX - percentWidth,
                rectStartY + initHeight);
        arcPath.lineTo(rectEndX, rectStartY + initHeight);
        canvas.drawPath(arcPath, paint);

        pathMeasure.setPath(arcPath, false);
        restorePaintParams();

        float pathLength = pathMeasure.getLength();
        float percentPathLength = pathLength / (hCount + 1); //8等分
        for (int k = 0; k < hCount + 2; k++) {  //9条线
            pathMeasure.getPosTan(k * percentPathLength, pos, tan);
            canvas.drawLine(pos[0], rectStartY, pos[0], pos[1], paint);

            if (k < hCount + 1) {
                hUnit = direction + zuoDun + "-" + youDun + "-";
                String text = hUnit + k + "#";
                drawText(canvas, Paint.Align.LEFT, text,
                        pos[0],
                        pos[1] + textToScaleSize,
                        1f);
            }
        }
    }

    @Override
    public float[] getBounds() {
        //宽度
        float mapWidth = wCount * wScale * wStep + 2 * margin;

        //高度
        float mapHeight = initHeight + (hCount - 1) * hScale * hStep + 2 * margin;
        return new float[]{mapWidth, mapHeight};
    }
}
