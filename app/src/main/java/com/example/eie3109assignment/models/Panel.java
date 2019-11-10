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


                int testTop, testLeft, testRight, testBottom;
                int rectRight = x + bmpW * 2, rectBottom = y + bmpH * 2;

                graphic.setAvailableSpace(new Rect(getLeft(), getTop(), getRight(), getBottom()));
                for (GraphicObject graphic2 : graphics)
                {
                    if (x > graphic2.getCoordinates().getX() && x < graphic2.getCoordinates().getX() + graphic2.getGraphic().getWidth()
                        && y > graphic2.getCoordinates().getY() && y < graphic2.getCoordinates().getY() + graphic2.getGraphic().getHeight())
                    {
                        graphics.remove(graphic2);
                        return true;
                    }

                    testTop = graphic2.getCoordinates().getY();
                    testLeft = graphic2.getCoordinates().getX();
                    testRight = testLeft + graphic2.getGraphic().getWidth();
                    testBottom = testTop + graphic2.getGraphic().getHeight();

                    if ((y >= testTop && y <= testBottom && x >= testLeft && x <= testRight)
                        || (y >= testTop && y <= testBottom && rectRight >= testLeft && rectRight <= testRight)
                        || (rectBottom >= testTop && rectBottom <= testBottom && x >= testLeft && x <= testRight)
                        || (rectBottom >= testTop && rectBottom <= testBottom && rectRight >= testLeft && rectRight <= testRight))
                    {
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
        updateSpaces();
        for (GraphicObject graphic : graphics)
        {
            graphic.move();
        }

    }

    public void updateSpaces()
    {
        GraphicObject graphicObject;
        GraphicObject graphicObject1;

        boolean hasMore = graphics.size() > 1;
        Rect rect;

        int graphicHeight, graphicWidth;
        Coordinates originalCoordinates;
        Coordinates coordinate1;
        int top1, left1, right1, bottom1;
        for (int i = 0 ; i < graphics.size(); i++)
        {
            graphicObject = graphics.get(i);
            rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
            originalCoordinates = graphicObject.getCoordinates();
            graphicHeight = graphicObject.getGraphic().getHeight();
            graphicWidth = graphicObject.getGraphic().getWidth();
            double movingAngle = Math.atan2((double)graphicObject.getMovement().getYSpeed() * graphicObject.getMovement().getYDirection(), (double)graphicObject.getMovement().getXSpeed() * graphicObject.getMovement().getXDirection()) + Math.PI;
            movingAngle *= 180 / Math.PI;
            int quadrant = (int)(movingAngle / 90) % 4;
            Log.v("Panel", "moving direction: " + movingAngle + " quadrant :" + quadrant);
            if (hasMore)
            {
                for (int j = 0; j < graphics.size(); j++)
                {
                    if (i == j) continue;
                    graphicObject1 = graphics.get(j);
                    coordinate1 = graphicObject1.getCoordinates();

                    top1 = graphicObject1.getCoordinates().getY();
                    left1 = graphicObject1.getCoordinates().getY();
                    right1 = graphicObject1.getCoordinates().getX() + graphicObject1.getGraphic().getWidth();
                    bottom1 = graphicObject1.getCoordinates().getY() + graphicObject1.getGraphic().getHeight();


                    if (top1 < 0) top1 = 0;
                    if (left1 < 0) left1 = 0;
                    if (right1 > getWidth()) right1 = getWidth();
                    if (bottom1 > getHeight()) bottom1 = getHeight();

                    double angleToObject = Math.atan2((double)originalCoordinates.getY() - coordinate1.getY(), (double)(originalCoordinates.getX() - coordinate1.getX()));
                    angleToObject *= 180 / Math.PI;

                    Log.v("Panel", "direction towards object: " + angleToObject);

                    if (Math.abs(movingAngle - angleToObject) < 30)
                    {
                        switch (quadrant)
                        {
                            case 0:
                                if (originalCoordinates.getX() > right1) // update left
                                {
                                    if (right1 < rect.left && right1 > 0) rect.left = right1;
                                    Log.v("Panel", "Update Left");
                                }
                                if (originalCoordinates.getY() > bottom1) // update top
                                {
                                    if (bottom1 > rect.top && bottom1 > 0) rect.top = bottom1;
                                    Log.v("Panel", "Update top");
                                }
                                break;
                            case 1:
                                if (originalCoordinates.getX() + graphicWidth < left1) // update right
                                {
                                    if (left1 < rect.right && left1 > 0) rect.right = left1;
                                    Log.v("Panel", "Update Right");
                                }
                                if (originalCoordinates.getY() > bottom1) // update top
                                {
                                    if (bottom1 > rect.top && bottom1 > 0) rect.top = bottom1;
                                    Log.v("Panel", "Update Top");
                                }
                                break;
                            case 2:
                                if (originalCoordinates.getX() + graphicWidth < left1) // update right
                                {
                                    if (left1 < rect.right && left1 > 0) rect.right = left1;
                                    Log.v("Panel", "Update Right");
                                }
                                if (originalCoordinates.getY() + graphicHeight < top1) // update bottom
                                {
                                    if (top1 < rect.bottom && top1 > 0) rect.bottom = top1;
                                    Log.v("Panel", "Update Bottom");
                                }
                                break;
                            case 3:
                                if (originalCoordinates.getX() > right1) // update left
                                {
                                    if (right1 < rect.left && right1 > 0) rect.left = right1;
                                    Log.v("Panel", "Update Left");
                                }
                                if (originalCoordinates.getY() + graphicHeight < top1) // update bottom
                                {
                                    if (top1 < rect.bottom && top1 > 0) rect.bottom = top1;
                                    Log.v("Panel", "Update Bottom");
                                }
                                break;
                        }
                    }
                }
            }
            graphicObject.setAvailableSpace(rect);
            Log.v("Panel", String.format("available space of object %d: top: %d, left: %d , right: %d, bottom: %d", i, rect.top, rect.left, rect.right, rect.bottom));
        }

        Log.v("Panel", "update ends");
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
