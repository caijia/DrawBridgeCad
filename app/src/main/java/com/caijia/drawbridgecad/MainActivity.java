package com.caijia.drawbridgecad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.caijia.drawbridgecad.view.BaseBridgeView;
import com.caijia.drawbridgecad.view.BridgeView1;

import java.io.File;

import me.kareluo.imaging.IMGEditActivity;

public class MainActivity extends AppCompatActivity {

    private BaseBridgeView bridgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bridgeView = findViewById(R.id.bridge_view);
    }

    public void editImage(View view) {
        startActivityForResult(
                new Intent(this, IMGEditActivity.class)
                        .putExtra(IMGEditActivity.EXTRA_IMAGE_URI, Uri.fromFile(new File("/sdcard/Pictures/1488273356385.jpg")))
                        .putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, "/sdcard/Pictures/1488273356385.jpg"), 11);
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
