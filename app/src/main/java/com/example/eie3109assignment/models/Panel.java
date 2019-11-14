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

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.util.ArrayList;

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

                graphic.getCoordinates().setX(x - bmpW);
                graphic.getCoordinates().setY(y - bmpH);

                graphic.setAvailableSpace(new Rect(getLeft(), getTop(), getRight(), getBottom()));
                for (GraphicObject graphic2 : graphics)
                {
                    if (x > graphic2.getCoordinates().getX() && x < graphic2.getCoordinates().getX() + graphic2.getGraphic().getWidth()
                        && y > graphic2.getCoordinates().getY() && y < graphic2.getCoordinates().getY() + graphic2.getGraphic().getHeight())
                    {
                        graphics.remove(graphic2);
                        return true;
                    }
                }
                graphics.add(graphic);
            }
            return true;
        }
    }

    public boolean checkSpaceAvailable(Rect rect)
    {
        int testTop, testLeft, testRight, testBottom;
        for (GraphicObject graphic : graphics)
        {
            testTop = graphic.getCoordinates().getY();
            testLeft = graphic.getCoordinates().getX();
            testRight = testLeft + graphic.getGraphic().getWidth();
            testBottom = testTop + graphic.getGraphic().getHeight();

            if ((rect.top >= testTop && rect.top <= testBottom && rect.left >= testLeft && rect.left <= testRight)
                    || (rect.top >= testTop && rect.top <= testBottom && rect.right >= testLeft && rect.right <= testRight)
                    || (rect.bottom >= testTop && rect.bottom <= testBottom && rect.left >= testLeft && rect.left <= testRight)
                    || (rect.bottom >= testTop && rect.bottom <= testBottom && rect.right >= testLeft && rect.right <= testRight))
            {
                return false;
            }
        }
        return true;
    }

    public void updateMovement()
    {
        for (GraphicObject graphic : graphics)
        {
            graphic.move();
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

    public class GraphicRect
    {
        private Rect rect;
        private boolean inverted;

        GraphicRect(Rect _rect, boolean _inverted)
        {
            rect = _rect;
            inverted = _inverted;
        }
    }
}
