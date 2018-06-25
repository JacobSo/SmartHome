package com.zuni.library.ui;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.zuni.library.R;
import com.zuni.library.databinding.FragmentTimePickerBinding;
import com.zuni.library.listener.OnTimeSelectListener;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/29.
 */
public class PickerFragment extends Fragment implements View.OnClickListener, NumberPicker.OnScrollListener {
    private FragmentTimePickerBinding timeBinding;
    private int which = 0;
    private static OnTimeSelectListener timeSelectListener;
    private String[] month = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
    static PickerFragment newInstance(int flag, OnTimeSelectListener listener) {
        PickerFragment fragment = new PickerFragment();
        Bundle args = new Bundle();
        timeSelectListener = listener;
        args.putInt("flag", flag);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        timeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_picker, null, false);
        which = getArguments().getInt("flag");
        if (which == 0) {
            timeBinding.dayPicker.setVisibility(View.GONE);
            timeBinding.monthPicker.setVisibility(View.GONE);
            timeBinding.dayTxt.setVisibility(View.GONE);
            timeBinding.monthTxt.setVisibility(View.GONE);
        } else if (which == 1) {
            timeBinding.dayPicker.setVisibility(View.GONE);
            timeBinding.dayTxt.setVisibility(View.GONE);
        } else if (which == 3) {
            timeBinding.timeAreaLayout.setVisibility(View.VISIBLE);
            timeBinding.timeSingleLayout.setVisibility(View.GONE);
        }
        setToday();
        setDay();
        timeBinding.yearPicker.setOnScrollListener(this);
        timeBinding.monthPicker.setOnScrollListener(this);
        timeBinding.submitPick.setOnClickListener(this);
        timeBinding.beginTimeBtn.setOnClickListener(this);
        timeBinding.endTimeBtn.setOnClickListener(this);
        return timeBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view == timeBinding.submitPick) {
            switch (which) {
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
        } else {
            onBookingTime(timeBinding.endTimeBtn);
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {
        setDay();
    }

    private void onBookingTime(final Button b) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                String begin = timeBinding.beginTimeBtn.getText().toString();
                String end = timeBinding.endTimeBtn.getText().toString();
                String set = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                if (!begin.equals(getString(R.string.set_begin_time)) && b == timeBinding.endTimeBtn) {
                    if (zDateUtil.compareDate(begin, set))
                        b.setText(set);
                    else
                        zToastUtil.show(getActivity(), "选取的时间不正确");
                } else if (!end.equals(getString(R.string.set_end_time)) && b == timeBinding.beginTimeBtn) {
                    if (zDateUtil.compareDate(set, end))
                        b.setText(set);
                    else
                        zToastUtil.show(getActivity(), "选取的时间不正确");
                } else {
                    b.setText(set);
                }


            }
        }, zDateUtil.getCalendar().get(Calendar.YEAR), zDateUtil.getCalendar().get(Calendar.MONTH), zDateUtil.getCalendar().get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setDay() {
        String date = timeBinding.yearPicker.getValue() + timeBinding.monthPicker.getDisplayedValues()[timeBinding.monthPicker.getValue()];
        timeBinding.dayPicker.setMaxValue(getDayOfMonth(date));
    }

    private void setToday() {
        timeBinding.yearPicker.setMaxValue(zDateUtil.getCalendar().get(Calendar.YEAR));
        timeBinding.yearPicker.setMinValue(2010);
        timeBinding.yearPicker.setValue(zDateUtil.getCalendar().get(Calendar.YEAR));

        timeBinding.monthPicker.setDisplayedValues(month);
        timeBinding.monthPicker.setMaxValue(month.length - 1);
        timeBinding.monthPicker.setValue(zDateUtil.getCalendar().get(Calendar.MONTH));

        timeBinding.dayPicker.setMinValue(1);
        setDay();
        timeBinding.dayPicker.setValue(zDateUtil.getCalendar().get(Calendar.DAY_OF_MONTH));
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
}