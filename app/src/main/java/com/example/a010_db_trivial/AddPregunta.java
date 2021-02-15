package com.example.a010_db_trivial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.a010_db_trivial.Model.Pregunta;

public class AddPregunta extends AppCompatActivity {
    private EditText textPregunta;
    private EditText textExplicacion;
    private CheckBox checkRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pregunta);
        textPregunta = findViewById(R.id.editPregunta);
        textExplicacion = findViewById(R.id.editExplicacion);
        checkRespuesta = findViewById(R.id.checkVerdadero);
    }

    public void nueva(View view) {
        String pregunta = textPregunta.getText().toString();
        String explicacion = textExplicacion.getText().toString();
        boolean verdadero = checkRespuesta.isChecked();
        Pregunta nuevaPregunta = new Pregunta(pregunta, verdadero, explicacion);

        Intent intent = new Intent();
        intent.putExtra("pregunta" , nuevaPregunta);
        setResult(RESULT_OK, intent);

        finish();

    }


}