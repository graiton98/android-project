package graiton.res.aplicacio24;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class Puntuacions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorsPuntuacions adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Assigna layout a l'activitat
        setContentView(R.layout.puntuacions);

        //Configura el recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Especificar el tipus de format de visualització del RecyclerView i l'assigna
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //crea i assigna adaptador del recyclerView
        adaptador = new AdaptadorsPuntuacions(this, MainActivity.magatzem.llistaPuntuacions(10));
        recyclerView.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mètode que indica la posició d'una vista dins l'adaptador
                int pos = recyclerView.getChildAdapterPosition(v);
                String s = MainActivity.magatzem.llistaPuntuacions(10).get(pos);
                Toast.makeText(Puntuacions.this, "Seleccio: "+pos+"-"+s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
