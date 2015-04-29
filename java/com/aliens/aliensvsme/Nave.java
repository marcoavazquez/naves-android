package com.aliens.aliensvsme;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Nave {

    public Bitmap nave;
    public GameView gameview;
    public int ancho;
    public int alto;
    public int posAlto = 100;
    public int posAncho = 10;
    public int bmpH;
    public int bmpW;
    public Controles controles;

    public Nave(GameView gameView, Bitmap bitmap){
        this.gameview = gameView;
        this.nave     = bitmap;

        this.alto  = gameView.getHeight();
        this.ancho = gameView.getWidth();

        this.bmpH = bitmap.getHeight();
        this.bmpW = bitmap.getWidth();

    }

    public void onDraw(Canvas canvas){
        moverNave(canvas);
    }

    public void moverNave(Canvas canvas){



        if(gameview.touched && gameview.touched_x > posAncho && gameview.touched_x < posAncho + bmpW &&
                gameview.touched_y > posAlto && gameview.touched_y < posAlto + bmpH){

            canvas.drawBitmap(nave, gameview.touched_x - bmpW / 2, gameview.touched_y - bmpH / 2, null);
            posAlto = gameview.touched_y - bmpH / 2;
            posAncho = gameview.touched_x - bmpW / 2;
        }else{
            canvas.drawBitmap(nave, posAncho, posAlto, null);
        }

    }
}
