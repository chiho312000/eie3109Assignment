package com.example.eie3109assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.eie3109assignment.models.CalculatorBrain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Part2Activity extends AppCompatActivity
{
    private CalculatorBrain calculatorBrain;
    private TextView monitor;
    private boolean userStillTyping = false;
    DecimalFormat hashes = new DecimalFormat("#.####");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part2);
        calculatorBrain = new CalculatorBrain();
        calculatorBrain.setOperand(0.0);

        monitor = findViewById(R.id.part2ResultView);

        ArrayList<Button> numBtns = new ArrayList<>(Arrays.asList(
                (Button) findViewById(R.id.part2Btn1),
                (Button) findViewById(R.id.part2Btn2),
                (Button) findViewById(R.id.part2Btn3),
                (Button) findViewById(R.id.part2Btn4),
                (Button) findViewById(R.id.part2Btn5),
                (Button) findViewById(R.id.part2Btn6),
                (Button) findViewById(R.id.part2Btn7),
                (Button) findViewById(R.id.part2Btn8),
                (Button) findViewById(R.id.part2Btn9),
                (Button) findViewById(R.id.part2Btn0)));

        ArrayList<Button> functionBtns = new ArrayList<>(Arrays.asList(
                (Button) findViewById(R.id.part2BtnE),
                (Button) findViewById(R.id.part2BtnPi),
                (Button) findViewById(R.id.part2BtnEqual),
                (Button) findViewById(R.id.part2BtnAdd),
                (Button) findViewById(R.id.part2BtnSubtract),
                (Button) findViewById(R.id.part2BtnMultiply),
                (Button) findViewById(R.id.part2BtnDivide),
                (Button) findViewById(R.id.part2BtnSin),
                (Button) findViewById(R.id.part2BtnCos)));

        for (Button b : numBtns)
        {
            b.setOnClickListener(new NumbersBtnOnClickHandler());
        }
        for (Button b : functionBtns)
        {
            b.setOnClickListener(new FunctionBtnOnClickHandler());
        }

        Button btnBack = findViewById(R.id.part2BtnBack);
        if (btnBack != null)
        {
            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
    }

    public class NumbersBtnOnClickHandler implements Button.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            int tag = Integer.parseInt(((Button)v).getText().toString());
            String text = monitor.getText().toString();
            text = userStillTyping ? String.format("%s%d", text.equals("0") ? "" : text, tag) : String.format("%d", tag);
            monitor.setText(text);
            calculatorBrain.setOperand(Double.parseDouble(text));
            userStillTyping = true;
        }
    }

    public class FunctionBtnOnClickHandler implements Button.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String text = ((Button)v).getText().toString();
            calculatorBrain.performOperation(text);
            if (calculatorBrain.getNeedUpdateMonitor()) monitor.setText(hashes.format(calculatorBrain.getResult()));
            userStillTyping = false;
        }
    }
}
