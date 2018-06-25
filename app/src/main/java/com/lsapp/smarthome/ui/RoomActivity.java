package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityRecyclerViewBinding;
import com.lsapp.smarthome.listener.OnRecycleViewItemClickListener;
import com.lsapp.smarthome.ui.adapter.RoomAdapter;
import com.lsapp.smarthome.ui.model.RoomModel;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 2016/11/2.
 */
public class RoomActivity extends BaseActivity implements OnRecycleViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivityRecyclerViewBinding binding;
    private RoomModel model;
    private SparseArray<Boolean> checkMap;
    private RoomAdapter adapter;
    private Handler guiderHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            guideUtil.createToolbar(Const.GUIDE_ROOM_ADD, binding.toolbar,R.id.action_add,
                    getString(R.string.guide_room_title), getString(R.string.guide_room_content));
        }
    };

    @Override
    protected void initModel() {
        model = new RoomModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        setTitle(binding.toolbar, getString(R.string.scene_manage), true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.swipeContainer.setOnRefreshListener(this);
        binding.toolbar.inflateMenu(R.menu.menu_add);

    }

    @Override
    public void onItemClick(View v, int position) {
        model.setScene(model.getSceneList().get(position));
        if (v.getTag() == null) {
            if (!checkMap.get(position)) {
                for (int i = 0; i < model.getSceneList().size(); i++) {
                    checkMap.put(i, false);
                }
            }
            checkMap.put(position, !checkMap.get(position));

            adapter.notifyDataSetChanged();
        } else {
            if (v.getTag().equals("modify")) {
                model.modifyDialog();
            } else if (v.getTag().equals("delete")) {
                model.deleteDialog();
            }
        }
    }

    public void setView() {
        binding.swipeContainer.setRefreshing(false);

        if (model.getSceneList() == null || model.getSceneList().size() == 0) {
         //   model.addSceneDialog();
            viewVisibleAnim(binding.emptyView, View.VISIBLE);
            binding.emptyBtn.setText(getString(R.string.scene_add));
            binding.recyclerView.setAdapter(null);
        } else {
            viewVisibleAnim(binding.emptyView, View.GONE);
            checkMap = new SparseArray<>();
            for (int i = 0; i < model.getSceneList().size(); i++) {
                checkMap.put(i, false);
            }
            adapter = new RoomAdapter(model.getSceneList(), checkMap, this);
            binding.recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

            guiderHandler.postDelayed(() -> guiderHandler.sendEmptyMessage(0), 1000);
        }

    }

    public void onEmpty(View v) {
        model.addSceneDialog();
    }

    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(true);
        model.get();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        else model.addSceneDialog();
        return true;
    }

}
