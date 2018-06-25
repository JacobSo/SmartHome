package com.lsapp.smarthome.ui;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.databinding.DialogLoadingBinding;
import com.lsapp.smarthome.utils.GuideUtil;

/**
 * Created by Administrator on 2016/8/9.
 */
public abstract class BaseFragment extends Fragment {
    protected Dialog dialog;
    protected abstract void initModel();
    public abstract View getBindingView();
    protected GuideUtil guideUtil;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guideUtil = new GuideUtil(getActivity());
        initModel();
    }


    public void loadingDialog(String text) {
        DialogLoadingBinding loadingBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_loading, null, false);
        //   Dialog dialog = new Dialog(this,R.style.selectorDialog);
        loadingBinding.textView.setText(text);
        dialog = new android.app.AlertDialog.Builder(getActivity()).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(loadingBinding.getRoot());
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void viewVisibleAnim(View v, int visible) {
        Animation animation;
        if(visible==View.VISIBLE){
            if(v.getVisibility()==View.GONE||v.getVisibility()==View.INVISIBLE){
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_center);
                v.setVisibility(View.VISIBLE);
                v.startAnimation(animation);
            }

        }else{
            if(v.getVisibility()==View.VISIBLE) {
                animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_center);
                v.setVisibility(View.GONE);
                v.startAnimation(animation);
            }
        }

    }
}
