package com.caijia.drawbridgecad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.caijia.drawbridgecad.component.BaseBridgeComponent;

public class BridgeView extends BaseBridgeView {

    private BaseBridgeComponent component;

    public BridgeView(Context context) {
        this(context, null);
    }

    public BridgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BridgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBridgeComponent(BaseBridgeComponent component) {
        this.component = component;
    }

    @Override
    public void drawBackgroundComponent(Canvas canvas) {

    }

    @Override
    public float getMapWidth() {
        return 0;
    }

    @Override
    public float getMapHeight() {
        return 0;
    }
}
