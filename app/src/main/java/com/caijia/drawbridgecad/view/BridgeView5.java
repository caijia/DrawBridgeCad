package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent5;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView5 extends BaseBridgeView {

    private BridgeComponent5 bridgeComponent5;

    public BridgeView5(Context context) {
        this(context, null);
    }

    public BridgeView5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent5 = new BridgeComponent5(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent5.draw(canvas, getWidth(), getHeight(), 12.4f, 9, "L",
                34);
    }
}
