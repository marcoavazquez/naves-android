package com.aliens.aliensvsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView {

    // Maximos touch al mismo tiempo, 2, uno para nave otro para balas
    final int MAX_POINT_CNT = 2;

    float[] x = new float[MAX_POINT_CNT];
    float[] y = new float[MAX_POINT_CNT];
    boolean[] isTouch = new boolean[MAX_POINT_CNT];

    float[] x_last = new float[MAX_POINT_CNT];
    float[] y_last = new float[MAX_POINT_CNT];
    boolean[] isTouch_last = new boolean[MAX_POINT_CNT];

    private final Bitmap boton;
    private final Bitmap badnave1;
    private Bitmap bmp_nave;
    private Bitmap fondo;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    private Controles controles;
    private Nave nave;

    public boolean touched;
    public int touched_x, touched_y;

    public GameView(Context context){
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                boolean retry = true;
                gameLoopThread.setRunning(false);

                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createEveryThing();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });

        bmp_nave  = BitmapFactory.decodeResource(getResources(), R.drawable.nave1);
        badnave1 = BitmapFactory.decodeResource(getResources(), R.drawable.badnave1);

        fondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
        boton = BitmapFactory.decodeResource(getResources(), R.drawable.boton);

      }

    public void createEveryThing(){
        controles = new Controles(this, boton);
        nave = new Nave(this, bmp_nave);
    }

    @Override

    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(fondo, -100 , 0, null);
        controles.onDraw(canvas);
            /* Evita que dos hilos se ejecuten al mismo tiempo */
        nave.onDraw(canvas);
        controles.disparar(nave.posAncho, bmp_nave.getWidth(), nave.posAlto, bmp_nave.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // DoubleTouch

        int pointerIndex  = ((event.getAction() & event.ACTION_POINTER_ID_MASK)
                                                >> event.ACTION_POINTER_ID_SHIFT);

        int pointerId = event.getPointerId(pointerIndex);

        int action = (event.getAction() & event.ACTION_MASK);
        int pointCnt = event.getPointerCount();

        touched_x = (int) event.getX();
        touched_y = (int) event.getY();

        // Se obtiene el evento, ya sea tocar pantalla, levantar dedo o mover dedo

        if (pointCnt <= MAX_POINT_CNT){
            if(pointerIndex <= MAX_POINT_CNT - 1){
                for (int i = 0; i < pointCnt; i++){
                    int id = event.getPointerId(i);
                    x_last[id] = x[id];
                    y_last[id] = y[id];

                    isTouch_last[id] = isTouch[id];

                    x[id] = event.getX();
                    y[id] = event.getY();
                }

                Log.e("tocado(x.y)","(" + touched_x + "," + touched_y + ")");

                switch (action) {

                    // Tocar la pantalla
                    case MotionEvent.ACTION_DOWN:
                        isTouch[pointerId] = true;
                        touched = true;
                        break;

                    // Mover dedo sobre la pantalla
                    case MotionEvent.ACTION_MOVE:
                        isTouch[pointerId] = true;
                        touched = true;
                        break;

                    // Levantar dedo de la pantalla
                    case MotionEvent.ACTION_UP:
                        isTouch[pointerId] = false;
                        touched = false;
                        break;


                    case MotionEvent.ACTION_CANCEL:
                        isTouch[pointerId] = false;
                        touched = false;
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        isTouch[pointerId] = false;
                        touched = false;
                        break;

                    default:
                        touched = false;
                /* Nothing to do */
                }
            }
        }

        //int daction = event.getAction();


        return true;
    }
}
