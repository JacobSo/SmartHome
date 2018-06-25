package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.data.RecoveryDeviceData;
import com.lsapp.smarthome.data.SceneData;
import com.lsapp.smarthome.data.base.RecoveryDevice;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.DialogRecoveryBinding;
import com.lsapp.smarthome.databinding.DialogSceneAddBinding;
import com.lsapp.smarthome.ui.RecoveryListActivity;
import com.lsapp.smarthome.utils.MacUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.List;

import rx.functions.Action1;


/**
 * Created by Administrator on 2017/1/12.
 */

public class RecoveryModel extends BaseModel<RecoveryDeviceData> {
    private RecoveryListActivity view;
    private List<RecoveryDevice> list;
    private List<Scene> scenes = null;

    public RecoveryModel(RecoveryListActivity context) {
        super(context);
        view = context;
    }

    public List<RecoveryDevice> getList() {
        return list;
    }

    public void get() {
        getUnbindingList(this::action);
    }

    public void getScene(int position) {
       // if (BaseApplication.get(context).getPermission().equals("0")) {
/*        if(scenes.get(position).getIsAllowOperat()==0){
            zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.no_permission));
            return;
        }*/
        if (scenes != null) {
            recoveryDialog(position);
        } else {
            view.loadingDialog(context.getString(R.string.loading));
            getSceneModify(sceneData -> {
                view.dismissDialog();
                if (sceneData.isSuccess()) {
                    scenes = sceneData.getList();
                    recoveryDialog(position);
                } else
                    zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.get_scene_failed));
            });
        }
    }

    private void recoveryDialog(int position) {
        DialogRecoveryBinding dialogBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_recovery, null, false);
        dialogBinding.setTitle(list.get(position).getTypeNamePure()+"-"+ list.get(position).getItemName());
        String[] array = new String[scenes.size()];
        for (int i = 0; i < scenes.size(); i++) {
            array[i] = scenes.get(i).getSpaceName();
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, R.layout.text_view, array);
        adapter.setDropDownViewResource(R.layout.text_view);
        dialogBinding.spinner.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        dialogBinding.sceneDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
        dialogBinding.sceneConfirm.setOnClickListener(v -> {
            bind(position, dialogBinding.spinner.getSelectedItemPosition());
            alertDialog.dismiss();
        });
    }

    public void bind(int devicePos, int scenePos) {
        addDevice(scenes.get(scenePos).getUserId(),scenes.get(scenePos).getSpaceId(), list.get(devicePos).getMac(), scenes.get(scenePos).getSpaceName(), 1, baseData -> {
            if (baseData.isSuccess()) {
                zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.success_binding));
                view.onRefresh();
            } else {
                zToastUtil.showSnack(view.getBindingView(), baseData.getFailExecuteMsg());
            }
        });
    }

    @Override
    protected void success(RecoveryDeviceData model) {
        list = model.getList();
        view.setView();
    }

    @Override
    protected void failed(RecoveryDeviceData model) {
        view.setView();
    }

    @Override
    protected void connectFail() {
        view.setView();
    }

    @Override
    protected void throwError() {
        view.setView();
    }
}
