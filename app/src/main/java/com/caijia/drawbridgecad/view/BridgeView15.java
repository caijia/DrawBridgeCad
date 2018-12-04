package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent15;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView15 extends BaseBridgeView {

    private BridgeComponent15 bridgeComponent15;

    public BridgeView15(Context context) {
        this(context, null);
    }

    public BridgeView15(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView15(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent15 = new BridgeComponent15(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent15.draw(canvas, getWidth(), getHeight(), 5);
    }

    @Override
    public float getMapWidth() {
        return bridgeComponent15.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent15.getBounds()[1];
    }
}
