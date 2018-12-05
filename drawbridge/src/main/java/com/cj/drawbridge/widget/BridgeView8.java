package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent8;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView8 extends BaseBridgeView {

    private BridgeComponent8 bridgeComponent8;
    private BridgeParams params;

    public BridgeView8(Context context) {
        this(context, null);
    }

    public BridgeView8(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView8(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent8 = new BridgeComponent8(context);
        params = new BridgeParams();
        params.setLength(8);
        params.setDiBan(1.2f);
        params.setFuBan(1.2f);
        params.setYiYuanBan(1.2f);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent8.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getYiYuanBan(), params.getFuBan(), params.getDiBan(), params.getUnit());
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
        return bridgeComponent8.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent8.getBounds()[1];
    }
}
