package com.caijia.drawbridgecad.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.caijia.drawbridgecad.MoveGestureDetector;
import com.caijia.drawbridgecad.component.ActionComponent;
import com.caijia.drawbridgecad.component.DrawTextComponent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 桥涵的底图
 * Created by cai.jia 2018/11/14 15:20
 */
public abstract class BaseBridgeView extends View implements MoveGestureDetector.OnMoveGestureListener {

    private ActionComponent actionComponent;
    private List<DrawTextComponent> textList = new CopyOnWriteArrayList<>();
    private MoveGestureDetector gestureDetector;
    private float xOffset;
    private float yOffset;
    private boolean move = true;

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

    public float spToPx(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    public float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private void init(final Context context, AttributeSet attrs) {
        actionComponent = new ActionComponent(this);
        gestureDetector = new MoveGestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int save = canvas.save();
        canvas.translate(xOffset, yOffset);
        drawBackgroundComponent(canvas);
        actionComponent.draw(canvas);
        canvas.restoreToCount(save);

        for (DrawTextComponent textComponent : textList) {
            textComponent.drawText(
                    canvas,
                    viewWidth / 2 + xOffset,
                    viewHeight / 2 + yOffset,
                    spToPx(22));
        }
    }

    public abstract void drawBackgroundComponent(Canvas canvas);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandleText = false;
        //操作文字
        for (DrawTextComponent textComponent : textList) {
            textComponent.onTouchEvent(event);
            if (!isHandleText) {
                isHandleText = textComponent.isHandleText();
            }
        }

        //移动整体
        if (move && !isHandleText) {
            gestureDetector.onTouchEvent(event);
        }

        //基本图形
        if (!move && !isHandleText) {
            actionComponent.onTouchEvent(event);
        }
        return true;
    }

    public void drawText(String text) {
        DrawTextComponent textComponent = new DrawTextComponent(this, text);
        textComponent.setOnCloseTextComponentListener(() -> {
            textList.remove(textComponent);
            invalidate();
        });
        textList.add(textComponent);
        invalidate();
    }

    public void drawPath() {
        move = false;
        actionComponent.drawPath();
    }

    public void drawLine() {
        move = false;
        actionComponent.drawLine();
    }

    public void drawCircle() {
        move = false;
        actionComponent.drawOval();
    }

    public void drawRect() {
        move = false;
        actionComponent.drawRect();
    }

    public void cancelPreviousDraw() {
        actionComponent.cancelPreviousDraw();
    }

    public void move() {
        move = true;
        actionComponent.disable();
    }

    @Override
    public void onMoveGestureScroll(MotionEvent downEvent, MotionEvent currentEvent,
                                    int pointerIndex, float dx, float dy, float distanceX,
                                    float distanceY) {
        xOffset += dx;
        yOffset += dy;
        actionComponent.setOffset(xOffset, yOffset);

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
        return false;
    }

    /**
     * 生产bitmap
     *
     * @return bitmap
     */
    public Bitmap saveToBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        return getDrawingCache();
    }
}
