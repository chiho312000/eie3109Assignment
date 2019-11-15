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
import java.util.Collections;

import androidx.annotation.NonNull;

public class Panel extends SurfaceView implements SurfaceHolder.Callback
{
    private Bitmap bmp;
    private GameThread thread;

    private ArrayList<GraphicObject> graphics = new ArrayList<>();

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
            x = coords.getLeft();
            y = coords.getTop();
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

                graphic.getCoordinates().setX(x);
                graphic.getCoordinates().setY(y);

                graphic.setAvailableSpace(new Rect(getLeft(), getTop(), getRight(), getBottom()));
                for (GraphicObject graphic2 : graphics)
                {
                    if (x > graphic2.getNextLeftCoordinate() && x < graphic2.getNextRightCoordinate()
                        && y > graphic2.getNextTopCoordinate() && y < graphic2.getNextBottomCoordinate())
                    {
                        graphics.remove(graphic2);
                        return true;
                    }
                    if (checkOverlap(graphic, graphic2) != 0) return true;
                }
                if (graphic.getCoordinates().getLeft() >= getLeft() && graphic.getCoordinates().getRight() <= getRight()
                    && graphic.getCoordinates().getTop() >= getTop() && graphic.getCoordinates().getBottom() <= getBottom()) graphics.add(graphic);
            }
            return true;
        }
    }

    public void updateMovement()
    {
        updateDirection();
        for (GraphicObject graphic : graphics)
        {
            graphic.move();
        }
        Collections.shuffle(graphics);
    }

    public int checkOverlap(GraphicObject original, GraphicObject test)
    {
        int Ol = original.getNextLeftCoordinate();
        int Ot = original.getNextTopCoordinate();
        int Or = original.getNextRightCoordinate();
        int Ob = original.getNextBottomCoordinate();

        int Tl = test.getNextLeftCoordinate();
        int Tt = test.getNextTopCoordinate();
        int Tr = test.getNextRightCoordinate();
        int Tb = test.getNextBottomCoordinate();

        if (Ol > Tl && Ol < Tr)
        {
            if (Ot > Tt && Ot < Tb) return 1;
            if (Ob > Tt &&  Ob < Tb) return 3;
        }

        if (Or > Tl && Or < Tr)
        {
            if (Ot > Tt && Ot < Tb) return 2;
            if (Ob > Tt && Ob < Tb) return 4;
        }

        return 0;
    }

    public void updateDirection()
    {
        GraphicObject graphicObject;
        GraphicObject graphicObject1;

        boolean hasMore = graphics.size() > 1;

        for (int i = 0 ; i < graphics.size(); i++)
        {
            graphicObject = graphics.get(i);
            if (hasMore)
            {
                for (int j = 0; j < graphics.size(); j++)
                {
                    if (i == j) continue;
                    graphicObject1 = graphics.get(j);
                    if (checkOverlap(graphicObject, graphicObject1) > 0)
                    {
                        graphicObject1.getMovement().setDirections(graphicObject.getMovement().getXDirection(), graphicObject.getMovement().getYDirection());
                        graphicObject.getMovement().toggleXDirection();
                        graphicObject.getMovement().toggleYDirection();
                    }
                }
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

    public GameThread getThread()
    {
        return thread;
    }
}
