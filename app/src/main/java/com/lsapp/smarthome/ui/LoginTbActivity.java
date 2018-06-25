package com.lsapp.smarthome.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.app.SignWay;
import com.lsapp.smarthome.databinding.ActivityWebBinding;
import com.lsapp.smarthome.event.ThirdPartyLoginEvent;
import com.lsapp.smarthome.utils.EncodeUtil;
import com.lsapp.smarthome.utils.UrlUtil;

import java.util.Map;


/**
 * Created by Administrator on 2016/8/4.
 */
public class LoginTbActivity extends BaseActivity {
    private final static String TAG = "LoginTbActivity";
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

        binding.webView.getSettings().setJavaScriptEnabled(true);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar2.setVisibility(View.GONE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //loadingDialog();
                if (url.contains("oauth2")) {
                    Map<String, String> map = UrlUtil.URLRequest(url.replaceFirst("#", "&"));
                    try {
                        String sign = EncodeUtil.Signing(map, "7b717af92600931ac09221beb2ed94ea", "MD5");//"HmacMD5"
                        Log.d(TAG, "shouldOverrideUrlLoading: sign=" + sign);
                        RxBus.getDefault().post(new ThirdPartyLoginEvent(SignWay.TAOBAO.ordinal(), map.get("taobao_user_nick"), map.get("taobao_open_uid"),false));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    view.loadUrl(url);
                }
                Log.d(TAG, "shouldOverrideUrlLoading: response" + url);
                return true;
            }
        });
        binding.webView.loadUrl("https://oauth.taobao.com/authorize?response_type=token&client_id=23576835&view=wap");

    }

}
