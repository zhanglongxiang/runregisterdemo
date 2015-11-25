package com.example.zhanglongxiang.runregisterdemo;

import android.app.Activity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class MainActivity extends Activity {

    private Button startButton = null;
    //private EditText mResultText = null;
    private TextView mResultText = null;
    private SpeechRecognizer iatRecognizer;
    private String engine = "iat";
    private String rate = "16000";
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechUtility.createUtility(this,SpeechConstant.APPID+"=563cafe2");

        //mResultText = (EditText)findViewById(R.id.editText);
        mResultText = (TextView)findViewById(R.id.showText);
        startButton = (Button)findViewById(R.id.button_start);

        //Typeface fontFace = Typeface.createFromAsset(getAssets(),"fonts/华文楷体.ttf");
        //mResultText.setTypeface(fontFace);

        //final char[] tc = {'h','e','l','l','o','w','o','r','l','d'};

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mResultText.setText(tc,2,6);

                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(MainActivity.this,null);
                mIat.setParameter(SpeechConstant.DOMAIN,"iat");
                mIat.setParameter(SpeechConstant.LANGUAGE,"zh_cn");

                mIat.startListening(mRecoListener);
            }
        });
    }




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
            String s = (String)text.subSequence(0,1);//只取第一个字符

            mResultText.setText(s);
            //mResultText.setSelection(mResultText.length());
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
}
