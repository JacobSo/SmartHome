package com.zuni.library.ui;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.zuni.library.R;
import com.zuni.library.databinding.FragmentTimePickerSinglePagerBinding;
import com.zuni.library.listener.OnTimeSelectListener;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/30.
 */
public class DatePickerDialogHelperWithView implements NumberPicker.OnScrollListener{
    private String[] month = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    private DialogPlus dialog;
    private OnTimeSelectListener timeSelectListener;
    private FragmentActivity activity;
    private Calendar calendar = Calendar.getInstance();
    private FragmentTimePickerSinglePagerBinding timeBinding;
    public DatePickerDialogHelperWithView(FragmentActivity activity, Fragment context, boolean isFull) {
        this.activity = activity;
        timeSelectListener = (OnTimeSelectListener) context;
        timeBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.fragment_time_picker_single_pager, null, false);
        timeBinding.tabLayout.addTab(timeBinding.tabLayout.newTab().setText("年"));
        timeBinding.tabLayout.addTab(timeBinding.tabLayout.newTab().setText("月"));
        if(isFull){
            timeBinding.tabLayout.addTab(timeBinding.tabLayout.newTab().setText("日"));
            timeBinding.tabLayout.addTab(timeBinding.tabLayout.newTab().setText("区间"));
        }

        timeBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        timeBinding.timeAreaLayout.setVisibility(View.GONE);
                        timeBinding.timeSingleLayout.setVisibility(View.VISIBLE);
                        timeBinding.dayPicker.setVisibility(View.GONE);
                        timeBinding.dayTxt.setVisibility(View.GONE);
                        timeBinding.monthPicker.setVisibility(View.GONE);
                        timeBinding.monthTxt.setVisibility(View.GONE);
                        break;
                    case 1:
                        timeBinding.timeAreaLayout.setVisibility(View.GONE);
                        timeBinding.timeSingleLayout.setVisibility(View.VISIBLE);
                        timeBinding.monthPicker.setVisibility(View.VISIBLE);
                        timeBinding.monthTxt.setVisibility(View.VISIBLE);
                        timeBinding.dayPicker.setVisibility(View.GONE);
                        timeBinding.dayTxt.setVisibility(View.GONE);
                        break;
                    case 2:
                        timeBinding.timeAreaLayout.setVisibility(View.GONE);
                        timeBinding.timeSingleLayout.setVisibility(View.VISIBLE);
                        timeBinding.monthPicker.setVisibility(View.VISIBLE);
                        timeBinding.monthTxt.setVisibility(View.VISIBLE);
                        timeBinding.dayPicker.setVisibility(View.VISIBLE);
                        timeBinding.dayTxt.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        timeBinding.timeAreaLayout.setVisibility(View.VISIBLE);
                        timeBinding.timeSingleLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setToday();
        setDay();
        timeBinding.yearPicker.setOnScrollListener(this);
        timeBinding.monthPicker.setOnScrollListener(this);

        dialog = DialogPlus.newDialog(activity)
                .setGravity(Gravity.TOP)
                .setCancelable(true)
                .setContentHolder(new ViewHolder(timeBinding.getRoot()))
                .setExpanded(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view == timeBinding.submitPick) {
                            switch (timeBinding.tabLayout.getSelectedTabPosition()) {
                                case 0:
                                    timeSelectListener.onTimeSelect(timeBinding.yearPicker.getValue() + "", timeBinding.yearPicker.getValue() + "年");
                                    Log.v("PickerFragment",timeBinding.yearPicker.getValue()+"");
                                    break;
                                case 1:
                                    timeSelectListener.onTimeSelect(timeBinding.yearPicker.getValue() + "-" +
                                                    timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()],
                                            timeBinding.yearPicker.getValue() + "年" +
                                                    timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()] + "月");
                                    Log.v("PickerFragment",timeBinding.yearPicker.getValue() + "-" +
                                            timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()]+"");

                                    break;
                                case 2:
                                    timeSelectListener.onTimeSelect(timeBinding.yearPicker.getValue() + "-" +
                                                    timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()] + "-" +
                                                    timeBinding.dayPicker.getValue(),
                                            timeBinding.yearPicker.getValue() + "年" +
                                                    timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()] + "月" +
                                                    timeBinding.dayPicker.getValue() + "日");
                                    Log.v("PickerFragment",timeBinding.yearPicker.getValue() + "-" +
                                            timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()] + "-" +
                                            timeBinding.dayPicker.getValue());
                                    break;
                                case 3:
                                    timeSelectListener.onTimeSelect(timeBinding.beginTimeBtn.getText().toString() + "," + timeBinding.endTimeBtn.getText().toString(),
                                            timeBinding.beginTimeBtn.getText().toString() + " 到 " + timeBinding.endTimeBtn.getText().toString());
                                    Log.v("PickerFragment",timeBinding.beginTimeBtn.getText().toString() + "," + timeBinding.endTimeBtn.getText().toString());
                                    break;
                            }
                        } else if (view == timeBinding.beginTimeBtn) {
                            onBookingTime(timeBinding.beginTimeBtn);
                        } else if(view == timeBinding.endTimeBtn){
                            onBookingTime(timeBinding.endTimeBtn);
                        }
                    }
                })
                .create();
    }

    private DialogPlus getDialog() {
        return dialog;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void setDay() {
        String date = timeBinding.yearPicker.getValue() + timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()];
        timeBinding.dayPicker.setMaxValue(getDayOfMonth(date));
    }

    private void setToday() {
        timeBinding.yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        timeBinding.yearPicker.setMinValue(2010);
        timeBinding.yearPicker.setValue(calendar.get(Calendar.YEAR));

        timeBinding.monthPicker.setDisplayedValues(month);
        timeBinding.monthPicker.setMaxValue(month.length - 1);
        timeBinding.monthPicker.setValue(calendar.get(Calendar.MONTH));

        timeBinding.dayPicker.setMinValue(1);
        setDay();
        timeBinding.dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
    }
    public int getDayOfMonth(String date) {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat oSdf = new SimpleDateFormat("", Locale.CHINA);
        oSdf.applyPattern("yyyyMM");
        try {
            //  System.out.println(date);
            cal.setTime(oSdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //     System.out.println(num2 + "");
        return num2;
    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {
        setDay();
    }


    private void onBookingTime(final Button b) {
        new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    String begin = timeBinding.beginTimeBtn.getText().toString();
                    String end = timeBinding.endTimeBtn.getText().toString();
                    String set = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    if (!begin.equals(activity.getString(R.string.set_begin_time)) && b == timeBinding.endTimeBtn) {
                        if (zDateUtil.compareDate(begin, set))
                            b.setText(set);
                        else
                            zToastUtil.show(activity, "选取的时间不正确");
                    } else if (!end.equals(activity.getString(R.string.set_end_time)) && b == timeBinding.beginTimeBtn) {
                        if (zDateUtil.compareDate(set, end))
                            b.setText(set);
                        else
                            zToastUtil.show(activity, "选取的时间不正确");
                    } else {
                        b.setText(set);
                    }


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
