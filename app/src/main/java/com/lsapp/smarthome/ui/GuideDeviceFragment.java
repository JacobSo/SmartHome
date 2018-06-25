package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.GuideDevice;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentGuideDeviceBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.GuideDeviceListAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GuideDeviceFragment extends BaseFragment implements OnRecycleViewItemClickListener, View.OnClickListener {
    private FragmentGuideDeviceBinding binding;
    private ArrayList<GuideDevice> devices;
    private ArrayList<Scene> scenes;

    public static GuideDeviceFragment newInstance(ArrayList<GuideDevice> devices, ArrayList<Scene> scenes) {
        GuideDeviceFragment fragment = new GuideDeviceFragment();
        Bundle args = new Bundle();
        args.putSerializable("devices", devices);
        args.putSerializable("scenes", scenes);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devices = (ArrayList<GuideDevice>) getArguments().getSerializable("devices");
        scenes = (ArrayList<Scene>) getArguments().getSerializable("scenes");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_device, container, false);
        binding.btnNext.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(new GuideDeviceListAdapter(devices, this));
        binding.btnNext.setBackgroundResource(R.color.colorAccent);
        binding.btnNext.setEnabled(true);
        return binding.getRoot();
    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnBack) {
            RxBus.getDefault().post(new FragmentSwitchEvent(null, true));
        } else if (view == binding.btnNext) {
            RxBus.getDefault().post(new FragmentSwitchEvent(GuideSceneFragment.newInstance(scenes), true));
        }
    }
}
