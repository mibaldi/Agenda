package com.mikel.agenda;

import android.app.ProgressDialog;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mikel.agenda.R;

import java.util.ArrayList;
import java.util.Arrays;

import BD.BD;
import BD.EAsignatura;


public class crear_asignatura extends ActionBarActivity {
    EditText texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_asignatura);
        texto=(EditText)findViewById(R.id.nombreAsig);

        /*ProcesoCarga proceso = new ProcesoCarga();
        proceso.execute();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_asignatura, menu);
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
    public void insertar(View view){
        BD  helper = new BD(crear_asignatura.this);
        EAsignatura asig = new EAsignatura();
        asig.setNombre(texto.getText().toString());
        helper.insertarAsignatura(asig);
        Toast.makeText(getApplicationContext(), "Asignatura guardada", Toast.LENGTH_LONG).show();
    }
   /* private class ProcesoCarga extends AsyncTask<Void,Void,Void>{

        ProgressDialog dialog;
        ArrayList<EAsignatura> asignaturas= new ArrayList<EAsignatura>(Arrays.asList(
                new EAsignatura("ABD"),
                new EAsignatura("ABD2"),
                new EAsignatura("ABD3"),
                new EAsignatura("PFG")
        ));

        @Override
        protected void onPreExecute() {
           dialog= new ProgressDialog(crear_asignatura.this);
            dialog.setTitle("ESTO ES EL TITULO");
            dialog.setMessage("INSERTANDO EN BD");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            BD  helper = new BD(crear_asignatura.this);
            for (int i = 0; i < asignaturas.size(); i++) {
                EAsignatura asig = new EAsignatura();
                asig = asignaturas.get(i);
                helper.insertarAsignatura(asig);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }*/
}
