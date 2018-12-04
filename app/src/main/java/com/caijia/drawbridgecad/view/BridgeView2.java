package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent2;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView2 extends BaseBridgeView {

    private BridgeComponent2 bridgeComponent2;

    public BridgeView2(Context context) {
        this(context, null);
    }

    public BridgeView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private BridgeParams params;

    public BridgeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent2 = new BridgeComponent2(context);
        params = new BridgeParams();
        params.setLength(12.2f);
        params.setDunShu(6);
        params.setZuoDun(34);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent2.draw(canvas, getWidth(), getHeight(), params.getLength(), params.getDunShu(),
                params.getDirection(), params.getZuoDun());
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
        return bridgeComponent2.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent2.getBounds()[1];
    }
}
