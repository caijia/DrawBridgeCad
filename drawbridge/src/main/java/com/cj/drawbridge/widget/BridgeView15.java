package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent15;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView15 extends BaseBridgeView {

    private BridgeComponent15 bridgeComponent15;
    private BridgeParams params;

    public BridgeView15(Context context) {
        this(context, null);
    }

    public BridgeView15(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView15(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent15 = new BridgeComponent15(context);
        params = new BridgeParams();
        params.setWidth(8);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas, Paint paint) {
        bridgeComponent15.draw(canvas, getWidth(), getHeight(), params.getWidth(),
                params.getUnit(), paint);
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
        return bridgeComponent15.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent15.getBounds()[1];
    }
}
