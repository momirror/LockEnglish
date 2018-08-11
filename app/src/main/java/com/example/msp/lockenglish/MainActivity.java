package com.example.msp.lockenglish;

import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.msp.greendao.entity.greendao.CET4Entity;
import com.example.msp.greendao.entity.greendao.CET4EntityDao;
import com.example.msp.greendao.entity.greendao.DaoMaster;
import com.example.msp.greendao.entity.greendao.DaoSession;
import com.iflytek.cloud.speech.SpeechSynthesizer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
