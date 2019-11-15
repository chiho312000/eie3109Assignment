package com.example.eie3109assignment.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class GraphicObject
{
    private Coordinates coordinates;
    private Movement movement;
    private Rect availableSpace;

    public GraphicObject(Bitmap bitmap)
    {
        this.coordinates = new Coordinates(bitmap);
        Random random = new Random();
        this.movement = new Movement(random.nextInt(9) + 1, random.nextInt(9) + 1);
    }

    public void move()
    {
        int left = getNextLeftCoordinate();
        int top = getNextTopCoordinate();
        int right = getNextRightCoordinate();
        int bottom = getNextBottomCoordinate();

        if (left <= availableSpace.left || right >= availableSpace.right) movement.toggleXDirection();
        if (top <= availableSpace.top || bottom >= availableSpace.bottom) movement.toggleYDirection();

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

    public int getNextLeftCoordinate()
    {
        return coordinates.getLeft() + movement.getXDirection() * movement.getXSpeed();
    }

    public int getNextRightCoordinate()
    {
        return coordinates.getRight() + movement.getXDirection() * movement.getXSpeed();
    }

    public int getNextTopCoordinate()
    {
        return coordinates.getTop() + movement.getYDirection() * movement.getYSpeed();
    }

    public int getNextBottomCoordinate()
    {
        return coordinates.getBottom() + movement.getYDirection() * movement.getYSpeed();
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
