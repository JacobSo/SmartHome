package com.lsapp.smarthome.ui.model;

import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.MainFragment;
import com.lsapp.smarthome.utils.ColorUtil;
import com.zuni.library.utils.zToastUtil;


/**
 * Created by Administrator on 2016/12/1.
 */
public class MainModel extends BaseModel<BaseData>{
    private MainFragment view;
    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public MainModel(MainFragment context) {
        super(context.getActivity());
        view = context;
    }

    public void turnOn(){//012
        quickTurn(scene.getActionId(), scene.getVocType()==0?scene.getVocType():scene.getVocType()-1, this::action);
    }

    public void turnOnWithOutCheck() {//012
        int type;
        if (BaseApplication.get(context).getWeatherData() != null) {
            String condition = BaseApplication.get(context).getWeatherData().getResult().get(0).getAirCondition();
            if (condition.contains("优")) {
                type = 0;
            } else if (condition.contains("污染")) {
                type = 3;
            } else {
                type = 2;
            }
        }else{
            type = 3;
        }
        scene.setVocType(type);
        quickTurn(scene.getActionId(),type, this::action);
    }

    @Override
    protected void success(BaseData model) {
        view.setTurnView(true);
    }

    @Override
    protected void failed(BaseData model) {
        view.setTurnView(false);
        zToastUtil.showSnack(view.getBindingView(),(model.getFailExecuteMsg()));
    }

    @Override
    protected void connectFail() {
        view.setTurnView(false);
    }

    @Override
    protected void throwError() {
        view.setTurnView(false);
    }
}
