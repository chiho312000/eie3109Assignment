package com.example.eie3109assignment.models;

import android.graphics.Bitmap;

public class Coordinates
{
    private int x = 100, y = 0;
    private Bitmap bitmap;

    public Coordinates(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public void setX(int x)
    {
        this.x = x - bitmap.getWidth() / 2;
    }

    public int getX()
    {
        return x + bitmap.getWidth() / 2;
    }

    public void setY(int y)
    {
        this.y = y - bitmap.getHeight() / 2;
    }

    public int getY()
    {
        return y + bitmap.getWidth() / 2;
    }
}
