package com.example.a010_db_trivial.Model;

import java.io.Serializable;

public class Pregunta implements Serializable {

    private int id;
    private String pregunta;
    private boolean respuesta;
    private String explicacion;


    public Pregunta(String pregunta, boolean respuesta, String explicacion) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.explicacion = explicacion;
    }

    public Pregunta(int id, String pregunta, boolean respuesta, String explicacion) {
        this.id = id;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.explicacion = explicacion;
    }
    public Pregunta() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
}
