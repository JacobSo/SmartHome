package com.lsapp.smarthome.ui.model;


import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.ui.ProfileFragment;
import com.zuni.library.utils.zToastUtil;

import java.util.List;


/**
 * Created by Administrator on 2016/8/12.
 */
public class ProfileModel extends BaseModel<AccountData> {
    private ProfileFragment view;
    private Account account;
    private List<Account> accountList ;

    public ProfileModel(ProfileFragment fragment) {
        super(fragment.getActivity());
        view = fragment;
    }

    public ProfileFragment getView() {
        return view;
    }

    public void setView(ProfileFragment view) {
        this.view = view;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }



    public void info(){
        getUserInfo(this::action);
    }

    public void logout(){
        signOut(model -> {
            //  view.dismissDialog();
            if(model.isSuccess()){
                BaseApplication.get(context).removeSessionKey();
                view.setViewLogout();
            }else{
                zToastUtil.showSnack(view.getView(),model.getSuccessExecuteMsg());
            }
        });
    }

    @Override
    protected void success(AccountData model) {
        setAccount(model.getList().get(0));
        setAccountList(model.getBindUserList());
        view.setView(model);

    }

    @Override
    protected void failed(AccountData model) {
        zToastUtil.showSnack(view.getBindingView(),model.getFailExecuteMsg());
        view.setFailLogout();
    }

    @Override
    protected void connectFail() {
        view.setFailLogout();
    }

    @Override
    protected void throwError() {
        view.setFailLogout();
      //  zToastUtil.showSnack(view.getBindingView(),"出错了，请稍后再试");
    }
}
