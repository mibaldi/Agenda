package com.mikel.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import BD.BD;
import BD.EAsignatura;


public class Asignatura extends ActionBarActivity {
    private BD objAsignaturas;
    private TextView nombreAsig,eval,notaMax;
    private String ID;
    private EAsignatura asigEscogida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura);
        Intent intent =getIntent();
        ID=intent.getStringExtra("ID");
        LinearLayout ll= (LinearLayout)findViewById(R.id.LlAsignatura);
        Log.e("ID", ID);
        objAsignaturas = new BD(this);
        asigEscogida=objAsignaturas.getEAsignatura(ID);
        nombreAsig= (TextView)findViewById(R.id.NombreAsig);
        eval= (TextView)findViewById(R.id.eval);
        notaMax= (TextView)findViewById(R.id.notaMax);
        if (asigEscogida!=null){
            nombreAsig.setText(asigEscogida.getNombre());
            notaMax.setText(String.valueOf(asigEscogida.getNota()));
            eval.setText(asigEscogida.getEvaluacion());
            String enlaces= asigEscogida.getEnlaces();
            String enlacesSueltos[]=enlaces.split("[;]");
            for (int i =0; i< enlacesSueltos.length;i++){
                TextView texto=new TextView(this);
                texto.setText(enlacesSueltos[i]);
                texto.setAutoLinkMask(1);
                texto.setTextSize(25);
                texto.setSingleLine();
                texto.setMovementMethod(LinkMovementMethod.getInstance());
                ll.addView(texto);
            }

        }else{
            nombreAsig.setText("sin Asignatura");
        }

    }
}
