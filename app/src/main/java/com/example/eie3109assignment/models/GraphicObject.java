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
        int x = getNextXCoordinate();
        int y = getNextYCoordinate();
        int width = getGraphic().getWidth();
        int height = getGraphic().getHeight();

        if (x <= availableSpace.left || x + width >= availableSpace.right) movement.toggleXDirection();
        if (y <= availableSpace.top || y + height >= availableSpace.bottom) movement.toggleYDirection();

        coordinates.setX(getNextXCoordinate());
        coordinates.setY(getNextYCoordinate());
    }

    public int getNextXCoordinate()
    {
        return coordinates.getX() + movement.getXDirection() * movement.getXSpeed();
    }

    public int getNextYCoordinate()
    {
        return coordinates.getY() + movement.getYDirection() * movement.getYSpeed();
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

    public void setAvailableSpace(Rect _availableSpace)
    {
        availableSpace = _availableSpace;
    }
}
