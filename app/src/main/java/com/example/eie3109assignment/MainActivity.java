package com.example.eie3109assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private final int PART1 = 1, PART2 = 2, PART3 = 3;
    ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(this);
        findViewById(R.id.btnPart1).setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pushToPart(PART1);
            }
        });
        findViewById(R.id.btnPart2).setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pushToPart(PART2);
            }
        });
        findViewById(R.id.btnPart3).setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pushToPart(PART3);
            }
        });

    }

    void pushToPart(int part)
    {
        Intent newIntent;
        switch (part)
        {
            case PART1:
                newIntent = new Intent(MainActivity.this, Part1Activity.class);
                break;
            case PART2:
                newIntent = new Intent(MainActivity.this, Part2Activity.class);
                break;
            case PART3:
                newIntent = new Intent(MainActivity.this, Part3Activity.class);
                break;
            default:
                return;
        }
        MainActivity.this.startActivity(newIntent);
    }
}
