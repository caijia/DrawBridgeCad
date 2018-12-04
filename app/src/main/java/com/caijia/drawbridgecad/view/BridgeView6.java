package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.BridgeParams;
import com.caijia.drawbridgecad.component.BridgeComponent6;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView6 extends BaseBridgeView {

    private BridgeComponent6 bridgeComponent6;
    private BridgeParams params;

    public BridgeView6(Context context) {
        this(context, null);
    }

    public BridgeView6(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent6 = new BridgeComponent6(context);
        params = new BridgeParams();
        params.setLength(4f);
        params.setWidth(4f);

    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent6.draw(canvas, getWidth(), getHeight(), params.getLength(),
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
        return bridgeComponent6.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent6.getBounds()[1];
    }
}
