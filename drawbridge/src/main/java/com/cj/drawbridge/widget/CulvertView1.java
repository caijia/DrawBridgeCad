package com.cj.drawbridge.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cj.drawbridge.component.BridgeComponent16;
import com.cj.drawbridge.constants.Constants;
import com.cj.drawbridge.entity.BridgeParams;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class CulvertView1 extends BaseBridgeView {

    private BridgeComponent16 bridgeComponent16;
    private BridgeParams params;

    public CulvertView1(Context context) {
        this(context, null);
    }

    public CulvertView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CulvertView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent16 = new BridgeComponent16(context);
        params = new BridgeParams();
        params.setHanTai(1);
        params.setHanJieArray(new float[]{8, 5, 5, 5, 8});
        params.setDirection(Constants.BRIDGE_L);
        params.setUnit(Constants.UNIT_M);
        params.setWidth(12);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas, Paint paint) {
        bridgeComponent16.draw(canvas, getWidth(), getHeight(), params.getHanJieArray(),
                params.getWidth(), params.getDirection(), params.getHanTai(), params.getUnit(), paint);
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
        return bridgeComponent16.getBounds()[0];
    }

    @Override
    public float getMapHeight() {
        return bridgeComponent16.getBounds()[1];
    }
}
