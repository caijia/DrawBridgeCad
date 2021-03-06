package com.cj.drawbridge.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cj.drawbridge.R;
import com.cj.drawbridge.constants.Constants;
import com.cj.drawbridge.entity.BridgeParams;
import com.cj.drawbridge.helper.ToastManager;
import com.cj.drawbridge.helper.Util;

public class BridgeParamsDialog extends DialogFragment {

    private static final String EXTRA_PARAMS = "extra:params";
    private static final String BRIDGE_TYPE = "extra:bridgeType";
    private BridgeParams bridgeParams;
    private RelativeLayout rlLength;
    private RelativeLayout rlWidth;
    private RelativeLayout rlDiban;
    private RelativeLayout rlFuban;
    private RelativeLayout rlYiyuanban;
    private RelativeLayout rlZuodun;
    private RelativeLayout rlYoudun;
    private RelativeLayout rlDunshu;
    private RelativeLayout rlDirection;
    private RelativeLayout rlUnit;
    private RelativeLayout rlHanJie;
    private RelativeLayout rlHanTai;
    private EditText etLength;
    private EditText etWidth;
    private EditText etDiban;
    private EditText etFuban;
    private EditText etYiyuanban;
    private EditText etZuodun;
    private EditText etYoudun;
    private EditText etDunshu;
    private EditText etHanJie;
    private EditText etHanTai;
    private RadioButton rbLeft;
    private RadioButton rbRight;
    private RadioButton rbCm;
    private RadioButton rbM;
    private TextView tvApply;
    private int bridgeType;
    private OnChangeBridgeParamsListener onChangeBridgeParamsListener;

