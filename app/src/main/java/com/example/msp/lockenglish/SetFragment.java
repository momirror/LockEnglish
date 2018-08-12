package com.example.msp.lockenglish;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SetFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private  SwitchButton switchButton;
    private Spinner spinnerDifficulty;
    private Spinner spinnerAllNum;
    private Spinner spinnerNewNum;
    private Spinner spinnerReviseNum;

    private ArrayAdapter<String> adapterDifficulty, adapterAllNum, adapterNewNum, adapterReviseNum;

    String [] difficulty = new String[] {"小学","初中","高中","四级","六级"};
    String [] allNum = new String[] {"2道","4道","6道","8道"};
    String [] newNum = new String[] {"10","30","50","100"};
    String [] reviseNum = new String[] {"10","30","50","100"};

    SharedPreferences.Editor editor = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.set_fragment_layout,null);
        init(view);
        return view;
    }

    public void onStart() {
        super.onStart();

        if(sharedPreferences.getBoolean("btnTf",false)){
            switchButton.openSwitch();
        } else {
            switchButton.closeSwitch();
        }
    }

    private void  init (View view) {
        sharedPreferences = getActivity().getSharedPreferences("share", Context.MODE_PRIVATE);

        switchButton = view.findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(this);

        spinnerDifficulty = view.findViewById(R.id.spinner_difficulty);
        spinnerAllNum = view.findViewById(R.id.spinner_all_number);
        spinnerNewNum = view.findViewById(R.id.spinner_new_number);
        spinnerReviseNum = view.findViewById(R.id.spinner_revise_number);

        //难度
        adapterDifficulty = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item,difficulty);
        spinnerDifficulty.setAdapter(adapterDifficulty);
        setSpinnerItemSelectedByValue(spinnerDifficulty,sharedPreferences.getString("diffiulty","四级"));

        this.spinnerDifficulty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String msg = adapterView.getItemAtPosition(position).toString();
                editor.putString("difficulty",msg);
                editor.commit();
            }
        });

//        //解锁个数
        adapterAllNum = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item,allNum);
        spinnerAllNum.setAdapter(adapterAllNum);
        setSpinnerItemSelectedByValue(spinnerAllNum,sharedPreferences.getInt("allNum",2)+"道");

        this.spinnerAllNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String msg = adapterView.getItemAtPosition(position).toString();
                int i = Integer.parseInt(msg.substring(0, 1));
                editor.putInt("allNum", i);
                editor.commit();
            }

            });


        //每日个数

        adapterNewNum = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item,newNum);
        spinnerNewNum.setAdapter(adapterNewNum);
        setSpinnerItemSelectedByValue(spinnerNewNum,sharedPreferences.getString("newNum","10"));

        this.spinnerNewNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String msg = adapterView.getItemAtPosition(position).toString();
                editor.putString("newNum",msg);
                editor.commit();
            }

        });

        //复习个数

        adapterReviseNum = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item,reviseNum);
        spinnerReviseNum.setAdapter(adapterReviseNum);
        setSpinnerItemSelectedByValue(spinnerReviseNum,sharedPreferences.getString("reviseNum","10"));

        this.spinnerNewNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String msg = adapterView.getItemAtPosition(position).toString();
                editor.putString("reviseNum",msg);
                editor.commit();
            }

        });




    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.switch_btn:
                if(switchButton.isSwitchOpen()) {
                    switchButton.closeSwitch();
                    editor.putBoolean("btnTf",false);
                } else {
                    switchButton.openSwitch();
                    editor.putBoolean("btnTf",true);
                }

                editor.commit();
                break;
        }

    }

    public void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
            }
        }
    }
}
