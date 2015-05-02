package com.aliens.aliensvsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.camera2.params.BlackLevelPattern;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;


public class GameView extends SurfaceView {

    private Inicio inicio;


    private Bitmap bmp_nave;
    private Bitmap bmp_badnave1,bmp_badnave2,bmp_badnave3;
    private Bitmap bmp_bad_navecita, bmp_bad_navecita2, bmp_bad_navecita3;
    private Bitmap const1, const2, const3;
    private Bitmap pausa, resume;

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    private Escenario escenario;
    private Nave nave;
    private BadNave badnave;
    private List<BadNavecita> badNavecita = new ArrayList<BadNavecita>();
    private Bala bala;
    private List<Fondo> estrella = new ArrayList<Fondo>();

    public boolean touched;
    public boolean multi;
    public int indice, color, colorEstrella, velocidad;

    public int x;
    public int y;

    private int danho;

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

        bmp_nave  = BitmapFactory.decodeResource(getResources(), R.drawable.nave1);
        pausa  = BitmapFactory.decodeResource(getResources(), R.drawable.pausa);


        const1 = BitmapFactory.decodeResource(getResources(), R.drawable.const1);
        const2 = BitmapFactory.decodeResource(getResources(), R.drawable.const2);
        const3 = BitmapFactory.decodeResource(getResources(), R.drawable.const3);


