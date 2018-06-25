package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Switch;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentFunctionCustomCauseBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.CustomCauseAdapter;
import com.lsapp.smarthome.ui.model.FunctionCustomModel;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


/**
 * Created by Administrator on 2016/9/14.
 */
public class CustomCauseFragment extends BaseFragment implements OnRecycleViewItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private FragmentFunctionCustomCauseBinding binding;
    private FunctionCustomModel model;

    public static CustomCauseFragment newInstance(Scene scene) {
        CustomCauseFragment fragment = new CustomCauseFragment();
        Bundle args = new Bundle();
        args.putSerializable("scene", scene);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void initModel() {
        model = new FunctionCustomModel(this);
        model.setScene((Scene) getArguments().getSerializable("scene"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_function_custom_cause, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.causeLevelSpinner.setOnItemSelectedListener(this);
        binding.causeEffectSpinner.setOnItemSelectedListener(this);
        binding.timeRunTime.setOnClickListener(this);
        binding.causeEffectSpinner.setAdapter(model.getSpinnerAdapter(R.array.air_effect));
        binding.causeLevelSpinner.setAdapter(model.getSpinnerAdapter(R.array.air_level));
        model.getCustomData();
        return binding.getRoot();
    }


    public void setView() {
        ScaleInAnimationAdapter  adapter = new ScaleInAnimationAdapter(new CustomCauseAdapter(model.getCheckData(), this));
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View v, int position) {
        Switch s = (Switch) v;
        if (position == 0) {
            model.setJqTriggerValue(s.isChecked() ? 1 : 0);
        } else if (position == 1) {
            model.setPm25TriggerValue(s.isChecked() ? 1 : 0);
        }  else {
            model.setJbTriggerValue(s.isChecked() ? 1 : 0);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == binding.causeLevelSpinner) {
            model.setWind(i+1);
        } else if (adapterView == binding.causeEffectSpinner) {
            model.setAnion(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
           // model.submit(binding.causeName.getText().toString());
            model.prepareSubmit();
        } else getActivity().finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        model.selectTimeDialog(binding.timeRunTimeText);
    }
}
