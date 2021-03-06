package com.aliens.aliensvsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

/* Este es el archivo mas extenso porque se encarga de realizar la mayoria de los procesos, aqui se
cargan todas las imagenes de naves
 */

public class GameView extends SurfaceView {

    // Se guarda en una variable la pantalla de inicio para despues usarse
    private Inicio inicio;

    // Los bitmaps son todoas la imagenes que se usaran

    // Tu nave
    private Bitmap bmp_nave, xplo;

    // Las diferentes naves malvadas grandes
    private Bitmap bmp_badnave1,bmp_badnave2,bmp_badnave3;

    // las diferentes naves pequeñas
    private Bitmap bmp_bad_navecita, bmp_bad_navecita2, bmp_bad_navecita3;

    //Imagenes de las constelaciones que aparecen al elegir nivel
    private Bitmap const1, const2, const3;

    //imagen de los botones de pausa y resume
    private Bitmap pausa, resume;


    // Esta clase es necesario para poder ejecutar la aplicacion
    private SurfaceHolder holder;

    //CArga el arhico GAmeLoopThread.java que es el encargado de crear el movimiento de los objetos
    // que se pintan en la pantalla
    private GameLoopThread gameLoopThread;

    // Carga las caracteristicas de la constelacion que se haya elegido
    private Escenario escenario;

    // Carga el archivo nave.java que es donde se gestiona todo lo referente a tu nave, como salud
    // o si ten han tocado o si has disparado
    private Nave nave;

    // Lo mismo que la anterior, en el archivo se describe más a detalle
    private BadNave badnave;

    // se crea una "Lista" o sea un grupo de este mismo archivo o objeto, ya que tieenen que ser varias
    // naves del mismo tipo
    private List<BadNavecita> badNavecita = new ArrayList<BadNavecita>();

    // ARchivo que dibuja las balas
    private Bala bala;

    // Para el fondo en movimiento se crea una "lista" de estrellas para que se muevan de manera independiente
    private List<Fondo> estrella = new ArrayList<Fondo>();

    // variable que determina si se ha tocado la pantalla
    public boolean touched;
    public boolean multi;// Esta variable no tiene un uso, solo detecta si se ha tocado la pantalla con mas de un dedo

    //variables para darle a las constelaciones color de fondo, color de estrella y velocidad, la variable indice no tiene uso.
    public int indice, colorEstrella, velocidad, nivel = 0, fondoColor;

    public int x; //variable que determina tu posicion en X
    public int y; //variable que determina tu posicion en Y

    private int danho; //VAriable que determina en daño que les causas a las naves enemigas dependiendo el nivel

    private List<TempSprite> temps = new ArrayList<TempSprite>();

    SoundPool soundPool;
    int fondom, disparo1, colision1, colision2, explosion1, explosion2;
    Random rnd; // Variable para obtener un numero aleatorio para que las naves y estrellas se muestren en lugares al azar
    long lastClick;
    boolean ultimo;

