package com.caijia.drawbridgecad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private BaseBridgeView bridgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bridgeView = findViewById(R.id.bridge_view);
    }

    public void drawText(View view) {
        bridgeView.drawText("支座");
    }
}
