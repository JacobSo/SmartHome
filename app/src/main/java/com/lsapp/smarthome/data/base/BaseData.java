package com.lsapp.smarthome.data.base;

/**
 * Created by Administrator on 2016/8/11.
 */
public class BaseData {
    private boolean IsSuccess = false;
    private String ExecuteTime;
    private String SuccessExecuteMsg;
    private String FailExecuteMsg;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public String getExecuteTime() {
        return ExecuteTime;
    }

    public void setExecuteTime(String executeTime) {
        ExecuteTime = executeTime;
    }

    public String getSuccessExecuteMsg() {
        return SuccessExecuteMsg;
    }

    public void setSuccessExecuteMsg(String successExecuteMsg) {
        SuccessExecuteMsg = successExecuteMsg;
    }

    public String getFailExecuteMsg() {
        return FailExecuteMsg;
    }

    public void setFailExecuteMsg(String failExecuteMsg) {
        FailExecuteMsg = failExecuteMsg;
    }
}
