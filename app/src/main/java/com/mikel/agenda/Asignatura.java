package com.mikel.agenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import BD.BD;
import BD.EAsignatura;


public class Asignatura extends ActionBarActivity {
    private BD objAsignaturas;
    private TextView nombreAsig,eval,notaMax,notaTotal;
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
        notaTotal= (TextView)findViewById(R.id.notaTotal);
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
           Float  nota= objAsignaturas.getNotaTotal(asigEscogida.getNombre());
            notaTotal.setText(String.valueOf(nota)+"/"+String.valueOf(asigEscogida.getNota()));


        }else{
            nombreAsig.setText("sin Asignatura");
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_asignatura, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                Intent intent = new Intent(Asignatura.this, Modificar_Asignatura.class);
                intent.putExtra("ID",String.valueOf(asigEscogida.getId()));
                startActivity(intent);
                finish();
                return true;
            case R.id.action_borrar:
                new AlertDialog.Builder(this).setTitle(R.string.delete_title)
                        .setMessage(asigEscogida.getNombre())
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if( objAsignaturas.deleteAsignatura(asigEscogida.getId(),asigEscogida.getNombre())==-1){
                                    Toast.makeText(getApplicationContext(), "No se pueden borrar asignaturas con examenes", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Asignatura borrada", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent1 = new Intent(Asignatura.this, lista.class);
                                    startActivity(intent1);
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .create()
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
