package com.lsapp.smarthome.ui.model;

import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.LSException;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.CheckData;
import com.lsapp.smarthome.data.base.Control;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.MainSubFragment;
import com.pgyersdk.crash.PgyCrashManager;

import rx.Subscription;


/**
 * Created by Administrator on 2016/12/12.
 */

public class MainSubModel extends BaseModel<BaseData>{
    private Scene scene;
    private Control control;
    private int wind = 0;
    private int ion = 0;
    private int time = 0;
    private MainSubFragment view;
    private Subscription loopSub = null;
    public MainSubModel(MainSubFragment context) {
        super(context.getActivity());
        view = context;
    }

    public Subscription getLoopSub() {
        return loopSub;
    }

    public Control getControl() {
        return control;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        try {
            control = scene.getAirClerState();
        }catch (Exception e){
            PgyCrashManager.reportCaughtException(context,
                    new LSException(e+""));

        }
    }

    public void airControl(int type, int value) {
        if(control==null){
            if(scene!=null){
                control = scene.getAirClerState();
            }else{
                control = BaseApplication.get(context).getSelectScene().getAirClerState();
            }
        }
        if(control==null){
           control = new Control(0,0,0,0,0,0,0,0);
        }
        int tempWind = control.getPurifier();
        int tempIon = control.getIonMode();
        int tempTime = control.getTimer();
        int tempPanelLight = control.getPanelLight();
        int tempOnOff = control.getOnOff();
        if (type == 0) {
            wind = value;
            if (value == 0) {
                ion = 0;
                time = 0;
            }
        } else if (type == 1) {
            ion = value;
        } else if (type == 2) {
            time = value;
        }

        if (wind != 0) {
            control.setOnOff(1);
        } else {
            control.setOnOff(0);
        }
        control.setPurifier(wind);
        control.setIonMode(ion);
        control.setTimer(time);
        view.setControlView(false);
        ariControl(scene.getActionId(), wind, ion, time*60, control.getPanelLight(), baseData -> {
            if (!baseData.isSuccess()) {
                control.setPurifier(tempWind);
                control.setIonMode(tempIon);
                control.setTimer(tempTime);
                control.setPanelLight(tempPanelLight);
                control.setOnOff(tempOnOff);
                view.setControlView(true);
            }
            BaseApplication.get(context).setGuide(false);//block main guide
        });
    }

    public void loop(){
        loopSub = realTimeData(scene.getDeviceId(), baseData -> {
            if(baseData.isSuccess()){
                for(CheckData past:scene.getSpaceData()){
                    for(CheckData loop:baseData.getList().get(0).getList()){
                        if(past.getName().equals(loop.getName())&&!past.getData().equals(loop.getData())){
                            Log.v("rxjava",loop.getData()+":"+loop.getLevel());
                            past.setData(loop.getData());
                            past.setLevel(loop.getLevel());
                            past.setDetectTime(loop.getDetectTime());
                        }
                    }
                }
                view.updateAdapterView();
            }
        });
    }

    public void stopLoop(){
        if(!loopSub.isUnsubscribed()) loopSub.unsubscribe();
    }


    @Override
    protected void success(BaseData model) {

    }

    @Override
    protected void failed(BaseData model) {

    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {

    }
}
