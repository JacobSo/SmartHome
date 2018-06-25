package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.text.InputType;
import android.text.TextUtils;

import com.lsapp.smarthome.app.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.EditViewBinding;
import com.lsapp.smarthome.ui.CustomCauseFragment;
import com.lsapp.smarthome.ui.CustomTimerFragment;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/23.
 */
public class FunctionCustomModel extends BaseModel<BaseData> {
    private static final String TAG = "FunctionCustomModel";
    private CustomCauseFragment causeView;
    private CustomTimerFragment timerView;
    private Calendar calendar = Calendar.getInstance();
    private Scene scene;


    private ArrayList<String> checkData = new ArrayList<>();
    private boolean isOpen = false;
    private int type;//0timer1other
    private int runTime;
    private int wind;
    private int anion;
    private String startTime = null;
    private String startDate = null;
    private boolean isRepeat = false;

    private int JqTriggerValue = 0;
    private int Pm25TriggerValue = 0;
    private int VocTriggerValue = 0;
    private int JbTriggerValue = 0;

    public FunctionCustomModel(CustomCauseFragment context) {
        super(context.getActivity());
        causeView = context;
        type = 1;
    }

    public FunctionCustomModel(CustomTimerFragment context) {
        super(context.getActivity());
        timerView = context;
        type = 0;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public void setAnion(int anion) {
        this.anion = anion;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public void setJqTriggerValue(int jqTriggerValue) {
        JqTriggerValue = jqTriggerValue;
    }

    public void setPm25TriggerValue(int pm25TriggerValue) {
        Pm25TriggerValue = pm25TriggerValue;
    }

    public void setVocTriggerValue(int vocTriggerValue) {
        VocTriggerValue = vocTriggerValue;
    }

    public void setJbTriggerValue(int jbTriggerValue) {
        JbTriggerValue = jbTriggerValue;
    }

    public ArrayList<String> getCheckData() {
        return checkData;
    }

    public void setCheckData(ArrayList<String> checkData) {
        this.checkData = checkData;
    }

    public void getCustomData() {
        getCustomCheckData(baseData -> {
            if (baseData.isSuccess()) {
                setCheckData(baseData.getList());
                causeView.setView();
            }
        });
    }

    public void prepareSubmit() {
        if (type == 0) {

            if (TextUtils.isEmpty(startDate) || startTime.equals("0:0")) {
                zToastUtil.showSnack(timerView.getBindingView(), context.getString(R.string.no_time_date_toast));
                return;
            }

            if (runTime == 0) {
                zToastUtil.showSnack(timerView.getBindingView(), context.getString(R.string.no_run_time_toast));
                return;
            }

            if (zDateUtil.isBeforeNow(startDate + " " + startTime + ":00")) {
                Log.d(TAG, startDate + " " + startTime);
                zToastUtil.showSnack(timerView.getBindingView(), context.getString(R.string.time_over_toast));
                return;
            }
        } else {
            if (JqTriggerValue == 0 && JbTriggerValue == 0 && Pm25TriggerValue == 0 && VocTriggerValue == 0) {
                zToastUtil.showSnack(causeView.getBindingView(), context.getString(R.string.no_item_toast));
                return;
            }
            if (runTime == 0) {
                zToastUtil.showSnack(causeView.getBindingView(), context.getString(R.string.no_run_time_toast));
                return;
            }
        }
        EditViewBinding binding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.edit_view, null, false);
        binding.editText.setText(type==0?context.getString(R.string.customize_timer):context.getString(R.string.result_check_over));
        binding.editText.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.task_name));
        dialog.setView(binding.getRoot());
        dialog.setPositiveButton(context.getString(R.string.create_task), (dialogInterface, i) -> {
            if (TextUtils.isEmpty(binding.editText.getText().toString())) {
                binding.editText.setError(context.getString(R.string.no_task_name_toast));
            } else
                submit(binding.editText.getText().toString());
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), null);
        dialog.show();
    }

    private void submit(String name) {
        String condition;
        if (type == 0) {
            condition = "{\"StartTime\":\"" + startDate + " " + startTime + "\",IsRepeat:" + (isRepeat ? "1" : "0") + "}";
        } else {
            condition = "{\"JqTriggerValue\":" + JqTriggerValue + ",\"Pm25TriggerValue\":" + Pm25TriggerValue +
                    ",\"VocTriggerValue\":" + VocTriggerValue + ",\"JbTriggerValue\":" + JbTriggerValue + "}";
        }

        Log.e(TAG, "submit: " + condition);
        addCustomTask(name, scene.getActionId(), isOpen, type, runTime, wind, anion, condition, scene.getDeviceId(), this::action);
    }

    public void timePicker(TextView tx) {
        TimePickerDialog datePickerDialog;
        datePickerDialog = new TimePickerDialog(context, (timePicker, i, i1) ->
                tx.setText((i < 10 ? "0" + i : i) + ":" + (i1 < 10 ? "0" + i1 : i1)), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        datePickerDialog.show();
    }

    public void datePicker(TextView tx) {
        tx.setVisibility(View.VISIBLE);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(context, (DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) -> {
            if (zDateUtil.isBeforeNow(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth + 1) + " " + "00:00:00")) {
                zToastUtil.show(context, context.getString(R.string.time_over_toast));
            } else {
                tx.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void timeSetDialog(TextView tx) {
        EditText et = new EditText(context);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint(context.getString(R.string.minute));
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.customize))
                .setView(et)
                .setPositiveButton(context.getString(R.string.confirm), (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(et.getText().toString())) {
                        return;
                    }
                    setRunTime(Integer.valueOf(et.getText().toString()));
                    tx.setText(et.getText().toString() + context.getString(R.string.minute));
                })
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }

    public void selectTimeDialog(TextView tx) {
        AlertDialog.Builder dateDialog = new AlertDialog.Builder(context);
        dateDialog.setItems(R.array.time_minute, (dialogInterface, i) -> {
            if (i == 3) {
                timeSetDialog(tx);
            } else {
                tx.setText(context.getResources().getStringArray(R.array.time_minute)[i]);
                setRunTime((i + 1) * 60);
            }
        });
        dateDialog.show();
    }

    public ArrayAdapter<CharSequence> getSpinnerAdapter(int resource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                resource, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        return adapter;
    }

    @Override
    protected void success(BaseData model) {
        context.finish();
    }

    @Override
    protected void failed(BaseData model) {
        if (type == 0) {
            zToastUtil.showSnack(timerView.getBindingView(), model.getFailExecuteMsg());
        } else {
            zToastUtil.showSnack(causeView.getBindingView(), model.getFailExecuteMsg());
        }
    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {
        if (type == 0)
            zToastUtil.showSnack(timerView.getBindingView(), context.getString(R.string.exception_toast));
        else
            zToastUtil.showSnack(causeView.getBindingView(), context.getString(R.string.exception_toast));
    }
}