        bmp_badnave1 = BitmapFactory.decodeResource(getResources(), R.drawable.badnave1);
        bmp_bad_navecita2 = BitmapFactory.decodeResource(getResources(), R.drawable.badnavecita2);
        bmp_bad_navecita = BitmapFactory.decodeResource(getResources(), R.drawable.badnavecita);
        bmp_badnave2 = BitmapFactory.decodeResource(getResources(), R.drawable.badnave2);
        bmp_bad_navecita3 = BitmapFactory.decodeResource(getResources(), R.drawable.badnavecita3);
        bmp_badnave3 = BitmapFactory.decodeResource(getResources(), R.drawable.badnave3);


      }

    public void createEveryThing(){

        inicio = new Inicio(this);
        escenario = new Escenario(this);

        rnd = new Random();

        for (int i = 0; i < 25; i++) {
            synchronized (getHolder()){
                try {
                    sleep(20);
                    estrella.add(new Fondo(this,rnd.nextInt(this.getWidth()), rnd.nextInt(this.getHeight())));
                } catch (Exception e) {}
            }

        }

        nave = new Nave(this);
        bala = new Bala(this);
        badnave = new BadNave(this);

        for (int i = 0; i < 10 ; i++) {
            badNavecita.add(new BadNavecita(this, rnd.nextInt(this.getWidth() / 2) + 200, rnd.nextInt(this.getHeight()) - 10));
        }

    }

    @Override

    protected void onDraw(Canvas canvas) {


        if (inicio.get_iniciado() && !escenario.getFueConstSeleccionada()) {

            elegirConstelacion(canvas);


        }
        if (inicio.get_iniciado() && escenario.getFueSeleccionada()) {


            if (escenario.getNaveSeleccionada() == 1){
                color = Color.BLACK;
                colorEstrella = Color.WHITE;
                velocidad = 20;
                badnave.setBitmap(bmp_badnave1);
                danho = 5;

                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita2);
                }

            }else if (escenario.getNaveSeleccionada() == 2) {
                color = Color.BLUE;
                colorEstrella = Color.GREEN;
                velocidad = 15;
                badnave.setBitmap(bmp_badnave2);
                danho = 3;
                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita);
                }
            }else if (escenario.getNaveSeleccionada() == 3) {
                color = Color.MAGENTA;
                colorEstrella = Color.BLUE;
                velocidad = 30;
                badnave.setBitmap(bmp_badnave3);
                danho = 1;
                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita3);
                }
            }


            batalla(canvas, bmp_nave, Color.WHITE, color, velocidad);
            escenario.setFueConstSeleccionada(true);

            canvas.drawBitmap(pausa, this.getWidth() - (this.getWidth()/20), this.getHeight() - (this.getHeight() / 10), null);


            pausar();

            continuar(canvas);

        }


        if (!inicio.get_iniciado()) {

            canvas.drawColor(Color.BLUE);

            for (Fondo estrellas : estrella) {
                estrellas.onDraw(canvas, Color.YELLOW, 30, 5);

            }

            inicio.onDraw(canvas);
            inicio.comenzar();

        }
    }


    public void elegirConstelacion ( Canvas canvas) {

        canvas.drawColor(Color.BLACK);
        for (Fondo estrellas : estrella) {
            estrellas.onDraw(canvas, Color.GREEN, 8, 3);

        }

        Paint txt = new Paint();
        txt.setColor(Color.WHITE);
        txt.setTextSize(this.getHeight() / 15);
        txt.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Seleccione constelación", this.getWidth() / 2, this.getHeight() / 6, txt);

        canvas.drawBitmap(const1, this.getWidth() / 10, this.getHeight() / 2, null);
        canvas.drawBitmap(const2, (this.getWidth() / 10) * 4, this.getHeight() / 2, null);
        canvas.drawBitmap(const3, (this.getWidth() / 10) * 7, this.getHeight() / 2, null);

        if( touched &&
                x >= (this.getWidth() / 10) * 4  && x <= (this.getWidth() / 10) * 4 + const2.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const2.getHeight()){

            escenario.setNaveSeleccionada(2);
            escenario.setFueSeleccionada(true);

        }


        if ( touched &&
                x >= this.getWidth() / 10  && x <= this.getWidth() / 10 + const1.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const1.getHeight()){

            escenario.setNaveSeleccionada(1);
            escenario.setFueSeleccionada(true);

        }

        if( touched &&
                x >= (this.getWidth() / 10) * 7  && x <= (this.getWidth() / 10) * 7 + const3.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const3.getHeight()){

            escenario.setNaveSeleccionada(3);
            escenario.setFueSeleccionada(true);
        }

    }


    public void golpearNaveMayor(Canvas canvas, int bx, int by) {

        if ( badnave.leHasDado(bx, by) && badnave.get_vida() > 0 ) {
            dibujarGolpe(canvas, bx, by);
            badnave.set_vida(danho);
            badnave.set_nvl_indicador(badnave.get_nivel());//) += badnave.nivel;
            nave.set_puntuacion(15);
        }

    }


    public void accionesNavecitas(Canvas canvas) {

        synchronized (getHolder()) {

            for ( int i = badNavecita.size() - 1; i >= 0; i--) {
                BadNavecita bn = badNavecita.get(i);

                if (nave.meHanDado(bn.get_posX(), bn.get_posY())){
                    nave.set_vida(1);
                }

                if ( bn.leHasDado(bala.get_x(), bala.get_posInicialY()) ){

                    dibujarGolpe(canvas, bala.get_x(), bala.get_posInicialY());
                    nave.set_puntuacion(10);

                    bn.set_vida(danho);


                    if (bn.get_vida() <= 0){
                        nave.set_puntuacion(20);
                        badNavecita.remove(bn);
                        break;
                    }
                }
            }
        }
    }

    public void dibujarGolpe(Canvas canvas, int x, int y){
        Paint golpe = new Paint();
        golpe.setColor(Color.RED);
        golpe.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - 5,y - 5, 30, golpe);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){


        int action = MotionEventCompat.getActionMasked(event);

        //Obtiene el index del pointer asociado con la accion

        int index = MotionEventCompat.getActionIndex(event);


        if (event.getPointerCount() > 1) {


            x = (int) MotionEventCompat.getX(event, index);
            y = (int) MotionEventCompat.getY(event, index);

            get_evento(action, true, indice);

        } else {

            x = (int) MotionEventCompat.getX(event, index);
            y = (int) MotionEventCompat.getY(event, index);

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
                Log.e("resume", "(ne" + "marco");

                if (!gameLoopThread.getRunning()) {
                    gameLoopThread.setRunning(true);
                    gameLoopThread.run();
                }
                Log.e("resume", "r(ne" + gameLoopThread.getRunning());
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

    public void batalla(Canvas canvas, Bitmap bmp_nave, int estrellaColor, int fondoColor, int estrellaVelocidad){

        canvas.drawColor(fondoColor);

        for (Fondo estrellas : estrella) {
            estrellas.onDraw(canvas, estrellaColor, 10, 5);

        }


        if (nave.get_vida() > 0) {
            nave.onDraw(canvas, bmp_nave);
            nave.indicadorVida(canvas);
        }


        if (badnave.vida > 0) {
            badnave.onDraw(canvas);
            badnave.indicadorVida(canvas);
        }

        if ( nave.disparado() &&  nave.get_vida() > 0){

            for (int i = 0; i < 3 ; i++) {

                bala.onDraw(canvas, i, y);
            }
            bala.set_levantado(false);

            accionesNavecitas(canvas);
            golpearNaveMayor(canvas, bala.get_x(), bala.get_posInicialY());

        }else{
            bala.set_levantado(true);

        }

        for (BadNavecita badNavecitas : badNavecita) {
            badNavecitas.onDraw(canvas);

        }

        if (nave.meHanDado(badnave.get_posX(), badnave.get_posY())) {
            nave.set_vida(2);
        }

        //Log.e("Vida de ni nave: ", nave.get_vida() + " <");

    }

    public void pausar() {

        if( touched &&
            x >= this.getWidth() - (this.getWidth()/20)  && x <= this.getWidth() - (this.getWidth()/20) + pausa.getWidth() &&
            y >= this.getHeight() - (this.getHeight() / 10) && y <= this.getHeight() - (this.getHeight() / 10) + pausa.getHeight()){

            if (gameLoopThread.getRunning()){

                gameLoopThread.setRunning(false);

                Log.e("Pausa", "(ire" + gameLoopThread.getState());
                Log.e("Pausa", "ire" + gameLoopThread.getRunning());
            }


        }

    }

    public void continuar(Canvas canvas) {
        if (!gameLoopThread.getRunning()) {

            resume = BitmapFactory.decodeResource(getResources(), R.drawable.resume);

            canvas.drawBitmap(resume, this.getWidth() / 2 - (resume.getWidth() / 2), this.getHeight() /2  - (resume.getHeight() / 2), null);

            if (touched &&
                x >= this.getWidth() / 2 - (resume.getWidth() / 2) &&
                x <= this.getWidth() / 2 - (resume.getWidth() / 2) + resume.getWidth() &&
                y <= this.getHeight() / 2 - (resume.getHeight() / 2) &&
                y >= this.getHeight() / 2 - (resume.getHeight() / 2) + resume.getHeight()
            ){

                gameLoopThread.setRunning(true);
                Log.e("resume", "(ne" + gameLoopThread.getRunning());


            }

                    }
    }
}
