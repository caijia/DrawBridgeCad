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
public class BridgeComponent5 extends BaseBridgeComponent {

    private static final String REGEX = "(L\\d+-\\d+-)(\\d+)";

    /**
     * 单位
     */
    private static final String W_UNIT = "m";
    private Pattern pattern = Pattern.compile(REGEX);
    private String hUnit;

    public BridgeComponent5(Context context) {
        super(context);
        minScale = (int) dpToPx(60);
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width, int height) {
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

        //矩形
        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectStartX, rectStartY, rectEndX, rectEndY, paint);
        restorePaintParams();

        float percentWidth = mapWidth / hCount;
        for (int k = 0; k < hCount; k++) {
            canvas.drawLine(
                    rectStartX + percentWidth * k + 0.5f,
                    rectStartY,
                    rectStartX + percentWidth * k,
                    rectEndY,
                    paint
            );
            String text = hUnit + (int) (hCount - k) + "#";
            drawText(canvas, text, rectStartX + percentWidth * k,rectEndY);
        }
    }

    private void drawText(Canvas canvas, String text, float x,float y) {
        savePaintParams();
        paint.setTextSize(spToPx(10));
        paint.setStrokeWidth(0);
        int[] textBounds = getTextBounds(text, paint);
        canvas.drawText(
                text,
                x,
                y + textBounds[1] + textToScaleSize,
                paint);
        restorePaintParams();
    }
}
