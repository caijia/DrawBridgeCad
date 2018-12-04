package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.Constants;
import com.caijia.drawbridgecad.component.BridgeComponent12;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView12 extends BaseBridgeView {

    private BridgeComponent12 bridgeComponent12;
    private BridgeParams params;

    public BridgeView12(Context context) {
        this(context, null);
    }

    public BridgeView12(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView12(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent12 = new BridgeComponent12(context);
        params = new BridgeParams();
        params.setDunShu(8);
        params.setWidth(12f);
        params.setZuoDun(1);
        params.setDirection(Constants.BRIDGE_L);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent12.draw(canvas, getWidth(), getHeight(), params.getDunShu(),
                params.getWidth(), params.getDirection(), params.getZuoDun(), params.getUnit());
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
        return bridgeComponent12.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent12.getBounds()[1];
    }
}
