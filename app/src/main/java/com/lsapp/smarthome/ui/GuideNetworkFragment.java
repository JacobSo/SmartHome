package com.lsapp.smarthome.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.FragmentGuideNetworkBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GuideNetworkFragment extends BaseFragment implements View.OnClickListener {

    private FragmentGuideNetworkBinding binding;

    public static GuideNetworkFragment newInstance() {
        GuideNetworkFragment fragment = new GuideNetworkFragment();
        Bundle args = new Bundle();
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

    @Override
    public void onResume() {
        super.onResume();
        binding.btnNext.setText(zContextUtil.isWifi(getActivity()) ? getString(R.string.next) : getString(R.string.set));
        binding.wifiSsd.setText(getString(R.string.wifi_now)+zContextUtil.getSSid(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_network, container, false);
        binding.btnBack.setText(getString(R.string.exit));
        binding.btnBack.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnBack) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.quit_quick_set));
            builder.setMessage(getString(R.string.quit_quick_set_content));
            builder.setNegativeButton(getString(R.string.cancel), null);
            builder.setPositiveButton(getString(R.string.confirm), (dialogInterface, i) -> getActivity().finish());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (view == binding.btnNext) {
            if (zContextUtil.isWifi(getActivity())) {
                String pwd = binding.wifiPwd.getText().toString();
                if(TextUtils.isEmpty(pwd)||pwd.length()<8){
                    zToastUtil.showSnack(binding.getRoot(),getString(R.string.guide_page_1_wifi_pwd_error));
                }else{
                    RxBus.getDefault().post(new FragmentSwitchEvent(GuideScanFragment.newInstance(pwd,0), true));
                }
            } else {
                startActivity(new Intent().setAction("android.net.wifi.PICK_WIFI_NETWORK"));
            }
        }
    }

}
