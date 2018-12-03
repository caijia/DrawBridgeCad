package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent3;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView3 extends BaseBridgeView {

    private BridgeComponent3 bridgeComponent3;

    public BridgeView3(Context context) {
        this(context, null);
    }

    public BridgeView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent3 = new BridgeComponent3(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent3.draw(canvas, getWidth(), getHeight(), 12.2f, 7, "L", 34);
    }
}
