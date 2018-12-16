package graiton.res.aplicacio24;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VistaJoc extends View implements SensorEventListener {

    // so
    private SoundPool soundPool;
    private int idDispar, idExplosio;

    private Context context;

    // Variables pel Missil
    private Grafic missil;
    private static int PAS_VELOCITAT_MISSIL = 12;
    //private boolean missilActiu = false;
    //private int tempsMissil;
    private List<Integer> tempsMissils = new ArrayList<Integer>();
    private List<Boolean> missilsActiu = new ArrayList<Boolean>();

    private List<Grafic> missils = new ArrayList<Grafic>();
    private int numMissils = 20;
    private static int missilActual = 0;

    private Grafic nau;
    private int girNau;
    private double acceleracioNau;
    private static final int MAX_VELOCITAT_NAU = 20;
    private static final int PAS_GIR_NAU = 5;
    private static final float PAS_ACELERACIO_NAU = 0.5f;
    private Vector<Grafic> asteroides;
    private int numAsteroides = 5;
    private int numFragments = 3;
    // FILS I TEMPS
    // Fil encarregat de processar el joc
    private ThreadJoc fil = new ThreadJoc();
    // Cada quan volem processar canvis (ms)
    private static int PERIODE_PROCES=50;
    // Quan es va realitzar el darrer procés
    private long darrerProces=0;

    // Manejador d'events de la pantalla tàctil per la nau
    private float mX=0, mY = 0;
    private boolean dispar = false;

    // Guardem si l'usuari vol jugar amb sensor de moviment
    private boolean useSensor;

    private SensorManager mSensorManager;

    public VistaJoc(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        Drawable drawableNau, drawableAsteroide, drawableMissil;

        //drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(pref.getString(getResources().getString(R.string.pa2_key), "1").equals("0")){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Path pathNau = new Path();
            pathNau.moveTo((float)0,(float)0);
            pathNau.lineTo((float)1,(float)0.5);
            pathNau.lineTo((float)0,(float)1);
            pathNau.lineTo((float)0,(float)0);
            ShapeDrawable dNau = new ShapeDrawable(new PathShape(pathNau,1,1));
            dNau.getPaint().setColor(Color.WHITE);
            dNau.getPaint().setStyle(Paint.Style.FILL);
            dNau.setIntrinsicWidth(20);
            dNau.setIntrinsicHeight(15);
            drawableNau = dNau;
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float)0.3, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.3);
            pathAsteroide.lineTo((float)0.8, (float)0.2);
            pathAsteroide.lineTo((float)1.0, (float)0.4);
            pathAsteroide.lineTo((float)0.8, (float)0.6);
            pathAsteroide.lineTo((float)0.9, (float)0.9);
            pathAsteroide.lineTo((float)0.8, (float)1.0);
            pathAsteroide.lineTo((float)0.4, (float)1.0);
            pathAsteroide.lineTo((float)0.0, (float)0.6);
            pathAsteroide.lineTo((float)0.0, (float)0.2);
            pathAsteroide.lineTo((float)0.3, (float)0.0);
            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1,1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);

            // Gràfic vectorial Missil
            ShapeDrawable dMissil = new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil = dMissil;

        }else{
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNau = context.getResources().getDrawable(R.drawable.nau);
            drawableMissil = context.getResources().getDrawable(R.drawable.missil1);
        }

        missil = new Grafic(this, drawableMissil);

        /*missils = new Vector<Grafic>();
        for (int i = 0; i < numMissils; i++){
            missil = new Grafic(this, drawableMissil);
            missils.add(missil);
        }*/
        nau = new Grafic(this, drawableNau);

        asteroides = new Vector<Grafic>();
        for (int i = 0; i< numAsteroides; i++){
            Grafic asteroide = new Grafic(this, drawableAsteroide);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int)(Math.random()*8-4));
            asteroide.setRotacio((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }
        // Registre el sensor d'orientació i indicar gestió d'events
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(!llistaSensors.isEmpty()){
            Sensor orientacioSensor = llistaSensors.get(0);
            mSensorManager.registerListener(this, orientacioSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        useSensor = pref.getBoolean(getResources().getString(R.string.key_sensor), true);

        // so
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        idDispar = soundPool.load(context, R.raw.dispar, 0);
        idExplosio = soundPool.load(context, R.raw.explosio, 0);
    }

    @Override
    protected void onSizeChanged(int ample, int alt, int ample_ant, int alt_ant){
        super.onSizeChanged(ample, alt, ample_ant, alt_ant);

        nau.setCenX(getMeasuredWidth()/2);
        nau.setCenY(getMeasuredHeight()/2);

        for(Grafic asteroide : asteroides){
            do{
                asteroide.setCenX((int)(Math.random()*ample));
                asteroide.setCenY((int)(Math.random()*alt));
            }while(asteroide.distancia(nau) < (ample+alt)/5);

        }
        // Llança un nou fil
        darrerProces = System.currentTimeMillis();
        fil.start();
    }
    @Override
    synchronized protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        nau.dibuixaGrafic(canvas);
        for(Grafic asteroide : asteroides){
            asteroide.dibuixaGrafic(canvas);
        }
        for(Grafic missil: missils){
            missil.dibuixaGrafic(canvas);
        }
        /*if(missilActiu){
            try {
                missils.get(missilActual).dibuixaGrafic(canvas);
            }catch(ArrayIndexOutOfBoundsException e){
                missilActual = 0;
                missils.get(missilActual).dibuixaGrafic(canvas);
            }
        }*/
    }

    // Actalitza els valors dels elements
    // És a dir, gestiona els moviments
    synchronized protected void actualitzaFisica(){
        // Hora actual en milisegons
        long ara = System.currentTimeMillis();
        // No fer res si el periode de proces NO s'ha complert
        if(darrerProces+PERIODE_PROCES > ara){
            return;
        }
        // Per una execució en temps real calcules retard
        double retard = (ara-darrerProces)/PERIODE_PROCES;
        darrerProces = ara; // Per la propera vegada
        // Actualitzem velocitat i direcció de la nau a partir de girNau i
        //acceleracióNau segons l'entrada del jugador
        nau.setAngle((int)(nau.getAngle()+girNau*retard));
        double nIncX = nau.getIncX()+acceleracioNau*Math.cos(Math.toRadians(nau.getAngle()))*retard;
        double nIncY = nau.getIncY()+acceleracioNau*Math.sin(Math.toRadians(nau.getAngle()))*retard;

        // Actualitzem si el mòdul de la velocitat no passa el màxim
        if(Math.hypot(nIncX, nIncY) <= MAX_VELOCITAT_NAU){
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }
        // Actualitzem les posicions X i Y
        nau.incrementaPos(retard);
        for(int i = 0;i < asteroides.size(); i++){
            asteroides.get(i).incrementaPos(retard);
        }
        //  Actualitzem posició del missil
        for(int i = 0; i<missils.size(); i++){
            missils.get(i).incrementaPos(retard);
            double aux = tempsMissils.get(i) - retard;
            tempsMissils.set(i, (int)aux);
            if(aux < 1){
                missils.remove(i);
                tempsMissils.remove(i);
            }
            if(!missils.isEmpty()){
                for (int u = 0; u < asteroides.size(); u++){
                    if(missils.get(i).verificaColisio(asteroides.get(u))){
                        destrueixAteroide(u);
                        missils.remove(i);
                        break;
                    }
                }
            }
        }
       /* if(missilActiu){
            try {
                missils.get(missilActual).incrementaPos(retard);
            }catch(ArrayIndexOutOfBoundsException e){
                missilActual = 0;
                missils.get(missilActual).incrementaPos(retard);
            }

            tempsMissil-=retard;
            if(tempsMissil<0) missilActiu = false;
            else{
                for (int i = 0; i < asteroides.size(); i++){
                    if(missils.get(missilActual).verificaColisio(asteroides.get(i))){
                        destrueixAteroide(i);
                        break;
                    }
                }
            }
        }*/

    }

    private boolean hihaValorInicial = false;
    private float valorInicial;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (useSensor) {
            float valor = event.values[1]; //eix y
            if (!hihaValorInicial) {
                valorInicial = valor;
                hihaValorInicial = true;
            }
            girNau = (int) (valor - valorInicial);
        }
    }

    // Gestió d'events de sensors per la nau
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class ThreadJoc extends Thread{
        private boolean pausa, corrent;

        public synchronized void pausar(){
            pausa=true;
        }
        public synchronized void reanudar(){
            pausa = false;
            notify();
        }
        public synchronized void aturar(){
            corrent = false;
            if(pausa) reanudar();
        }

        public void run(){
            corrent = true;
            while (corrent){
                actualitzaFisica();
                synchronized (this){
                    while (pausa){
                        try{
                            wait();
                        }catch(Exception e){

                        }
                    }
                }
            }
        }
    }

    // Gestió d'events de la nau amb pantalla tàctil
    @Override
    public boolean onTouchEvent(MotionEvent mevent){

        super.onTouchEvent(mevent);
        float x = mevent.getX();
        float y = mevent.getY();
        switch (mevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispar = true;
                break;
            case MotionEvent.ACTION_MOVE:

                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    if (!useSensor) {
                        girNau = Math.round((x - mX) / 2);
                        dispar = false;
                    }
                } else if (dx < 6 && dy > 6) {
                    acceleracioNau = Math.round((mY - y) / 25);
                    dispar = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                girNau = 0;
                acceleracioNau = 0;
                if (dispar) {
                    ActivaMissil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;

    }

    // Mètodes auxiliars
    private void destrueixAteroide(int i){
        asteroides.remove(i);
        soundPool.play(idExplosio, 1,1,0,0,1);
        //missilActiu = false;
    }

    private void ActivaMissil(){
        /*System.out.println("----entra missilActual "+missilActual);
        try{
            missils.get(missilActual).setCenX(nau.getCenX());
        }catch(ArrayIndexOutOfBoundsException e){
            missilActual = 0;
            missils.get(missilActual).setCenX(nau.getCenX());
        }
        missils.get(missilActual).setCenX(nau.getCenX());
        missils.get(missilActual).setCenY(nau.getCenY());
        missils.get(missilActual).setAngle(nau.getAngle());
        missils.get(missilActual).setIncX(Math.cos(Math.toRadians(missils.get(missilActual).getAngle()))*PAS_VELOCITAT_MISSIL);
        missils.get(missilActual).setIncY(Math.sin(Math.toRadians(missils.get(missilActual).getAngle()))*PAS_VELOCITAT_MISSIL);
        tempsMissil = (int)Math.min(this.getWidth()/Math.abs(missils.get(missilActual).getIncX()),this.getHeight()/Math.abs(missils.get(missilActual).getIncY()))-2;
        missilActiu = true;
        missilActual++;*/
        Drawable drawableMissil = this.context.getResources().getDrawable(R.drawable.missil1);
        missil = new Grafic(this, drawableMissil);
        missil.setCenX(nau.getCenX());
        missil.setCenY(nau.getCenY());
        missil.setAngle(nau.getAngle());
        missil.setIncX(Math.cos(Math.toRadians(missil.getAngle()))*PAS_VELOCITAT_MISSIL);
        missil.setIncY(Math.sin(Math.toRadians(missil.getAngle()))*PAS_VELOCITAT_MISSIL);
        double tempsMissil = (int)Math.min(this.getWidth()/Math.abs(missil.getIncX()),this.getHeight()/Math.abs(missil.getIncY()))-2;
        missils.add(missil);
        tempsMissils.add((int)tempsMissil);
        soundPool.play(idDispar, 1,1,1,0,1);
    }
    public ThreadJoc getFil() {
        return fil;
    }
    public SensorManager getmSensorManager(){
        return mSensorManager;
    }
}
