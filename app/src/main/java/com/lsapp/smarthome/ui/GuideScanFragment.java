package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartlinklib.SmartLinkManipulator;
import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.FragmentGuideQrcodeBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.lsapp.smarthome.utils.MacUtil;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GuideScanFragment extends BaseFragment implements View.OnClickListener {
    private FragmentGuideQrcodeBinding binding;
    private SmartLinkManipulator sm;
    private String wifiPwd;
    private CallBackValue callBackValue;
    private boolean isSuccess = false;
    private String mac = null;
    private int step = -1;//0check1action
    private ISmartLinker mSnifferSmartLinker;

    public interface CallBackValue {
        void sendMessageValue(String strValue, String pwd);
    }

    private Handler handler = new Handler() {//first connect or reconnect

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                dismissDialog();
                loadingDialog("发现设备:" + msg.getData().getString("mac"));
                mac = MacUtil.formatMac(msg.getData().getString("mac"));
                isSuccess = true;
            } else {
                mSnifferSmartLinker.setOnSmartLinkListener(null);
                mSnifferSmartLinker.stop();
                dismissDialog();
                if (isSuccess)
                    callBackValue.sendMessageValue(mac, wifiPwd);
                else
                    zToastUtil.show(getActivity(), msg.getData().getString("msg"));

                isSuccess = false;

            }
        }
    };

    public static GuideScanFragment newInstance(String pwd, int step) {
        GuideScanFragment fragment = new GuideScanFragment();
        Bundle args = new Bundle();
        args.putString("pwd", pwd);
        args.putInt("step", step);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initModel() {

    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_qrcode, container, false);
        binding.btnBack.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.guideSkip.setVisibility(View.VISIBLE);
        binding.guideSkip.setOnClickListener(this);
        wifiPwd = getArguments().getString("pwd");
        step = getArguments().getInt("step");
        callBackValue = (CallBackValue) getActivity();
        binding.guideTitle.setText(step == 0 ? getString(R.string.guide_page_2_title) : getString(R.string.guide_page_ex_title));
        binding.guideContent.setText(step == 0 ? getString(R.string.guide_page_2_content) : getString(R.string.guide_page_ex_content));
        //    binding.btnBack.setText(step == 0 ? getString(R.string.back) : getString(R.string.skip_step));
        binding.guideImage.setImageResource(step == 0 ? R.drawable.guide_check_machine : R.drawable.guide_action_machine);

/*        Spannable spanText = Spannable.Factory.getInstance().newSpannable(binding.guideContent.getText());
        spanText.setSpan(new BackgroundColorSpan(Color.parseColor("#00BCD4")), 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.guideContent.setText(spanText);*/
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnBack) {
            RxBus.getDefault().post(new FragmentSwitchEvent(null, true));
        } else if (view == binding.guideSkip) {
            if (step == 0)
                callBackValue.sendMessageValue(null, wifiPwd);
            else
                callBackValue.sendMessageValue(null, null);
        } else if (view == binding.btnNext) {
            loadingDialog(getString(R.string.connecting_toast));

            int smartLinkVersion = getActivity().getIntent().getIntExtra("EXTRA_SMARTLINK_VERSION", 3);
            if (smartLinkVersion == 7) {
                mSnifferSmartLinker = MulticastSmartLinker.getInstance();
            } else {
                mSnifferSmartLinker = SnifferSmartLinker.getInstance();
            }
            try {
                mSnifferSmartLinker.setOnSmartLinkListener(new OnSmartLinkListener() {

                    @Override
                    public void onLinked(SmartLinkedModule smartLinkedModule) {
                        Log.d("find", "发现设备  " + smartLinkedModule.getMid() + "mac" + smartLinkedModule.getMac() + "IP" + smartLinkedModule.getModuleIP());
                        Message msg = handler.obtainMessage(0);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", getActivity().getString(R.string.connect_title));
                        bundle.putString("mac", smartLinkedModule.getMac());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("find", "onCompleted: ");
                        Message msg = handler.obtainMessage(2);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", getActivity().getString(R.string.connect_success_toast));
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onTimeOut() {
                        Log.d("find", "onTimeOut: ");
                        Message msg = handler.obtainMessage(1);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", getActivity().getString(R.string.connect_time_out_toast));
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                });
                //开始 smartLink
                mSnifferSmartLinker.start(getActivity(), wifiPwd, zContextUtil.getSSid(getActivity()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Message msg = handler.obtainMessage(1);
                Bundle bundle = new Bundle();
                bundle.putString("msg", getActivity().getString(R.string.connect_time_out_toast));
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

        }
    }
}
