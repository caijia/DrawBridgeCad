package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent10;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView10 extends BaseBridgeView {

    private BridgeComponent10 bridgeComponent10;
    private BridgeParams params;

    public BridgeView10(Context context) {
        this(context, null);
    }

    public BridgeView10(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView10(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent10 = new BridgeComponent10(context);
        params = new BridgeParams();
        params.setLength(6f);
        params.setWidth(4f);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent10.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getWidth(), params.getUnit());
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
        return bridgeComponent10.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent10.getBounds()[1];
    }
}
