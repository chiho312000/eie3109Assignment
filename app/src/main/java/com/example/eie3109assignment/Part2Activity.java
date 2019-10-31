package com.example.eie3109assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eie3109assignment.models.CalculatorBrain;

public class Part2Activity extends AppCompatActivity
{

    private CalculatorBrain calculatorBrain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part2);
        calculatorBrain = new CalculatorBrain();
        calculatorBrain.setOperand(0.0);
    }
}
