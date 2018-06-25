package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.zuni.library.utils.zSharedPreferencesUtil;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

/**
 * Created by Administrator on 2017/1/6.
 */

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableLastSlideAlphaExitTransition(true);
        hideBackButton();
        getBackButtonTranslationWrapper().setEnterTranslation(View::setAlpha);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent_light)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.intro_1)
                .title(getString(R.string.intro_title_1))
                .description(getString(R.string.intro_content_1))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent_light)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.intro_2)
                .title(getString(R.string.intro_title_2))
                .description(getString(R.string.intro_content_2))
                .build());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorAccent_light)
                        .buttonsColor(R.color.colorPrimary)
                        .image(R.drawable.intro_3)
                        .title(getString(R.string.intro_title_3))
                        .description(getString(R.string.intro_content_3))
                        .build(),
                new MessageButtonBehaviour(v -> {
                    finish();
                    startActivity(new Intent(this,MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    onFinish();
                }, getString(R.string.intro_begin)));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        zSharedPreferencesUtil.save(IntroActivity.this, Const.GUIDE_SP,Const.GUIDE_INTRO,"1");
    }

}
