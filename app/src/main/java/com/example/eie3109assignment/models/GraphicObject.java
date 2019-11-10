package com.example.eie3109assignment.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class GraphicObject
{
    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;
    private Rect availableSpace;

    public GraphicObject(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        this.coordinates = new Coordinates(bitmap);
        Random random = new Random();
        this.movement = new Movement(random.nextInt(9) + 1, random.nextInt(9) + 1);
    }

    public void move()
    {
        int x = coordinates.getX() + movement.getXDirection() * movement.getXSpeed();
        int y = coordinates.getY() + movement.getYDirection() * movement.getYSpeed();
        int width = getGraphic().getWidth();
        int height = getGraphic().getHeight();

        if (x <= availableSpace.left || x + width >= availableSpace.right)
        {
            movement.toggleXDirection();
            x += 2 * movement.getXDirection() * movement.getXSpeed();
        }

        if (y <= availableSpace.top || y + height >= availableSpace.bottom)
        {
            movement.toggleYDirection();
            y += 2 * movement.getYDirection() * movement.getYSpeed();
        }

        coordinates.setX(x);
        coordinates.setY(y);
    }

    public Bitmap getGraphic()
    {
        return bitmap;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public Rect getAvailableSpace()
    {
        return availableSpace;
    }

    public Movement getMovement()
    {
        return movement;
    }

    public void setAvailableSpace(Rect _availableSpace)
    {
        availableSpace = _availableSpace;
    }
}
