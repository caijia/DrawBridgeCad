package com.caijia.drawbridgecad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * 绘制文本组件
 * Created by cai.jia 2018/11/18 18:50
 */
public class DrawTextComponent implements MoveGestureDetector.OnMoveGestureListener, DrawComponent {

    /**
     * 缩放+旋转文字组件
     */
    private static final int ACTION_SCALE_ROTATE_TEXT = 2;

    /**
     * 移动文字组件
     */
    private static final int ACTION_MOVE_TEXT = 3;

    /**
     * 是否显示文字的边框和操作按钮
     */
    private boolean showTextDecoration = true;

    /**
     * 文字组件的旋转角度
     */
    private float textDegree;

    /**
     * 文字组件缩放大小
     */
    private float textScale = 1;

    /**
     * 前一次文字组件旋转的角度
     */
    private double previousDegree;

    /**
     * 前一次文字组件缩放大小
     */
    private float previousTextScale;

    /**
     * 文字组件的大小
     */
    private RectF textRect = new RectF();

    /**
     * 文字组件操作按钮的背景颜色
     */
    private int circleIconColor;

    /**
     * 计算文字组件大小的Rect
     */
    private Rect textBounds = new Rect();

    /**
     * 文字组件操作按钮的半径
     */
    private int radius;

    /**
     * 画笔的属性
     */
    private PaintParams paintParams = new PaintParams();

    /**
     * 上下文
     */
    private Context context;

    /**
     * 手势帮助类
     */
    private MoveGestureDetector moveGestureDetector;

    /**
     * 文字组件X轴偏移量
     */
    private float textOffsetX;

    /**
     * 文字组件Y轴偏移量
     */
    private float textOffsetY;

    /**
     * 文字组件的动作，移动 ，放大，旋转，关闭
     */
    private int action;
    private OnCloseTextComponentListener onCloseTextComponentListener;

    public DrawTextComponent(Context context) {
        this.context = context;
        radius = (int) dpToPx(12);
        circleIconColor = Color.parseColor("#ddffffff");
        moveGestureDetector = new MoveGestureDetector(context, this);
    }


    @Override
    public void onDraw(Canvas canvas, Paint paint) {

    }

    /**
     * 绘制文字组件,包含边框+操作按钮+关闭按钮+文字
     *
     * @param canvas   canvas
     * @param text     文字
     * @param centerX  中心点x坐标
     * @param centerY  中心点y坐标
     * @param testSize 文字字体大小
     * @param paint    paint
     */
    public void drawText(Canvas canvas, String text, int centerX, int centerY, float testSize, Paint paint) {
        int saveTotal = canvas.save();
        canvas.rotate(textDegree, centerX + textOffsetX, centerY + textOffsetY);
        canvas.scale(textScale, textScale, centerX + textOffsetX, centerY + textOffsetY);
        //绘制文字边框
        drawTextBounds(canvas, text, centerX + textOffsetX, centerY + textOffsetY, testSize, paint);
        //绘制文字关闭图标
        drawCloseTextIcon(canvas, paint);
        //绘制文字操作图标
        drawHandleTextIcon(canvas, paint);
        //绘制文字
        drawActualText(canvas, text, testSize, paint);
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
     * @param paint    paint
     */
    private void drawTextBounds(Canvas canvas, String text, float centerX, float centerY,
                                float testSize, Paint paint) {

        savePaintParams(paint);
        paint.setTextSize(testSize);
        paint.setStrokeWidth(0);
        int[] bounds = getTextBounds(text, paint);
        restorePaintParams(paint);

        textRect.set(
                centerX - bounds[0] / 2,
                centerY - bounds[1] / 2,
                centerX + bounds[0] / 2,
                centerY + bounds[1] / 2);
        textRect.inset(-radius, -radius);
        savePaintParams(paint);
        paint.setStyle(Paint.Style.STROKE);
        if (!showTextDecoration) {
            paint.setColor(Color.TRANSPARENT);
        }
        canvas.drawRect(textRect, paint);
        restorePaintParams(paint);
    }

    /**
     * 绘制文字
     *
     * @param canvas   canvas
     * @param text     文字
     * @param testSize 文字字体大小
     * @param paint    paint
     */
    private void drawActualText(Canvas canvas, String text, float testSize, Paint paint) {
        savePaintParams(paint);
        paint.setTextSize(testSize);
        paint.setStrokeWidth(0);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, textRect.left + radius, baseline, paint);
        restorePaintParams(paint);
    }

