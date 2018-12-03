package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent11;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView11 extends BaseBridgeView {

    private BridgeComponent11 bridgeComponent11;

    public BridgeView11(Context context) {
        this(context, null);
    }

    public BridgeView11(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView11(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent11 = new BridgeComponent11(context, 80);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent11.draw(canvas, getWidth(), getHeight(), 36.5f,
                1.2f, 1.4f, 0.9f);
    }
}
