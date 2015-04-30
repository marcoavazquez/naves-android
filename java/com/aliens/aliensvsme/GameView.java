package com.aliens.aliensvsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import static java.lang.Thread.sleep;


public class GameView extends SurfaceView {

    private Bitmap bmp_nave;
    private Bitmap fondo;
    private Bitmap bmp_badnave1;
    private Bitmap bmp_bad_navecita;

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    private Nave nave;
    private BadNave badnave;
    private BadNavecita badNavecita[];
    private Bala bala;

    public boolean touched;
    public boolean multi;
    public int indice;

    public int x;
    public int y;

    Random rnd;

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

        bmp_nave     = BitmapFactory.decodeResource(getResources(), R.drawable.nave1);
        bmp_bad_navecita = BitmapFactory.decodeResource(getResources(), R.drawable.badnavecita);
        bmp_badnave1 = BitmapFactory.decodeResource(getResources(), R.drawable.badnave1);

        fondo        = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);


      }

    public void createEveryThing(){
        //controles = new Controles(this, boton);
        nave = new Nave(this, bmp_nave);
        bala = new Bala(this);
        badnave = new BadNave(this, bmp_badnave1);

        badNavecita = new BadNavecita[10];
        rnd = new Random();
        for (int i = 0; i < 10 ; i++) {
            badNavecita[i] = new BadNavecita(this, bmp_bad_navecita, rnd.nextInt(this.getWidth()) + 200, rnd.nextInt(this.getHeight()));
        }

    }

    @Override

    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(fondo, 0 , 0, null);
        //controles.onDraw(canvas);
        nave.onDraw(canvas);
        badnave.onDraw(canvas);
        if ( nave.disparado()){

            for (int i = 0; i < 3 ; i++) {

                bala.onDraw(canvas, i, y);
            }
            bala.levantado = false;

        }else{
            bala.levantado = true;

        }

        for (int i = 0; i < 10 ; i++) {
            badNavecita[i].onDraw(canvas);
        }




    }




    @Override
    public boolean onTouchEvent(MotionEvent event){


        int action = MotionEventCompat.getActionMasked(event);

        //Obtiene el index del pointer asociado con la accion

        int index = MotionEventCompat.getActionIndex(event);


        if (event.getPointerCount() > 1) {


            x = (int) MotionEventCompat.getX(event, index);
            y = (int) MotionEventCompat.getY(event, index);

            Log.e("Psociones", "(" + x + "," + y + ") " + index );

            get_evento(action, true, indice);

        } else {

            x = (int) MotionEventCompat.getX(event, index);
            y = (int) MotionEventCompat.getY(event, index);

            Log.e("Psociones", "(" + x + "," + y + ") " + index );

            get_evento(action, false, indice);
        }

        return true;
    }


    public void get_evento(int action, boolean sondos, int index_event){

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                touched = true;
                multi = sondos;
                indice = index_event;
                //posBalaY = y;
                break;

            case MotionEvent.ACTION_MOVE:

                touched = true;
                multi = sondos;
                indice = index_event;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                touched = true;
                multi = sondos;
                indice = index_event;
                break;

            case MotionEvent.ACTION_POINTER_UP:

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_UP:

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_OUTSIDE:

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_CANCEL:

                touched = false;
                multi = false;
                break;

        }
    }
}
