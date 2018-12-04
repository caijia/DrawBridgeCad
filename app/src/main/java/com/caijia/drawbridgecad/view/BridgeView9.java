package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent9;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView9 extends BaseBridgeView {

    private BridgeComponent9 bridgeComponent9;

    public BridgeView9(Context context) {
        this(context, null);
    }

    public BridgeView9(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView9(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent9 = new BridgeComponent9(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent9.draw(canvas, getWidth(), getHeight(), 36.3f,
                2.9f, 3.4f, 0.8f);
    }

    @Override
    public float getMapWidth() {
        return bridgeComponent9.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent9.getBounds()[1];
    }
}
