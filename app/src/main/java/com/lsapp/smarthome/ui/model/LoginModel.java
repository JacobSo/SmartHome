package com.lsapp.smarthome.ui.model;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.databinding.DialogMemberBindingBinding;
import com.lsapp.smarthome.ui.LoginActivity;
import com.zuni.library.utils.zToastUtil;



/**
 * Created by Administrator on 2016/8/11.
 */
public class LoginModel extends BaseModel<AccountData>{
    private LoginActivity view;
    public LoginModel(LoginActivity context) {
        super(context);
        view = context;
    }

    public void sms(String phone){
        getCode(phone, baseData -> {
            if(!baseData.isSuccess()){
                zToastUtil.showSnack(view.getBindingView(),baseData.getFailExecuteMsg());
            }
        });
    }

    public void login(String account,int way,String name,String pwd){
    //    view.loadingDialog("正在登录");
        signIn(account, way, name,pwd,this::action);
    }

    public void confirm(int result){
        confirmMembers(result, baseData -> {
        //    view.dismissDialog();
            if(baseData.isSuccess()){
                if(result==1){
                    BaseApplication.get(context).setUid(baseData.getSuccessExecuteMsg());
                }
                view.setView();
            }else{
                zToastUtil.showSnack(view.getBindingView(),baseData.getFailExecuteMsg());
            }
        });
    }


    public void inviteConfirm(Account account){
        DialogMemberBindingBinding bindingBinding = DataBindingUtil.inflate(view.getLayoutInflater(), R.layout.dialog_member_binding, null, false);
        bindingBinding.memberTitle.setTextColor(Color.parseColor("#00BCD4"));
        bindingBinding.memberTitle.setText(account.getMasterUser()+context.getString(R.string.get_invitation));
        bindingBinding.memberContent.setText(account.getMasterUser() +context.getString(R.string.invitation_content_1)
                +account.getMasterUser()+context.getString(R.string.invitation_content_2));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(bindingBinding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        bindingBinding.memberDismiss.setText(context.getString(R.string.accept));
        bindingBinding.memberDismiss.setOnClickListener(view1 -> {
          //  view.loadingDialog();
            confirm(1);
        });
        bindingBinding.memberDisconnect.setText(context.getString(R.string.reject));
        bindingBinding.memberDisconnect.setOnClickListener(view1 -> {
           // view.loadingDialog();
            confirm(0);
        });
    }

    public void shareLogin(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("登录提示");
        builder.setMessage("使用第三方登录，将会限制家庭成员功能，无法接受其他人分享给您的设备，是否继续？");
        builder.setPositiveButton("我知道了", (dialogInterface, i) -> {
            if(type==0)
                view.wechat();
            else if(type==1)
                view.sina();
            else
                view.taobao();
        });
        builder.setNegativeButton("取消",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void success(AccountData model) {
        BaseApplication.get(context).setSessionKey(model.getSuccessExecuteMsg());
        BaseApplication.get(context).setUid(model.getList().get(0).getUserId());
        BaseApplication.get(context).setUserName(model.getList().get(0).getUserName());
        BaseApplication.get(context).setParty(model.getList().get(0).getIsThirdParty()+"");
        if(model.getMasterInfo()==null){
            BaseApplication.get(context).setPermission("1");
        }else{
            BaseApplication.get(context).setPermission(model.getList().get(0).getIsAllowOperat()+model.getList().get(0).getIsBindingUser()+"");
        }

        if(model.getList().get(0).getIsConfirm()==0&&model.getList().get(0).getIsRelationUser()==1){
            inviteConfirm(model.getList().get(0));
        }else{
            view.setView();
        }
    }

    @Override
    protected void failed(AccountData model) {
        zToastUtil.show(context,model.getFailExecuteMsg());
        view.loginFail();
    }

    @Override
    protected void connectFail() {
        view.loginFail();
    }

    @Override
    protected void throwError() {
        zToastUtil.show(context,context.getString(R.string.exception_toast));
        view.loginFail();
    }
}
