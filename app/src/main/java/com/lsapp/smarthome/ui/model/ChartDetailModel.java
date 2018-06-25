package com.lsapp.smarthome.ui.model;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.DeviceDetailData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.ChartDetailActivity;
import com.lsapp.smarthome.ui.widget.MyMarkerView;
import com.lsapp.smarthome.utils.ColorUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/28.
 * <p>
 * public static double methanal_over_val = 0.1;  // 3.06;                    //0.1 mg/m3    分子量 30.03   0.06
 * public static double pm25_over_val =75;// 3.56;                        //0.075mg/m3      20   0.09
 * public static double pm10_voer_val = 150;
 * public static double C02_over_val =1600;// 3.24;                       //0.1mg/m3        44   0.06
 * public static double C0_over_val =10;// 1.45;                        //10mg/m3         30   7.46
 * public static double toluene_over_val =0.2;// 6.65;                   //0.2mg/m3        92   0.05
 */
public class ChartDetailModel extends BaseModel<DeviceDetailData> {
    private ChartDetailActivity view;
    private DeviceDetailData deviceDetailData;
    private int type;
    private String deviceId;
    private String unit;

    public ChartDetailModel(ChartDetailActivity context) {
        super(context);
        view = context;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public float getMaxValue() {
        float temp = 0.0f;
        for (int j = 0; j < deviceDetailData.getList().get(0).getDataList().size(); j++) {
            if (Float.valueOf(deviceDetailData.getList().get(0).getDataList().get(j).getValue()) > temp) {
                temp = Float.valueOf(deviceDetailData.getList().get(0).getDataList().get(j).getValue());
            }
        }
        if (deviceDetailData.getList().size() == 2) {
            for (int j = 0; j < deviceDetailData.getList().get(1).getDataList().size(); j++) {
                if (Float.valueOf(deviceDetailData.getList().get(1).getDataList().get(j).getValue()) > temp) {
                    temp = Float.valueOf(deviceDetailData.getList().get(1).getDataList().get(j).getValue());
                }
            }
        }

        if (1.0f<temp&&temp <50.0f) {
            return 100.0f;
        }else  if(temp<=1.0f){
            return 1.0f;
        } return temp * 2;


    }


    public float getDangerValue(int type) {
        if (type == 1) {//甲醛
            return 0.1f;
        } else if (type == 2) {//pm25
            return 75f;
        } else if (type == 3) {//温度
            return 0f;
        } else if (type == 4) {//co2
            return 1600f;
        } else if (type == 5) {//co
            return 10f;
        } else if (type == 7) {//甲苯
            return 0.2f;
        } else if (type == 8) {//pm10
            return 150f;
        } else if (type == 9) {//湿度
            return 0f;
        }
        return 0f;
    }

    public DeviceDetailData getDeviceDetailData() {
        return deviceDetailData;
    }

    public void setDeviceDetailData(DeviceDetailData deviceDetailData) {
        this.deviceDetailData = deviceDetailData;
    }

    public void get(int hour) {
        getChartDetail(type, hour, deviceId, this::action);
    }

    public void week() {
        getWeekChart(deviceId, type, this::action);
    }

    @Override
    protected void success(DeviceDetailData model) {
        setDeviceDetailData(model);
        view.setView();
    }

    @Override
    protected void failed(DeviceDetailData model) {
        //  zToastUtil.showSnack(view.getBindingView(),"获取检测数据失败，请稍后再试");
        view.setEmptyView();
    }

    @Override
    protected void connectFail() {
        view.setEmptyView();
    }

    @Override
    protected void throwError() {
        //   zToastUtil.showSnack(view.getBindingView(),"出错了，请稍后再试");
        view.setEmptyView();
    }


    public void setChart(LineChart chart1, DeviceDetailData checkData) {
        chart1.clear();
        chart1.setTouchEnabled(true);
        chart1.setDragEnabled(true);
        chart1.setScaleEnabled(true);
        chart1.setPinchZoom(true);
        chart1.setAutoScaleMinMaxEnabled(true);
        chart1.setHighlightPerDragEnabled(true);
        chart1.setScaleYEnabled(true);//纵向放大


        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        chart1.setMarkerView(mv); // Set the marker to the chart

        chart1.getAxisRight().setEnabled(false);
        chart1.getAxisLeft().setEnabled(true);

        chart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart1.getXAxis().setGridColor(Color.parseColor("#00000000"));
        chart1.getXAxis().setEnabled(false);

        chart1.setContentDescription("");
        chart1.setDescription("");
        chart1.setNoDataText("");
        YAxis leftAxis = chart1.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

        if (getDangerValue(type) != 0f) {
            LimitLine dangerLine = new LimitLine(getDangerValue(type), context.getString(R.string.danger));
            setLine(dangerLine);
            leftAxis.addLimitLine(dangerLine);
        }

        leftAxis.setAxisMaxValue(getMaxValue());
        leftAxis.setAxisMinValue(0.0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        chart1.animateY(1000);

        //data set
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < checkData.getList().size(); i++) {
            ArrayList<Entry> values = new ArrayList<>();
            for (int j = 0; j < checkData.getList().get(i).getDataList().size(); j++) {
                values.add(new Entry(j, Float.valueOf(checkData.getList().get(i).getDataList().get(j).getValue())));
            }

            LineDataSet set1;
            if (chart1.getData() != null &&
                    chart1.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) chart1.getData().getDataSetByIndex(0);
                set1.setValues(values);
                set1.setDrawValues(false);
                set1.setDrawCircles(false);
                set1.setMode(LineDataSet.Mode.STEPPED);
                chart1.getData().notifyDataChanged();
                chart1.notifyDataSetChanged();
            } else {
                if (checkData.getList().size() == 2) {
                    set1 = new LineDataSet(values, i == 1 ? context.getString(R.string.minimum) : context.getString(R.string.maximum));
                } else {
                    set1 = new LineDataSet(values, checkData.getList().get(i).getName());
                }
                set1.setDrawValues(false);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.enableDashedLine(10f, 0f, 0f);
                set1.enableDashedHighlightLine(10f, 0f, 0f);
                set1.setColor(ColorUtil.getDarkTypeColor(i == 1 ?type + 1 :type));
                set1.setCircleColor(ColorUtil.getDarkTypeColor(i == 1 ? type + 1 : type));
                set1.setLineWidth(3f);
                set1.setCircleRadius(6f);
                set1.setDrawCircleHole(false);
                set1.setDrawCircles(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setHighLightColor(Color.parseColor("#00000000"));
                set1.setFillColor(ColorUtil.getLightTypeColor(i == 1 ? type + 1 : type));
                dataSets.add(set1); // add the datasets

            }
        }
        LineData data = new LineData(dataSets);
        chart1.setData(data);
        chart1.highlightValue(checkData.getList().get(0).getDataList().size() - 1, 0);
        Legend l = chart1.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);

        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        chart1.invalidate();


    }

    public void setLine(LimitLine line) {
        line.setLineWidth(4f);
        line.enableDashedLine(10f, 10f, 0f);
        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        line.setTextSize(10f);
    }
}
