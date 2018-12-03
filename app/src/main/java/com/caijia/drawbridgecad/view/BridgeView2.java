package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent2;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView2 extends BaseBridgeView {

    private BridgeComponent2 bridgeComponent2;

    public BridgeView2(Context context) {
        this(context, null);
    }

    public BridgeView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent2 = new BridgeComponent2(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent2.draw(canvas, getWidth(), getHeight(), 12.2f, 6,
                "L", 34);
    }
}
