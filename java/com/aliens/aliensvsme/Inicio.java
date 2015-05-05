package com.aliens.aliensvsme;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;



public class Inicio {

    public GameView gameView;

    public Boolean iniciado = false;
    public Paint mensaje;
    public Paint boton;
    public Paint msg_boton;


    public Inicio(GameView gameView) {
        this.gameView = gameView;
    }

    public Boolean get_iniciado() {
        return this.iniciado;
    }

    public void set_iniciado(boolean b) {
        this.iniciado = b;
    }

    public void onDraw(Canvas canvas) {

        mensaje = new Paint();
        mensaje.setColor(Color.YELLOW);
        mensaje.setTextSize(gameView.getHeight() / 10);
        mensaje.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Solo naves...", gameView.getWidth() / 2, gameView.getHeight() / 4, mensaje);

        boton = new Paint();
        boton.setColor(Color.WHITE);
        canvas.drawCircle(gameView.getWidth() / 2, gameView.getHeight() / 2, gameView.getWidth() / 10, boton);

        msg_boton = new Paint();
        msg_boton.setColor(Color.RED);
        msg_boton.setTextAlign(Paint.Align.CENTER);
        msg_boton.setTextSize(gameView.getHeight() / 20);
        canvas.drawText("Comenzar", gameView.getWidth() / 2, gameView.getHeight() / 2, msg_boton);
    }

    public void comenzar() {
        if (!iniciado) {
            if (gameView.touched &&
                gameView.x >= (gameView.getWidth() / 8) * 3 &&
                gameView.x <= (gameView.getWidth() / 8) * 5 &&
                gameView.y >= (gameView.getHeight() / 2) - gameView.getWidth() / 8 &&
                gameView.y <= (gameView.getHeight() / 2) + gameView.getWidth() / 8 ) {

                this.iniciado = true;
            }
        }
    }

}
