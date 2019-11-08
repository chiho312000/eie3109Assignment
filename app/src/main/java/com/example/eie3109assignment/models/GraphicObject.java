package com.example.eie3109assignment.models;

import android.graphics.Bitmap;

public class GraphicObject
{
    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;

    public GraphicObject(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        this.coordinates = new Coordinates(bitmap);
        this.movement = new Movement((int)(Math.random() * 10), (int)(Math.random() * 10));
    }

    public Bitmap getGraphic()
    {
        return bitmap;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public Movement getMovement()
    {
        return movement;
    }
}
