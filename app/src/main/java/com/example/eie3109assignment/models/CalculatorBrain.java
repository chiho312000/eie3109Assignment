package com.example.eie3109assignment.models;

public class CalculatorBrain
{
    private double accumulator = 0.0;

    public double getResult()
    {
        return accumulator;
    }

    public void setOperand(double operand)
    {
        accumulator = operand;
    }

    public void performOperation(String operation)
    {}
}
