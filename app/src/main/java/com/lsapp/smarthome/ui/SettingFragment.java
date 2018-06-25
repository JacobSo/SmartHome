package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/8/16.
 */
public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.preferences);
        zSharedPreferencesUtil.save(getActivity(), Const.SP, Const.SP_SETTING_UPDATE, Const.SP_Y);


        PreferenceScreen clear = (PreferenceScreen) findPreference("clear");
        PreferenceScreen guide = (PreferenceScreen) findPreference("guide");
        PreferenceScreen unbinding = (PreferenceScreen) findPreference("unbinding");


        clear.setOnPreferenceClickListener(preference -> {
            new Thread(() -> {
                Glide.get(getActivity()).clearDiskCache();
            }).start();
            Glide.get(getActivity()).clearMemory();
            zToastUtil.showSnack(getView(), getString(R.string.clear_success));
            return false;
        });

        guide.setOnPreferenceClickListener(preference -> {
            zSharedPreferencesUtil.clear(getActivity(), Const.GUIDE_SP);
            return false;
        });

        unbinding.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(),RecoveryListActivity.class));
            return false;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().finish();
        return true;
    }
}
