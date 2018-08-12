package com.example.msp.lockenglish;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ScreenListener screenListener;
    private SharedPreferences sharedPreferences;
    private FragmentTransaction transaction;
    private  StudyFragment studyFragment;
    private SetFragment setFragment;
    private Button wrongBtn;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_layout);
        init();
    }

    private void init() {

        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
        wrongBtn = findViewById(R.id.wrong_btn);
        wrongBtn.setOnClickListener(this);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                if(sharedPreferences.getBoolean("btnTf",false)) {
                    if(sharedPreferences.getBoolean("btnTf",false)) {
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onScreenOff() {

                editor.putBoolean("tf",true);
                editor.commit();
                BaseApplication.destroyActivity("mainActivity");
            }

            @Override
            public void onUserPresent() {
                editor.putBoolean("tf",false);
                editor.commit();
            }
        });

        studyFragment = new StudyFragment();
        setFragment(studyFragment);

    }

    public void setFragment(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();
    }

    public void study(View view) {
        if(studyFragment == null) {
            studyFragment = new StudyFragment();
        }

        setFragment(studyFragment);
    }

    public void set(View view) {
        if(setFragment == null) {
            setFragment = new SetFragment();
        }

        setFragment(setFragment);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.wrong_btn:
                Toast.makeText(this,"跳转到错题界面",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenListener.unregisterListener();
    }
}
