package com.example.eie3109assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.eie3109assignment.models.Panel;

public class Part3Activity extends AppCompatActivity
{
    private Panel panel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        panel = new Panel(this);
        setContentView(panel);
    }

    @Override
    protected void onDestroy()
    {
        if (panel.getThread() != null)
        {
            panel.getThread().setRunning(false);
            Log.v("part3", "activity destroyed");
        }
        super.onDestroy();
    }
}
