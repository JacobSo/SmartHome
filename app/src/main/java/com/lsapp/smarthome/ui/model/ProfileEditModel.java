package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.SignWay;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.databinding.EditViewBinding;
import com.lsapp.smarthome.ui.ProfileEditFragment;
import com.zuni.library.utils.zToastUtil;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ProfileEditModel extends BaseModel<BaseData>{
    private ProfileEditFragment view;
    private boolean isTaobao = false;
    private boolean isSina = false;
    private boolean isWechat = false;
    private Platform wechatPlatform;
    private Platform sinaPlatform;
    private String taobaoId,wechatId,sinaId;

    public void setTaobao(boolean taobao) {
        isTaobao = taobao;
    }

    public void setSina(boolean sina) {
        isSina = sina;
    }

    public void setWechat(boolean wechat) {
        isWechat = wechat;
    }

    public boolean isTaobao() {
        return isTaobao;
    }

    public boolean isSina() {
        return isSina;
    }

    public boolean isWechat() {
        return isWechat;
    }

    public Platform getWechatPlatform() {
        return wechatPlatform;
    }

    public Platform getSinaPlatform() {
        return sinaPlatform;
    }

    public String getTaobaoId() {
        return taobaoId;
    }

    public void setTaobaoId(String taobaoId) {
        this.taobaoId = taobaoId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public ProfileEditModel(ProfileEditFragment context) {
        super(context.getActivity());
        view  = context;
        wechatPlatform = new Wechat();
        wechatPlatform.SSOSetting(false);
        sinaPlatform = new SinaWeibo();
        sinaPlatform.SSOSetting(false);
    }

    public void edit(Account account){
        updateUserInfo(account.getRealName(), account.getMobile(),account.getBirthday(), account.getSex(), account.getProvince(),
                account.getCity(), account.getArea(), account.getAddress(), baseData -> {
            if(!baseData.isSuccess()){
                zToastUtil.show(context,context.getString(R.string.update_info_failed)+baseData.getFailExecuteMsg());
            }
        });
    }

    public void bind(String uid,String name,int platform){
        bindAccount(uid, name,platform, baseData -> {
            if(baseData.isSuccess()){
                zToastUtil.show(context,context.getString(R.string.success_binding));
                setFlag(platform,true);
                view.setBindView(platform,baseData.getSuccessExecuteMsg());
            }else{
                zToastUtil.show(context,context.getString(R.string.binding_failed_port)+baseData.getFailExecuteMsg());


            }
        });
    }

    private void unbind(String uid,int platform){
        unbindAccount(uid, platform, baseData -> {
            if(baseData.isSuccess()){
                zToastUtil.show(context,context.getString(R.string.unbinding_account_success));
                setFlag(platform,false);
                view.setUnbindView(platform);
            }else{
                zToastUtil.show(context,context.getString(R.string.unbinding_account_failed_port)+baseData.getFailExecuteMsg());
            }
        });
    }
    public void unbindingDialog(String user, int platform) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.unbinding_account_content));
        builder.setPositiveButton(context.getString(R.string.unbinding), (dialogInterface, i) -> {
            unbind(user, platform);

        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.create().show();
    }
    private void setFlag(int platform,boolean isBind){
        if(platform== SignWay.WECHAT.ordinal()){
            isWechat = isBind;
        }else if(platform == SignWay.WEIBO.ordinal()){
            isSina = isBind;
        }else if(platform == SignWay.TAOBAO.ordinal()){
            isTaobao = isBind;
        }
    }

    public void passwordDialog(){
        EditViewBinding binding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.edit_view, null, false);
        binding.editText.setHint(context.getString(R.string.password_first));
        binding.editText2.setVisibility(View.VISIBLE);
        binding.editText2.setHint(context.getString(R.string.password_second));
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.set_pwd_login));
        dialog.setView(binding.getRoot());
        dialog.setPositiveButton(context.getString(R.string.confirm), (dialogInterface, i) -> {
            if (TextUtils.isEmpty(binding.editText.getText().toString())||TextUtils.isEmpty(binding.editText2.getText().toString())) {
                zToastUtil.show(context,R.string.password_null_toast);
            } else if(binding.editText.getText().toString().length()<5||binding.editText2.getText().toString().length()<5){
                zToastUtil.show(context,R.string.password_least);
            }else if(!binding.editText.getText().toString().equals(binding.editText2.getText().toString())){
                zToastUtil.show(context,R.string.password_double_check);
            }else modify(binding.editText.getText().toString());
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), null);
        dialog.show();
    }

    private void modify(String pwd){
        passwordSet(pwd, this::action);
    }

    @Override
    protected void success(BaseData model) {
        zToastUtil.show(context,model.getSuccessExecuteMsg());
    }

    @Override
    protected void failed(BaseData model) {
        zToastUtil.show(context,model.getFailExecuteMsg());
    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {

    }
}
