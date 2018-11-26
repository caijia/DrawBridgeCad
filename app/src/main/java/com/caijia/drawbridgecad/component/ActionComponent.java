package com.caijia.drawbridgecad.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.caijia.drawbridgecad.MoveGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia 2018/11/26 09:27
 */
public class ActionComponent implements MoveGestureDetector.OnMoveGestureListener {

    private static final int OVAL = 1;
    private static final int PATH = 2;
    private static final int RECT = 3;
    private static final int LINE = 4;
    private int shapeType = PATH;
    private List<Shape> shapeList;
    private Paint paint;
    private Shape shape;
    private MoveGestureDetector gestureDetector;
    private View parentView;
    private Context context;

    public ActionComponent(View parentView) {
        init(parentView);
    }

    private Context getContext() {
        return context;
    }

    public void init(View parentView) {
        this.parentView = parentView;
        this.context = parentView.getContext();
        shapeList = new ArrayList<>();
        gestureDetector = new MoveGestureDetector(getContext(), this);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(dpToPx(1));
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {
        if (shapeList == null) {
            return;
        }

        for (Shape s : shapeList) {
            drawShape(canvas, s);
        }
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private void drawShape(Canvas canvas, Shape s) {
        int shapeType = s.shapeType;
        switch (shapeType) {
            case PATH:
                canvas.drawPath(s.path, paint);
                break;

            case OVAL:
                if (s.hasTwoPoint()) {
                    PointF firstPoint = s.getFirstPoint();
                    PointF lastPoint = s.getLastPoint();
                    canvas.drawOval(new RectF(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y), paint);
                }
                break;

            case RECT:
                if (s.hasTwoPoint()) {
                    PointF firstPoint = s.getFirstPoint();
                    PointF lastPoint = s.getLastPoint();
                    canvas.drawRect(new RectF(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y), paint);
                }
                break;

            case LINE:
                if (s.hasTwoPoint()) {
                    PointF firstPoint = s.getFirstPoint();
                    PointF lastPoint = s.getLastPoint();
                    canvas.drawLine(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y, paint);
                }
                break;
        }
    }

    public void drawLine(){
        this.shapeType = LINE;
    }

    public void drawOval() {
        this.shapeType = OVAL;
    }

    public void drawPath() {
        this.shapeType = PATH;
    }

    public void drawRect() {
        this.shapeType = RECT;
    }

    public void cancelPreviousDraw() {
        if (shapeList == null || shapeList.isEmpty()) {
            return;
        }

        shapeList.remove(shapeList.size() - 1);
        parentView.invalidate();
    }

    @Override
    public void onMoveGestureScroll(MotionEvent downEvent, MotionEvent currentEvent,
                                    int pointerIndex, float dx, float dy, float distanceX,
                                    float distanceY) {
        addShapePoint(currentEvent);
    }

    private void addShapePoint(MotionEvent e) {
        if (shape == null) {
            shape = new Shape();
            shape.shapeType = shapeType;
            shapeList.add(shape);
        }

        if (shape.hasPoint()) {
            shape.addPoint(e.getX(), e.getY());
            if (shapeType == PATH) {
                shape.path.lineTo(e.getX(), e.getY());
            }

        } else {
            shape.addPoint(e.getX(), e.getY());
            if (shapeType == PATH) {
                shape.path.moveTo(e.getX(), e.getY());
            }
        }
        parentView.invalidate();
    }

    @Override
    public void onMoveGestureUpOrCancel(MotionEvent e) {
        shape = null;
    }

    @Override
    public void onMoveGestureDoubleTap(MotionEvent event) {
    }

    @Override
    public boolean onMoveGestureBeginTap(MotionEvent e) {
        addShapePoint(e);
        return false;
    }

    public void onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
    }

    private static class Shape {

        int shapeType;

        List<PointF> points = new ArrayList<>();

        Path path = new Path();

        void addPoint(float x, float y) {
            points.add(new PointF(x, y));
        }

        boolean hasPoint() {
            return !points.isEmpty();
        }

        boolean hasTwoPoint() {
            return points.size() > 1;
        }

        PointF getFirstPoint() {
            return points.get(0);
        }

        PointF getLastPoint() {
            return points.get(points.size() - 1);
        }

    }
}
