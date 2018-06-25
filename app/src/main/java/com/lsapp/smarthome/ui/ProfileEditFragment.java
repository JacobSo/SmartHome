package com.lsapp.smarthome.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.app.SignWay;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.databinding.DialogLocationSetBinding;
import com.lsapp.smarthome.event.ThirdPartyLoginEvent;
import com.lsapp.smarthome.ui.model.ProfileEditModel;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zToastUtil;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import rx.Subscription;

/**
 * Created by Administrator on 2016/8/16.
 */
public class ProfileEditFragment extends PreferenceFragment {
    private Account account;
    private List<Account> accountList;
    private String TAG = getClass().getName();
    private Calendar calendar = Calendar.getInstance();
    private AlertDialog alertDialog = null;

    private EditTextPreference mobile, name;
    private PreferenceScreen wechat, taobao, sina, birth, sex, location, password;
    private Subscription subscription;
    private ProfileEditModel model;

    public static ProfileEditFragment newInstance(Account account, List<Account> list) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("account", account);
        args.putSerializable("list", (Serializable) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //taobao binding
    public void initSubscription() {
        subscription = RxBus.getDefault().toObserverable(ThirdPartyLoginEvent.class).subscribe(thirdPartyLoginEvent -> {
            if (thirdPartyLoginEvent.getPlatform() == SignWay.TAOBAO.ordinal()) {
                model.bind(thirdPartyLoginEvent.getId(), thirdPartyLoginEvent.getName(), thirdPartyLoginEvent.getPlatform());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.profile);
        model = new ProfileEditModel(this);
        initSubscription();
        this.accountList = (List<Account>) getArguments().getSerializable("list");
        this.account = (Account) getArguments().getSerializable("account");
        mobile = (EditTextPreference) findPreference("mobile");
        wechat = (PreferenceScreen) findPreference("wechat");
        taobao = (PreferenceScreen) findPreference("taobao");
        sina = (PreferenceScreen) findPreference("sina");
        name = (EditTextPreference) findPreference("name");
        birth = (PreferenceScreen) findPreference("birth");
        sex = (PreferenceScreen) findPreference("sex");
        location = (PreferenceScreen) findPreference("location");
        password = (PreferenceScreen) findPreference("password");

        if (accountList != null) {
            for (Account a : accountList) {
                if (Integer.valueOf(a.getIsThirdParty()) == SignWay.TAOBAO.ordinal()) {
                    taobao.setSummary(a.getRealName());
                    model.setTaobao(true);
                    model.setTaobaoId(a.getUserName());
                } else if (Integer.valueOf(a.getIsThirdParty()) == SignWay.WECHAT.ordinal()) {
                    wechat.setSummary(a.getRealName());
                    model.setWechat(true);
                    model.setWechatId(a.getUserName());
                } else if (Integer.valueOf(a.getIsThirdParty()) == SignWay.WEIBO.ordinal()) {
                    sina.setSummary(a.getRealName());
                    model.setSina(true);
                    model.setSinaId(a.getUserName());
                }
            }
        }

        if (account.getIsThirdParty() == SignWay.WEIBO.ordinal()) {
            sina.setSummary(getString(R.string.this_account));
        } else if (account.getIsThirdParty() == SignWay.WECHAT.ordinal()) {
            wechat.setSummary(getString(R.string.this_account));
        } else if (account.getIsThirdParty() == SignWay.TAOBAO.ordinal()) {
            taobao.setSummary(getString(R.string.this_account));
        }

        if (account.getRealName() != null && !TextUtils.isEmpty(account.getRealName())) {
            name.setTitle(account.getRealName());
            name.setText(account.getRealName());

        }
        if (account.getMobile() != null && !TextUtils.isEmpty(account.getMobile())) {
            mobile.setTitle(account.getMobile());
            mobile.setText(account.getMobile());

        }

        if (account.getBirthday() != null && !TextUtils.isEmpty(account.getBirthday())) {
            birth.setTitle(zDateUtil.getFormatDate(account.getBirthday(), "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"));
        }

        if (account.getSex() != null && !TextUtils.isEmpty(account.getSex()))
            sex.setTitle(account.getSex());

        location.setTitle(account.getProvince() +
                getString(R.string.dash) + account.getCity() +
                getString(R.string.dash) + account.getArea() +
                getString(R.string.dash) + account.getAddress());


        password.setOnPreferenceClickListener(preference -> {
            if (account.getIsThirdParty() == SignWay.SMS.ordinal())
                model.passwordDialog();
            else
                zToastUtil.showSnack(getView(),getString(R.string.password_tips));
            return false;
        });

        taobao.setOnPreferenceClickListener(preference -> {
            if (taobao.getSummary() != null && taobao.getSummary().equals(getString(R.string.this_account))) {
                zToastUtil.showSnack(getView(), getString(R.string.this_account_remove_unable));
            } else {
                if (model.isTaobao()) {
                    model.unbindingDialog(model.getTaobaoId(), SignWay.TAOBAO.ordinal());
                } else
                    startActivity(new Intent(getActivity(), LoginTbActivity.class));
            }
            return false;
        });

        wechat.setOnPreferenceClickListener(preference -> {
            if (wechat.getSummary() != null && wechat.getSummary().equals(getString(R.string.this_account))) {
                zToastUtil.showSnack(getView(), getString(R.string.this_account_remove_unable));
            } else {
                if (model.isWechat()) {
                    model.unbindingDialog(model.getWechatId(), SignWay.WECHAT.ordinal());
                } else {
                    model.getWechatPlatform().setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            Log.d(TAG, "onComplete: ");
                            if (platform.isAuthValid()) {
                                model.bind(platform.getDb().getUserId(), platform.getDb().getUserName(), SignWay.WECHAT.ordinal());

                            }
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            Log.d(TAG, "onError: ");
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                            Log.d(TAG, "onCancel: ");
                        }
                    });
                    model.getWechatPlatform().authorize();
                }
            }
            return false;
        });

        sina.setOnPreferenceClickListener(preference -> {
            if (sina != null && sina.getSummary().equals(getString(R.string.this_account))) {
                zToastUtil.showSnack(getView(), getString(R.string.this_account_remove_unable));
            } else {
                if (model.isSina()) {
                    model.unbindingDialog(model.getSinaId(), SignWay.WEIBO.ordinal());
                } else {
                    model.getSinaPlatform().setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            Log.d(TAG, "onComplete: ");
                            if (platform.isAuthValid()) {
                                model.bind(platform.getDb().getUserId(), platform.getDb().getUserName(), SignWay.WEIBO.ordinal());
                            }
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            Log.d(TAG, "onError: ");
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                            Log.d(TAG, "onCancel: ");
                        }
                    });
                    model.getSinaPlatform().authorize();
                }
            }
            return false;
        });

        mobile.setOnPreferenceChangeListener((preference, o) -> {
            mobile.setTitle(mobile.getEditText().getText().toString());
            mobile.setText(mobile.getEditText().getText().toString());
            account.setMobile(mobile.getEditText().getText().toString());
            model.edit(account);
            return false;
        });

        name.setOnPreferenceChangeListener((preference, o) -> {
            name.setTitle(name.getEditText().getText().toString());
            name.setText(name.getEditText().getText().toString());
            account.setRealName(name.getEditText().getText().toString());
            model.edit(account);
            return false;
        });

        sex.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.sex));
            builder.setItems(new String[]{getString(R.string.male), getString(R.string.female)}, (dialogInterface, i) -> {
                sex.setTitle(i == 0 ? getString(R.string.male) : getString(R.string.female));
                account.setSex(i == 0 ? getString(R.string.male) : getString(R.string.female));
                model.edit(account);
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        birth.setOnPreferenceClickListener(preference -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
                birth.setTitle(+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                account.setBirthday(+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                model.edit(account);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            return true;
        });

        location.setOnPreferenceClickListener(preference -> {
            DialogLocationSetBinding locationSetBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_location_set, null, false);
            locationSetBinding.locationProvince.setText(BaseApplication.get(getActivity()).getProvince());
            locationSetBinding.locationCity.setText(BaseApplication.get(getActivity()).getCity());
            locationSetBinding.locationArea.setText(BaseApplication.get(getActivity()).getArea());
            locationSetBinding.locationAddress.setText(BaseApplication.get(getActivity()).getAddress());
            locationSetBinding.locationConfirm.setOnClickListener(view -> {
                account.setProvince(locationSetBinding.locationProvince.getText().toString());
                account.setCity(locationSetBinding.locationCity.getText().toString());
                account.setArea(locationSetBinding.locationArea.getText().toString());
                account.setAddress(locationSetBinding.locationAddress.getText().toString());
                location.setTitle(account.getProvince() + "-" + account.getCity() + "-" + account.getArea() + "-" + account.getAddress());
                model.edit(account);
                alertDialog.dismiss();
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(locationSetBinding.getRoot());

            alertDialog = builder.create();
            alertDialog.show();
            return true;
        });
        if (account.getIssetpwd() == 0&&account.getIsThirdParty()==SignWay.SMS.ordinal()) {
            new Handler().postDelayed(() -> {
                View view = getView();
                zToastUtil.showSnackIndefiniteWithButton(view, getString(R.string.unset_password), getString(R.string.setting_push), view1 -> model.passwordDialog());
            }, (300));
        }

    }

    public void setBindView(int platform, String name) {
        if (platform == SignWay.WECHAT.ordinal()) {
            wechat.setSummary(name);
        } else if (platform == SignWay.WEIBO.ordinal()) {
            sina.setSummary(name);
        } else if (platform == SignWay.TAOBAO.ordinal()) {
            taobao.setSummary(name);
        }
    }

    public void setUnbindView(int platform) {
        if (platform == SignWay.WECHAT.ordinal()) {
            wechat.setSummary(getString(R.string.not_binding));
            model.getWechatPlatform().removeAccount(true);
        } else if (platform == SignWay.WEIBO.ordinal()) {
            sina.setSummary(getString(R.string.not_binding));
            model.getSinaPlatform().removeAccount(true);

        } else if (platform == SignWay.TAOBAO.ordinal()) {
            taobao.setSummary(getString(R.string.not_binding));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().finish();
        return true;
    }

}
