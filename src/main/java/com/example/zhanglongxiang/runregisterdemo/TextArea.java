package com.example.zhanglongxiang.runregisterdemo;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zhanglongxiang on 15/12/16.
 *
 */

/*
自定义控件，显示语音识别结果，重写onTouchEvent方法
 */
public class TextArea extends EditText {
    private int off;
    private String result;
    private TextView textView;

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    public String getResult() {
        return result;
    }

    public TextArea(Context context) {
        super(context);
        initialize();
    }

    public TextArea(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
        initialize();
    }

    private void initialize() {
        setGravity(Gravity.TOP);
        //setBackgroundColor(Color.WHITE);
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
                textView.setText(result);
                break;
        }
        return true;
    }
}
