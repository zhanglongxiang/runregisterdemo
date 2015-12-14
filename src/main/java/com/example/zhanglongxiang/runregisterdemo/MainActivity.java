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
    private TextPage mResultText = null;
    private static TextView showText = null;
    private String showResult = null;
    private SpeechRecognizer iatRecognizer;
    private String engine = "iat";
    private String rate = "16000";
    private Toast mToast;

    private GifImageView giv = null;//声明自定义控件对象
    protected static final String GIF_URL = "http://rayspace.cn/upload/mi.gif";//gif图片地址

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
        mResultText = (TextPage)findViewById(R.id.resultText);//获取textview
        startButton = (Button)findViewById(R.id.button_start);//获取开始说话按钮
        showButton = (Button)findViewById(R.id.button_show);//获取显示笔画按钮
        clearButton = (Button)findViewById(R.id.button_clear);//获取清屏按钮
        gameButton = (Button)findViewById(R.id.button_game);

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
                Intent intent = new Intent(MainActivity.this,Gif_Activity.class);//跳转到Gif界面
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    自定义控件内部类
     */
    public static class TextPage extends EditText {

        private int off;
        private String result;


        public TextPage(Context context) {
            super(context);
            initialize();
        }

        public TextPage(Context context, AttributeSet paramAttributeSet) {
            super(context, paramAttributeSet);
            initialize();
        }

        private void initialize() {
            setGravity(Gravity.TOP);
            setBackgroundColor(Color.WHITE);
        }

        protected void onCreateContextMenu(ContextMenu menu) {
            //不做任何处理、为了阻止长按的时候弹出上下文菜单
        }

        protected MovementMethod getDefaultMovementMethod() { //这个方法什么意思？
            return null;
        }

        public boolean getDefaultEditable() {
            return false;
        }

        public boolean onTouchEvent(MotionEvent event) {//触屏事件
            int action = event.getAction();
            Layout layout = getLayout();
            int line = 0;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    off = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), off);
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int curOff = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), off, curOff);
                    CharSequence sel = getEditableText().toString().subSequence(off, curOff);
                    result = (String) sel;
                    Log.d("MainActivity", result);
                    showText.setText(result);
                    break;
            }
            return true;
        }
    }

    public void jumpToLayout2(){
        setContentView(R.layout.mylayout);//显示这个布局
        giv = (GifImageView)findViewById(R.id.gifView);//找到自定义组件GifImageView
        giv.setImageResource(R.drawable.ji);//给组件设置gif图片资源
    }
}
