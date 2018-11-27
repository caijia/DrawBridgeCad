package com.caijia.drawbridgecad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.caijia.drawbridgecad.view.BaseBridgeView;

public class MainActivity extends AppCompatActivity {

    private BaseBridgeView bridgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bridgeView = findViewById(R.id.bridge_view);
    }

    public void editImage(View view) {
    }

    public void drawText(View view) {
        bridgeView.drawText("哈哈哈");
    }

    public void drawPath(View view) {
        bridgeView.drawPath();
    }

    public void drawLine(View view) {
        bridgeView.drawLine();
    }

    public void drawCircle(View view) {
        bridgeView.drawCircle();
    }

    public void drawRect(View view) {
        bridgeView.drawRect();
    }

    public void cancelPrevious(View view) {
        bridgeView.cancelPreviousDraw();
    }
}
