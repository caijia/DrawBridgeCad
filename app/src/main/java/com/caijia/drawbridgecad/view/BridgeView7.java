package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BridgeComponent7;

/**
 * Created by cai.jia 2018/11/26 15:52
 */
public class BridgeView7 extends BaseBridgeView {

    private BridgeComponent7 bridgeComponent7;

    public BridgeView7(Context context) {
        this(context, null);
    }

    public BridgeView7(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BridgeView7(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bridgeComponent7 = new BridgeComponent7(context);
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {
        bridgeComponent7.draw(canvas, getWidth(), getHeight(), 8, 4);
    }
}
