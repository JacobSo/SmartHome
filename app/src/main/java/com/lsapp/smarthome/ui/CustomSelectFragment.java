package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentFunctionCustomSelectBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/9/14.
 */
public class CustomSelectFragment extends BaseFragment implements View.OnClickListener {
    private FragmentFunctionCustomSelectBinding binding;
    private Scene scene;

    public static CustomSelectFragment newInstance(Scene scene) {
        CustomSelectFragment fragment = new CustomSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable("scene", scene);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scene = (Scene) getArguments().getSerializable("scene");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_function_custom_select, container, false);
        binding.functionTimer.setOnClickListener(this);
        binding.functionCause.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(scene.getIsShareSpace()==1&&scene.getIsAllowOperat()==0){
            zToastUtil.showSnack(binding.getRoot(), getString(R.string.no_permission));
            return;
        }
        if (scene.getActionId() == null || scene.getAirclearConnect() != 1) {
            zToastUtil.showSnack(binding.getRoot(), getString(R.string.air_offline));
            return;
        }

        if (view == binding.functionTimer) {
                RxBus.getDefault().post(new FragmentSwitchEvent(CustomTimerFragment.newInstance(scene), true));
        } else {
            if (scene.getDeviceId() != null && scene.getCheckairConnect() == 1) {
                RxBus.getDefault().post(new FragmentSwitchEvent(CustomCauseFragment.newInstance(scene), true));
            }else zToastUtil.showSnack(binding.getRoot(),getString(R.string.check_offline));

        }
    }
}
