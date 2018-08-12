package com.example.msp.lockenglish;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assetsbasedata.AssetsDatabaseManager;
import com.example.msp.greendao.entity.greendao.CET4Entity;
import com.example.msp.greendao.entity.greendao.CET4EntityDao;
import com.example.msp.greendao.entity.greendao.DaoMaster;
import com.example.msp.greendao.entity.greendao.DaoSession;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, SynthesizerListener {

    private TextView timeText,dateText,wordText, englishText;
    private ImageView playVoice;
    private String mMonth,mDay,mWay,mHours,mMinute;
    private SpeechSynthesizer speechSynthesizer;

    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;
    private RadioGroup radioGroup;
    private RadioButton radioOne,radioTwo,radioThree;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor = null;
    int j = 0;
    List<Integer> list;
    List<CET4Entity> datas;
    int k;

    float x1 = 0,y1 = 0, x2 = 0, y2 = 0;

    private SQLiteDatabase db;
    private DaoMaster mDaoMaster,dbMaster;
    private DaoSession mDaoSession,dbSession;
    private CET4EntityDao questionDao,dbDao;


//    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setShowWhenLocked(true);
//        setTurnScreenOn(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        list = new ArrayList<Integer>();
        Random r = new Random();
        int i ;
        while (list.size() < 10) {
            i = r.nextInt(20);
            if(!list.contains(i)) {
                list.add(i);
            }
        }


      Log.d ("test",list.get(9).toString());

        km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");
        AssetsDatabaseManager.initManager(this);
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();

        SQLiteDatabase db1 = mg.getDatabase("word.db");
        mDaoMaster = new DaoMaster(db1);
        mDaoSession = mDaoMaster.newSession();
        questionDao = mDaoSession.getCET4EntityDao();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"wrong.db",null);


        db = helper.getWritableDatabase();
        dbMaster = new DaoMaster(db);
        dbSession = dbMaster.newSession();
        dbDao = dbSession.getCET4EntityDao();

        timeText = findViewById(R.id.time_text);
        dateText = findViewById(R.id.date_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        playVoice = findViewById(R.id.play_voice);
        playVoice.setOnClickListener(this);
        radioGroup = findViewById(R.id.choose_group);
        radioOne = findViewById(R.id.choose_btn_one);
        radioTwo = findViewById(R.id.choose_btn_two);
        radioThree = findViewById(R.id.choose_btn_three);
        radioGroup.setOnCheckedChangeListener(this);
        setParam();
        SpeechUser.getUser().login(MainActivity.this,null,null,"appid=573a7bf0",listener);

    }

    protected void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();
        mMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+1);
        mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

        if(calendar.get(Calendar.HOUR) < 10) {
            mHours = "0"+calendar.get(Calendar.HOUR);
        } else {
            mHours = String.valueOf(calendar.get(Calendar.HOUR));
        }

        if(calendar.get(Calendar.MINUTE) < 10) {
            mMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            mMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        if("1".equals(mWay)) {
            mWay = "天";
        } else if("2".equals(mWay)) {
            mWay = "一";
        }else if("3".equals(mWay)) {
            mWay = "二";
        }else if("4".equals(mWay)) {
            mWay = "三";
        }else if("5".equals(mWay)) {
            mWay = "四";
        }else if("6".equals(mWay)) {
            mWay = "五";
        }else if("7".equals(mWay)) {
            mWay = "六";
        }

        timeText.setText(mHours+":"+mMinute);
        dateText.setText(mMonth+"月"+mDay+"日"+" "+"日期"+mWay);
//        getDBData(); //这里会因为缺少音频库文件而挂掉
    }

    private void saveWrongData() {
        String word = datas.get(k).getWord();
        String english = datas.get(k).getEnglish();
        String china = datas.get(k).getChina();
        String sign = datas.get(k).getSign();
        CET4Entity data = new CET4Entity(Long.valueOf(dbDao.count()),word,english,china,sign);
        dbDao.insertOrReplace(data);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play_voice:
                String text = wordText.getText().toString();
                speechSynthesizer.startSpeaking(text,this);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        radioGroup.setClickable(false);
        switch (i) {
            case R.id.choose_btn_one:
                String msg = radioOne.getText().toString().substring(3);
                Log.d("test",radioOne.getText().toString());
                btnGetText(msg,radioOne);
                break;
            case R.id.choose_btn_two:
                String msg1 = radioTwo.getText().toString().substring(3);
                btnGetText(msg1,radioTwo);
                break;
            case R.id.choose_btn_three:
                String msg2 = radioThree.getText().toString().substring(3);
                btnGetText(msg2,radioThree);
                break;
        }
    }

    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {


    }

    private SpeechListener listener = new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onData(byte[] bytes) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }
    };

    public void setParam() {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        speechSynthesizer.setParameter(SpeechConstant.SPEED,"50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME,"50");
        speechSynthesizer.setParameter(SpeechConstant.PITCH,"50");

    }


    private void btnGetText(String msg,RadioButton btn) {

        Log.d("test","test");

        if(msg.equals(datas.get(k).getChina())) {

            wordText.setTextColor(Color.GREEN);
            englishText.setTextColor(Color.GREEN);
        }
        else {

            wordText.setTextColor(Color.RED);
            englishText.setTextColor(Color.RED);
            btn.setTextColor(Color.RED);
            saveWrongData();

            int wrong = sharedPreferences.getInt("wrong",0);
            editor.putInt("wrong",wrong+1);
            editor.putString("wrongId",","+datas.get(j).getId());
            editor.commit();


        }
    }

    private void setTextColor() {
        radioOne.setChecked(false);
        radioTwo.setChecked(false);
        radioThree.setChecked(false);

        radioOne.setTextColor(Color.parseColor("#FFFFFF"));
        radioTwo.setTextColor(Color.parseColor("#FFFFFF"));
        radioThree.setTextColor(Color.parseColor("#FFFFFF"));
        wordText.setTextColor(Color.parseColor("#FFFFFF"));
        englishText.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void unlock() {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent1);
        kl.disableKeyguard();
        finish();
    }

    /**
     * 设置选项
     */
    private void setChina(List<CET4Entity> datas, int j) {
        /**
         * 随机产生几个随机数， 这里面产生几个随机数  是用于解锁单词
         * 因为此demo输入数据库里面20个单词， 所以产生的随机数是20以内的
         * */
        Random r = new Random();
        List<Integer> listInt = new ArrayList<>();
        int i;
        while (listInt.size() < 4) {
            i = r.nextInt(20);
            if (!listInt.contains(i)) {
                listInt.add(i);
            }
        }
        /**
         * 以下的判断是给这个单词设置三个选项，设置单词选项是有规律的
         *三个选项，分别是正确的、正确的前一个、正确的的后一个
         *将这三个解释设置到单词的选项上，以下为实现逻辑
         * */
        if (listInt.get(0) < 7) {
            radioOne.setText("A: " + datas.get(k).getChina());
            if (k - 1 >= 0) {
                radioTwo.setText("B: " + datas.get(k - 1).getChina());
            } else {
                radioTwo.setText("B: " + datas.get(k + 2).getChina());
            }
            if (k + 1 < 20) {
                radioThree.setText("C: " + datas.get(k + 1).getChina());
            } else {
                radioThree.setText("C: " + datas.get(k - 1).getChina());
            }
        } else if (listInt.get(0) < 14) {
            radioTwo.setText("B: " + datas.get(k).getChina());
            if (k - 1 >= 0) {
                radioOne.setText("A: " + datas.get(k - 1).getChina());
            } else {
                radioOne.setText("A: " + datas.get(k + 2).getChina());
            }
            if (k + 1 < 20) {
                radioThree.setText("C: " + datas.get(k + 1).getChina());
            } else {
                radioThree.setText("C: " + datas.get(k - 1).getChina());
            }
        } else {
            radioThree.setText("C: " + datas.get(k).getChina());
            if (k - 1 >= 0) {
                radioTwo.setText("B: " + datas.get(k - 1).getChina());
            } else {
                radioTwo.setText("B: " + datas.get(k + 2).getChina());
            }
            if (k + 1 < 20) {
                radioOne.setText("A: " + datas.get(k + 1).getChina());
            } else {
                radioOne.setText("A: " + datas.get(k - 1).getChina());
            }
        }

    }

    private void getDBData() {
        datas = questionDao.queryBuilder().list();
        k = list.get(j);
        wordText.setText(datas.get(k).getWord());
        englishText.setText(datas.get(k).getEnglish());
        setChina(datas,k);
    }


    private void getNextData() {
        j++;
        int i = sharedPreferences.getInt("allNum",2);
        if(i > j) {
            getDBData();
            setTextColor();
            int num = sharedPreferences.getInt("alreadyStudy",0)+1;
            editor.putInt("alreadyStudy",num);
            editor.commit();
        } else {
            unlock();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();

            if(y1 - y2 > 200) {
                int num = sharedPreferences.getInt("alreadyMastered",0)+1;
                editor.putInt("alreadyMastered",num);
                editor.commit();
            }
            Toast.makeText(this,"已掌握",Toast.LENGTH_SHORT).show();
            getNextData();
        } else if (y2 - y1 > 200) {
            Toast.makeText(this,"待加功能",Toast.LENGTH_SHORT).show();
        } else if (x1 - x2 > 200) {
            getNextData();
        } else if(x2 - x1 > 200) {
            unlock();
        }

        return  super.onTouchEvent(event);
    }

}