    public static BridgeParamsDialog getInstance(BridgeParams params, int bridgeType) {
        BridgeParamsDialog f = new BridgeParamsDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PARAMS, params);
        args.putInt(BRIDGE_TYPE, bridgeType);
        f.setArguments(args);
        return f;
    }

    private void handleArgs() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        bridgeParams = (BridgeParams) args.getSerializable(EXTRA_PARAMS);
        bridgeType = args.getInt(BRIDGE_TYPE);
    }

    private void setViewState() {
        if (bridgeParams == null) {
            return;
        }

        rlLength.setVisibility(bridgeParams.getLength() < 0 ? View.GONE : View.VISIBLE);
        rlWidth.setVisibility(bridgeParams.getWidth() < 0 ? View.GONE : View.VISIBLE);
        rlDiban.setVisibility(bridgeParams.getDiBan() < 0 ? View.GONE : View.VISIBLE);
        rlFuban.setVisibility(bridgeParams.getFuBan() < 0 ? View.GONE : View.VISIBLE);
        rlYiyuanban.setVisibility(bridgeParams.getYiYuanBan() < 0 ? View.GONE : View.VISIBLE);
        rlZuodun.setVisibility(bridgeParams.getZuoDun() < 0 ? View.GONE : View.VISIBLE);
        rlYoudun.setVisibility(bridgeParams.getYouDun() < 0 ? View.GONE : View.VISIBLE);
        rlDunshu.setVisibility(bridgeParams.getDunShu() < 0 ? View.GONE : View.VISIBLE);
        rlDirection.setVisibility(TextUtils.isEmpty(bridgeParams.getDirection()) ? View.GONE : View.VISIBLE);
        rlUnit.setVisibility(TextUtils.isEmpty(bridgeParams.getUnit()) ? View.GONE : View.VISIBLE);
        rlHanJie.setVisibility(bridgeParams.getHanJieArray() == null ? View.GONE : View.VISIBLE);
        rlHanTai.setVisibility(bridgeParams.getHanTai() < 0 ? View.GONE : View.VISIBLE);

        etLength.setText(Util.removeZero(bridgeParams.getLength()));
        etWidth.setText(Util.removeZero(bridgeParams.getWidth()));
        etDiban.setText(Util.removeZero(bridgeParams.getDiBan()));
        etFuban.setText(Util.removeZero(bridgeParams.getFuBan()));
        etYiyuanban.setText(Util.removeZero(bridgeParams.getYiYuanBan()));
        etZuodun.setText(String.valueOf(bridgeParams.getZuoDun()));
        etYoudun.setText(String.valueOf(bridgeParams.getYouDun()));
        etDunshu.setText(String.valueOf(bridgeParams.getDunShu()));
        etHanTai.setText(String.valueOf(bridgeParams.getHanTai()));
        etHanJie.setText(getHanJie(bridgeParams.getHanJieArray()));

        String direction = bridgeParams.getDirection();
        if (!TextUtils.isEmpty(direction)) {
            switch (direction) {
                case Constants.BRIDGE_L:
                    rbLeft.setChecked(true);
                    break;

                case Constants.BRIDGE_R:
                    rbRight.setChecked(true);
                    break;
            }
        }

        String unit = bridgeParams.getUnit();
        if (!TextUtils.isEmpty(unit)) {
            switch (unit) {
                case Constants.UNIT_CM:
                    rbCm.setChecked(true);
                    break;

                case Constants.UNIT_M:
                    rbM.setChecked(true);
                    break;
            }
        }
    }

    private String getHanJie(float[] hanJies) {
        if (hanJies == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (float hanJie : hanJies) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(Util.removeZero(hanJie));
        }
        return sb.toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.customDialog);
        handleArgs();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bridge_params, container, false);
        rlLength = view.findViewById(R.id.rl_length);
        rlWidth = view.findViewById(R.id.rl_width);
        rlDiban = view.findViewById(R.id.rl_diban);
        rlFuban = view.findViewById(R.id.rl_fuban);
        rlYiyuanban = view.findViewById(R.id.rl_yiyuanban);
        rlZuodun = view.findViewById(R.id.rl_zuodun);
        rlYoudun = view.findViewById(R.id.rl_youdun);
        rlDunshu = view.findViewById(R.id.rl_dunshu);
        rlDirection = view.findViewById(R.id.rl_direction);
        rlUnit = view.findViewById(R.id.rl_unit);
        rlHanJie = view.findViewById(R.id.rl_hanjie);
        rlHanTai = view.findViewById(R.id.rl_hantai);

        etLength = view.findViewById(R.id.et_length);
        etWidth = view.findViewById(R.id.et_width);
        etDiban = view.findViewById(R.id.et_diban);
        etFuban = view.findViewById(R.id.et_fuban);
        etYiyuanban = view.findViewById(R.id.et_yiyuanban);
        etZuodun = view.findViewById(R.id.et_zuodun);
        etYoudun = view.findViewById(R.id.et_youdun);
        etDunshu = view.findViewById(R.id.et_dunshu);
        etHanJie = view.findViewById(R.id.et_hanjie);
        etHanTai = view.findViewById(R.id.et_hantai);

        rbLeft = view.findViewById(R.id.rb_left);
        rbRight = view.findViewById(R.id.rb_right);
        rbCm = view.findViewById(R.id.rb_cm);
        rbM = view.findViewById(R.id.rb_m);

        tvApply = view.findViewById(R.id.tv_apply);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewState();
        tvApply.setOnClickListener(v -> callbackParams());
    }

    private void callbackParams() {
        String length = checkParams(etLength, rlLength, "请输入长度");
        if (length == null) return;

        String width = checkParams(etWidth, rlWidth, "请输入宽度");
        if (width == null) return;

        String diBan = checkParams(etDiban, rlDiban, "请输入底板");
        if (diBan == null) return;

        String fuBan = checkParams(etFuban, rlFuban, "请输入腹板");
        if (fuBan == null) return;

        String yiYuanBan = checkParams(etYiyuanban, rlYiyuanban, "请输入翼缘版");
        if (yiYuanBan == null) return;

        String zuoDun = checkParams(etZuodun, rlZuodun, "请输入左墩");
        if (zuoDun == null) return;

        String youDun = checkParams(etYoudun, rlYoudun, "请输入右墩");
        if (youDun == null) return;

        String dunShu = checkParams(etDunshu, rlDunshu, "请输入墩数");
        if (dunShu == null) return;


        String hanJies = "";
        if (bridgeType == Constants.CULVERT_TYPE_1 ||
                bridgeType == Constants.CULVERT_TYPE_2) {
            hanJies = checkParams(etHanJie, rlHanJie, "请输入涵节");
            if (hanJies == null) return;
            boolean hasSplit = hanJies.contains(",") || hanJies.contains("，");
            if (hasSplit && !validateHanJie(hanJies)) {
                ToastManager.getInstance(getContext()).showToast("多个涵节必须用逗号分割,涵节长度必须大于0");
                return;
            }

            if (!hasSplit) {
                if (Util.parseInt(hanJies, -1) < 0) {
                    ToastManager.getInstance(getContext()).showToast("涵节长度必须大于0");
                }
                return;
            }

            String hanTai = checkParams(etHanTai, rlHanTai, "请输入涵台");
            if (hanTai == null) return;
        }

        String direction = rbLeft.isChecked()
                ? Constants.BRIDGE_L
                : rbRight.isChecked() ? Constants.BRIDGE_R : "";

        String unit = rbCm.isChecked()
                ? Constants.UNIT_CM
                : rbM.isChecked() ? Constants.UNIT_M : "";

        bridgeParams.setLength(TextUtils.isEmpty(length) ? -1 : Float.parseFloat(length));
        bridgeParams.setWidth(TextUtils.isEmpty(width) ? -1 : Float.parseFloat(width));
        bridgeParams.setDiBan(TextUtils.isEmpty(diBan) ? -1 : Float.parseFloat(diBan));
        bridgeParams.setFuBan(TextUtils.isEmpty(fuBan) ? -1 : Float.parseFloat(fuBan));
        bridgeParams.setYiYuanBan(TextUtils.isEmpty(yiYuanBan) ? -1 : Float.parseFloat(yiYuanBan));
        bridgeParams.setZuoDun(TextUtils.isEmpty(zuoDun) ? -1 : Integer.parseInt(zuoDun));
        bridgeParams.setYouDun(TextUtils.isEmpty(youDun) ? -1 : Integer.parseInt(youDun));
        bridgeParams.setDunShu(TextUtils.isEmpty(dunShu) ? -1 : Integer.parseInt(dunShu));
        bridgeParams.setDirection(direction);
        bridgeParams.setUnit(unit);
        bridgeParams.setHanJieArray(getHanJieArray(hanJies));

        if (onChangeBridgeParamsListener != null) {
            onChangeBridgeParamsListener.onChangeBridgeParams(bridgeParams);
        }

        dismissAllowingStateLoss();
    }

    private float[] getHanJieArray(String hanJies) {
        String[] hanJie = hanJies.split("[,，]");
        float[] floats = new float[hanJie.length];
        for (int i = 0; i < hanJie.length; i++) {
            floats[i] = Util.parseFloat(hanJie[i], -1);
        }
        return floats;
    }

    private boolean validateHanJie(String hanJies) {
        String[] hanJie = hanJies.split("[,，]");
        boolean validate = true;
        for (String s : hanJie) {
            if (Util.parseFloat(s, -1) < 0) {
                validate = false;
            }
        }
        return validate;
    }

    @Nullable
    private String checkParams(EditText et, RelativeLayout rl, String msg) {
        String text = et.getText().toString();
        if (rl.getVisibility() == View.VISIBLE && TextUtils.isEmpty(text)) {
            ToastManager.getInstance(getContext()).showToast(msg);
            return null;
        }
        return text;
    }

    public void setOnChangeBridgeParamsListener(OnChangeBridgeParamsListener listener) {
        this.onChangeBridgeParamsListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWindowParams();
    }

    private void setWindowParams() {
        if (getDialog().getWindow() == null) {
            return;
        }
        final WindowManager.LayoutParams params = this.getDialog().getWindow().getAttributes();
        params.width = (int) (Util.getScreenWidth(getContext()) * 0.5f);
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.RIGHT;
        this.getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public interface OnChangeBridgeParamsListener {
        void onChangeBridgeParams(BridgeParams params);
    }
}
