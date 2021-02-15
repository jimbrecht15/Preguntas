package com.example.a010_db_trivial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.a010_db_trivial.Adaptador.MiAdaptador;
import com.example.a010_db_trivial.Model.Pregunta;
import com.example.a010_db_trivial.dataBase.PreguntasDataBase;

public class MantenimientoPreguntas extends AppCompatActivity {

    PreguntasDataBase mDB;
    MiAdaptador myAdapter;
    RecyclerView recyclerView;
    public EditText textPregunta;
    public EditText textExplicacion;
    public CheckBox checkVerdadero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mantenimiento_preguntas);

        //Para que el teclado no oculte la informacion- Tambien hay que cambiar el manifest android:theme="@style/Theme.010_DB_Trivial"
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        recyclerView = findViewById(R.id.listaRecyclerView); //Llamar al elemento dentro de nuestro layout que va a generar el recycler
        //recyclerView.setHasFixedSize(true); //para que solo pregunte la primera vez cuantos elementos tiene
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        reinciarVista();

        this.textPregunta = findViewById(R.id.editPregunta);
        this.textExplicacion= findViewById(R.id.editExplicacion);
        this.checkVerdadero = findViewById(R.id.checkVerdadero);

        //fab = findViewById(R.id.fab);
    }

    private void reinciarVista() {
        mDB = new PreguntasDataBase(MantenimientoPreguntas.this);
        myAdapter = new MiAdaptador(mDB, this );
        recyclerView.setAdapter(myAdapter);
    }

    public void modificar(View view) {
        myAdapter.Update();
        reinciarVista();
    }

    public void CrearPregunta(View view) {
        Intent intent = new Intent(this, AddPregunta.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //String pregunta = data.getStringExtra("pregunta");
        //String explicacion = data.getStringExtra("explicacion");
        //Serializable para recibir un objeto completo
        Pregunta preguntaNueva = (Pregunta) data.getSerializableExtra("pregunta");

        if (requestCode == 1 ){

            if (preguntaNueva !=null)
                myAdapter.addPregunta(preguntaNueva);
        }
    }
}