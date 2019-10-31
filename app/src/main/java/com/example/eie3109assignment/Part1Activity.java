package com.example.eie3109assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Part1Activity extends AppCompatActivity
{
    private static final String TAG = "Part1Activity";

    private Button btnBigger, btnSmaller, btnBack;
    private boolean btnBiggerLongPressed = false, btnSmallerLongPressed = false;
    private ImageView imgTop;

    private static final int scaleFactor = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);
        btnBigger = findViewById(R.id.part1BtnBigger);
        btnSmaller = findViewById(R.id.part1BtnSmaller);
        btnBack = findViewById(R.id.part1BtnBack);

        imgTop = findViewById(R.id.part1ImgToChange);

        initBtnBigger();
        initBtnSmaller();
        initBtnBack();
    }

    void initBtnBigger()
    {
        btnBigger.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeImg(true);
            }
        });

        btnBigger.setOnLongClickListener(new Button.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                btnBiggerLongPressed = true;
                return true;
            }
        });

        btnBigger.setOnTouchListener(new Button.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (btnBiggerLongPressed)
                {
                    changeImg(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) btnBiggerLongPressed = false;
                }
                return false;
            }
        });

    }

    void initBtnSmaller()
    {
        btnSmaller.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeImg(false);
            }
        });

        btnSmaller.setOnLongClickListener(new Button.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                btnSmallerLongPressed = true;
                return true;
            }
        });

        btnSmaller.setOnTouchListener(new Button.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (btnSmallerLongPressed)
                {
                    changeImg(false);
                    if (event.getAction() == MotionEvent.ACTION_UP) btnSmallerLongPressed = false;
                }
                return false;
            }
        });
    }

    void initBtnBack()
    {
        btnBack.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Part1Activity.super.finish();
            }
        });
    }

    void changeImg(boolean isEnlarge)
    {
        ViewGroup.LayoutParams params = imgTop.getLayoutParams();
        params.width += isEnlarge ? scaleFactor : -scaleFactor;
        params.height += isEnlarge ? scaleFactor : -scaleFactor;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (params.width > size.x || params.height > size.y) return;
        imgTop.setLayoutParams(params);
    }
}
