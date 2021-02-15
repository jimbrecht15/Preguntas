package com.example.a010_db_trivial.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a010_db_trivial.Model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class PreguntasDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="preguntas.db";
    private static final int DATABASE_VERSION = 3;

    private static final String NOMBRE_TABLA= "PREGUNTAS";
    private static final String CAMPO_ID= "_id";
    private static final String CAMPO_PREGUNTA= "pregunta";
    private static final String CAMPO_EXPLICACION= "explicacion";
    private static final String CAMPO_RESPUESTA= "respuesta";

    private static final String CREATE_TABLE_PREGUNTAS =
            "CREATE TABLE " + NOMBRE_TABLA +  "("  + CAMPO_ID + " INTEGER PRIMARY KEY, " +
                                   CAMPO_PREGUNTA + "  TEXT, " +
                                   CAMPO_EXPLICACION +  "  TEXT, " +
                                   CAMPO_RESPUESTA + " NUMERIC DEFAULT 0 );";

    private static final String QUERY_TOTAL_REGISTROS =
            "SELECT COUNT ( " +  CAMPO_ID + ") from " +  NOMBRE_TABLA + ";";

    public PreguntasDataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLite", "CREANDO tabla");
        db.execSQL(CREATE_TABLE_PREGUNTAS);
           crearPreguntas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA + ";");
        onCreate(db);
    }

    private void crearPreguntas(SQLiteDatabase db) {
        Pregunta[] misPreguntas = new Pregunta[]{
                new Pregunta("China es el país más poblado del mundo", true, "Para el 2020, China seguia siendo el país más poblado del mundo"),
                new Pregunta("Los virus pueden reproducirse por si mismos", false, "Los virus no poseen material genetico para reproducirse por si mismos"),
                new Pregunta("Las hienas son felinos ", false, "No pertencen a la familia de los felinos, aunque muchos de sus comportamientos podrían corresponder a esta familia")
        };
        for (Pregunta pregunta0 : misPreguntas){
            ContentValues values = new ContentValues();
            values.put(CAMPO_PREGUNTA, pregunta0.getPregunta());
            values.put(CAMPO_RESPUESTA, pregunta0.isRespuesta()? 1:0);
            values.put(CAMPO_EXPLICACION, pregunta0.getExplicacion());
            db.insert(NOMBRE_TABLA, null, values);
        }
    }


    public long crearRegistro(Pregunta pregunta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_PREGUNTA, pregunta.getPregunta());
        values.put(CAMPO_RESPUESTA,pregunta.isRespuesta());
        values.put(CAMPO_EXPLICACION,pregunta.getExplicacion());
        return db.insert(NOMBRE_TABLA,null,  values);
    }


    public Pregunta buscarPorId(long id){
        Pregunta returnVal = new Pregunta();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + CAMPO_ID + ", " + CAMPO_PREGUNTA + " , "
                        + CAMPO_RESPUESTA + " , " + CAMPO_EXPLICACION  +
                        " FROM " +  NOMBRE_TABLA +
                        " WHERE " + CAMPO_ID + " = ?" ,
                new String[]{String.valueOf(id)});
        if (cursor.getCount()== 1){
            cursor.moveToFirst();
            returnVal= new Pregunta(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2) == 1 ?true:false,
                    cursor.getString(3)
            );

        }
        return returnVal;
    }


    public int borrarRegistro(long id){
        SQLiteDatabase db = getWritableDatabase();
        return  db.delete(NOMBRE_TABLA, CAMPO_ID + "= ?",
                new String[]{String.valueOf(id)});
    }

    public long getTotalRegistros(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c= db.rawQuery(QUERY_TOTAL_REGISTROS, null);
        c.moveToFirst();
        long total = c.getLong(0);
        return total;
    }

    public List<Pregunta> getAll(){
        List<Pregunta> listaPreguntas = new ArrayList<Pregunta>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + CAMPO_ID + ", " + CAMPO_PREGUNTA + " , "
                + CAMPO_RESPUESTA + " , " + CAMPO_EXPLICACION  +
                " FROM " +  NOMBRE_TABLA;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            Pregunta nuevaPregunta =
                    new Pregunta(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1 ?true:false,
                        cursor.getString(3));
            listaPreguntas.add(nuevaPregunta);
        };
        return listaPreguntas;
    }


    public int modificarRegistro(Pregunta pregunta) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_ID, pregunta.getId());
        values.put(CAMPO_PREGUNTA, pregunta.getPregunta());
        values.put(CAMPO_RESPUESTA, pregunta.isRespuesta());
        values.put(CAMPO_EXPLICACION, pregunta.getExplicacion());
        return db.update(NOMBRE_TABLA,
                values,
                "_id= ?",
                new String[]{String.valueOf(pregunta.getId())});
    }
}
