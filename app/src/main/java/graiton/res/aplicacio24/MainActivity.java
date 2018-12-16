package graiton.res.aplicacio24;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, GestureOverlayView.OnGesturePerformedListener {

    public static MagatzemPuntuacions magatzem = new MagatzemPuntuacionsArray();
    private Button bt1, bt2, bt3, bt4;
    private TextView titol;
    private GestureLibrary llibreria;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp=MediaPlayer.create(this, R.raw.espana);
        bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(this);
        /*bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llancarJoc(null);
            }
        });*/

        bt2 = (Button)findViewById(R.id.button2);
        bt2.setOnClickListener(this);
        bt2.setOnLongClickListener(this);
        /*bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llancarConfig(null);
            }
        });*/
        /*bt2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mostrarPreferencies(null);
                return true;
            }
        });*/

        bt3 = (Button)findViewById(R.id.button3);
        bt3.setOnClickListener(this);
        /*bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llancarAcercaDe(null);
            }
        });*/

        bt4 = (Button)findViewById(R.id.button4);
        bt4.setOnClickListener(this);
        bt4.setOnLongClickListener(this);
        /*bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                llancarPuntuacions(v);
                return true;
            }
        });*/
        //bt4.setBackgroundResource(R.drawable.degradat);

        // Gestures Part
        llibreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!llibreria.load())finish();
        GestureOverlayView gestureView = (GestureOverlayView)findViewById(R.id.gestures);

        gestureView.addOnGesturePerformedListener(this);
        titol = (TextView)findViewById(R.id.textView);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(mp == null) mp=MediaPlayer.create(this, R.raw.espana);
        if(!mp.isPlaying()) mp.start();
    }
    @Override
    protected void onPause(){
        //mp.stop();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }
    @Override
    protected void onStop(){
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        //mp.stop();
        mp.pause();
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        /*mp=MediaPlayer.create(this, R.raw.madrid);
        mp.start();*/
    }

    @Override
    protected void onDestroy(){
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    /*protected void onStop(){

    }*/

    @Override
    public void onGesturePerformed(GestureOverlayView ov, Gesture gesture){
        ArrayList<Prediction> predictions = llibreria.recognize(gesture);
        titol.setText("");
        Prediction aux = predictions.get(0);
        for(Prediction prediction : predictions){
            if(!aux.equals(prediction)){
                if(aux.score<prediction.score){
                    aux = prediction;
                }
            }
        }

        titol.setText(aux.name);
        if(aux.name.equals("acercade")){
            llancarAcercaDe(null);
        }else if (aux.name.equals("cancelar")){
            finish();
        } else if (aux.name.equals("configurar")){
            llancarConfig(null);
        }else if (aux.name.equals("jugar")){
            llancarJoc(null);
        }else if (aux.name.equals("puntuaciones")){
            llancarPuntuacions(null);
        }else if (aux.name.equals("verpreferencias")){
            mostrarPreferencies(null);
        }
    }

    public void llancarJoc(View view){
        Intent i = new Intent(this, Joc.class);
        startActivity(i);
    }

    public void llancarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }
    private void llancarConfig(View view){
        Intent i = new Intent(this, Preferencies.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.config){
            llancarConfig(null);
            return true;
        }
        if(id == R.id.acercaDe){
            llancarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void mostrarPreferencies(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s="Musica: "+ pref.getBoolean(getResources().getString(R.string.pa1_key), false)+
                "\nTipus grafics "+ pref.getString(getResources().getString(R.string.pa2_key), "1")+
                "\nFragments "+ pref.getString( getResources().getString(R.string.pa3_key), "1")+
                "\nMultijugador "+ pref.getBoolean("check_box_preference_1", true)+
                "\nMaximo Jugadores "+ pref.getString("edit_text_preference_1", "2")+
                "\nTipos de conexion "+ pref.getString("tipusConexio", "1");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void llancarPuntuacions(View view){
        Intent i = new Intent(this, Puntuacions.class);

        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                //onStop();
                llancarJoc(v);
                break;

            case R.id.button2: llancarConfig(v);
                break;
            case R.id.button3: llancarAcercaDe(v);
                break;
            case R.id.button4: finish();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.button2: mostrarPreferencies(v);
                return true;
            case R.id.button4: llancarPuntuacions(v);
                return true;
        }
        return false;
    }
    @Override
    protected void onSaveInstanceState(Bundle estatMusica) {
        int pos=mp.getCurrentPosition();
        estatMusica.putInt("pos", pos);
        super.onSaveInstanceState(estatMusica);
    }
    @Override
    protected void onRestoreInstanceState(Bundle estatMusica) {
        super.onRestoreInstanceState(estatMusica);
        int pos = estatMusica.getInt("pos");
        mp.seekTo(pos);
    }

}
