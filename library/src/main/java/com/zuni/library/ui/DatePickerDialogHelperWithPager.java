package com.zuni.library.ui;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zuni.library.R;
import com.zuni.library.databinding.DialogPickerLayoutBinding;
import com.zuni.library.listener.OnTimeSelectListener;

/**
 * Created by Administrator on 2015/11/30.
 */
public class DatePickerDialogHelperWithPager {
    DialogPickerLayoutBinding dialogBinding;
    DialogPlus dialog;
    OnTimeSelectListener timeSelectListener;
    int tabCount = 2;

    public DatePickerDialogHelperWithPager(FragmentActivity activity, Fragment context, boolean isFull) {
        dialogBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.dialog_picker_layout, null, false);
        dialogBinding.tabLayout.addTab(dialogBinding.tabLayout.newTab().setText("年"));
        dialogBinding.tabLayout.addTab(dialogBinding.tabLayout.newTab().setText("月"));
        if (isFull) {
            dialogBinding.tabLayout.addTab(dialogBinding.tabLayout.newTab().setText("日"));
            dialogBinding.tabLayout.addTab(dialogBinding.tabLayout.newTab().setText("区间"));
            tabCount = 4;
        }
        dialogBinding.mPager.setAdapter(new FragmentAdapter(context.getFragmentManager()));

        dialogBinding.mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(dialogBinding.tabLayout));
        timeSelectListener = (OnTimeSelectListener) context;
        dialogBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dialogBinding.mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        dialog = DialogPlus.newDialog(activity)
                .setGravity(Gravity.TOP)
                .setCancelable(true)
                .setContentHolder(new ViewHolder(dialogBinding.getRoot()))
                .setExpanded(false)
                .create();
    }

    private DialogPlus getDialog() {
        return dialog;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        private FragmentManager fm;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("" + position);
            return PickerFragment.newInstance(position, timeSelectListener);
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            String fragmentTag = fragment.getTag();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            fragment = PickerFragment.newInstance(position, timeSelectListener);
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();
            return fragment;
        }
    }
}
