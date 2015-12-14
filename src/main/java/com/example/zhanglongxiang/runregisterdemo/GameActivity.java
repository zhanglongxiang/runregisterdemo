package com.example.zhanglongxiang.runregisterdemo;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by zhanglongxiang on 15/12/10.
 */
public class GameActivity extends ActionBarActivity {
    private int row = 0;//行
    private int col = 0;//列
    private int width = 0;//imageview的宽
    private int height = 0;//imageview的高
    private int mis_count = 0;

    private int chosen_num = -1;//记录上一次选中的块数，但是一旦交换后需重新置-1
    private int []num_arr = null;//储存显示位置对应的new_bitmap中的位置
    private Bitmap[]pic_arr = null;//储存显示位置对应的new_bitmap中的图块
    private Bitmap src_bitmap = null;//原始位图
    private Bitmap new_bitmap = null;//分割之后的位图

    private ImageView img = null;//imageview控件
    private Button btn_start = null;//start按扭

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//调用setDisplayHomeAsUpEnabled() 来把 app icon 设置成可用的向上按钮：

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mm2);
        src_bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/5,bitmap.getHeight()/5,false);
        bitmap.recycle();

        img = (ImageView)findViewById(R.id.img_game);
        img.setImageBitmap(src_bitmap);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new ButtonListener());
    }


    class ButtonListener implements View.OnClickListener{
        public void onClick(View v){
            initSourceBitmap();
        }
    }

    private void initSourceBitmap(){
        row = 2;
        col = 2;


        num_arr = randArray(row * col);
        mis_count = misCount(num_arr,row*col);
        pic_arr = new Bitmap[row * col];
        width = img.getWidth();
        height = img.getHeight();
        new_bitmap = centerCrop(src_bitmap,width,height);

        int tmp_height = new_bitmap.getHeight();
        int tmp_width = new_bitmap.getWidth();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                int pos = i * row + j;
                int src_row = num_arr[pos] / row;
                int src_col = num_arr[pos] % col;
                pic_arr[pos] = Bitmap.createBitmap(new_bitmap,src_col * (tmp_width / col),src_row * (tmp_height / row),tmp_width/col,tmp_height/row);
            }
        }

        Canvas to_draw = new Canvas(new_bitmap);
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++){
                to_draw.drawBitmap(pic_arr[i * row + j], j * (tmp_width / col), i * (tmp_height / row),null);
            }
        to_draw.save(Canvas.ALL_SAVE_FLAG);
        to_draw.restore();
        img.setImageBitmap(new_bitmap);
        img.setOnTouchListener(new ImageListener());

    }

    private int misCount(int[]arr,int len){
        int tmp = 0;
        for(int i = 0; i < len; i++){
            if(arr[i] != i)
                tmp++;
        }
        return tmp;
    }

    /*
    生成的arr数组里面存储的是随机数，
     */
    private int[] randArray(int len){
        int [] arr = new int[len];
        boolean []status = new boolean[len];
        Random random = new Random();
        int c = 0;
        while(c < len){
            int tmp = random.nextInt(len);
            if(status[tmp] != true){
                arr[c] = tmp;
                status[tmp] = true;
                c++;
            }
        }
        return arr;
    }

    /*
    分割图片
     */
    private Bitmap centerCrop(Bitmap src,int W,int H){
        int w = src.getWidth();
        int h = src.getHeight();
        float ratio = (float)W/H;
        Bitmap dst = null;
        if((float)(w/h) > ratio){
            dst = Bitmap.createBitmap(src_bitmap,(int)((w - ratio*h)/2),0,(int)(ratio*h),h);
        }else{
            dst = Bitmap.createBitmap(src_bitmap,0,(int)((h - w/ratio)/2),w,(int)(w/ratio));
        }
        return dst;
    }

    class ImageListener implements View.OnTouchListener{
        public boolean onTouch(View arg0,MotionEvent arg1) {
            float x = arg1.getX();
            float y = arg1.getY();

            int tmp_col = (int) (x / (width / col));
            int tmp_row = (int) (y / (height / row));

            int new_chosen_num = tmp_row * col + tmp_col;

            if ((chosen_num != -1) && (new_chosen_num != chosen_num)) {
                swapBlock(chosen_num, new_chosen_num);
                chosen_num = -1;

                if (mis_count == 0) {
                    Toast.makeText(GameActivity.this, "拼对了!", Toast.LENGTH_SHORT).show();
                }
            } else {
                chosen_num = new_chosen_num;
            }
            return false;
        }
    }

    private void swapBlock(int a,int b){
        if(a == b) return;
        int x = 0;
        int y = 0;
        if(num_arr[a] == a)
            x++;
        if(num_arr[b] == b)
            x++;
        if(a == num_arr[b])
            y++;
        if(b == num_arr[a])
            y++;
        mis_count -= (y - x);

        //swap values
        int i_tmp = num_arr[a];
        num_arr[a] = num_arr[b];
        num_arr[b] = i_tmp;
        Bitmap b_tmp = Bitmap.createBitmap(pic_arr[a]);
        pic_arr[a] = Bitmap.createBitmap(pic_arr[b]);
        pic_arr[b] = Bitmap.createBitmap(b_tmp);
        b_tmp.recycle();
        //draw effect
        Canvas to_draw = new Canvas(new_bitmap);
        to_draw.drawBitmap(pic_arr[a],(a%row)*(new_bitmap.getWidth()/col),(a/row)*(new_bitmap.getHeight()/row),null);
        to_draw.drawBitmap(pic_arr[b],(b%row)*(new_bitmap.getWidth()/col),(b/row)*(new_bitmap.getHeight()/row),null);
        to_draw.save(Canvas.ALL_SAVE_FLAG);
        to_draw.restore();
        img.setImageBitmap(new_bitmap);
    }
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

