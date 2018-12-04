package com.caijia.drawbridgecad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by cai.jia 2018/12/3 20:38
 */
public class OtherActivity extends AppCompatActivity {

    private EditText etType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        etType = findViewById(R.id.et_type);
    }

    public void editImage(View view) {
        String s = etType.getText().toString();
        int type = MathUtils.clamp(Integer.parseInt(s), 1, 15);
        Intent i = EditBridgeActivity.getIntent(this, type);
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
