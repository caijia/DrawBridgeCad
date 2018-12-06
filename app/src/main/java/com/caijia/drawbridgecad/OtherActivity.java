package com.caijia.drawbridgecad;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cj.drawbridge.EditBridgeActivity;
import com.cj.drawbridge.dialog.BridgePictureListDialog;

import java.io.File;

/**
 * Created by cai.jia 2018/12/3 20:38
 */
public class OtherActivity extends AppCompatActivity {

    private static final int REQUEST_BRIDGE_CODE = 200;
    String saveFilePath;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        imageView = findViewById(R.id.image_view);
    }

    public void editImage(View view) {
        BridgePictureListDialog dialog = new BridgePictureListDialog();
        dialog.setOnClickBridgeListener(type -> {
            saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/caijia.jpg";
            Intent intent = EditBridgeActivity.getIntent(this, saveFilePath, type);
            startActivityForResult(intent, REQUEST_BRIDGE_CODE);
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_BRIDGE_CODE:
                imageView.setImageURI(Uri.fromFile(new File(saveFilePath)));
                break;
        }
    }
}
