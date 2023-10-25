package com.hihonor.adsdk.demo.external.picturetext;

import android.content.Intent;
import android.os.Bundle;

import com.hihonor.adsdk.demo.external.R;
import com.hihonor.adsdk.demo.external.common.BaseActivity;

public class PictureTextActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_picture_text_ad));

        setContentView(R.layout.activity_picture_text);

        Intent intent = new Intent();
        findViewById(R.id.picture_text_template_render).setOnClickListener(view -> {
            intent.setClass(PictureTextActivity.this, PictureTextTemplateRenderActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.picture_text_self_render).setOnClickListener(view -> {
            intent.setClass(PictureTextActivity.this, PictureTextSelfRenderActivity.class);
            startActivity(intent);
        });
    }
}
