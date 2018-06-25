package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.data.base.BaseData;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class AccountData extends BaseData {
    private List<Account> list;
    private Account MasterInfo;
    private List<Account>  BindUserList;
    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }

    public Account getMasterInfo() {
        return MasterInfo;
    }

    public void setMasterInfo(Account masterInfo) {
        MasterInfo = masterInfo;
    }

    public List<Account> getBindUserList() {
        return BindUserList;
    }

    public void setBindUserList(List<Account> bindUserList) {
        BindUserList = bindUserList;
    }
}
