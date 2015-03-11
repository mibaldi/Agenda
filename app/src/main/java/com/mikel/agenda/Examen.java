package com.mikel.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import BD.BD;
import BD.EExamen;


public class Examen extends ActionBarActivity {
    private BD helper;
    private TextView nombre,nombreAsig,fecha,hora,tipoGuardado;
    private String ID;
    private EExamen ExamenEscogido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
        Intent intent =getIntent();
        ID=intent.getStringExtra("ID");
        Log.e("ID", ID);
        helper = new BD(this);
        ExamenEscogido=helper.getEExamen(ID);
        nombre= (TextView)findViewById(R.id.nombreExamen);
        nombreAsig= (TextView)findViewById(R.id.nombreAsig);
        fecha= (TextView)findViewById(R.id.fecha);
        hora= (TextView)findViewById(R.id.hora);
        tipoGuardado= (TextView)findViewById(R.id.tvTipoGuardado);
        if (ExamenEscogido!=null){
            nombre.setText(ExamenEscogido.getNombre());
            nombreAsig.setText(ExamenEscogido.getAsignatura());
            fecha.setText(ExamenEscogido.getFecha());
            hora.setText(ExamenEscogido.getHora());
            tipoGuardado.setText(ExamenEscogido.getTipoGuardado());


        }else{
            nombreAsig.setText("sin Asignatura");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_examen, menu);
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
