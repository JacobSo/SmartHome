package com.lsapp.smarthome.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.base.DateCheck;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
    private ArrayList<DateCheck> data;

    public CaldroidSampleCustomAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData, ArrayList<DateCheck> data) {
        super(context, month, year, caldroidData, extraData);
        this.data = data;
    }

    public ArrayList<DateCheck> getData() {
        return data;
    }

    public void setData(ArrayList<DateCheck> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        if (convertView == null)
            cellView = inflater.inflate(R.layout.custom_cell, null);

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        View todayPoint = cellView.findViewById(R.id.today_point);

        tv1.setTextColor(Color.BLACK);
        DateTime dateTime = this.datetimeList.get(position);


        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;


        if ((minDateTime != null && dateTime.lt(minDateTime)) || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {
            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }
        } else
            shouldResetDiabledView = true;

        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1)
            tv1.setTextColor(Color.BLACK);
        else {
            shouldResetSelectedView = true;
            cellView.setBackgroundResource(R.color.white);//今日背景
        }

        //今日样式
        if (shouldResetDiabledView && shouldResetSelectedView) {
            if (dateTime.equals(getToday())) {
                todayPoint.setVisibility(View.VISIBLE);
                View v = cellView.findViewById(R.id.refresh_point);
                v.setVisibility(View.GONE);
            } else {
                cellView.setBackgroundResource(R.color.white);
                todayPoint.setVisibility(View.GONE);
            }
        }
        tv1.setText("" + dateTime.getDay());

        for (DateCheck s : data) {
            if ((dateTime.getDay().toString().length() == 1 ?
                    "0" + dateTime.getDay() : dateTime.getDay().toString()).equals(s.getDate().substring(8, 10))) {
                if (s.getIsopenAirCleaner() == 1) {
                    cellView.findViewById(R.id.refresh_point).setVisibility(View.VISIBLE);
                }else  cellView.findViewById(R.id.refresh_point).setVisibility(View.INVISIBLE);
            }
        }
        //非本月
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(context.getResources()
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
            //cellView.setVisibility(View.GONE);
        } else {
            setCustomResources(dateTime, cellView.findViewById(R.id.high_light_bg), tv1);
        }
/*        if(tv1.getCurrentTextColor()!=-1){
            cellView.setBackgroundResource(R.color.white);
        }*/
    System.out.println("nodata:"+data.size());
        if(data.size()==0)
            cellView.findViewById(R.id.high_light_bg).setBackgroundColor(Color.WHITE);
        return cellView;
    }


}
