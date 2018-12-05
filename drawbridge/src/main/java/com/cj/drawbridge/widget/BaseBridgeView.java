package com.cj.drawbridge.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.cj.drawbridge.component.ActionComponent;
import com.cj.drawbridge.component.DrawTextComponent;
import com.cj.drawbridge.entity.BridgeParams;
import com.cj.drawbridge.helper.MoveGestureDetector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 桥涵的底图
 * Created by cai.jia 2018/11/14 15:20
 */
public abstract class BaseBridgeView extends View implements MoveGestureDetector.OnMoveGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    private ActionComponent actionComponent;
    private List<DrawTextComponent> textList = new CopyOnWriteArrayList<>();
    private MoveGestureDetector gestureDetector;
    private ScaleGestureDetector scaleGesture;
    private float xOffset;
    private float yOffset;
    private boolean move = true;
    private float scale = 1;
    private float sourceScale = 1;
    private Matrix canvasMatrix = new Matrix();
    private float preScaleFocusX;
    private float preScaleFocusY;

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
        scaleGesture = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int save = canvas.save();
        canvas.setMatrix(canvasMatrix);
        drawBackgroundComponent(canvas);
        actionComponent.draw(canvas);

        for (DrawTextComponent textComponent : textList) {
            textComponent.drawText(canvas, viewWidth / 2, viewHeight / 2);
        }
        canvas.restoreToCount(save);
    }

    public abstract void drawBackgroundComponent(Canvas canvas);

    public abstract void applyBridgeParams(BridgeParams params);

    public abstract BridgeParams getBridgeParams();

    public abstract float getMapWidth();

    public abstract float getMapHeight();

    public Bitmap createBitmap() {
        int mapWidth = (int) getMapWidth();
        int mapHeight = (int) getMapHeight();
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int save = canvas.save();
        canvas.translate((mapWidth - viewWidth) / 2, (mapHeight - viewHeight) / 2);
        drawBackgroundComponent(canvas);
        actionComponent.draw(canvas);
        for (DrawTextComponent textComponent : textList) {
            textComponent.drawText(canvas, viewWidth / 2, viewHeight / 2);
        }
        canvas.restoreToCount(save);
        return bitmap;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGesture.onTouchEvent(event);
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
        DrawTextComponent textComponent = new DrawTextComponent(this, text,
                spToPx(22));
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
        changeCanvasMatrix();
        invalidate();
    }

    private void changeCanvasMatrix() {
        canvasMatrix.reset();
        boolean scaleInProgress = scaleGesture.isInProgress();
        if (scaleInProgress) {
            canvasMatrix.postScale(scale, scale, scaleGesture.getFocusX(), scaleGesture.getFocusY());
        } else {
            canvasMatrix.postScale(scale, scale, preScaleFocusX, preScaleFocusY);
        }
        canvasMatrix.postTranslate(xOffset, yOffset);

        actionComponent.setMatrix(canvasMatrix);
        for (DrawTextComponent drawTextComponent : textList) {
            drawTextComponent.setMatrix(canvasMatrix);
        }
    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent event) {
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {
    }

    @Override
    public boolean onMoveGestureBeginTap(MotionEvent event) {
        changeCanvasMatrix();
        invalidate();
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = sourceScale * detector.getScaleFactor();
        preScaleFocusX = detector.getFocusX();
        preScaleFocusY = detector.getFocusY();
        changeCanvasMatrix();
        invalidate();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        sourceScale = scale;
    }
}
