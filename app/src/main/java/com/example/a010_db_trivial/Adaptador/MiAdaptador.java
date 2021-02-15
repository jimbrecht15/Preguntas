package com.example.a010_db_trivial.Adaptador;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a010_db_trivial.MantenimientoPreguntas;
import com.example.a010_db_trivial.Model.Pregunta;
import com.example.a010_db_trivial.R;
import com.example.a010_db_trivial.dataBase.PreguntasDataBase;

import java.util.List;

public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.MiViewHolder>{

    private List<Pregunta> listaPregunta;
    private Pregunta preguntaSeleccionada;

    private PreguntasDataBase pDB; // Pasar la base de datos para poder agregar, editar y eliminar elementos directamente
    private MantenimientoPreguntas mttoPreguntas;  //pasar el mantenimiento Activity, para acceder a los datos directamente
    private int posicionSelc = 0;

    public MiAdaptador(PreguntasDataBase pDB, MantenimientoPreguntas mttoPreguntas ){

        this.pDB = pDB;
        this.mttoPreguntas = mttoPreguntas;
        listaPregunta = pDB.getAll();
    }

    @NonNull
    @Override
    public MiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //la plantilla de cada item va a ser item pregunta
        View view = inflater.inflate(R.layout.item_pregunta, parent, false);
        MiAdaptador.MiViewHolder miViewHolder = new MiAdaptador.MiViewHolder(view);
        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MiViewHolder holder, int position) {
        listaPregunta = pDB.getAll();
        final Pregunta pregunta= listaPregunta.get(position);
        holder.txtId.setText(String.valueOf(pregunta.getId()));
        holder.txtPregunta.setText(pregunta.getPregunta());
        holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //este metodo cambia para que sea desde la base de datos
                pDB.borrarRegistro(listaPregunta.get(position).getId());
                listaPregunta = pDB.getAll();
                notifyItemRemoved(position);  // REVISAR SI AL SER DE LA BASE DE DATOS CAMBIA es necesario ponerlo siempre porque si no no se entera de la acción
                notifyItemRangeChanged(position, getItemCount());

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preguntaSeleccionada = listaPregunta.get(position);
                Log.i("informacionPregunta", preguntaSeleccionada.getPregunta());
                mttoPreguntas.textPregunta.setText(preguntaSeleccionada.getPregunta());
                mttoPreguntas.textExplicacion.setText((preguntaSeleccionada.getExplicacion()));
                mttoPreguntas.checkVerdadero.setChecked(preguntaSeleccionada.isRespuesta());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pDB.getTotalRegistros() == 0 ){
            return 0;
        } else {
            return (int) pDB.getTotalRegistros();
        }
    }


    public void addPregunta(Pregunta pregunta){
        long id = pDB.crearRegistro(pregunta);
        pregunta.setId((int)id);
        this.listaPregunta.add(pregunta);
        notifyItemInserted(listaPregunta.size());
    }

    public void Update (){
        Pregunta preguntaActualizar = preguntaSeleccionada;
        if (preguntaActualizar == null) preguntaActualizar = listaPregunta.get(1);
        preguntaActualizar.setPregunta(mttoPreguntas.textPregunta.getText().toString());
        preguntaActualizar.setExplicacion(mttoPreguntas.textExplicacion.getText().toString());
        preguntaActualizar.setRespuesta(mttoPreguntas.checkVerdadero.isChecked());
        pDB.modificarRegistro(preguntaActualizar);
        notifyItemChanged(posicionSelc);
        posicionSelc = 0;
    }

    public class MiViewHolder extends RecyclerView.ViewHolder{

        public TextView txtId;
        public TextView txtPregunta;
        public Button botonBorrar;

        public MiViewHolder(View itemView){ //se va a pasar item pais
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtPregunta = itemView.findViewById(R.id.txtPregunta);
            botonBorrar = itemView.findViewById(R.id.botonBorrar);

        }
    }

}
