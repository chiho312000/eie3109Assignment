package com.example.eie3109assignment.models;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
    private SurfaceHolder surfaceHolder;
    private Panel panel;
    private boolean run = false;

    public GameThread(SurfaceHolder surfaceHolder, Panel panel)
    {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }

    public void setRunning(boolean run)
    {
        this.run = run;
    }

    @Override
    public void run()
    {
        super.run();
        Canvas c;
        while (run)
        {
            c = null;
            try
            {
                c = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    panel.onDraw(c);
                }
            }
            finally
            {
                if (c != null)
                {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}
