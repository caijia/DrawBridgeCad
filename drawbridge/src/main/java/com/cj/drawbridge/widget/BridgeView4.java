package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent4;
import com.cj.drawbridge.constants.Constants;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView4 extends BaseBridgeView {

    private BridgeComponent4 bridgeComponent4;
    private BridgeParams params;

    public BridgeView4(Context context) {
        this(context, null);
    }

    public BridgeView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent4 = new BridgeComponent4(context);
        params = new BridgeParams();
        params.setLength(12.1f);
        params.setDunShu(6);
        params.setZuoDun(36);
        params.setYouDun(35);
        params.setDirection(Constants.BRIDGE_L);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent4.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getDunShu(), params.getDirection(), params.getZuoDun(), params.getYouDun(),
                params.getUnit());
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
        return bridgeComponent4.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent4.getBounds()[1];
    }
}
