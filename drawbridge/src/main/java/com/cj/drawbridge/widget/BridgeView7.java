package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent7;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView7 extends BaseBridgeView {

    private BridgeComponent7 bridgeComponent7;
    private BridgeParams params;

    public BridgeView7(Context context) {
        this(context, null);
    }

    public BridgeView7(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView7(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent7 = new BridgeComponent7(context);
        params = new BridgeParams();
        params.setLength(8f);
        params.setWidth(4f);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent7.draw(canvas, getWidth(), getHeight(), params.getLength(),
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
        return bridgeComponent7.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent7.getBounds()[1];
    }
}
