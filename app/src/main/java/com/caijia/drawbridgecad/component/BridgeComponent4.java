package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia 2018/11/26 08:44
 */
public class BridgeComponent4 extends BaseBridgeComponent {

    private static final String REGEX = "(L\\d+-\\d+-)(\\d+)";

    /**
     * 单位
     */
    private static final String W_UNIT = "m";
    PathMeasure pathMeasure = new PathMeasure();
    private Pattern pattern = Pattern.compile(REGEX);
    private String hUnit;
    //弧形部分
    private Path arcPath = new Path();
    private int initHeight;
    private float[] pos = new float[2];
    private float[] tan = new float[2];

    public BridgeComponent4(Context context) {
        super(context);
        initHeight = (int) dpToPx(26);
        minScale = (int) dpToPx(56);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width, int height) {
        hCount = height;
        hStep = 1;
        hScale = (int) dpToPx(10);

        int freeWidth = viewWidth - margin * 2;
        float percentWidth = freeWidth / (2 * hCount + 1);
        if (percentWidth < minScale) {
            freeWidth = (int) (minScale * (2 * hCount + 1));
        }
        wScale = freeWidth / width;
        if (minScale > wScale) {
            int stepCount = freeWidth / minScale;
            wStep = width / stepCount;
        }
        wCount = (float) width / wStep;
    }

    public void draw(Canvas canvas, int viewWidth, int viewHeight, int width, String heightExtra) {
        if (TextUtils.isEmpty(heightExtra)) {
            throw new RuntimeException("heightExtra is null");
        }

        boolean isMatcher = heightExtra.matches(REGEX);
        if (!isMatcher) {
            throw new RuntimeException("heightExtra format is error must be matcher " + REGEX);
        }
        Matcher matcher = pattern.matcher(heightExtra);
        if (matcher.matches()) {
            hUnit = matcher.group(1);
            int height = Integer.parseInt(matcher.group(2));
            draw(canvas, viewWidth, viewHeight, width, height);
        }
    }

    private void draw(Canvas canvas, int viewWidth, int viewHeight, int width, int height) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
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
            int textHalfWidth = getTextBounds((int) (i * wStep) + W_UNIT, paint)[0] / 2;
            canvas.drawText(
                    (int) (i * wStep) + W_UNIT,
                    rectStartX + i * wScale * wStep - textHalfWidth,
                    rectStartY - scaleSize - rectToScaleSize - textToScaleSize,
                    paint);
            restorePaintParams();
        }

        //横线
        canvas.drawLine(rectStartX, rectStartY, rectEndX, rectStartY, paint);

        //画弧
        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        float percentWidth = mapWidth / (2 * hCount + 1);
        arcPath.reset();
        arcPath.moveTo(rectStartX, rectEndY);
        arcPath.lineTo(rectStartX + percentWidth, rectEndY);
        arcPath.quadTo(
                rectStartX + (rectEndX - rectStartX) / 2,
                rectStartY - (mapHeight - initHeight) / 2,
                rectEndX - percentWidth,
                rectEndY);
        arcPath.lineTo(rectEndX, rectEndY);
        canvas.drawPath(arcPath, paint);

        pathMeasure.setPath(arcPath, false);
        restorePaintParams();

        float pathLength = pathMeasure.getLength();
        float percentPathLength = pathLength / (2 * hCount + 1); // 8 -> 17
        for (int k = 0; k < 2 * hCount + 2; k++) {

            if (k <= hCount) {
                pathMeasure.getPosTan(k * percentPathLength, pos, tan);
                canvas.drawLine(pos[0], rectStartY, pos[0], pos[1], paint);

                if (k != hCount) {
                    String text = hUnit + k + "#";
                    drawText(canvas, text, pos[0]);
                }

            } else {
                pathMeasure.getPosTan(
                        (2 * hCount + 1 - (k - hCount - 1)) * percentPathLength, pos, tan);
                canvas.drawLine(pos[0], rectStartY, pos[0], pos[1], paint);

                String text = hUnit + (int) (k - hCount - 1) + "#";
                drawText(canvas, text, pos[0] - percentWidth);
            }
        }
    }

    private void drawText(Canvas canvas, String text, float x) {
        savePaintParams();
        paint.setTextSize(spToPx(10));
        paint.setStrokeWidth(0);
        int[] textBounds = getTextBounds(text, paint);
        canvas.drawText(
                text,
                x,
                pos[1] + textBounds[1] + textToScaleSize,
                paint);
        restorePaintParams();
    }
}
