package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent10;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView10 extends BaseBridgeView {

    private BridgeComponent10 bridgeComponent10;

    public BridgeView10(Context context) {
        this(context, null);
    }

    public BridgeView10(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView10(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent10 = new BridgeComponent10(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent10.draw(canvas, getWidth(), getHeight(), 6f, 4f);
    }
}
