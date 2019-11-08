package com.example.eie3109assignment.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.eie3109assignment.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class Panel extends SurfaceView implements SurfaceHolder.Callback
{
    private Bitmap bmp;
    private GameThread thread;

    private ArrayList<GraphicObject> graphics = new ArrayList<>();
    private ArrayList<Rect> forbiddenAreas = new ArrayList<>();

    public Panel(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        Resources resources = getResources();
        bmp = getBitmapFromDrawable(resources.getDrawable(R.mipmap.ic_launcher));
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        Coordinates coords;
        int x, y;
        canvas.drawColor(Color.BLACK);
        for (GraphicObject graphic : graphics)
        {
            coords = graphic.getCoordinates();
            x = coords.getX();
            y = coords.getY();
            canvas.drawBitmap(bmp, x, y, null);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        thread.setRunning(false);
        while (retry)
        {
            try
            {
                thread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                Log.v("Panel.java", e.getMessage() != null ? e.getMessage() : "");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        synchronized (thread.getSurfaceHolder())
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                int x = (int) event.getX();
                int y = (int) event.getY();

                GraphicObject graphic = new GraphicObject(bmp);

                int bmpW = graphic.getGraphic().getWidth() / 2;
                int bmpH = graphic.getGraphic().getHeight() / 2;

                graphic.getCoordinates().setX(x - bmpW / 2);
                graphic.getCoordinates().setY(y - bmpH / 2);

                Rect rect = new Rect(x, y,x + bmpW * 2,y + bmpH * 2);
                if (checkSpaceAvailable(rect))
                {
                    graphics.add(graphic);
                    forbiddenAreas.add(rect);
                }
            }
            return true;
        }
    }

    public boolean checkSpaceAvailable(Rect rect)
    {
        for (Rect test : forbiddenAreas)
        {
            if (rect.top == test.top && rect.bottom == test.bottom && rect.right == test.right && rect.left == test.left) continue;

            if ((rect.top >= test.top && rect.top <= test.bottom && rect.left >= test.left && rect.left <= test.right)
                || (rect.top >= test.top && rect.top <= test.bottom && rect.right >= test.left && rect.right <= test.right)
                || (rect.bottom >= test.top && rect.bottom <= test.bottom && rect.left >= test.left && rect.left <= test.right)
                || (rect.bottom >= test.top && rect.bottom <= test.bottom && rect.right >= test.left && rect.right <= test.right))
            {
                Log.v("Panel.java", String.format("rejected %d %d %d %d", rect.top, rect.right, rect.left, rect.bottom));
                return false;
            }
        }
        return true;
    }

    public void updateMovement()
    {
        Coordinates coord;
        Movement movement;

        int x, y;

        for (int i = 0; i < graphics.size(); i++)
        {
            GraphicObject graphic = graphics.get(i);

            coord = graphic.getCoordinates();
            movement = graphic.getMovement();

            x = coord.getX() + ((movement.getXDirection() == Movement.X_DIRECTION_RIGHT) ? 1 : -1) * movement.getXSpeed();
            //check x if reaches border
            if (x < 0)
            {
                movement.toggleXDirection();
                coord.setX(-x);
            }
            else if (x + graphic.getGraphic().getWidth() > getWidth())
            {
                movement.toggleXDirection();
                coord.setX(x + getWidth() - (x + graphic.getGraphic().getWidth()));
            }
            else
            {
                coord.setX(x);
            }

            y = coord.getY() + ((movement.getYDirection() == Movement.Y_DIRECTION_DOWN) ? 1 : -1) * movement.getYSpeed();
            //check y if reaches border
            if (y < 0)
            {
                movement.toggleYDirection();
                coord.setY(-y);
            }
            else if (y + graphic.getGraphic().getHeight() > getHeight())
            {
                movement.toggleYDirection();
                coord.setY(y + getHeight() - (y + graphic.getGraphic().getHeight()));
            }
            else
            {
                coord.setY(y);
            }
        }
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable)
    {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}
