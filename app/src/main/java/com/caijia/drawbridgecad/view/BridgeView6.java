package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent6;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView6 extends BaseBridgeView {

    private BridgeComponent6 bridgeComponent6;

    public BridgeView6(Context context) {
        this(context, null);
    }

    public BridgeView6(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent6 = new BridgeComponent6(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent6.draw(canvas, getWidth(), getHeight(), 26, 22);
    }
}
