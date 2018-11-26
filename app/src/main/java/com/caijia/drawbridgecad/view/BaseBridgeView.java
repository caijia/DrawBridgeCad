package com.caijia.drawbridgecad.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.caijia.drawbridgecad.component.ActionComponent;
import com.caijia.drawbridgecad.component.DrawTextComponent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 桥涵的底图
 * Created by cai.jia 2018/11/14 15:20
 */
public abstract class BaseBridgeView extends View {

    private ActionComponent actionComponent;
    private List<DrawTextComponent> textList = new CopyOnWriteArrayList<>();

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        drawBackgroundComponent(canvas);
        actionComponent.draw(canvas);
        for (DrawTextComponent textComponent : textList) {
            textComponent.drawText(canvas, viewWidth / 2, viewHeight / 2,
                    spToPx(22));
        }
    }

    public abstract void drawBackgroundComponent(Canvas canvas);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandleText = false;
        for (DrawTextComponent textComponent : textList) {
            textComponent.onTouchEvent(event);
            if (!isHandleText) {
                isHandleText = textComponent.isHandleText();
            }
        }

        if (!isHandleText) {
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
        actionComponent.drawPath();
    }

    public void drawLine() {
        actionComponent.drawLine();
    }

    public void drawCircle() {
        actionComponent.drawOval();
    }

    public void drawRect() {
        actionComponent.drawRect();
    }

    public void cancelPreviousDraw() {
        actionComponent.cancelPreviousDraw();
    }
}
