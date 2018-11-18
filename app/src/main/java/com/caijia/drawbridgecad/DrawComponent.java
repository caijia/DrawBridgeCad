package com.caijia.drawbridgecad;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by cai.jia 2018/11/18 20:52
 */
interface DrawComponent {
    void onDraw(Canvas canvas, Paint paint);

    void onTouchEvent(MotionEvent event);

    boolean contains(float x, float y);
}
