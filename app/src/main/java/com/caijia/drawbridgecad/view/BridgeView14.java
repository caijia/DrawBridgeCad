package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent14;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView14 extends BaseBridgeView {

    private BridgeComponent14 bridgeComponent14;
    private BridgeParams params;

    public BridgeView14(Context context) {
        this(context, null);
    }

    public BridgeView14(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView14(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent14 = new BridgeComponent14(context, 80, "m");
        params = new BridgeParams();
        params.setLength(8);
        params.setWidth(8);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent14.draw(canvas, getWidth(), getHeight(), params.getLength(),
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
        return bridgeComponent14.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent14.getBounds()[1];
    }
}
