package com.example.eie3109assignment.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.eie3109assignment.R;

public class Panel extends SurfaceView implements SurfaceHolder.Callback
{
    private Bitmap bmp;
    private GameThread thread;

    public Panel(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        Resources resources = getResources();
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        thread = new GameThread(getHolder(), this);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bmp, 10, 10, null);
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
                Log.v("Panel.java", e.getMessage());
            }
        }
    }
}
