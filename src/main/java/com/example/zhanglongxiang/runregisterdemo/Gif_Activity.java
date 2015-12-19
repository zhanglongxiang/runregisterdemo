package com.example.zhanglongxiang.runregisterdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Button stopButton = null;
    private Button playButton = null;
    private Button resetButton = null;

    private ProgressDialog dialog;
    private AsyncHttpClient asyncHttpClient;
    private String data;//存放intent传来的数据
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gif_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//调用setDisplayHomeAsUpEnabled() 来把 app icon 设置成可用的向上按钮：

        network_gifimageview = (GifImageView) findViewById(R.id.gifView);
        stopButton = (Button)findViewById(R.id.stop_button);
        playButton = (Button)findViewById(R.id.play_button);
        resetButton = (Button)findViewById(R.id.reset_button);


        /*
        把汉字键值对写进xml文件中，这个工作需要提前做好，现在只是实验所以写在这里
         */
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("蜜","http://rayspace.cn/upload/mi.gif");
        editor.putString("季","http://rayspace.cn/upload/ji.gif");
        editor.commit();

        /*
        获取intent传来的数据
         */

        Intent intent = getIntent();
        data = intent.getStringExtra("extra_data");//获取到第1个Activity传来的数据



        /*
        本地资源gif图片的加载
         */
//        try{
//            GifDrawable gifDrawable = new GifDrawable(getResources(),R.drawable.ji);
//            network_gifimageview.setBackgroundDrawable(gifDrawable);
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }




        /*
        网络连接 gif图片
         */
        dialog = ProgressDialog.show(Gif_Activity.this, "加载网络gif图片", "加载中...");

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String url = pref.getString(data,"");//从xml文件中获取相关gif的url

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    gifDrawable = new GifDrawable(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                network_gifimageview.setBackgroundDrawable(gifDrawable);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(getApplicationContext(), "加载网络图片出错", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


        /*
        按钮事件监听器
         */

        stopButton.setOnClickListener(new View.OnClickListener() {//停止按钮监听器
            @Override
            public void onClick(View view) {
                gifDrawable.stop();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {//播放按钮监听器
            @Override
            public void onClick(View view) {
                gifDrawable.start();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {//重置按钮监听器
            @Override
            public void onClick(View view) {
                gifDrawable.reset();
            }
        });
    }
}
