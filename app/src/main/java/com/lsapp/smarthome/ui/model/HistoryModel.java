package com.lsapp.smarthome.ui.model;

import com.lsapp.smarthome.data.base.DateCheck;
import com.lsapp.smarthome.data.HistoryData;
import com.lsapp.smarthome.data.base.DateData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.HistoryActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/9.
 */
public class HistoryModel extends BaseModel<HistoryData> {
    private Scene scene;
    private ArrayList<DateData> dataList;
    private HistoryActivity view;
    private ArrayList<DateCheck> calendarData;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat cnFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private ArrayList<String> saveMonth = new ArrayList<>();

    public ArrayList<DateData> getDataList() {
        return dataList;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public SimpleDateFormat getCnFormat() {
        return cnFormat;
    }

    public ArrayList<DateCheck> getCalendarData() {
        return calendarData;
    }

    public HistoryModel(HistoryActivity context) {
        super(context);
        view = context;
    }

    public void calendar(String month) {
/*        boolean isNewMonth = true;
        for(String s:saveMonth){
            if(s.equals(month))
                isNewMonth = false;
        }
        if(isNewMonth){*/
        view.loadingDialog("获取空气质量记录");
        getCalendar(
                scene.getDeviceId(),
                scene.getActionId(),
                month == null ? monthFormat.format(new Date()) : month,
                dateCheckData -> {
                    view.dismissDialog();
                    if (dateCheckData.isSuccess()) {
                        calendarData = dateCheckData.getList();
                        try {
                            saveMonth.add(month);
                            view.setCalendar();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //   }

    }


    public void data(String date) {
        getDateData(scene.getDeviceId(), date, this::action);
    }


    @Override
    protected void success(HistoryData model) {
        dataList = model.getList();
        view.setView();
    }

    @Override
    protected void failed(HistoryData model) {
        view.setView();
    }

    @Override
    protected void connectFail() {
        view.setView();
    }

    @Override
    protected void throwError() {
        view.setView();
    }
}
