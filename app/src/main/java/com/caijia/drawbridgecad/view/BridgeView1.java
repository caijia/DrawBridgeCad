package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent1;

/**
 * Created by cai.jia 2018/11/26 15:14
 */
public class BridgeView1 extends BaseBridgeView {

    private BridgeComponent1 bridgeComponent1;
    private BridgeParams params;

    public BridgeView1(Context context) {
        this(context, null);
    }

    public BridgeView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent1 = new BridgeComponent1(context);
        params = new BridgeParams();
        params.setLength(10);
        params.setWidth(10);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent1.draw(canvas, getWidth(), getHeight(), params.getLength(), params.getWidth());
    }

    @Override
    public void applyBridgeParams(BridgeParams params) {
        this.params = params;
        invalidate();
    }

    @Override
    public BridgeParams getBridgeParams() {
        return params;
    }

    @Override
    public float getMapWidth() {
        return bridgeComponent1.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent1.getBounds()[1];
    }
}
