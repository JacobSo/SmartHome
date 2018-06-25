package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.FragmentFunctionCustomTimerBinding;
import com.lsapp.smarthome.ui.model.FunctionCustomModel;

/**
 * Created by Administrator on 2016/9/14.
 */
public class CustomTimerFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private FragmentFunctionCustomTimerBinding binding;
    private FunctionCustomModel model;


    public static CustomTimerFragment newInstance(Scene scene) {
        CustomTimerFragment fragment = new CustomTimerFragment();
        Bundle args = new Bundle();
        args.putSerializable("scene", scene);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initModel() {
        model = new FunctionCustomModel(this);
        model.setScene((Scene) getArguments().getSerializable("scene"));
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_function_custom_timer, container, false);
        binding.timerEffectSpinner.setAdapter(model.getSpinnerAdapter(R.array.air_effect));
        binding.timerLevelSpinner.setAdapter(model.getSpinnerAdapter(R.array.air_level));
        binding.timerEffectSpinner.setOnItemSelectedListener(this);
        binding.timerLevelSpinner.setOnItemSelectedListener(this);
        binding.timeBeginTimeText.setOnClickListener(this);
        binding.timeBeginDate.setOnClickListener(this);
        binding.timeRunTime.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.timeBeginTimeText) {
            model.timePicker(binding.timeBeginTimeText);
        } else if (view == binding.timeBeginDate) {
            model.datePicker(binding.timeBeginDateText);
        } else if (view == binding.timeRunTime) {
            model.selectTimeDialog(binding.timeRunTimeText);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
            model.setRepeat(binding.repeatCheckbox.isChecked());
            model.setStartTime(binding.timeBeginTimeText.getText().toString());
            model.setStartDate(binding.timeBeginDateText.getText().toString());
            model.prepareSubmit();
        } else getActivity().finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == binding.timerLevelSpinner) {
            model.setWind(i + 1);
         //   binding.timerEffectSpinner.setEnabled(i != 0);
        } else if (adapterView == binding.timerEffectSpinner) {
            model.setAnion(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
