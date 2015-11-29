package com.example.zhanglongxiang.runregisterdemo;

import android.app.Activity;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
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


public class MainActivity extends Activity {

    private Button startButton = null;
    private TextPage mResultText = null;
    private static TextView showText = null;
    private String showResult = null;
    private SpeechRecognizer iatRecognizer;
    private String engine = "iat";
    private String rate = "16000";
    private Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(this,SpeechConstant.APPID+"=563cafe2");

        showText = (TextView)findViewById(R.id.showText);

        //mResultText = (EditText)findViewById(R.id.editText);
        mResultText = (TextPage)findViewById(R.id.resultText);//获取textview
//        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/hwkt.ttf");//设置楷体
//        mResultText.setTypeface(face);
        startButton = (Button)findViewById(R.id.button_start);//获取按钮

//        String s1 = mResultText.getText().toString();
//        showText.setText(s1);

        //Typeface fontFace = Typeface.createFromAsset(getAssets(),"fonts/华文楷体.ttf");
        //mResultText.setTypeface(fontFace);

        //final char[] tc = {'h','e','l','l','o','w','o','r','l','d'};

        /*
        按钮监听器
         */
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mResultText.setText(tc,2,6);

                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(MainActivity.this,null);
                mIat.setParameter(SpeechConstant.DOMAIN,"iat");
                mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
                mIat.setParameter(SpeechConstant.ASR_PTT,"0");//不要句号
                mIat.startListening(mRecoListener);
            }
        });
//        /*
//        显示结果EditText 触摸监听器
//         */
//        mResultText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    Log.d("MainActivity","onTouchEvent_Action.Down");
//                }else if(event.getAction() == MotionEvent.ACTION_UP){
//                    Log.d("MainActivity","onTouchEvent_Action.UP");
//                }
//                Log.d("onTouchEvent","onTouchEvent_x:" + event.getX());
//                Log.d("onTouchEvent","onTouchEvent_y:" + event.getY());
//
//                return false;
//
//            }
//        });
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
            //String s = (String)text.subSequence(0,1);//只取第一个字符
            mResultText.append(text);
            //mResultText.setText(text);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
}
