package graiton.res.aplicacio24;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class AdaptadorsPuntuacions extends RecyclerView.Adapter<AdaptadorsPuntuacions.ViewHolder> {
    private LayoutInflater inflador;
    private Vector<String> llista;
    //private int cont = 0;
    private Context context;

    protected View.OnClickListener onClickListener;

    public AdaptadorsPuntuacions(Context context, Vector<String> llista){
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista = llista;
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titol, subtitol;
        public ImageView icono;
        public ViewHolder(View itemView){
            super(itemView);
            titol = (TextView) itemView.findViewById(R.id.titol);
            subtitol = (TextView) itemView.findViewById(R.id.subtitul);
            icono = (ImageView) itemView.findViewById(R.id.icono);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = inflador.inflate(R.layout.element_puntuacio, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
       // Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        holder.titol.setText(llista.get(position));
        switch (Math.round((float)Math.random()*3)){
            case 0:
                holder.icono.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                holder.icono.setImageResource(R.drawable.asteroide2);
                break;
            default:
                holder.icono.setImageResource(R.drawable.asteroide3);
                break;
        }
    }
    @Override
    public int getItemCount(){
        return llista.size();
    }
}
