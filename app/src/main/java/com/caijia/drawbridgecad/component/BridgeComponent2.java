package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia 2018/11/26 15:24
 */
public class BridgeComponent2 extends BaseBridgeComponent {

    private static final String REGEX = "(L\\d+-)(\\d+)";

    /**
     * 单位
     */
    private static final String H_UNIT = "m";

    private Pattern pattern = Pattern.compile(REGEX);

    private String wUnit;

    public BridgeComponent2(Context context) {
        super(context);
    }

    /**
     * @param canvas     canvas
     * @param viewWidth  viewWidth
     * @param viewHeight viewHeight
     * @param widthStr   这个值必须是L1-1  这样的格式
     * @param height     height
     */
    public void draw(Canvas canvas, int viewWidth, int viewHeight, String widthStr, int height) {
        if (TextUtils.isEmpty(widthStr)) {
            throw new RuntimeException("widthStr is null");
        }
        boolean isMatcher = widthStr.matches(REGEX);
        if (!isMatcher) {
            throw new RuntimeException("widthStr format is error must be matcher " + REGEX);
        }
        Matcher matcher = pattern.matcher(widthStr);
        if (matcher.matches()) {
            wUnit = matcher.group(1);
            int width = Integer.parseInt(matcher.group(2));
            draw(canvas, viewWidth, viewHeight, width, height);
        }
    }

    private void computeScaleAndStep(int viewWidth, int viewHeight, int width, int height) {
        int freeHeight = viewHeight - margin * 2;
        hScale = freeHeight / height;
        if (minScale > hScale) {
            int stepCount = freeHeight / minScale;
            hStep = height / stepCount;
        }
        hCount = (float) height / hStep;
        wCount = width;
    }

    private void draw(Canvas canvas, int viewWidth, int viewHeight, int width, int height) {
        computeScaleAndStep(viewWidth, viewHeight, width, height);
        //矩形宽度
        float mapWidth = wCount * wScale * wStep;

        //矩形高度
        float mapHeight = hCount * hScale * hStep;

        float rectStartX = (viewWidth - mapWidth) / 2;
        float rectStartY = (viewHeight - mapHeight) / 2;

        float rectEndX = rectStartX + mapWidth;
        float rectEndY = rectStartY + mapHeight;

        //横上部编号
        for (int i = 0; i < wCount; i++) {
            canvas.drawLine(
                    rectStartX + i * wScale * wStep,
                    rectStartY,
                    rectStartX + i * wScale * wStep,
                    rectEndY,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(
                    wUnit + (i + 1),
                    rectStartX + i * wScale * wStep + wScale * wStep / 2,
                    rectStartY - rectToScaleSize,
                    paint);

            if (i > 0 && i < wCount) {
                int textHalfHeight = getTextBounds(wUnit + i, paint)[1] / 2;
                canvas.drawText(
                        wUnit + i,
                        rectStartX + i * wScale * wStep,
                        rectEndY + rectToScaleSize + textHalfHeight,
                        paint);
            }
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
            int textHalfHeight = getTextBounds((int) (i * hStep) + H_UNIT, paint)[1] / 2;
            canvas.drawText(
                    (int) (i * hStep) + H_UNIT,
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
