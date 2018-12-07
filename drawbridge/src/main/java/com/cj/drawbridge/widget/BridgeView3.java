package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent3;
import com.cj.drawbridge.constants.Constants;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView3 extends BaseBridgeView {

    private BridgeComponent3 bridgeComponent3;
    private BridgeParams params;

    public BridgeView3(Context context) {
        this(context, null);
    }

    public BridgeView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent3 = new BridgeComponent3(context);
        params = new BridgeParams();
        params.setLength(12.2f);
        params.setDunShu(7);
        params.setZuoDun(34);
        params.setYouDun(34);
        params.setDirection(Constants.BRIDGE_L);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas, Paint paint) {
        bridgeComponent3.draw(canvas, getWidth(), getHeight(), params.getLength(),
                params.getDunShu(), params.getDirection(), params.getZuoDun(),
                params.getYouDun(), params.getUnit(), paint);
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
        return bridgeComponent3.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent3.getBounds()[1];
    }
}
