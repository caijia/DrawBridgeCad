package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent12;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView12 extends BaseBridgeView {

    private BridgeComponent12 bridgeComponent12;

    public BridgeView12(Context context) {
        this(context, null);
    }

    public BridgeView12(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView12(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent12 = new BridgeComponent12(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent12.draw(canvas, getWidth(), getHeight(), 36,
                1.9f, 1.4f, 0.8f);
    }
}
