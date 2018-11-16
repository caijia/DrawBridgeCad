package com.caijia.drawbridgecad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 桥涵的底图
 * Created by cai.jia 2018/11/14 15:20
 */
public class BaseBridgeView extends View implements ScaleGestureDetector.OnScaleGestureListener,
        MoveGestureDetector.OnMoveGestureListener {

    /**
     * 关闭文字组件
     */
    private static final int ACTION_CLOSE_TEXT = 1;
    /**
     * 缩放+旋转文字组件
     */
    private static final int ACTION_SCALE_ROTATE_TEXT = 2;
    /**
     * 移动文字组件
     */
    private static final int ACTION_MOVE_TEXT = 3;

    /**
     * 移动整个绘制的图形整体
     */
    private static final int ACTION_MOVE_SHAPE = 4;

    RectF textRect = new RectF();
    private Paint paint;
    /**
     * 宽度
     */
    private int width = 10;
    /**
     * 高度
     */
    private int height = 10;
    /**
     * 单位
     */
    private String unit = "m";
    /**
     * 每个刻度的大小
     */
    private int scale;
    private int sourceScale;
    /**
     * 刻度高度
     */
    private int scaleSize;
    /**
     * 矩形到刻度之间的间隔
     */
    private int space;
    /**
     * 文字到刻度的间隔
     */
    private int textToScaleSize;
    private Rect textBounds = new Rect();
    private MoveGestureDetector moveGestureDetector;
    private int xOffset;
    private int yOffset;
    private ScaleGestureDetector scaleGestureDetector;
    private int radius;
    private PaintParams paintParams = new PaintParams();
    /**
     * 文字圆形图标的颜色
     */
    private int circleIconColor;
    private int textOffsetX;
    private int textOffsetY;
    private float textSize;
    private int action = -1;
    /**
     * 绘制图形的总区域
     */
    private RectF drawRect = new RectF();
    private double textDegree;
    private float textScale = 1;

    public BaseBridgeView(Context context) {
        this(context, null);
    }

    public BaseBridgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBridgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private float spToPx(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dpToPx(1));

        sourceScale = scale = (int) dpToPx(26);
        scaleSize = (int) dpToPx(4);
        space = (int) dpToPx(8);
        textToScaleSize = (int) dpToPx(2);
        radius = (int) dpToPx(8);
        circleIconColor = Color.parseColor("#ddffffff");
        textSize = spToPx(28);

        scaleGestureDetector = new ScaleGestureDetector(context, this);
        moveGestureDetector = new MoveGestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        //矩形宽度
        int mapWidth = width * scale;

        //矩形高度
        int mapHeight = height * scale;

        int rectStartX = (viewWidth - mapWidth) / 2 + xOffset;
        int rectStartY = (viewHeight - mapHeight) / 2 + yOffset;

        int rectEndX = rectStartX + mapWidth;
        int rectEndY = rectStartY + mapHeight;

        //横刻度
        canvas.drawLine(
                rectStartX,
                rectStartY - space,
                rectEndX,
                rectStartY - space,
                paint);

        for (int i = 0; i < width + 1; i++) {
            canvas.drawLine(
                    rectStartX + i * scale,
                    rectStartY - scaleSize - space,
                    rectStartX + i * scale,
                    rectStartY - space,
                    paint);

            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfWidth = getTextBounds(i + unit)[0] / 2;
            canvas.drawText(
                    i + unit,
                    rectStartX + i * scale - textHalfWidth,
                    rectStartY - scaleSize - space - textToScaleSize,
                    paint);
            restorePaintParams();
        }

        //竖刻度
        canvas.drawLine(
                rectEndX + space,
                rectStartY,
                rectEndX + space,
                rectEndY,
                paint);

        for (int i = 0; i < height + 1; i++) {
            canvas.drawLine(
                    rectEndX + space,
                    rectStartY + i * scale,
                    rectEndX + space + scaleSize,
                    rectStartY + i * scale,
                    paint);


            savePaintParams();
            paint.setTextSize(spToPx(10));
            paint.setStrokeWidth(0);
            int textHalfHeight = getTextBounds(i + unit)[1] / 2;
            canvas.drawText(
                    i + unit,
                    rectEndX + space + scaleSize + textToScaleSize,
                    rectStartY + i * scale + textHalfHeight,
                    paint);
            restorePaintParams();
        }

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        drawRect.set(rectStartX, rectStartY, rectEndX, rectEndY);
        canvas.drawRect(drawRect, paint);
        restorePaintParams();

        //绘制文字
        drawText(canvas,
                "绘制",
                viewWidth / 2 + textOffsetX + xOffset,
                viewHeight / 2 + textOffsetY + yOffset,
                textSize, textScale, (float) textDegree);

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(getTextRbRect(), paint);
        restorePaintParams();
    }

    /**
     * 绘制文字组件,包含边框+操作按钮+关闭按钮+文字
     *
     * @param canvas   canvas
     * @param text     文字
     * @param centerX  x坐标
     * @param centerY  y坐标
     * @param testSize 文字字体大小
     * @param scale    缩放
     * @param degree   旋转角度
     */
    private void drawText(Canvas canvas, String text, int centerX, int centerY, float testSize,
                          float scale, float degree) {
        int saveTotal = canvas.save();
        canvas.rotate(degree, centerX, centerY);
        //绘制文字边框
        drawTextBounds(canvas, text, centerX, centerY, testSize, scale);
        //绘制文字
        drawActualText(canvas, text, testSize, scale);
        //绘制文字关闭图标
        drawCloseTextIcon(canvas);
        //绘制文字操作图标
        drawHandleTextIcon(canvas);
        canvas.restoreToCount(saveTotal);
    }

    /**
     * 绘制文字边框
     *
     * @param canvas   canvas
     * @param text     文字
     * @param centerX  x坐标
     * @param centerY  y坐标
     * @param testSize 文字字体大小
     * @param scale    缩放
     */
    private void drawTextBounds(Canvas canvas, String text, int centerX, int centerY,
                                float testSize, float scale) {
        savePaintParams();
        paint.setTextSize(testSize * scale);
        paint.setStrokeWidth(0);
        int[] bounds = getTextBounds(text);
        restorePaintParams();

        textRect.set(
                centerX - bounds[0] / 2,
                centerY - bounds[1] / 2,
                centerX + bounds[0] / 2,
                centerY + bounds[1] / 2);
        textRect.inset(-radius, -radius);

        savePaintParams();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                centerX - bounds[0] / 2 - radius,
                centerY - bounds[1] / 2 - radius,
                centerX + bounds[0] / 2 + radius,
                centerY + bounds[1] / 2 + radius,
                paint);
        restorePaintParams();
    }

    /**
     * 绘制文字
     *
     * @param canvas   canvas
     * @param text     文字
     * @param testSize 文字字体大小
     * @param scale    缩放
     */
    private void drawActualText(Canvas canvas, String text, float testSize, float scale) {
        savePaintParams();
        paint.setTextSize(testSize * scale);
        paint.setStrokeWidth(0);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, textRect.left + radius, baseline, paint);
        restorePaintParams();
    }

    /**
     * 绘制关闭文字的图标
     *
     * @param canvas canvas
     */
    private void drawCloseTextIcon(Canvas canvas) {
        //绘制文字左上角关闭图标
        savePaintParams();
        paint.setColor(circleIconColor);
        canvas.drawCircle(textRect.left, textRect.top, radius, paint);
        restorePaintParams();

        //绘制关闭图标的X
        savePaintParams();
        paint.setColor(Color.GRAY);
        float spacing = dpToPx(3);
        float radiusSin45 = (float) (Math.sin(Math.toRadians(45)) * (radius - spacing));

        canvas.drawLine(
                textRect.left - radiusSin45,
                textRect.top - radiusSin45,
                textRect.left + radiusSin45,
                textRect.top + radiusSin45,
                paint);

        canvas.drawLine(
                textRect.left - radiusSin45,
                textRect.top + radiusSin45,
                textRect.left + radiusSin45,
                textRect.top - radiusSin45,
                paint);
        restorePaintParams();
    }

    /**
     * 绘制文字右下角的操作图标
     *
     * @param canvas canvas
     */
    private void drawHandleTextIcon(Canvas canvas) {
        float spacing = dpToPx(3);
        //绘制文字右下角操作图标
        savePaintParams();
        paint.setColor(circleIconColor);
        canvas.drawCircle(textRect.right, textRect.bottom, radius, paint);
        restorePaintParams();

        savePaintParams();
        paint.setColor(Color.GRAY);
        int arrowWidth = (int) dpToPx(4);
        float radiusSin45 = (float) (Math.sin(Math.toRadians(45)) * (radius - spacing));
        float left = textRect.right - radiusSin45;
        float top = textRect.bottom - radiusSin45;
        float right = textRect.right + radiusSin45;
        float bottom = textRect.bottom + radiusSin45;

        canvas.drawLine(left, top, right, bottom, paint);
        canvas.drawLine(left, top, left + arrowWidth, top, paint);
        canvas.drawLine(left, top, left, top + arrowWidth, paint);
        canvas.drawLine(right - arrowWidth, bottom, right, bottom, paint);
        canvas.drawLine(right, bottom - arrowWidth, right, bottom, paint);
        restorePaintParams();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        moveGestureDetector.onTouchEvent(event);
        return true;
    }

    private int[] getTextBounds(String text) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return new int[]{textBounds.width(), textBounds.height()};
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        scale = (int) (sourceScale * scaleFactor);
        invalidate();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        sourceScale = (int) (detector.getScaleFactor() * sourceScale);
    }

    @Override
    public void onMoveGestureScroll(MotionEvent e, int pointerIndex, float dx, float dy,
                                    float distanceX, float distanceY) {
        switch (action) {
            case ACTION_CLOSE_TEXT:
                clearText();
                break;

            case ACTION_MOVE_TEXT:
                textOffsetX += dx;
                textOffsetY += dy;
                break;

            case ACTION_SCALE_ROTATE_TEXT:
                float centerX = textRect.centerX();
                float centerY = textRect.centerY();
                float currentX = e.getX(pointerIndex);
                float currentY = e.getY(pointerIndex);
                double radians = Math.atan2(currentY - centerY, currentX - centerX);
                textDegree = Math.toDegrees(radians);
                break;

            case ACTION_MOVE_SHAPE:
                xOffset += dx;
                yOffset += dy;
                break;
        }
        invalidate();
    }

    /**
     * 清除文字
     */
    private void clearText() {

    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent event) {
        action = -1;
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {

    }


    private RectF getTextLfRect() {
        Matrix textMatrix = new Matrix();
        textMatrix.setRotate((float) textDegree, textRect.centerX(), textRect.centerY());
        textMatrix.mapRect(textRect);
        return new RectF(textRect.left - radius, textRect.top - radius,
                textRect.left + radius, textRect.top + radius);
    }

    private RectF getTextRbRect() {
        RectF dst = new RectF(textRect.right - radius, textRect.bottom - radius,
                textRect.right + radius, textRect.bottom + radius);
        dst.inset(-dpToPx(10),-dpToPx(10));
        return dst;
    }

    private float[] mapPoint(float x,float y) {
        Matrix textMatrix = new Matrix();
        textMatrix.setRotate(-(float) textDegree, textRect.centerX(), textRect.centerY());
        float[] dst = new float[2];
        textMatrix.mapPoints(dst,new float[]{x, y});
        return dst;
    }

    @Override
    public boolean onMoveGestureBeginTap(MotionEvent event) {
        float[] mapPoints = mapPoint(event.getX(), event.getY());
        float x = mapPoints[0];
        float y = mapPoints[1];

        if (getTextLfRect().contains(x, y)) {
            action = ACTION_CLOSE_TEXT;

        } else if (getTextRbRect().contains(x, y)) {
            action = ACTION_SCALE_ROTATE_TEXT;

        } else if (textRect.contains(x, y)) {
            action = ACTION_MOVE_TEXT;

        } else if (drawRect.contains(x, y)) {
            action = ACTION_MOVE_SHAPE;
        }
        return false;
    }

    public void drawText(String s) {

    }

    private void restorePaintParams() {
        paint.setColor(paintParams.color);
        paint.setStrokeWidth(paintParams.strokeWidth);
        paint.setTextSize(paintParams.textSize);
        paint.setStyle(paintParams.style);
    }

    private void savePaintParams() {
        paintParams.color = paint.getColor();
        paintParams.strokeWidth = paint.getStrokeWidth();
        paintParams.textSize = paint.getTextSize();
        paintParams.style = paint.getStyle();
    }

    /**
     * 画笔常用属性
     */
    static class PaintParams {
        int color;
        float strokeWidth;
        float textSize;
        Paint.Style style;
    }
}
