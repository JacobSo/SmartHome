package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.base.Account;

import com.lsapp.smarthome.databinding.DialogMemberAddBinding;
import com.lsapp.smarthome.databinding.DialogMemberBindingBinding;
import com.lsapp.smarthome.ui.LoginActivity;
import com.lsapp.smarthome.ui.MemberActivity;
import com.zuni.library.utils.zPhoneUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.List;



/**
 * Created by Administrator on 2016/8/22.
 */
public class MemberModel extends BaseModel<AccountData> {
    private MemberActivity view;
    private List<Account> members;
    private Account master;

    public MemberModel(MemberActivity context) {
        super(context);
        view = context;
    }

    public List<Account> getMembers() {
        return members;
    }

    private void setMembers(List<Account> members) {
        this.members = members;
    }


    private Account getMaster() {
        return master;
    }

    private void setMaster(Account master) {
        this.master = master;
    }

    public void get() {
        getMembers(this::action);
    }


    public void add(String mobile, String name, boolean opera,boolean isSms) {
        //  view.loadingDialog();
        addMember(mobile, name, opera ? 1 : 0, isSms?1:0,baseData -> {
            //  view.dismissDialog();
            if (baseData.isSuccess()) {
                zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.send_invitation));
                view.onRefresh();
            } else {
                zToastUtil.showSnack(view.getBindingView(), baseData.getFailExecuteMsg());
            }
        });
    }

    private void update(String  uid,int party,int permission){
        updatePermission(uid, party, permission, baseData -> {
            if (baseData.isSuccess()) {
                view.onRefresh();
            } else {
                zToastUtil.showSnack(view.getBindingView(), baseData.getFailExecuteMsg());
            }
        });
    }

    private void disconnect(String id, boolean isSlave) {
        view.loadingDialog(context.getString(R.string.break_out_loading));
        unbindingMember(id, baseData -> {
            view.dismissDialog();
            if (baseData.isSuccess()) {
                view.onRefresh();
             //   if (isSlave) {
              //      context.startActivity(new Intent(context, LoginActivity.class));
             //   } else
                    zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.break_out));
            } else {
                zToastUtil.showSnack(view.getBindingView(), baseData.getFailExecuteMsg());
            }
        });
    }

    public void addMember() {
        DialogMemberAddBinding dialogBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_member_add, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        dialogBinding.memberConfirm.setOnClickListener(view1 -> {
            String phone = dialogBinding.memberMobile.getText().toString();
            String name = dialogBinding.memberName.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone))
                dialogBinding.memberMobile.setError(context.getString(R.string.info_null_toast));
            else if(!zPhoneUtil.isMobile(phone)){
                dialogBinding.memberMobile.setError(context.getString(R.string.enter_phone_toast));
            }
            else {
                add(phone, name, dialogBinding.memberOpera.isChecked(),dialogBinding.memberNotify.isChecked());
                alertDialog.dismiss();
            }
        });

        dialogBinding.memberDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
    }

    //string no format
    public void disconnectMember(int position) {
        DialogMemberBindingBinding bindingBinding = DataBindingUtil.inflate(view.getLayoutInflater(), R.layout.dialog_member_binding, null, false);
        if (position == -1) {//slave view
            bindingBinding.memberTitle.setText("与" + (TextUtils.isEmpty(getMaster().getRealName())?"家庭成员":getMaster().getRealName()) + "绑定中");
            bindingBinding.setData(getMaster());
        } else {//master view
            bindingBinding.memberUpdate.setEnabled(true);
            bindingBinding.setData(getMembers().get(position));
            if (getMembers().get(position).getIsConfirm() == 0) {
                bindingBinding.memberTitle.setText("等待" + getMembers().get(position).getNickname() + "接受邀请");
                bindingBinding.memberContent.setText("已经向" + getMembers().get(position).getNickname() + "发出共享设备邀请，一旦对方接受邀请，他将共同使用你的设备，是否撤销邀请？");
            } else {
                bindingBinding.memberTitle.setText("与" + getMembers().get(position).getNickname() + "绑定中");
                bindingBinding.memberContent.setText(getMembers().get(position).getNickname() + "正在绑定你的账号，共同使用你的设备，是否解除绑定？");
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(bindingBinding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        bindingBinding.memberDismiss.setOnClickListener(view1 -> {
            dialog.dismiss();
            if (position == -1)
                view.showSnack("与" + (TextUtils.isEmpty(getMaster().getRealName())?"家庭成员":getMaster().getRealName()) + "绑定中");
            else{
                update(getMembers().get(position).getUserName(),getMembers().get(position).getIsThirdParty(),bindingBinding.memberUpdate.isChecked()?1:0);
            }
        });
        bindingBinding.memberDisconnect.setOnClickListener(view1 -> {
            dialog.dismiss();
            disconnect(position == -1 ? master.getUserName() : getMembers().get(position).getUserName(), position == -1);
        });
    }

    @Override
    protected void success(AccountData model) {
        if (model.getMasterInfo() != null) {
            setMaster(model.getMasterInfo());
            view.showSnack(("与" + (TextUtils.isEmpty(getMaster().getRealName())?"家庭成员":getMaster().getRealName()) + "绑定中"));
        }
        setMembers(model.getList());
        view.setView();

    }

    @Override
    protected void failed(AccountData model) {
        zToastUtil.showSnack(view.getBindingView(), model.getFailExecuteMsg());
        view.setView();

    }

    @Override
    protected void connectFail() {
        view.setView();
    }

    @Override
    protected void throwError() {
        view.setView();
        zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.exception_toast));
    }
}