    // Se carga aqui todo lo necesario para el funcionamiento
    public GameView(Context context){

        super(context);




        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                // se ejecuta esto si se ha finalizado la aplicacion.

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

                // Se carga el archivo gameLoopThread.java que se encarga de crear lo ciclos para
                // lo movimientos de los objetos

                // este metodo crea las variables, si no se crean aqui puede mandar errores
                createEveryThing();


                gameLoopThread.setRunning(true);
                // Inicial el ciclo del Juego
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Aqui no sucede NADA
            }
        });


        // Se crean todos los recursos graficos

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

        xplo = BitmapFactory.decodeResource(getResources(), R.drawable.xplo);

        soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        fondom = soundPool.load(context, R.raw.fondom, 0);
        disparo1 = soundPool.load(context, R.raw.disparo, 0);
        colision1 = soundPool.load(context, R.raw.impact193, 0);
        colision2 = soundPool.load(context, R.raw.impact197, 0);
        explosion1 = soundPool.load(context, R.raw.explosion, 0);
        explosion2 = soundPool.load(context, R.raw.shot, 0);




      }

    public void createEveryThing(){

        // Este metodo crea todos los objetos antes de ejecutar cualquier accion para que no mande error

        inicio = new Inicio(this); // pantalla inicio
        escenario = new Escenario(this); // escenarios

        rnd = new Random(); //numero aleatorio

        for (int i = 0; i < 25; i++) {
            synchronized (getHolder()){  // esto se hace para que no haya conflictos en los procesos
                try {
                    // Se crean 25 veces las estrellas del fondo.

                    estrella.add(new Fondo(this,rnd.nextInt(this.getWidth()), rnd.nextInt(this.getHeight())));
                } catch (Exception e) {}
            }
        }

        nave = new Nave(this); // Tu nave
        bala = new Bala(this); // las balas que lanzan las naves
        badnave = new BadNave(this); // la nave mayor enemiga

        for (int i = 0; i < 5 ; i++) {

            // se crean las naves pequeñas enemigas, 10 veces
            badNavecita.add(new BadNavecita(this, rnd.nextInt(this.getWidth() / 2) + 200, rnd.nextInt(this.getHeight()) - 10));
        }



    }


    // Este metodo se encarga de dibujar todo en pantalla
    @Override

    protected void onDraw(Canvas canvas) {

        if (System.currentTimeMillis() - lastClick > 6000) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                soundPool.play(fondom, 1, 1, 1, 0, 1);
            }
        }

        Log.e("Mi Vida", "(" + nave.get_vida());
        if (inicio.get_iniciado() && !escenario.getFueConstSeleccionada()) {


            elegirConstelacion(canvas); // Si se ha tocado Comenzar aparece elegir constelacion, este metodo guarda la constelacion elegida
        }
        if (inicio.get_iniciado() && escenario.getFueSeleccionada()) { // Si ya se seleccionó constelacion se ejecuta esto:

            if (escenario.getNaveSeleccionada() == 1 && nivel == 0){ // Si se seleccionó la primera constelacion carga lo siguiene:

                fondoColor = Color.BLACK; // Fondo negro
                colorEstrella = Color.WHITE; // EStrellas blancas
                velocidad = 20; // Velocidad de estrellas
                badnave.setBitmap(bmp_badnave1); // La nave malvada
                danho = 10; // El daño que le provocas

                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita2); // las navecitas enemigas
                }

            }/*else if (escenario.getNaveSeleccionada() == 2) { // Similar que lo anterior pero con otros valores
                color = Color.BLUE;
                colorEstrella = Color.GREEN;
                velocidad = 15;
                badnave.setBitmap(bmp_badnave2);
                danho = 3;
                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita);
                }
            }else if (escenario.getNaveSeleccionada() == 3) { // Lo mismo de lo anterior
                color = Color.MAGENTA;
                colorEstrella = Color.BLUE;
                velocidad = 30;
                badnave.setBitmap(bmp_badnave3);
                danho = 1;
                for ( int i = 0; i < badNavecita.size(); i ++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita3);
                }*/


            batalla(canvas); // CArga la pantalla de batalla con los valores dados anteriormente

            escenario.setFueConstSeleccionada(true); // metodo para hacer desaparecer la pantalla de seleccion

            Paint score = new Paint();
            score.setColor(Color.GRAY);
            score.setTextSize(this.getHeight() / 20);
            score.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Puntos: " + nave.get_puntuacion(), this.getWidth() / 2, 50, score);

            // Dibuja el boton de pausa
            canvas.drawBitmap(pausa, this.getWidth() - (this.getWidth()/20), this.getHeight() - (this.getHeight() / 10), null);

            // ejecuta la acción de pausa
            pausar();

            // ejecuta la accion de restaurar despues de pausado
            continuar(canvas);
        }

        if (!inicio.get_iniciado()) { // Esto es lo que muestra al ejecutar el juego

            escenario.setFueConstSeleccionada(false);
            escenario.setFueSeleccionada(false);
            nave.setSalud(100);
            nivel = 0;
            nave.puntuacion = 0;
            Log.e("Inicio", "-");
            canvas.drawColor(Color.BLUE); // Color de fondo

            for (Fondo estrellas : estrella) {
                estrellas.onDraw(canvas, Color.YELLOW, 30, 5);  // Color de estrellas y velocidad
            }

            inicio.onDraw(canvas); // Dibuja la pantalla de inicio, desde el archivo inicio.java
            inicio.comenzar(); // ejecuta el metodo comenzar del arhico inicio.java
        }
    }


    public void elegirConstelacion ( Canvas canvas) {  // Esto se muestra cuando se va a seleccionar nivel

        canvas.drawColor(Color.BLACK); // color de fondo
        for (Fondo estrellas : estrella) {
            estrellas.onDraw(canvas, Color.GREEN, 8, 3); // Color de estrellas, tamaño y velocidad

        }

        Paint txt = new Paint();  // Este bloque escribe texto en la pantalla
        txt.setColor(Color.WHITE);
        txt.setTextSize(this.getHeight() / 15); // el tamaño del texto es de 1/15 del tamaño de la pantall
        txt.setTextAlign(Paint.Align.CENTER); // alinea al centro
        // Texto que muestra, mas posicion en X, Y. x = la mitad de la pantalla, y = 1/6 de la pantalla
        canvas.drawText("Seleccione constelación", this.getWidth() / 2, this.getHeight() / 6, txt);

        //Muestra las imagenes de los tres niveles a elegir:
                         // img , posicion en X       , posicion en Y       ,
        canvas.drawBitmap(const1, this.getWidth() / 10, this.getHeight() / 2, null);
        //canvas.drawBitmap(const2, (this.getWidth() / 10) * 4, this.getHeight() / 2, null);
        //canvas.drawBitmap(const3, (this.getWidth() / 10) * 7, this.getHeight() / 2, null);

        // Este if es para saber que imagen has tocado, este es para la segunda (no primera, está asi solo porque olvide corregir pero no influye en el juego).

        /*if( touched && // si se ha tocado
             // X y Y es donde has tocado, sí es en donde se encuesntra esta imagen, se ejecuta
                x >= (this.getWidth() / 10) * 4  && x <= (this.getWidth() / 10) * 4 + const2.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const2.getHeight()){

            escenario.setNaveSeleccionada(2); // Se le indica que se ha seleccionado el escenario 2
            escenario.setFueSeleccionada(true); // y que ha sido seleccionada
        }*/
        // para la segunda
        if ( touched &&
                x >= this.getWidth() / 10  && x <= this.getWidth() / 10 + const1.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const1.getHeight()){

            escenario.setNaveSeleccionada(1);
            escenario.setFueSeleccionada(true);
        }
        /*/ para la tercera
        if( touched &&
                x >= (this.getWidth() / 10) * 7  && x <= (this.getWidth() / 10) * 7 + const3.getWidth() &&
                y >= this.getHeight() / 2 && y <= this.getHeight() / 2 + const3.getHeight()){

            escenario.setNaveSeleccionada(3);
            escenario.setFueSeleccionada(true);
        }*/

    }

    public void golpearNaveMayor(Canvas canvas, int bx, int by) {  // Se ejecuta si una bala ha golpeado a la nave mayor

        if ( badnave.leHasDado(bx, by) && badnave.get_vida() > 0 ) {
            dibujarGolpe(canvas, bx, by, Color.RED); // Dibuja el golpe
            badnave.set_vida(danho); // Se le quita vida
            badnave.set_nvl_indicador(badnave.get_nivel());// Para que el indicador baje
            nave.set_puntuacion(15); // Sube tu puntaje
            soundPool.play(colision1, 1, 1, 1, 0, 1);
        }
    }


    public void accionesNavecitas(Canvas canvas) { //Acciones para navecitas

        synchronized (getHolder()) { // Esto evita conflictos en el juego

            for ( int i = badNavecita.size() - 1; i >= 0; i--) {
                BadNavecita bn = badNavecita.get(i);

                if (nave.meHanDado(bn.get_posX(), bn.get_posY())){  // si una navecita me ha golpeado
                    nave.set_vida(1);   // se me quita Salud o Vida
                    synchronized (getHolder()) {
                        soundPool.play(colision2, 1, 1, 1, 0, 1);
                    }

                    dibujarGolpe(canvas, nave.posAncho + nave.bmpW, nave.posAlto + nave.bmpH, Color.YELLOW);
                }

                if (bn.leHasDado(bala.get_x(), bala.get_posInicialY()) ){ // Si yo he golpeado a una

                    dibujarGolpe(canvas, bala.get_x(), bala.get_posInicialY(),Color.RED); // Dibuja golpe
                    nave.set_puntuacion(10); // Aumenta puntuacion

                    bn.set_vida(danho);  // Quita Salud o Vida a nave
                    soundPool.play(colision1, 1, 1, 1, 0, 1);

                    if (bn.get_vida() <= 0){     // Si su salud llega a Cero
                        nave.set_puntuacion(20); // Me dan 20 puntos

                        badNavecita.remove(bn); // y desaparece la nave
                        break;
                    }
                }
            }
        }
    }

    public void dibujarGolpe(Canvas canvas, int x, int y, int color){  // Dibuja el golpe al darle con una bala
        Paint golpe = new Paint();
        golpe.setColor(color);
        golpe.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x - 5,y - 5, 30, golpe);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // Este metodo obtiene si has tocado la pantalla, si has movido el dedo, si lo has quitado y las coordenadas de donde has tocado

        int action = MotionEventCompat.getActionMasked(event); // obtiene la accion

        //Obtiene el index del pointer asociado con la accion, pero NO se usa

        int index = MotionEventCompat.getActionIndex(event);

        if (event.getPointerCount() > 1) {

            x = (int) MotionEventCompat.getX(event, index); // Guarda la posicion de donde has tocado en X
            y = (int) MotionEventCompat.getY(event, index); // Guarda la posicion de donde has tocado en Y

            get_evento(action, true, indice); // SE ejecuta el metodo get_evento

        } else {  // Igual que lo anterior, esto era para saber si era multitouch o no, pero aqui no hay diferencia

            x = (int) MotionEventCompat.getX(event, index);
            y = (int) MotionEventCompat.getY(event, index);

            get_evento(action, false, indice);
        }
        return true;
    }


    public void get_evento(int action, boolean sondos, int index_event){ // Obtiene el evento que hayas hecho, tocar, mover

        switch (action) {

            case MotionEvent.ACTION_DOWN:  // si has tocado la pantalla

                touched = true;
                multi = sondos;
                indice = index_event;

                // Para restaurar, si se ha tocado el boton de pausa
                if (!gameLoopThread.getRunning()) {
                    gameLoopThread.setRunning(true);
                    gameLoopThread.run();
                }

                break;

            case MotionEvent.ACTION_MOVE: // si has movido el dedo

                touched = true;
                multi = sondos;
                indice = index_event;
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // no utilizado

                touched = true;
                multi = sondos;
                indice = index_event;
                break;

            case MotionEvent.ACTION_POINTER_UP: // no utilizado

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_UP: // Si has quitado el dedo de la pantall

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_OUTSIDE: // si el dedo esta afuera de la pantalla

                touched = false;
                multi = false;
                break;

            case MotionEvent.ACTION_CANCEL:

                touched = false;
                multi = false;
                break;

        }
    }

    // Este metodo dibuja la Batalla, obtiene como parametros:
                                      // Color de estrellas, color fondo,  velocidad estrella
    public void batalla(Canvas canvas){

        canvas.drawColor(fondoColor); // Da el color de fondo

        for (Fondo estrellas : estrella) {
            estrellas.onDraw(canvas, colorEstrella, velocidad, 5); // Dibuja las estrellas

        }



        if (nave.get_vida() > 0) { // Dibuja minave si aun tengo VIDA o Salud
            nave.onDraw(canvas, bmp_nave);
            nave.indicadorVida(canvas);
        }

        if (badnave.vida > 0) { // Dibuja la nave si aun tiene VIde
            badnave.onDraw(canvas);
            badnave.indicadorVida(canvas);
        }
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }

        if ( nave.disparado() &&  nave.get_vida() > 0){  // Dibuja las balas si he disparado y si aun sigo vivo

            for (int i = 0; i < 3 ; i++) {

                bala.onDraw(canvas, i, y); // Dibuja 3 balas

            }
            bala.set_levantado(false);
            accionesNavecitas(canvas); // Ejecuta las acciones de las navecitas
            golpearNaveMayor(canvas, bala.get_x(), bala.get_posInicialY()); //ejecuta esto por si he golpeado a la nave mayor

        }else{
            bala.set_levantado(true); // Indica que no he tocado la pantalla para que no muestre las balas

        }

        for (BadNavecita badNavecitas : badNavecita) {
            badNavecitas.onDraw(canvas); // Dibuja todas las navecitas

        }

        if (nave.meHanDado(badnave.get_posX(), badnave.get_posY())) { // si me tocan las naves enemigas
            nave.set_vida(2); // Si me han tocado me disminuye la vida en 2
            dibujarGolpe(canvas, nave.posAncho + nave.bmpW, nave.posAlto + nave.bmpH, Color.YELLOW);
            soundPool.play(disparo1, 1, 1, 0, 0, 1);
        }

        if (nave.get_vida() <= 0) {

            temps.add(new TempSprite(temps, this, nave.posAncho, nave.posAlto, xplo));

            gameOver(canvas);
            if (System.currentTimeMillis() - lastClick > 4000) {
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()) {
                    soundPool.play(explosion1, 1, 1, 1, 0, 1);
                }
            }

        }

        if (badnave.get_vida() <= 0) {
            if (System.currentTimeMillis() - lastClick > 4000) {
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()) {
                    soundPool.play(explosion1, 1, 1, 1, 0, 1);
                }
            }
            temps.add(new TempSprite(temps, this, badnave.get_posX(), badnave.get_posY(), xplo));


        }

        if (badnave.get_vida() <= 0 && badNavecita.size() <= 0) {
            if(nivel == 0) nivel = 1;
            avanzarNivel(canvas);
        }

        if (ultimo && badnave.get_vida() <= 0 && badNavecita.size() <= 0) {
            hasGanado(canvas);
        }
    }

    public void hasGanado(Canvas canvas) {
        Paint ganado = new Paint();
        ganado.setColor(Color.GREEN);
        ganado.setTextSize(this.getHeight() / 3);
        ganado.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Ganaste!!", this.getWidth() / 2, this.getHeight() / 4, ganado);

        Paint pnts = new Paint();
        pnts.setColor(Color.GREEN);
        pnts.setTextSize(this.getHeight() / 5);
        pnts.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Tu puntaje: " + nave.get_puntuacion(), this.getWidth() / 2, (this.getHeight() / 4) * 3, pnts);


        Paint cont  = new Paint();

        cont.setColor(Color.YELLOW);
        cont.setStyle(Paint.Style.FILL);
        canvas.drawRect(this.getWidth() / 8 * 3, this.getHeight() / 8 * 6, this.getWidth() / 8 * 5, this.getHeight() / 8 * 7, cont);

    }

    public void gameOver(Canvas canvas) {

        ultimo = false;
        Log.e("Game over", "-");
        Paint gover = new Paint();
        gover.setColor(Color.RED);
        gover.setTextSize(this.getHeight() / 4);
        gover.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over!", this.getWidth() / 2, this.getHeight() / 2, gover);

        Paint cont  = new Paint();

        cont.setColor(Color.YELLOW);
        cont.setStyle(Paint.Style.FILL);
        canvas.drawRect(this.getWidth() / 8 * 3, this.getHeight() / 8 * 6, this.getWidth() / 8 * 5, this.getHeight() / 8 * 7, cont);

        Paint msgCont = new Paint();
        msgCont.setColor(Color.RED);
        msgCont.setTextAlign(Paint.Align.CENTER);
        msgCont.setTextSize(this.getHeight() / 32);
        canvas.drawText("Continuar", this.getWidth() / 2, this.getHeight() /16 * 13, msgCont);

        if( touched &&
            x >= this.getWidth() / 8 * 3 && x <= this.getWidth() / 8 * 5 &&
            y >= this.getHeight() / 8 * 6 && y <= this.getHeight() / 8 * 7) {

            inicio.set_iniciado(false);
        }
    }

    public void avanzarNivel(Canvas canvas) {
        Paint cuadro = new Paint();
        Paint msg_nivel = new Paint();

        cuadro.setColor(Color.YELLOW);
        canvas.drawRect((this.getWidth() / 8) * 3, (this.getHeight() / 8) * 3, (this.getWidth() / 8) * 5,(this.getHeight() / 8) * 5 , cuadro);

        msg_nivel.setColor(Color.BLACK);
        msg_nivel.setTextSize(this.getHeight() / 32);
        msg_nivel.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Completado", this.getWidth() / 2, this.getHeight() / 16 * 7, msg_nivel);
        canvas.drawText("Toca aquí para continuar", this.getWidth() / 2, (this.getHeight() / 16) * 9, msg_nivel);


        if (touched &&
            x >= this.getWidth() / 8 * 3 && x <= this.getWidth() / 8 * 5 &&
            y >= this.getHeight() / 8 * 3 && y <= this.getHeight() / 8 * 5) {

            if (nivel == 2) {
                this.nivel = 2;
                this.fondoColor = Color.MAGENTA;
                this.colorEstrella = Color.RED;
                this.velocidad = 5;
                this.badnave.setBitmap(bmp_badnave3);
                this.danho = 1;
                ultimo = true;
                for (int i = 0; i < 10; i++) {
                    badNavecita.add(new BadNavecita(this, rnd.nextInt(this.getWidth() / 2) + 200, rnd.nextInt(this.getHeight()) - 10));
                }


                for (int i = 0; i < badNavecita.size(); i++) {
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita3);
                }
                badnave.set_vida(-100);

            }

            if (nivel == 1) {

                this.nivel = 2;
                this.fondoColor = Color.BLUE;
                this.colorEstrella = Color.GREEN;
                this.velocidad = 15;
                this.badnave.setBitmap(bmp_badnave2);
                this.danho = 5;

                for (int i = 0; i < 10; i++) {
                    badNavecita.add(new BadNavecita(this, rnd.nextInt(this.getWidth() / 2) + 200, rnd.nextInt(this.getHeight()) - 10));
                    BadNavecita bnvct = badNavecita.get(i);
                    bnvct.setBitmap(bmp_bad_navecita);
                }

                badnave.set_vida(-100);

                nivel = 2;
            }


        }
    }

    public void pausar() { // Se ejecuta para pausar

        if( touched && // Si se ha tocado el boton
            x >= this.getWidth() - (this.getWidth()/20)  && x <= this.getWidth() - (this.getWidth()/20) + pausa.getWidth() &&
            y >= this.getHeight() - (this.getHeight() / 10) && y <= this.getHeight() - (this.getHeight() / 10) + pausa.getHeight()){

            if (gameLoopThread.getRunning()){ // Se detiene el ciclo

                gameLoopThread.setRunning(false);

            }
        }
    }

    public void continuar(Canvas canvas) { // Para restaurar el juego.
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
            }
        }
    }
}
