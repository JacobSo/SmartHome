package com.lsapp.smarthome.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.zuni.library.utils.zSharedPreferencesUtil;

/**
 * Created by Administrator on 2016/12/20.
 */

public class GuideUtil {
    private Activity activity;
    private String[] mainGuide =
            new String[]{"guide_main_quick", "guide_main_slide", "guide_main_panel", "guide_main_panel_sub", "guide_main_weather"};

    public GuideUtil(Activity activity) {
        this.activity = activity;
    }

    public void create(View view, String title, String content, TapTargetView.Listener listener) {
        TapTargetView.showFor(activity,
                TapTarget.forView(view, title, content)
                        .outerCircleColor(R.color.colorPrimaryDark)
                        .targetCircleColor(R.color.colorAccent)
                        .dimColor(R.color.colorPrimaryDark)
                        .targetRadius(60)
                        .titleTextSize(25)
                        .descriptionTextSize(16)
                        .textColor(R.color.white)
                        .drawShadow(false)
                        .cancelable(true)
                        .tintTarget(false)
                        .transparentTarget(false),
                listener);

    }

    public void create(String tag, View view, String title, String content, TapTargetView.Listener listener) {
        if (!isBeenShowed(tag)) {
            TapTargetView.showFor(activity,
                    TapTarget.forView(view, title, content)
                            .outerCircleColor(R.color.colorPrimaryDark)
                            .targetCircleColor(R.color.colorAccent)
                            .dimColor(R.color.colorPrimaryDark)
                            .targetRadius(60)
                            .titleTextSize(25)
                            .descriptionTextSize(16)
                            .textColor(R.color.white)
                            .drawShadow(false)
                            .cancelable(true)
                            .tintTarget(false)
                            .transparentTarget(false),
                    listener);
            setShowed(tag);

        }
    }

    public void createMain(String tag, View view, String title, String content, TapTargetView.Listener listener) {
        if (!isMainGuideShow(tag)) {
            TapTargetView.showFor(activity,
                    TapTarget.forView(view, title, content)
                            .outerCircleColor(R.color.colorPrimaryDark)
                            .targetCircleColor(R.color.colorAccent)
                            .dimColor(R.color.colorPrimaryDark)
                            .targetRadius(60)
                            .titleTextSize(25)
                            .descriptionTextSize(16)
                            .textColor(R.color.white)
                            .drawShadow(true)
                            .cancelable(true)
                            .tintTarget(false)
                            .transparentTarget(false),
                    listener);
            setShowed(tag);

        }

    }

    public void createToolbar(String tag, Toolbar toolbar, int itemRes, String title, String content) {
        if (!isBeenShowed(tag)) {
            TapTargetSequence sequence = new TapTargetSequence(activity)
                    .targets(TapTarget.forToolbarMenuItem(toolbar, itemRes, title, content)
                            .outerCircleColor(R.color.colorPrimaryDark)
                            .targetCircleColor(R.color.colorAccent)
                            .dimColor(R.color.colorPrimaryDark)
                            .targetRadius(60)
                            .titleTextSize(25)
                            .descriptionTextSize(16)
                            .textColor(R.color.white)
                            .drawShadow(true)
                            .cancelable(true)
                            .tintTarget(false)
                            .transparentTarget(false)
                            .id(1));
            sequence.start();
            setShowed(tag);

        }
    }

    public void createToolbarHide(String tag, Toolbar toolbar, String title, String content) {
        if (!isBeenShowed(tag)) {
            TapTargetSequence sequence = new TapTargetSequence(activity)
                    .targets(TapTarget.forToolbarOverflow(toolbar, title, content).id(3)
                            .outerCircleColor(R.color.colorPrimaryDark)
                            .targetCircleColor(R.color.colorAccent)
                            .dimColor(R.color.colorPrimaryDark)
                            .targetRadius(60)
                            .titleTextSize(25)
                            .descriptionTextSize(16)
                            .textColor(R.color.white)
                            .drawShadow(true)
                            .cancelable(true)
                            .tintTarget(false)
                            .transparentTarget(false)
                            .id(1));
            sequence.start();
            setShowed(tag);
        }
    }

    public void createRect(Activity activity, Rect view, String title, String content, TapTargetView.Listener listener) {
        TapTargetView.showFor(activity,
                TapTarget.forBounds(view, title, content)
                        .outerCircleColor(R.color.colorPrimaryDark)
                        .targetCircleColor(R.color.colorAccent)
                        .dimColor(R.color.colorPrimaryDark)
                        .targetRadius(60)
                        .titleTextSize(25)
                        .descriptionTextSize(16)
                        .textColor(R.color.white)
                        .drawShadow(true)
                        .cancelable(true)
                        .tintTarget(false)
                        .transparentTarget(false),
                listener);


    }


    public void createMuilti(Activity activity, View view1, View view2) {
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(view1, "Gonna", ""),
                        TapTarget.forView(view2, "You", "Up")
                                .dimColor(R.color.colorAccent)
                                .outerCircleColor(R.color.black_semi_transparent)
                                .targetCircleColor(R.color.white)
                                .textColor(R.color.colorAccent))

                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }


                }).start();
    }

    private boolean isBeenShowed(String tag) {
        return zSharedPreferencesUtil.get(activity, Const.GUIDE_SP, tag).equals("1");
    }

    private boolean isMainGuideShow(String tag) {
        if (zSharedPreferencesUtil.get(activity, Const.GUIDE_SP, tag).equals("1")) {//显示过，不显示
            return true;
        } else {//未显示
            if (mainGuide[0].equals(tag)) {//起点，直接显示
                return false;
            }

            if (mainGuide[mainGuide.length - 1].equals(tag)) {
                if (zSharedPreferencesUtil.get(activity, Const.GUIDE_SP, mainGuide[mainGuide.length - 2]).equals("1")) {//终点的上一位显示过，显示终点
                    return false;
                }
            }

            for (int i = 0; i < mainGuide.length; i++) {
                if (mainGuide[i].equals(tag)) {
                    //上一位已经显示过，下一位未显示
                    return !(zSharedPreferencesUtil.get(activity, Const.GUIDE_SP, mainGuide[i - 1]).equals("1")
                            && !zSharedPreferencesUtil.get(activity, Const.GUIDE_SP, mainGuide[i + 1]).equals("1"));
                }
            }
        }
        return true;//默认不显示返回true
    }

    private void setShowed(String tag) {
        zSharedPreferencesUtil.save(activity, Const.GUIDE_SP, tag, "1");
    }
}
