package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent11;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView11 extends BaseBridgeView {

    private BridgeComponent11 bridgeComponent11;
    private BridgeParams params;

    public BridgeView11(Context context) {
        this(context, null);
    }

    public BridgeView11(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView11(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent11 = new BridgeComponent11(context, 80);
        params = new BridgeParams();
        params.setLength(8);
        params.setDiBan(1.2f);
        params.setFuBan(1.2f);
        params.setYiYuanBan(1.2f);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent11.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getYiYuanBan(), params.getFuBan(), params.getDiBan(), params.getUnit());
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
        return bridgeComponent11.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent11.getBounds()[1];
    }
}
