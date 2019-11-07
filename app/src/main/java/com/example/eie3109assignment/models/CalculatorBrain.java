package com.example.eie3109assignment.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;

public class CalculatorBrain
{
    private double accumulator = 0.0;

    private boolean needUpdateMonitor = false;

    private BinaryOperation pendingBinaryOperation = null;

    public double getResult()
    {
        return accumulator;
    }

    public void setOperand(double _operand)
    {
        accumulator = _operand;
    }

    public boolean getNeedUpdateMonitor()
    {
        return needUpdateMonitor;
    }

    private final HashMap<String, Function<ArrayList<Double>, Double>> availableOperations = new HashMap<String, Function<ArrayList<Double>, Double>>() {{
        put("+", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return input.get(0) + input.get(1);
            }
        });

        put("-", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return input.get(0) - input.get(1);
            }
        });

        put("*", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return input.get(0) * input.get(1);
            }
        });

        put("/", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return input.get(0) / input.get(1);
            }
        });

        put("sin", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return Math.sin(input.get(0));
            }
        });

        put("cos", new Function<ArrayList<Double>, Double>()
        {
            @Override
            public Double apply(ArrayList<Double> input)
            {
                return Math.cos(input.get(0));
            }
        });

    }};

    public void performOperation(@NonNull String symbol)
    {
        needUpdateMonitor = true;
        switch (symbol)
        {
            case "e":
                accumulator = Math.E;
                break;
            case "\u03C0":
                accumulator = Math.PI;
                break;
            case "=":
                performPendingBinaryOperation();
                break;
            default:
                pendingBinaryOperation = new BinaryOperation(accumulator, availableOperations.get(symbol));
                needUpdateMonitor = false;
                break;
        }
    }

    private void performPendingBinaryOperation()
    {
        accumulator = pendingBinaryOperation.performOperation(accumulator);
        pendingBinaryOperation = null;
    }

    public class BinaryOperation
    {
        private double firstOperand;
        private Function<ArrayList<Double>, Double> operation;

        BinaryOperation(double _firstOperand, Function<ArrayList<Double>, Double> _operation)
        {
            firstOperand = _firstOperand;
            operation = _operation;
        }

        private double performOperation(double secondOperand)
        {
            return operation.apply(new ArrayList<>(Arrays.asList(firstOperand, secondOperand)));
        }
    }
}