    /**
     * 绘制关闭文字的图标
     *
     * @param canvas canvas
     * @param paint  paint
     */
    private void drawCloseTextIcon(Canvas canvas, Paint paint) {
        //绘制文字左上角关闭图标
        savePaintParams(paint);
        paint.setColor(circleIconColor);
        if (!showTextDecoration) {
            paint.setColor(Color.TRANSPARENT);
        }
        canvas.drawCircle(textRect.left, textRect.top, radius, paint);
        restorePaintParams(paint);

        //绘制关闭图标的X
        savePaintParams(paint);
        paint.setColor(Color.GRAY);
        if (!showTextDecoration) {
            paint.setColor(Color.TRANSPARENT);
        }
        float spacing = radius / 3f;
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
        restorePaintParams(paint);
    }

    /**
     * 绘制文字右下角的操作图标
     *
     * @param canvas canvas
     * @param paint  paint
     */
    private void drawHandleTextIcon(Canvas canvas, Paint paint) {
        float spacing = radius / 3f;
        //绘制文字右下角操作图标
        savePaintParams(paint);
        paint.setColor(circleIconColor);
        if (!showTextDecoration) {
            paint.setColor(Color.TRANSPARENT);
        }
        canvas.drawCircle(textRect.right, textRect.bottom, radius, paint);
        restorePaintParams(paint);

        savePaintParams(paint);
        paint.setColor(Color.GRAY);
        if (!showTextDecoration) {
            paint.setColor(Color.TRANSPARENT);
        }
        int arrowWidth = radius / 2;
        float radiusSin45 = (float) (Math.sin(Math.toRadians(45)) * (radius - spacing));
        float left = textRect.right - radiusSin45;
        float top = textRect.bottom - radiusSin45;
        float right = textRect.right + radiusSin45;
        float bottom = textRect.bottom + radiusSin45;

        float halfStrokeW = paint.getStrokeWidth() / 2;
        canvas.drawLine(left, top, right, bottom, paint);
        canvas.drawLine(left - halfStrokeW, top, left + arrowWidth, top, paint);
        canvas.drawLine(left, top, left, top + arrowWidth, paint);
        canvas.drawLine(right - arrowWidth, bottom, right + halfStrokeW, bottom, paint);
        canvas.drawLine(right, bottom - arrowWidth, right, bottom, paint);
        restorePaintParams(paint);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        moveGestureDetector.onTouchEvent(event);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    private void restorePaintParams(Paint paint) {
        paint.setColor(paintParams.color);
        paint.setStrokeWidth(paintParams.strokeWidth);
        paint.setTextSize(paintParams.textSize);
        paint.setStyle(paintParams.style);
    }

    private void savePaintParams(Paint paint) {
        paintParams.color = paint.getColor();
        paintParams.strokeWidth = paint.getStrokeWidth();
        paintParams.textSize = paint.getTextSize();
        paintParams.style = paint.getStyle();
    }

    private int[] getTextBounds(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return new int[]{textBounds.width(), textBounds.height()};
    }

    @Override
    public void onMoveGestureScroll(MotionEvent downEvent, MotionEvent currentEvent,
                                    int pointerIndex, float dx, float dy,
                                    float distanceX, float distanceY) {
        switch (action) {
            case ACTION_MOVE_TEXT:
                textOffsetX += dx;
                textOffsetY += dy;
                break;

            case ACTION_SCALE_ROTATE_TEXT:
                double degree = getTextDegree(currentEvent, pointerIndex) - getTextDegree(downEvent, 0);
                textDegree = (float) ((degree + previousDegree) % 360);

                double startDis = toCenterDistance(textRect.right, textRect.bottom);
                double downDis = toCenterDistance(downEvent.getX(), downEvent.getY());
                double curDis = toCenterDistance(currentEvent.getX(pointerIndex), currentEvent.getY(pointerIndex));
                float deltaScale = (float) ((curDis - downDis) / startDis);
                textScale = deltaScale + previousTextScale;
                break;
        }
    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent event) {
        action = -1;
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {

    }

    @Override
    public boolean onMoveGestureBeginTap(MotionEvent event) {
        float[] points = mapPoint(event.getX(), event.getY());
        float x = points[0];
        float y = points[1];

        if (getTextRbRect().contains(x, y)) {
            if (showTextDecoration) {
                previousDegree = textDegree;
                previousTextScale = textScale;
                action = ACTION_SCALE_ROTATE_TEXT;
            }
            showTextDecoration = true;

        } else if (getTextLfRect().contains(x, y)) {
            if (showTextDecoration && onCloseTextComponentListener != null) {
                onCloseTextComponentListener.closeTextComponent();
            }
            showTextDecoration = true;

        } else if (textRect.contains(x, y)) {
            if (showTextDecoration) {
                action = ACTION_MOVE_TEXT;
            }
            showTextDecoration = true;

        } else {
            showTextDecoration = false;
        }

        return false;
    }

    /**
     * 文字组件是否包含了x,y
     *
     * @param x x坐标
     * @param y y坐标
     * @return true 包含
     */
    @Override
    public boolean contains(float x, float y) {
        float[] points = mapPoint(x, y);
        float mapX = points[0];
        float mapY = points[1];
        return getTextLfRect().contains(mapX, mapY) || getTextRbRect().contains(mapX, mapY) ||
                textRect.contains(mapX, mapY);
    }

    private double toCenterDistance(float x, float y) {
        return Math.hypot(x - textRect.centerX(), y - textRect.centerY());
    }

    private double getTextDegree(MotionEvent event, int pointerIndex) {
        double radians = Math.atan2(event.getY(pointerIndex) - textRect.centerY(),
                event.getX(pointerIndex) - textRect.centerX());
        return Math.toDegrees(radians);
    }

    private RectF getTextLfRect() {
        float[] points = {textRect.left, textRect.top};
        RectF dst = new RectF(points[0] - radius, points[1] - radius,
                points[0] + radius, points[1] + radius);
        dst.inset(-dpToPx(8), -dpToPx(8));
        return dst;
    }

    private RectF getTextRbRect() {
        float[] points = {textRect.right, textRect.bottom};
        RectF dst = new RectF(points[0] - radius, points[1] - radius,
                points[0] + radius, points[1] + radius);
        dst.inset(-dpToPx(8), -dpToPx(8));
        return dst;
    }

    private float[] mapPoint(float x, float y) {
        Matrix textMatrix = new Matrix();
        textMatrix.postRotate(-textDegree, textRect.centerX(), textRect.centerY());
        textMatrix.postScale(1 / textScale, 1 / textScale, textRect.centerX(), textRect.centerY());
        float[] dst = new float[2];
        textMatrix.mapPoints(dst, new float[]{x, y});
        return dst;
    }

    public void setOnCloseTextComponentListener(OnCloseTextComponentListener l) {
        this.onCloseTextComponentListener = l;
    }

    public interface OnCloseTextComponentListener {

        void closeTextComponent();
    }

    /**
     * 画笔常用属性
     */
    private static class PaintParams {
        int color;
        float strokeWidth;
        float textSize;
        Paint.Style style;
    }
}
