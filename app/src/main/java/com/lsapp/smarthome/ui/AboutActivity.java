package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityAboutBinding;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zOtherUtil;


/**
 * Created by Administrator on 2016/9/13.
 */
public class AboutActivity extends BaseActivity {
    private ActivityAboutBinding binding;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        setTitle(binding.toolbar, "关于", true);
        binding.setVersion(zContextUtil.getAppVersionName(this));
        binding.webView.getSettings().setJavaScriptEnabled(true);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //loadingDialog();
                binding.webView.loadUrl(url);
                return true;
            }
        });
        binding.webView.loadUrl("file:///android_asset/license_notice.html");
    }

    public void onDetail(View v) {
        binding.companyDetail.setVisibility(binding.companyDetail.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        ViewCompat.animate(binding.companyAngle).rotation(binding.companyDetail.getVisibility() == View.VISIBLE ? 180 : 0).start();
    }

    public void onLicense(View v) {
        binding.webView.setVisibility(binding.webView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        ViewCompat.animate(binding.licenseAngle).rotation(binding.webView.getVisibility() == View.VISIBLE ? 180 : 0).start();
    }

    public void onUpdate(View v){
        checkUpdate(true);
    }

    public void onFeedback(View v){
        startActivity(new Intent(this,CommonWebActivity.class).putExtra("title",getString(R.string.about_feedback)).putExtra("url",Const.FEEDBACK_HREF+
                "SessionKey="+ BaseApplication.get(this).getSessionKey()+"&UserId="+BaseApplication.get(this).getUid()));
    }

    public void onWebsite(View v){
        zOtherUtil.openURLByBrowser(Const.WEBSITE,this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}
