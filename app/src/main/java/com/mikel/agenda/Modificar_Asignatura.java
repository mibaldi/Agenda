package com.mikel.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Joiner;

import java.util.ArrayList;
import java.util.List;

import BD.BD;
import BD.EAsignatura;


public class Modificar_Asignatura extends ActionBarActivity {
    private BD objAsignaturas;
    private TextView nombreAsig;
    private String ID;
    private EAsignatura asigEscogida;
    EditText texto,nota;
    ArrayList<String> alist=new ArrayList<String>();
    int et;
    Spinner spinner_evaluacion;
    ArrayAdapter spinner_adapter;
    List<EditText> allEds = new ArrayList<EditText>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( savedInstanceState !=  null )
        {
            alist=savedInstanceState.getStringArrayList("allEds");
            et=savedInstanceState.getInt("et");

        }
        setContentView(R.layout.activity_modificar__asignatura);
        Intent intent =getIntent();
        ID=intent.getStringExtra("ID");
        final LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll2);
        objAsignaturas = new BD(this);
        asigEscogida=objAsignaturas.getEAsignatura(ID);
        nombreAsig= (TextView)findViewById(R.id.NombreAsig);
        Button guardar=(Button)findViewById(R.id.save);
        texto=(EditText)findViewById(R.id.texto);
        nota=(EditText)findViewById(R.id.nota);
        Button ib = (Button) findViewById(R.id.ib);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp = new EditText(Modificar_Asignatura.this);
                temp.setWidth(ll2.getWidth());
                allEds.add(temp);
                ll2.addView(temp);
            }
        });
        spinner_evaluacion = (Spinner) findViewById(R.id.spinner_evaluacion);
        spinner_adapter = ArrayAdapter.createFromResource(this, R.array.evaluación, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_evaluacion.setAdapter(spinner_adapter);
        int spinnerPosition=spinner_adapter.getPosition(asigEscogida.getEvaluacion());
        spinner_evaluacion.setSelection(spinnerPosition);
        spinnerPosition=0;
        if (asigEscogida!=null){
            nombreAsig.setText(asigEscogida.getNombre());
            String enlaces= asigEscogida.getEnlaces();
            String enlacesSueltos[]=enlaces.split("[;]");
            if (!enlacesSueltos[0].toString().matches("")){
                for (int i =0; i< enlacesSueltos.length;i++){
                    EditText texto=new EditText(this);
                    texto.setText(enlacesSueltos[i]);
                    texto.setWidth(ll2.getWidth());
                    allEds.add(texto);
                    ll2.addView(texto);
                }
            }
        nota.setText(String.valueOf(asigEscogida.getNota()));

        }else{
            nombreAsig.setText("sin Asignatura");
        }
    }
    public void guardar(View view){
        EAsignatura asigModificada=new EAsignatura();
        asigModificada.setId(asigEscogida.getId());
        asigModificada.setNombre(asigEscogida.getNombre());
        asigModificada.setEvaluacion(asigEscogida.getEvaluacion());
        ArrayList<String> a= new ArrayList<String>();
        for (int i=0;i<allEds.size();i++){
            if (!TextUtils.isEmpty(allEds.get(i).getText())){
                a.add(allEds.get(i).getText().toString());
            }
        }
        String enlaces=Joiner.on(';').join(a);
        asigModificada.setEnlaces(enlaces);
        asigModificada.setNota(Float.parseFloat(nota.getText().toString()));

        asigModificada.setEvaluacion(spinner_evaluacion.getSelectedItem().toString());
        int respuesta=objAsignaturas.updateAsignatura(asigModificada);
        if (respuesta>0){
            Toast.makeText(getApplicationContext(), "Modificación realizada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Modificar_Asignatura.this, Asignatura.class);
            intent.putExtra("ID",String.valueOf(asigEscogida.getId()));
            startActivity(intent);
        }
       else{
            Toast.makeText(getApplicationContext(), "No se ha podido modificar la asignatura", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected  void onSaveInstanceState ( Bundle outState )  {
        super.onSaveInstanceState(outState);
        ArrayList<String> alist=new ArrayList<String>();
        int et=0;
        for (int i=0;i<allEds.size();i++){
            alist.add(allEds.get(i).getText().toString());
            if (allEds.get(i).hasFocus()) et=i;
        }

        outState.putStringArrayList("allEds",alist);
        outState.putInt("et",et);
    }
}
