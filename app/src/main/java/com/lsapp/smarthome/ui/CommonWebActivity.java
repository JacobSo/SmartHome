package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.databinding.ActivityWebBinding;
import com.zuni.library.utils.zToastUtil;

/**
 * Created by Administrator on 2016/8/16.
 */
public class CommonWebActivity extends BaseActivity {
    private final static String TAG = "CommonWebActivity";
    private ActivityWebBinding binding;
    @Override
    protected void initModel() {

    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        binding.appbar.setVisibility(View.VISIBLE);
        setTitle(binding.toolbar,getIntent().getStringExtra("title"),true);

        binding.webView.clearCache(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: response" + url);
                if (url.contains("google")) {
                    zToastUtil.show(CommonWebActivity.this,getString(R.string.add_success));
                    finish();
                } else binding.webView.loadUrl(url);
                return true;
            }
        });

        binding.webView.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (binding.webView != null && i == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
                    binding.webView.goBack();
                } else {
                   finish();
                }
                return true;
            }
            return false;
        });
        Log.d(TAG,getIntent().getStringExtra("url"));
        binding.webView.loadUrl(getIntent().getStringExtra("url"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (binding.webView != null && binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            finish();
        }
        return true;
    }


}
