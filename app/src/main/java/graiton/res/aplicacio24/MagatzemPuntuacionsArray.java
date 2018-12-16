package graiton.res.aplicacio24;

import java.util.Vector;

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray(){
        puntuacions = new Vector<String>();
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martínez");
        puntuacions.add("011000 Paco Pérez");
    }
    @Override
    public void guardarPuntuacio(int punts, String nom, long data){
        puntuacions.add(0, punts+" "+nom);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        return puntuacions;
    }
}
