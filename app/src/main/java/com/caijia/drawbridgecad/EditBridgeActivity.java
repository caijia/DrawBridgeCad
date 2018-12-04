package com.caijia.drawbridgecad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.caijia.drawbridgecad.view.BaseBridgeView;
import com.caijia.drawbridgecad.view.BridgeView1;
import com.caijia.drawbridgecad.view.BridgeView10;
import com.caijia.drawbridgecad.view.BridgeView11;
import com.caijia.drawbridgecad.view.BridgeView12;
import com.caijia.drawbridgecad.view.BridgeView13;
import com.caijia.drawbridgecad.view.BridgeView14;
import com.caijia.drawbridgecad.view.BridgeView15;
import com.caijia.drawbridgecad.view.BridgeView2;
import com.caijia.drawbridgecad.view.BridgeView3;
import com.caijia.drawbridgecad.view.BridgeView4;
import com.caijia.drawbridgecad.view.BridgeView5;
import com.caijia.drawbridgecad.view.BridgeView6;
import com.caijia.drawbridgecad.view.BridgeView7;
import com.caijia.drawbridgecad.view.BridgeView8;
import com.caijia.drawbridgecad.view.BridgeView9;

public class EditBridgeActivity extends AppCompatActivity {

    private static final String EXTRA_BRIDGE_TYPE = "extra:bridgeType";
    private BaseBridgeView bridgeView;
    private EditText etText;
    private RelativeLayout rlEditText;
    private FrameLayout flBridgeViewContainer;
    private int bridgeType;

    public static Intent getIntent(Context context, int bridgeType) {
        Intent i = new Intent(context, EditBridgeActivity.class);
        i.putExtra(EXTRA_BRIDGE_TYPE, bridgeType);
        return i;
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        Bundle extras = intent.getExtras();
        bridgeType = extras.getInt(EXTRA_BRIDGE_TYPE);
    }

    private void dynamicAddBridgeView() {
        switch (bridgeType) {
            case Constants.BRIDGE_TYPE_1:
                bridgeView = new BridgeView1(this);
                break;

            case Constants.BRIDGE_TYPE_2:
                bridgeView = new BridgeView2(this);
                break;

            case Constants.BRIDGE_TYPE_3:
                bridgeView = new BridgeView3(this);
                break;

            case Constants.BRIDGE_TYPE_4:
                bridgeView = new BridgeView4(this);
                break;

            case Constants.BRIDGE_TYPE_5:
                bridgeView = new BridgeView5(this);
                break;

            case Constants.BRIDGE_TYPE_6:
                bridgeView = new BridgeView6(this);
                break;

            case Constants.BRIDGE_TYPE_7:
                bridgeView = new BridgeView7(this);
                break;

            case Constants.BRIDGE_TYPE_8:
                bridgeView = new BridgeView8(this);
                break;

            case Constants.BRIDGE_TYPE_9:
                bridgeView = new BridgeView9(this);
                break;

            case Constants.BRIDGE_TYPE_10:
                bridgeView = new BridgeView10(this);
                break;

            case Constants.BRIDGE_TYPE_11:
                bridgeView = new BridgeView11(this);
                break;

            case Constants.BRIDGE_TYPE_12:
                bridgeView = new BridgeView12(this);
                break;

            case Constants.BRIDGE_TYPE_13:
                bridgeView = new BridgeView13(this);
                break;

            case Constants.BRIDGE_TYPE_14:
                bridgeView = new BridgeView14(this);
                break;

            case Constants.BRIDGE_TYPE_15:
                bridgeView = new BridgeView15(this);
                break;
        }
        flBridgeViewContainer.removeAllViews();
        flBridgeViewContainer.addView(bridgeView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());

        flBridgeViewContainer = findViewById(R.id.fl_bridge_view);
        etText = findViewById(R.id.et_text);
        rlEditText = findViewById(R.id.rl_edit_text);

        dynamicAddBridgeView();
    }

    public void drawText(View view) {
        rlEditText.setVisibility(View.VISIBLE);
        Util.showKeyboard(etText);
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

    public void move(View view) {
        bridgeView.move();
    }

    public void save(View view) {
        Bitmap bitmap = bridgeView.createBitmap();
        Util.saveBitmap(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/caijia.jpg");
    }

    public void cancelEditText(View view) {
        rlEditText.setVisibility(View.GONE);
        Util.hideKeyboard(etText);
    }

    public void finishEditText(View view) {
        Util.hideKeyboard(etText);
        rlEditText.setVisibility(View.GONE);
        String text = etText.getText().toString();
        etText.setText("");
        if (!TextUtils.isEmpty(text)) {
            bridgeView.drawText(text);
        }
    }

    public void edit(View view) {
        BridgeParamsDialog dialog = BridgeParamsDialog.getInstance(bridgeView.getBridgeParams());
        dialog.setOnChangeBridgeParamsListener(params -> bridgeView.applyBridgeParams(params));
        dialog.show(getSupportFragmentManager(), "edit");
    }
}
