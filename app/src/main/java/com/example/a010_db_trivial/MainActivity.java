    package com.example.a010_db_trivial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a010_db_trivial.Model.Pregunta;
import com.example.a010_db_trivial.dataBase.PreguntasDataBase;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private PreguntasDataBase mDB;
    private Button btnVerdadero;
    private Button btnFalso;
    private TextView textViewPregunta;
    private int idPreguntaActual = 1;
    private Button btnSiguiente;
    private Button btnAnterior;
    private TextView textPuntuacion;
    private int puntuacion ;
    private Pregunta pregunta;
    private Animation anim;
    private static final int KEY_PUNTUACION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDB = new PreguntasDataBase(MainActivity.this);
        setContentView(R.layout.activity_main);
        this.btnFalso = findViewById(R.id.btn_falso);
        this.btnVerdadero = findViewById(R.id.btn_verdadero);
        this.textViewPregunta = findViewById(R.id.textViewPregunta);
        this.btnVerdadero.setOnClickListener(this);
        this.btnFalso.setOnClickListener(this);
        this.btnAnterior = findViewById(R.id.btnAnterior);
        this.btnSiguiente = findViewById(R.id.btnSiguiente);
        this.btnAnterior.setOnClickListener(this);
        this.btnSiguiente.setOnClickListener(this);
        this.textPuntuacion = findViewById(R.id.textPuntuacion);
        this.puntuacion =0;
        this.anim = AnimationUtils.
                loadAnimation(this, R.anim.animacion1);

        registerForContextMenu(this.textViewPregunta); //Registrar el menu contextual
        registerForContextMenu(this.btnSiguiente);
        iniciarServicio();
        cargarPregunta();
        //SharedPreferences settings = getPreferences(MODE_PRIVATE);
        //String nombre = settings.getString(KEY_NAME, "Introduce Nombre");
        //((EditText)findViewById(R.id.txtEditName)).setText(nombre);
    }

    private void reiniciar(){
        idPreguntaActual = 1;
        cargarPregunta();
    }

    private void cargarPregunta(){
        pregunta = mDB.buscarPorId(idPreguntaActual);
        while (pregunta.getPregunta() == null) {
            idPreguntaActual++;
            pregunta = mDB.buscarPorId(idPreguntaActual);
        }
        this.textViewPregunta.setText(pregunta.getPregunta());
        this.textPuntuacion.setText("" + this.puntuacion + " pts");
    }

    @Override
    public void onClick(View v) {
        Log.i("trivial", "id boton es: " + v.getId());
        pregunta = mDB.buscarPorId(idPreguntaActual);
        boolean respuesta = pregunta.isRespuesta();
        switch (v.getId()){
            case R.id.btn_falso:
                respuesta = false;
                mostrarRespuesta(respuesta);
                break;
            case R.id.btn_verdadero:
                respuesta = true;
                mostrarRespuesta(respuesta);
                break;
            case R.id.btnAnterior:
                this.idPreguntaActual--;
                comprobarUltimaPregunta();
                break;
            case R.id.btnSiguiente:
                this.idPreguntaActual++;
                comprobarUltimaPregunta();
                break;
        }
    }

    private void comprobarUltimaPregunta() {
        if (idPreguntaActual <=0 || idPreguntaActual > mDB.getTotalRegistros()){
            dialogoFinJuego();
        } else {
            cargarPregunta();
       }
    }

    private void abrirMantenimiento() {
        Intent intent = new Intent(this, MantenimientoPreguntas.class);
        startActivityForResult(intent, 1);
    }

    private void mostrarRespuesta(boolean respuesta) {
        String respuestaRecibida ="";
        String explicacion ="";
        pregunta = mDB.buscarPorId(idPreguntaActual);
        if (pregunta.isRespuesta() == respuesta){
            respuestaRecibida = getString(R.string.respuesta_OK);
            this.puntuacion += 10;
        } else {
            respuestaRecibida = getString(R.string.respuesta_NOT_OK);
            explicacion = pregunta.getExplicacion();
        }
        Intent intent = new Intent(this, Respuesta.class);
        intent.putExtra("dato", respuestaRecibida);
        intent.putExtra("explicacion", explicacion);
        startActivityForResult(intent, 1);

        if (pregunta.isRespuesta() == respuesta){
            Log.i("trivial", "acertó");
        } else {
            Log.i("trivial", "falló");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long totalPreguntas = mDB.getTotalRegistros();
        if (this.idPreguntaActual <= totalPreguntas){
            this.idPreguntaActual++;

        }else {
            dialogoFinJuego();
            this.idPreguntaActual=1;
        }
        cargarPregunta();
        actualizarPuntuacion();
     }

    private void actualizarPuntuacion() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textPuntuacion.startAnimation(anim);
            }
        },300);
    }

    //MENU OPCIONES -- EVENTOS PARA LAS OPCIONES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.optMantenimiento){
            abrirMantenimiento();
        }else {
            return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        if(v.getId() == R.id.textViewPregunta) {
            getMenuInflater().inflate(R.menu.menujuego, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.optReiniciar){
            reiniciar();
            return true;
        }else
        return super.onContextItemSelected(item);
    }

    private void dialogoFinJuego(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Reiniciar Juego");
        builder.setMessage("Deseas reiniciar el juego");
        builder.setPositiveButton("OK",
                (dialog, which) -> {
                    reiniciar();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        }

        private void iniciarServicio(){
            Intent servicio = new Intent(MainActivity.this, MyService.class);
            startService(servicio);
        }
}