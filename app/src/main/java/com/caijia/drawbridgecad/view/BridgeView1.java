package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent1;

/**
 * Created by cai.jia 2018/11/26 15:14
 */
public class BridgeView1 extends BaseBridgeView {

    private BridgeComponent1 bridgeComponent1;

    public BridgeView1(Context context) {
        this(context, null);
    }

    public BridgeView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent1 = new BridgeComponent1(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent1.draw(canvas, getWidth(), getHeight(), 34, 36);
    }
}
