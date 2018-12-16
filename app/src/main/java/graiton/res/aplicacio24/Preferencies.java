package graiton.res.aplicacio24;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Preferencies extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciesFragment()).commit();
    }
}
