package com.example.a010_db_trivial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Respuesta extends AppCompatActivity {

    private Button btnCerrar;
    private TextView textoRespuesta;
    private TextView textoComplemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);

        btnCerrar = findViewById(R.id.btnCerrar);
        textoRespuesta = findViewById(R.id.textAcertar);
        textoComplemento = findViewById(R.id.textFallar);
        btnCerrar.setText(getString(R.string.btnCerrar));
        cargarRespuesta();
    }

    private void cargarRespuesta(){

        if (getIntent()!= null && getIntent().hasExtra("dato")){
            textoRespuesta.setText(getIntent().getStringExtra("dato"));
        }
        if (getIntent()!= null && getIntent().hasExtra("explicacion")){
            textoComplemento.setText(getIntent().getStringExtra("explicacion"));
        }
    }



    public void OnClickClose(View view){
        Intent returnintent= new Intent();
        returnintent.putExtra("resultado", 1);
        setResult(RESULT_OK, returnintent);

        finish();
    }

}