package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent4;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView4 extends BaseBridgeView {

    private BridgeComponent4 bridgeComponent4;

    public BridgeView4(Context context) {
        this(context, null);
    }

    public BridgeView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent4 = new BridgeComponent4(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent4.draw(canvas, getWidth(), getHeight(), 152, "L34-34-14");
    }
}
