package graiton.res.aplicacio24;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Joc extends AppCompatActivity {
    private VistaJoc vistajoc;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistajoc = (VistaJoc)findViewById(R.id.VistaJoc);
    }
    @Override
    protected void onPause(){
        super.onPause();
        vistajoc.getFil().pausar();
    }

    @Override
    protected void onResume(){
        super.onResume();
        vistajoc.getFil().reanudar();
    }

    @Override
    protected void onDestroy(){
        vistajoc.getFil().aturar();
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        vistajoc.getmSensorManager().unregisterListener(vistajoc);
        super.onStop();
    }
}
