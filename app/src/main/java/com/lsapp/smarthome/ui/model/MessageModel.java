package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import com.lsapp.smarthome.app.Log;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.MessageData;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.UserMessage;
import com.lsapp.smarthome.databinding.DialogMessageHelpBinding;
import com.lsapp.smarthome.ui.MessageFragment;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MessageModel extends BaseModel<MessageData> {
    private MessageFragment view;
    private List<UserMessage> messageList;
    private int nextTime;
    private static final String TAG = "MessageModel";
    public MessageModel(MessageFragment context) {
        super(context.getActivity());
        view = context;
    }

    public List<UserMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<UserMessage> messageList) {
        this.messageList = messageList;
    }

    public int getNextTime() {
        return nextTime;
    }

    public void setNextTime(int nextTime) {
        this.nextTime = nextTime;
    }

    public void get(int pageTime) {
        nextTime = pageTime;
        getMessage(pageTime, this::action);
    }

    public void delete(int position){
        Log.d(TAG, "delete: "+getMessageList().get(position).getSendTime());
        
        deleteMessage(getMessageList().get(position).getReceiveUserName(), getMessageList().get(position).getReceiveThirdParty(), getMessageList().get(position).getSendTime(), new Action1<BaseData>() {
            @Override
            public void call(BaseData baseData) {
                if(baseData.isSuccess()){
                    Log.d(TAG, "call: delete message success");
                }else{
                    Log.d(TAG, "call: delete message failed");
                }
            }
        });
    }

    public void help(){
        DialogMessageHelpBinding helpBinding = DataBindingUtil.inflate(context.getLayoutInflater(),R.layout.dialog_message_help,null,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(helpBinding.getRoot());
        helpBinding.messageHelpClose.setOnClickListener(view1 -> alertDialog.dismiss());
        alertDialog.show();
    }


    @Override
    protected void success(MessageData model) {
        if (nextTime == 0) {//this time
            if (model.getList() != null && model.getList().size() != 0)
                zSharedPreferencesUtil.save(context, Const.SP, Const.SP_LAST_MSG, model.getList().get(0).getSendDt());
            setMessageList(model.getList());
            view.setRefreshFinish();
        } else if (nextTime == -1) {
            view.setOverLoading();
        } else {
            messageList.addAll(model.getList());
            view.setLoadFinish();
        }
        nextTime = model.getNextPageStartSendTime();//next time
    }

    @Override
    protected void failed(MessageData model) {
        zToastUtil.showSnack(view.getBindingView(),model.getFailExecuteMsg());
        view.setFailedView();
    }

    @Override
    protected void connectFail() {
        view.setFailedView();
    }

    @Override
    protected void throwError() {
        view.setFailedView();
      //  zToastUtil.showSnack(view.getBindingView(),"出错了，请稍后再试");
    }
}
