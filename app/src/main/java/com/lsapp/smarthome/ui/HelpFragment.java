package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.lsapp.smarthome.app.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityWebBinding;

/**
 * Created by Administrator on 2016/8/16.
 */
public class HelpFragment extends BaseFragment {
    private final static String TAG = "HelpFragment";
    private ActivityWebBinding binding;

    @Override
    protected void initModel() {

    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_web, container, false);
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
                if (url.contains("pdf")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                } else binding.webView.loadUrl(url);
                return true;
            }
        });

        binding.webView.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (binding.webView != null && i == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
                    binding.webView.goBack();
                } else {
                    getActivity().finish();
                }
                return true;
            }
            return false;
        });
        binding.webView.loadUrl(Const.HELP_HREF);

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (binding.webView != null && binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            getActivity().finish();
        }
        return true;
    }


}
