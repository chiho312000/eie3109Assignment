package com.example.eie3109assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eie3109assignment.models.Panel;

public class Part3Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_part3);
        setContentView(new Panel(this));
    }
}
