package graiton.res.aplicacio24;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafic {
    private Drawable drawable;
    private int cenX, cenY;
    private int amplada, altura;
    private double incX, incY;
    private double angle, rotacio;
    private int radiColisio;
    private int xAnterior, yAnterior;
    private int radiInval;
    private View view;

    public Grafic(View view, Drawable drawable){
        this.view = view;
        this.drawable = drawable;
        amplada = drawable.getIntrinsicWidth();
        altura = drawable.getIntrinsicHeight();
        radiColisio = (altura+amplada)/4;
        radiInval = (int)Math.hypot(amplada/2,altura/2);
    }
    public void dibuixaGrafic(Canvas canvas){
        int x = cenX - amplada/2;
        int y = cenY - altura/2;
        drawable.setBounds(x,y,x+amplada, y+altura);
        canvas.save();
        canvas.rotate((float)angle, cenX, cenY);
        drawable.draw(canvas);
        canvas.restore();
        view.invalidate(cenX-radiInval, cenY-radiInval, cenX+radiInval, cenY+radiInval);
        view.invalidate(xAnterior-radiInval, yAnterior-radiInval, xAnterior+radiInval, yAnterior+radiInval);
        xAnterior = cenX;
        yAnterior = cenY;
    }
    public void incrementaPos(double factor){
        cenX += incX*factor;
        cenY += incY*factor;
        angle += rotacio*factor;

        if(cenX < 0) cenX = view.getWidth();
        if(cenX > view.getWidth()) cenX = 0;
        if(cenY < 0) cenY = view.getHeight();
        if(cenY > view.getHeight()) cenY = 0;
    }
    public double distancia(Grafic g){
        return Math.hypot(cenX-g.cenX, cenY-g.cenY);
    }

    public boolean verificaColisio(Grafic g){
        return (distancia(g) < (radiColisio+g.radiColisio));
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getCenX() {
        return cenX;
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public int getCenY() {
        return cenY;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public int getAmplada() {
        return amplada;
    }

    public void setAmplada(int amplada) {
        this.amplada = amplada;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public double getIncX() {
        return incX;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public double getIncY() {
        return incY;
    }

    public void setIncY(double incY) {
        this.incY = incY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRotacio() {
        return rotacio;
    }

    public void setRotacio(double rotacio) {
        this.rotacio = rotacio;
    }

    public int getRadiColisio() {
        return radiColisio;
    }

    public void setRadiColisio(int radiColisio) {
        this.radiColisio = radiColisio;
    }

    public int getxAnterior() {
        return xAnterior;
    }

    public void setxAnterior(int xAnterior) {
        this.xAnterior = xAnterior;
    }

    public int getyAnterior() {
        return yAnterior;
    }

    public void setyAnterior(int yAnterior) {
        this.yAnterior = yAnterior;
    }

    public int getRadiInval() {
        return radiInval;
    }

    public void setRadiInval(int radiInval) {
        this.radiInval = radiInval;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
