package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentGuideSceneBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.lsapp.smarthome.event.GuideSceneEvent;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GuideSceneFragment extends BaseFragment implements View.OnClickListener {
    private FragmentGuideSceneBinding binding;
    private ArrayList<Scene> scenes;
    private Handler handler = new Handler();

    public static GuideSceneFragment newInstance(ArrayList<Scene> scenes) {
        GuideSceneFragment fragment = new GuideSceneFragment();
        Bundle args = new Bundle();
        args.putSerializable("scenes", scenes);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initModel() {
        scenes = (ArrayList<Scene>) getArguments().getSerializable("scenes");
    }

    @Override
    public View getBindingView() {
        return null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_scene, container, false);
        setSpinner();
        binding.btnBack.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        return binding.getRoot();
    }


    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.scene_list, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        binding.spinner.setAdapter(adapter);

        if (scenes.size() != 0) {
            binding.existSpinner.setVisibility(View.VISIBLE);
            binding.spinner.setVisibility(View.GONE);
            String[] array = new String[scenes.size() + 1];

            for (int i = 0; i < scenes.size(); i++) {
                array[i] = scenes.get(i).getSpaceName();
            }
            array[array.length - 1] = "新建";

            ArrayAdapter<CharSequence> existAdapter = new ArrayAdapter<>(getActivity(), R.layout.text_view, array);
            adapter.setDropDownViewResource(R.layout.text_view);
            binding.existSpinner.setAdapter(existAdapter);
            binding.existSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    binding.spinner.setVisibility(i == array.length - 1 ? View.VISIBLE : View.GONE);
                    binding.spinner.requestFocus();
                    handler.post(() -> binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnBack) {
            RxBus.getDefault().post(new FragmentSwitchEvent(null, true));
        } else {
            RxBus.getDefault().post(new GuideSceneEvent(
                    binding.spinner.getVisibility() == View.GONE ?
                            binding.existSpinner.getSelectedItemPosition() : binding.spinner.getSelectedItemPosition(),
                    binding.spinner.getVisibility() == View.GONE));
        }
    }
}
