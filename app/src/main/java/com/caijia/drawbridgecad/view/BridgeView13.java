package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent13;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView13 extends BaseBridgeView {

    private BridgeComponent13 bridgeComponent13;

    public BridgeView13(Context context) {
        this(context, null);
    }

    public BridgeView13(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView13(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent13 = new BridgeComponent13(context, 90, "m");
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent13.draw(canvas, getWidth(), getHeight(), 26.1f, 28.2f);
    }
}
