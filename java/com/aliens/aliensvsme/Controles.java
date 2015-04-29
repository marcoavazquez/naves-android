package com.aliens.aliensvsme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Controles {

    public GameView gameView;
    public Bitmap boton;
    public int ancho;
    public int alto;
    public int posArmaAncho;
    public int posArmaAlto;
    public int bmpH;
    public int bmpW;

    public int posBalaX = 310;
    public int posBalaY = 210;
    public int trayBala;

    public Controles(GameView gameview, Bitmap bitmap){
        this.gameView = gameview;
        this.boton    = bitmap;

        this.bmpH = bitmap.getHeight();
        this.bmpW = bitmap.getWidth();

        this.ancho    = gameview.getWidth();
        this.alto     = gameview.getHeight();

        posArmaAncho = ancho - (ancho / 8);
        posArmaAlto  = alto - (alto / 4);
    }

    public void onDraw(Canvas canvas){

        Paint bala = new Paint();
        bala.setColor(Color.WHITE);
        bala.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(boton, posArmaAncho, posArmaAlto , null);
        canvas.drawCircle(posBalaX, posBalaY, 10, bala);
    }

    public void disparar(int naveX, int naveW, int naveY, int naveH){
        if(gameView.touched &&
                gameView.touched_x > posArmaAncho &&
                gameView.touched_x < posArmaAncho + bmpW &&
                gameView.touched_y > posArmaAlto &&
                gameView.touched_y < posArmaAlto + bmpH ){

            // Si ha tocado el boton de arma retorna Verdadero

            posBalaX = naveX + naveW;
            posBalaY = naveY + naveH / 2;

            trayBala = posBalaX + 1;
        }
    }
}
