package com.example.zhanglongxiang.runregisterdemo;

import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.Selection;
import android.text.StaticLayout;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends ActionBarActivity{

    private Button startButton = null;
    private Button showButton = null;
    private Button clearButton = null;
    private Button gameButton = null;
    private TextArea mResultText = null;
    private TextView showText = null;
    private String showResult = null;
    private SpeechRecognizer iatRecognizer;
    private String engine = "iat";
    private String rate = "16000";
    private Toast mToast;


    private GifImageView giv = null;//声明自定义控件对象
    protected static final String GIF_URL = "http://rayspace.cn/upload/mi.gif";//gif图片地址

    private String showChar = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(this,SpeechConstant.APPID+"=563cafe2");//语音识别初始化
        initComponent();
  }

    private void initComponent()
    {
        showText = (TextView)findViewById(R.id.showText);
        mResultText = (TextArea)findViewById(R.id.resultText);//获取textview
        startButton = (Button)findViewById(R.id.button_start);//获取开始说话按钮
        showButton = (Button)findViewById(R.id.button_show);//获取显示笔画按钮
        clearButton = (Button)findViewById(R.id.button_clear);//获取清屏按钮
        gameButton = (Button)findViewById(R.id.button_game);//获取游戏按钮

        mResultText.setTextView(showText);//把显示大字的textview控件对象传进识别结果自定义控件中，为显示大字做准备

        /*
        开始说话按钮监听器
         */
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(MainActivity.this,null);
                mIat.setParameter(SpeechConstant.DOMAIN,"iat");
                mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
                mIat.setParameter(SpeechConstant.ASR_PTT,"0");//不要句号
                mIat.startListening(mRecoListener);
            }
        });
        /*
        显示笔画按钮监听器
         */
        showButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //jumpToLayout2();
                String data = mResultText.getResult();//获得选中的字符，做为数据将来传到第2个Activity
                Intent intent = new Intent(MainActivity.this,Gif_Activity.class);//跳转到Gif界面
                intent.putExtra("extra_data",data);
                startActivity(intent);
            }
        });
        /*
        清除内容按钮监听器
         */
        clearButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                mResultText.setText("");
            }
        });

        /*
        拼图游戏按钮监听器
         */
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);//跳转到game界面
                startActivity(intent);
            }
        });
    }
   /*
   讯飞语音合成
    */

    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }
        @Override
        public void onBeginOfSpeech() {
            mToast.makeText(getApplicationContext(),"开始说话",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onEndOfSpeech() {
            mToast.makeText(getApplicationContext(),"结束说话",Toast.LENGTH_SHORT).show();

        }
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
            mResultText.append(text);
            mResultText.setSelection(mResultText.length());
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i2, int i3, Bundle bundle) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(1,Menu.FIRST,1,"Quit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                super.finish();
                System.exit(0);
                return true;
            default:
                return false;
        }
        //return super.onOptionsItemSelected(item);
    }

    private void getChar(String result){
        this.showChar = result;
    }



    public void jumpToLayout2(){
        setContentView(R.layout.mylayout);//显示这个布局
        giv = (GifImageView)findViewById(R.id.gifView);//找到自定义组件GifImageView
        giv.setImageResource(R.drawable.ji);//给组件设置gif图片资源
    }

}
