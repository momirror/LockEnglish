package com.example.msp.lockenglish;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.jar.Attributes;

public class SwitchButton extends FrameLayout {

    private ImageView openImage;
    private ImageView closeImage;

    public SwitchButton(Context context) {
        this(context,null);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    public SwitchButton(Context context,AttributeSet attrs) {
        super(context,attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.SwitchButton);
        Drawable openDrawable = typedArray.getDrawable(R.styleable.SwitchButton_switchOpenImage);
        Drawable closeDrawable = typedArray.getDrawable(R.styleable.SwitchButton_switchCloseImage);
        int switchStatus = typedArray.getInt(R.styleable.SwitchButton_switchStatus,0);

        typedArray.recycle();
        LayoutInflater.from(context).inflate(R.layout.switch_button,this);
        openImage = findViewById(R.id.iv_switch_open);
        closeImage = findViewById(R.id.iv_switch_close);

        if(openDrawable != null) {
            openImage.setImageDrawable(openDrawable);
        }

        if(closeDrawable != null) {
            openImage.setImageDrawable(closeDrawable);
        }

        if(switchStatus == 1) {
            closeSwitch();
        }

    }

    public boolean isSwitchOpen() {
        return openImage.getVisibility() == View.VISIBLE;
    }

    public void openSwitch() {
        openImage.setVisibility(View.VISIBLE);
        closeImage.setVisibility(View.INVISIBLE);
    }

    public void closeSwitch() {
        openImage.setVisibility(View.INVISIBLE);
        closeImage.setVisibility(View.VISIBLE);
    }


}
