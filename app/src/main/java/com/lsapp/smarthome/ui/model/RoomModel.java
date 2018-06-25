package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.SceneData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.DialogSceneAddBinding;
import com.lsapp.smarthome.databinding.DialogSceneModifyBinding;
import com.lsapp.smarthome.ui.RoomActivity;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/11/2.
 */
public class RoomModel extends BaseModel<SceneData> {
    private RoomActivity view;
    private List<Scene> sceneList = null;
    private Scene scene;

    public List<Scene> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<Scene> sceneList) {
        this.sceneList = sceneList;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public RoomModel(RoomActivity context) {
        super(context);
        view = context;
    }

    public void get() {
        //   view.dismissDialog();
        getSceneModify(this::action);
    }

    private void add(String name, int type) {
        addScene(name, type, sceneData -> {
            //view.dismissDialog();
            if (sceneData.isSuccess()) {
                zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.add_success));

                if (sceneList == null)
                    sceneList = new ArrayList<>();
                sceneList.add(sceneData.getList().get(0));
                view.onRefresh();
            } else if (sceneData.getFailExecuteMsg() != null)
                zToastUtil.showSnack(view.getBindingView(),sceneData.getFailExecuteMsg());
        });
    }

    private void delete() {
        deleteScene(scene.getSpaceId(), baseData -> {
            ///   view.dismissDialog();
            if (baseData.isSuccess()) {
                zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.message_delete_success));
                view.onRefresh();
            //    BaseApplication.get(context).setSelectPosition(0);
            } else {
                if (baseData.getFailExecuteMsg() != null)
                    zToastUtil.showSnack(view.getBindingView(),baseData.getFailExecuteMsg());
            }
        });
    }


    private void modify(String name, int type) {
        updateScene(scene.getSpaceId(), name, type, baseData -> {
            ///    view.dismissDialog();
            if (baseData.isSuccess()) {
                zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.modify_success));
                //RxBus.getDefault().post(new CommonEvent());
                view.onRefresh();
            } else {
                if (baseData.getFailExecuteMsg() != null)
                    zToastUtil.showSnack(view.getBindingView(),baseData.getSuccessExecuteMsg());
            }
        });
    }

    public void modifyDialog() {
       // if(BaseApplication.get(context).getPermission().equals("0")){
/*        if(getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.no_permission));
            return;
        }*/
        DialogSceneModifyBinding dialogBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_scene_modify, null, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.scene_list, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        dialogBinding.sceneNameSpinner.setAdapter(adapter);
        dialogBinding.sceneName.setText(scene.getSpaceName());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogBinding.confirm.setOnClickListener(view1 -> {
            String name = dialogBinding.sceneName.getText().toString();
            if (TextUtils.isEmpty(name))
                dialogBinding.sceneName.setError(context.getString(R.string.info_null_toast));
            else {
                modify(name, dialogBinding.sceneNameSpinner.getSelectedItemPosition());
                alertDialog.dismiss();
            }
        });
        dialogBinding.dismiss.setOnClickListener(view1 -> alertDialog.dismiss());
    }

    public void addSceneDialog() {
  /*      if(BaseApplication.get(context).getPermission().equals("0")){
            zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.no_permission));
            return;
        }*/
        DialogSceneAddBinding dialogBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_scene_add, null, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.scene_list, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        dialogBinding.sceneNameSpinner.setAdapter(adapter);
        dialogBinding.sceneNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dialogBinding.sceneName.setText(context.getResources().getTextArray(R.array.scene_list)[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogBinding.sceneDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
        dialogBinding.sceneConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(dialogBinding.sceneName.getText().toString()))
                dialogBinding.sceneName.setError(context.getString(R.string.info_null_toast));
            else {
                add(dialogBinding.sceneName.getText().toString(), dialogBinding.sceneNameSpinner.getSelectedItemPosition());
                alertDialog.dismiss();
                //  view.loadingDialog();
            }
        });
    }

    public void deleteDialog() {
      //  if(BaseApplication.get(context).getPermission().equals("0")){
/*        if(getScene().getIsAllowOperat()==0){
            zToastUtil.showSnack(view.getBindingView(),context.getString(R.string.no_permission));
            return;
        }*/
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.delete_confirm_scene));
        builder.setPositiveButton(context.getString(R.string.confirm), (dialogInterface, i) -> {
            // view.loadingDialog();
            delete();
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.create().show();
    }

    @Override
    protected void success(SceneData model) {
        sceneList = model.getList();
        view.setView();
    }

    @Override
    protected void failed(SceneData model) {
        view.setView();
        zToastUtil.showSnack(view.getBindingView(),model.getFailExecuteMsg());
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
