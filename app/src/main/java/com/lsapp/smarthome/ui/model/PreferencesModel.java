package com.lsapp.smarthome.ui.model;

import android.content.SharedPreferences;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.ui.PreferencesActivity;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;


/**
 * Created by Administrator on 2016/8/18.
 */
public class PreferencesModel extends BaseModel<AccountData>{
    private PreferencesActivity view;

    public PreferencesModel(PreferencesActivity context) {
        super(context);
        view = context;

    }


    public void getPushConfig(){
        getPushConfig(this::action);
    }

    public void setPushConfig(boolean warn,boolean log,boolean news){
        setPushConfig(warn?1:0, log?1:0, news?1:0, baseData -> {
            if(baseData.isSuccess()){

            }else{

            }
        });
    }

    @Override
    protected void success(AccountData model) {
      //  zSharedPreferencesUtil.save(context,Const.SP,Const.SP_SETTING_UPDATE,Const.SP_N);
        if(model.getList()!=null&&model.getList().size()!=0){
            SharedPreferences.Editor editor = zSharedPreferencesUtil.getEditor(context, Const.DEFAULT_SP);
            editor.putBoolean(Const.SP_PUSH_WARM,model.getList().get(0).getIsReceiveWarn()==1);
            editor.putBoolean(Const.SP_PUSH_COMMON,model.getList().get(0).getIsReceiveLog()==1);
            editor.putBoolean(Const.SP_PUSH_NEWS,model.getList().get(0).getIsReceiveSaleMsg()==1);
            editor.commit();
        }
        view.setPushView();
    }

    @Override
    protected void failed(AccountData model) {

    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {
        zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.exception_toast));
    }
}
