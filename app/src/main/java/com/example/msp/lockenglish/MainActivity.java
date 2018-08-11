package com.example.msp.lockenglish;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

    private TextView timeText,dateText,wordText,endlishText;
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
        endlishText = findViewById(R.id.english_text);
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
}
