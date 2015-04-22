package com.mikel.agenda;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import BD.BD;
import BD.EExamen;


public class Examen extends ActionBarActivity {
    private BD helper;
    private TextView nombre,nombreAsig,fecha,hora,tipoGuardado,nota;
    private String ID;
    private EExamen ExamenEscogido;
    com.google.api.services.calendar.Calendar client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
        Intent intent =getIntent();
        ID=intent.getStringExtra("ID");
        Log.e("ID", ID);
        helper = new BD(this);
        client=ActividadPrincipal.client;
        ExamenEscogido=helper.getEExamen(ID);
        nombre= (TextView)findViewById(R.id.nombreExamen);
        nombreAsig= (TextView)findViewById(R.id.nombreAsig);
        fecha= (TextView)findViewById(R.id.fecha);
        hora= (TextView)findViewById(R.id.hora);
        tipoGuardado= (TextView)findViewById(R.id.tvTipoGuardado);
        nota=(TextView)findViewById(R.id.nota);
        if (ExamenEscogido!=null){
            nombre.setText(ExamenEscogido.getNombre());
            nombreAsig.setText(ExamenEscogido.getAsignatura());
            fecha.setText(ExamenEscogido.getFecha());
            hora.setText(ExamenEscogido.getHora());
            tipoGuardado.setText(ExamenEscogido.getTipoGuardado());
            String n=helper.getNotaExamen(ExamenEscogido.getNombre());
            nota.setText(n);
        }else{
            nombreAsig.setText("sin Asignatura");
        }

    }
    public void borrar(View view){
        new CallAPI().execute();

    }
    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                client.events().delete(ExamenEscogido.getCalendarioid(),ExamenEscogido.getEventoid()).execute();
                helper.deleteExamen(ID,ExamenEscogido.getNombre());
                Intent intent1 = new Intent(Examen.this, Examenes.class);
                startActivity(intent1);
                finish();
            } catch (IOException e) {
                helper.deleteExamen(ID,ExamenEscogido.getNombre());
                Intent intent1 = new Intent(Examen.this, Examenes.class);
                startActivity(intent1);
                finish();
            }
            return null;
        }
    }


}
