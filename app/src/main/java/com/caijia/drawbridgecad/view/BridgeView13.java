package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent13;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView13 extends BaseBridgeView {

    private BridgeComponent13 bridgeComponent13;
    private BridgeParams params;

    public BridgeView13(Context context) {
        this(context, null);
    }

    public BridgeView13(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView13(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent13 = new BridgeComponent13(context, 90);
        params = new BridgeParams();
        params.setLength(8);
        params.setWidth(8);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent13.draw(canvas, getWidth(), getHeight(), params.getLength(),
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
        return bridgeComponent13.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent13.getBounds()[1];
    }
}
