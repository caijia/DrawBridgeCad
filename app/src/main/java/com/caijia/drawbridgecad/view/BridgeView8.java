package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent8;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView8 extends BaseBridgeView {

    private BridgeComponent8 bridgeComponent8;

    public BridgeView8(Context context) {
        this(context, null);
    }

    public BridgeView8(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView8(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent8 = new BridgeComponent8(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent8.draw(canvas, getWidth(), getHeight(), 38.2f,
                3.9f, 3.4f, 0.8f);
    }

    @Override
    public float getMapWidth() {
        return bridgeComponent8.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent8.getBounds()[1];
    }
}
