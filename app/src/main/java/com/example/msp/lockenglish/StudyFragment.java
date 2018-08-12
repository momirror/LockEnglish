package com.example.msp.lockenglish;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assetsbasedata.AssetsDatabaseManager;
import com.example.msp.greendao.entity.greendao.DaoMaster;
import com.example.msp.greendao.entity.greendao.DaoSession;
import com.example.msp.greendao.entity.greendao.WisdomEntity;
import com.example.msp.greendao.entity.greendao.WisdomEntityDao;

import java.util.List;
import java.util.Random;

public class StudyFragment extends Fragment {
    private TextView difficultyTv,wisdomEnglish,wisdomChina,alreadyStudyText,alreadyMasteredText,wrongText;
    private SharedPreferences sharedPreferences;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private WisdomEntityDao questionDao;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_fragment_layout,null);
        sharedPreferences = getActivity().getSharedPreferences("share", Context.MODE_PRIVATE);

        difficultyTv = view.findViewById(R.id.difficulty_text);
        wisdomEnglish = view.findViewById(R.id.wisdom_english);
        wisdomChina = view.findViewById(R.id.wisdom_china);
        alreadyStudyText = view.findViewById(R.id.already_study);
        alreadyMasteredText = view.findViewById(R.id.already_mastered);
        wrongText = view.findViewById(R.id.wrong_text);

        AssetsDatabaseManager.initManager(getActivity());

        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db1 = mg.getDatabase("wisdom.db");
        mDaoMaster = new DaoMaster(db1);
        mDaoSession = mDaoMaster.newSession();
        questionDao = mDaoSession.getWisdomEntityDao();
        return  view;
    }

    public void  onStart() {
        super.onStart();

        difficultyTv.setText(sharedPreferences.getString("difficulty","四级")+"英语");
        List<WisdomEntity> datas = questionDao.queryBuilder().list();
        Random random = new Random();
        int i = random.nextInt(10);
        wisdomEnglish.setText(datas.get(i).getEnglish());
        wisdomChina.setText(datas.get(i).getChina());
        setText();
    }


    private void setText() {
        alreadyMasteredText.setText(sharedPreferences.getInt("alreadyMastered",0)+"");
        alreadyMasteredText.setText(sharedPreferences.getInt("alreadStudy",0)+"");
        wrongText.setText(sharedPreferences.getInt("wrong",0)+"");
    }
}
