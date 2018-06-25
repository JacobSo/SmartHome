package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.SignWay;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.base.Account;
import com.lsapp.smarthome.databinding.FragmentProfileBinding;
import com.lsapp.smarthome.ui.model.ProfileModel;
import com.zuni.library.ui.CircularAnim;
import com.zuni.library.utils.zToastUtil;;import java.io.Serializable;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/8/9.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private FragmentProfileBinding binding;
    private ProfileModel model;
    private boolean isEdit = false;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void initModel() {
        model = new ProfileModel(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEdit) {
            model.info();
            isEdit = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().finish();
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.profileLogout.setOnClickListener(this);
        binding.profileSetting.setOnClickListener(this);
        binding.profileEdit.setOnClickListener(this);
        binding.profileQuickSet.setOnClickListener(this);
        binding.profileMember.setOnClickListener(this);
        binding.profileRoomSet.setOnClickListener(this);
        binding.profileHelp.setOnClickListener(this);
        binding.profileAbout.setOnClickListener(this);
        model.info();
        return binding.getRoot();
    }

    public void setViewLogout() {
        BaseApplication.get(getActivity()).unbindCloudAccount();
        CircularAnim.fullActivity(getActivity(), binding.getRoot())
                .colorOrImageRes(R.color.colorPrimary)
                .go(() -> {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                });
        new Wechat().removeAccount(true);
        new SinaWeibo().removeAccount(true);
    }

    public void setFailLogout() {
        binding.profileLogout.setEnabled(true);
        binding.profileLogout.setText(getString(R.string.exit));
        binding.profileLogout.setBackgroundResource(R.color.float_btn);
    }

    public void setView(AccountData model) {
        if (model.getList().get(0).getRealName() != null && !TextUtils.isEmpty(model.getList().get(0).getRealName()))
            binding.profileName.setText(model.getList().get(0).getRealName());
        else binding.profileName.setText(getString(R.string.no_name));

        if(model.getList().get(0).getIssetpwd()==0&&model.getList().get(0).getIsThirdParty()==SignWay.SMS.ordinal()) binding.textView3.setText(getString(R.string.unset_password));

        if (model.getList() != null && model.getList().size() != 0) {
            for (Account a : model.getList()) {
                if (Integer.valueOf(a.getIsThirdParty()) == SignWay.TAOBAO.ordinal()) {
                    binding.profileTb.setVisibility(View.VISIBLE);
                } else if (Integer.valueOf(a.getIsThirdParty()) == SignWay.WECHAT.ordinal()) {
                    binding.profileWc.setVisibility(View.VISIBLE);
                    String img = new Wechat().getDb().getUserIcon();
                    if (img != null && !img.equals(""))
                        Glide.with(getActivity()).load(img).into(binding.userImage);
                } else if (Integer.valueOf(a.getIsThirdParty()) == SignWay.WEIBO.ordinal()) {
                    binding.profileWb.setVisibility(View.VISIBLE);
                    String img = new SinaWeibo().getDb().getUserIcon();
                    if (img != null && !img.equals(""))
                        Glide.with(getActivity()).load(img).into(binding.userImage);
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view == binding.profileLogout) {
            zToastUtil.showSnackWithButton(binding.getRoot(), getString(R.string.ask_for_exit),getString(R.string.confirm), v -> {
                model.logout();
                binding.profileLogout.setText(getString(R.string.loading_exit));
                binding.profileLogout.setEnabled(false);
                binding.profileLogout.setBackgroundResource(R.color.secondary_text);
                BaseApplication.get(getActivity()).setSelectPosition(0);
            });
        } else if (view == binding.profileSetting) {
            startActivity(new Intent(getActivity(), PreferencesActivity.class));
        } else if (view == binding.profileEdit) {
            if (model.getAccount() != null) {
                startActivity(new Intent(getActivity(), PreferencesActivity.class)
                        .putExtra("list", (Serializable) model.getAccountList())
                        .putExtra("account", model.getAccount()));
                isEdit = true;
                //
            }
        } else if (view == binding.profileMember) {
            startActivity(new Intent(getActivity(), MemberActivity.class));
        } else if (view == binding.profileRoomSet) {
            startActivity(new Intent(getActivity(), RoomActivity.class));
        } else if (view == binding.profileHelp) {
            startActivity(new Intent(getActivity(), PreferencesActivity.class)
                    .putExtra("help", ""));
        } else if (view == binding.profileQuickSet) {
            startActivity(new Intent(getActivity(), GuideActivity.class));
        } else {
            startActivity(new Intent(getActivity(), AboutActivity.class));
        }
    }
}
