package com.mikel.agenda;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import BD.BD;
import BD.EExamen;


public class Examen extends ActionBarActivity {
    private BD helper;
    private TextView nombre,nombreAsig,fecha,hora,tipoGuardado,nota;
    private String ID;
    private EExamen ExamenEscogido;
    String descripción;
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
        descripción=ExamenEscogido.getDescripcion();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_examen, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                Intent intent = new Intent(Examen.this, Modificar_Examen.class);
                intent.putExtra("ID",String.valueOf(ExamenEscogido.getId()));
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void borrar(View view){
        new CallAPI().execute();

    }
    public long borrar2(){
        Long res = Long.valueOf(0);
        /*CallAPI task=new CallAPI();
        task.execute();
        try {
            res= task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        new CallAPI().execute();
        return res;
    }
    private class CallAPI extends AsyncTask<String, String, Long> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Examen.this);
            pd.setMessage("Borrando examen");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected Long doInBackground(String... params) {
            Long l;
            try {
                client.events().delete(ExamenEscogido.getCalendarioid(),ExamenEscogido.getEventoid()).execute();
                l=helper.deleteExamen(ID,ExamenEscogido.getNombre());
                Intent intent1 = new Intent(Examen.this, Examenes.class);
                startActivity(intent1);
               finish();
            } catch (IOException e) {
                l=helper.deleteExamen(ID,ExamenEscogido.getNombre());
                Intent intent1 = new Intent(Examen.this, Examenes.class);
                startActivity(intent1);
                finish();
            }
            return l;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            if (pd!=null){
                pd.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Examen borrado", Toast.LENGTH_SHORT).show();
        }
    }
    public void descripcion(View view){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Examen.this);
        alertDialog.setTitle("DESCRIPCIÓN");
        //alertDialog.setMessage("Inserta la descripción");

        final TextView input = new TextView(Examen.this);
        input.setText(descripción);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        /* alertDialog.setPositiveButton("ACEPTAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //descripción = input.getText().toString();
                    }
                });

       alertDialog.setNegativeButton("CANCELAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/

        alertDialog.show();
    }


}
