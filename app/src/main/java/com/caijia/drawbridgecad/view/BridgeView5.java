package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent5;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView5 extends BaseBridgeView {

    private BridgeComponent5 bridgeComponent5;
    private BridgeParams params;

    public BridgeView5(Context context) {
        this(context, null);
    }

    public BridgeView5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent5 = new BridgeComponent5(context);
        params = new BridgeParams();
        params.setLength(12.4f);
        params.setDunShu(7);
        params.setZuoDun(34);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent5.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getDunShu(), params.getDirection(), params.getZuoDun());
    }

    @Override
    public void applyBridgeParams(BridgeParams params) {
        this.params = params;
    }

    @Override
    public BridgeParams getBridgeParams() {
        return params;
    }

    @Override
    public float getMapWidth() {
        return bridgeComponent5.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent5.getBounds()[1];
    }
}
