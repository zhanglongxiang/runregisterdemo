package com.example.zhanglongxiang.runregisterdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

import java.util.logging.LogRecord;


import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by zhanglongxiang on 15/12/10.
 */
public class Gif_Activity extends ActionBarActivity{

    private GifImageView network_gifimageview = null;

    private ProgressDialog dialog;
    private AsyncHttpClient asyncHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//调用setDisplayHomeAsUpEnabled() 来把 app icon 设置成可用的向上按钮：

        network_gifimageview = (GifImageView) findViewById(R.id.gifView);

        dialog = ProgressDialog.show(Gif_Activity.this, "加载网络gif图片", "加载中...");
        try{
            GifDrawable gifDrawable = new GifDrawable(getResources(),R.drawable.ji);
            network_gifimageview.setBackgroundDrawable(gifDrawable);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
        网络连接 gif图片
         */
//        asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.get("http://rayspace.cn/upload/mi.gif", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                GifDrawable drawable = null;
//                try {
//                    drawable = new GifDrawable(bytes);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                network_gifimageview.setBackgroundDrawable(drawable);
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//                Toast.makeText(getApplicationContext(), "加载网络图片出错", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//
//            }
//        });
    }
}
