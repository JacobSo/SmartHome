package com.lsapp.smarthome.ui.model;

import android.text.TextUtils;

import com.lsapp.smarthome.app.Log;

import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.ui.MainActivity;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/12/3.
 */
public class FeedModel extends BaseModel<BaseData> {
    private static final String TAG = "FeedModel";
    private MainActivity view ;
    private boolean isFeed = false;
    public FeedModel(MainActivity context) {
        super(context);
        view = context;
    }

    public boolean isFeed() {
        return isFeed;
    }

    public void setFeed(boolean feed) {
        isFeed = feed;
    }

    public void feed() {
        getNewFeed(this::action);
    }

    @Override
    protected void success(BaseData model) {
        Log.e(TAG, "success: "+ zSharedPreferencesUtil.get(context, Const.SP, Const.SP_LAST_MSG)+","+model.getSuccessExecuteMsg());


        if (zDateUtil.compareDate(zSharedPreferencesUtil.get(context, Const.SP, Const.SP_LAST_MSG), model.getSuccessExecuteMsg())) {
            view.setFeed();
            isFeed = true;
        }
        zSharedPreferencesUtil.save(context,Const.SP, Const.SP_LAST_MSG,model.getSuccessExecuteMsg());
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
