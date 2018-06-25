package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.databinding.ActivityPreferencesBinding;
import com.lsapp.smarthome.ui.model.PreferencesModel;
import com.zuni.library.utils.zSharedPreferencesUtil;

import java.util.List;



/**
 * 设置
 *
 * @author jacob
 */
public class PreferencesActivity extends BaseActivity {
    private ActivityPreferencesBinding binding;
    private PreferencesModel model;

    @Override
    protected void initModel() {
        model = new PreferencesModel(this);
    }
    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preferences);

        if (getIntent().hasExtra("account")) {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    ProfileEditFragment.newInstance((Account) getIntent().getSerializableExtra("account"),
                            (List<Account>) getIntent().getSerializableExtra("list"))).commit();
            setTitle(binding.toolbar, getString(R.string.setting_profile), true);
        } else if(getIntent().hasExtra("help")){
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
           setTitle(binding.toolbar, getString(R.string.setting_help), true);
          //  startActivity(new Intent(this,LicenseActivity.class));
        }else {
            model.getPushConfig();
        }
    }

    public void setPushView(){
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
        setTitle(binding.toolbar, getString(R.string.setting_push), true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (zSharedPreferencesUtil.get(this, Const.SP, Const.SP_SETTING_UPDATE).equals(Const.SP_Y)) {
            model.setPushConfig(zSharedPreferencesUtil.getSharedPreferences(this, Const.DEFAULT_SP).getBoolean(Const.SP_PUSH_WARM, true),
                    zSharedPreferencesUtil.getSharedPreferences(this, Const.DEFAULT_SP).getBoolean(Const.SP_PUSH_COMMON, true),
                    zSharedPreferencesUtil.getSharedPreferences(this, Const.DEFAULT_SP).getBoolean(Const.SP_PUSH_NEWS, true));
            zSharedPreferencesUtil.save(this,Const.SP,Const.SP_SETTING_UPDATE,Const.SP_N);
        }
    }

}
