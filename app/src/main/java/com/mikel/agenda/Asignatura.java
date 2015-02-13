package com.mikel.agenda;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import BD.*;
import objetos.AsignaturaObj;


public class Asignatura extends ActionBarActivity {
    private BD objAsignaturas;
    private TextView nombreAsig;
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
        asigEscogida=objAsignaturas.getAsignaturaObj(ID);
        TextView nombreAsig= (TextView)findViewById(R.id.NombreAsig);
        if (asigEscogida!=null){
            nombreAsig.setText(asigEscogida.getNombre());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asignatura, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
