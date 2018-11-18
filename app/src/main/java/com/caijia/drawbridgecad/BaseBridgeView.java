package com.caijia.drawbridgecad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

/**
 * 桥涵的底图
 * Created by cai.jia 2018/11/14 15:20
 */
public class BaseBridgeView extends View implements ScaleGestureDetector.OnScaleGestureListener,
        MoveGestureDetector.OnMoveGestureListener {

    /**
     * 移动整个绘制的图形整体
     */
    private static final int ACTION_MOVE_SHAPE = 4;

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
    private PaintParams paintParams = new PaintParams();

    private int action = -1;
    /**
     * 绘制图形的总区域
     */
    private RectF drawRect = new RectF();
    private DrawTextComponent textComponent;

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

    private void init(final Context context, AttributeSet attrs) {
        textComponent = new DrawTextComponent(context);
        textComponent.setOnCloseTextComponentListener(new DrawTextComponent.OnCloseTextComponentListener() {
            @Override
            public void closeTextComponent() {
                Toast.makeText(context, "关闭Text", Toast.LENGTH_SHORT).show();
            }
        });
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(dpToPx(1));

        sourceScale = scale = (int) dpToPx(26);
        scaleSize = (int) dpToPx(4);
        space = (int) dpToPx(8);
        textToScaleSize = (int) dpToPx(2);

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
            int textHalfWidth = getTextBounds(i + unit, paint)[0] / 2;
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
            int textHalfHeight = getTextBounds(i + unit, paint)[1] / 2;
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
        textComponent.drawText(canvas, "绘制", viewWidth / 2, viewHeight / 2, spToPx(28), paint);
    }

    private int[] getTextBounds(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return new int[]{textBounds.width(), textBounds.height()};
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        moveGestureDetector.onTouchEvent(event);
        textComponent.onTouchEvent(event);
        return true;
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
    public void onMoveGestureScroll(MotionEvent downEvent, MotionEvent currentEvent,
                                    int pointerIndex, float dx, float dy,
                                    float distanceX, float distanceY) {
        switch (action) {
            case 2:
                break;
            case ACTION_MOVE_SHAPE:
                xOffset += dx;
                yOffset += dy;
                break;
        }
        invalidate();
    }


    @Override
    public void onMoveGestureUpOrCancel(MotionEvent event) {
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {

    }


    @Override
    public boolean onMoveGestureBeginTap(MotionEvent event) {

        if (textComponent.contains(event.getX(), event.getY())) {
            action = 2;
        } else if (drawRect.contains(event.getX(), event.getY())) {
            action = ACTION_MOVE_SHAPE;
        }
        invalidate();
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
