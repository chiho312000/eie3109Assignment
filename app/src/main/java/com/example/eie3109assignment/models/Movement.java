package com.example.eie3109assignment.models;

public class Movement
{
    private static final int X_DIRECTION_RIGHT = 1;
    private static final int X_DIRECTION_LEFT = -1;
    private static final int Y_DIRECTION_DOWN = 1;
    private static final int Y_DIRECTION_UP = -1;

    private int xSpeed;
    private int ySpeed;

    private int xDirection;
    private int yDirection;

    Movement(int x, int y)
    {
        this.xSpeed = x;
        this.ySpeed = y;
        xDirection = Math.random() > 0.5 ? X_DIRECTION_LEFT : X_DIRECTION_RIGHT;
        yDirection = Math.random() > 0.5 ? Y_DIRECTION_UP : Y_DIRECTION_DOWN;
    }

    public void setDirections(int xDirection, int yDirection)
    {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public void toggleXDirection()
    {
        xDirection = xDirection == X_DIRECTION_LEFT ? X_DIRECTION_RIGHT : X_DIRECTION_LEFT;
    }

    public void toggleYDirection()
    {
        yDirection = yDirection == Y_DIRECTION_UP ? Y_DIRECTION_DOWN : Y_DIRECTION_UP;
    }

    public int getXSpeed()
    {
        return xSpeed;
    }

    public int getYSpeed()
    {
        return ySpeed;
    }

    public int getXDirection()
    {
        return xDirection;
    }

    public int getYDirection()
    {
        return yDirection;
    }
}
