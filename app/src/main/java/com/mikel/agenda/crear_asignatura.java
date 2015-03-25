package com.mikel.agenda;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.api.client.util.Joiner;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.List;

import BD.BD;
import BD.EAsignatura;


public class crear_asignatura extends ActionBarActivity {
    EditText texto;
    RadioGroup radioEvalucionGroup;
    RadioButton radioEleccion;
    LinearLayout ll2;
    ArrayList<String> alist=new ArrayList<String>();
    int et;

    List<EditText> allEds = new ArrayList<EditText>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if ( savedInstanceState !=  null )
        {
            alist=savedInstanceState.getStringArrayList("allEds");
            et=savedInstanceState.getInt("et");

        }
        setContentView(R.layout.activity_crear_asignatura);
        ll2 = (LinearLayout)findViewById(R.id.ll2);
        texto=(EditText)findViewById(R.id.texto);

        radioEvalucionGroup = (RadioGroup)findViewById(R.id.radioEvaluacionGroup);
        for (int i=0;i<alist.size();i++){
            EditText temp= new EditText(crear_asignatura.this);
            temp.setText(alist.get(i));
            temp.setWidth(ll2.getWidth());
            if (i==et) temp.requestFocus();
            allEds.add(temp);
            ll2.addView(temp);
        }
        Button ib = (Button) findViewById(R.id.ib);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText temp= new EditText(crear_asignatura.this);

                temp.setWidth(ll2.getWidth());
                allEds.add(temp);
                ll2.addView(temp);


            }
        });
        Button btGuardar = (Button) findViewById(R.id.btGuardar);
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mint.logEvent("Button1 pressed");
                BD  helper = new BD(crear_asignatura.this);
                EAsignatura asig = new EAsignatura();
                if (texto.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Campo de nombre obligatorio", Toast.LENGTH_SHORT).show();
                }
                else{
                    asig.setNombre(texto.getText().toString());
                    ArrayList<String> a= new ArrayList<String>();

                    for (int i=0;i<allEds.size();i++){
                        if (!TextUtils.isEmpty(allEds.get(i).getText())) a.add(allEds.get(i).getText().toString());

                    }

                    String enlaces=Joiner.on(';').join(a);
                    Log.d("Edit",enlaces);
                    asig.setEnlaces(enlaces);
                    int selectId=radioEvalucionGroup.getCheckedRadioButtonId();
                    radioEleccion=(RadioButton)findViewById(selectId);
                    asig.setEvaluacion(radioEleccion.getText().toString());
                    long res=helper.insertarAsignatura(asig);

                    if (res!=-1){
                        Toast.makeText(getApplicationContext(), "Asignatura guardada", Toast.LENGTH_SHORT).show();}
                    else Toast.makeText(getApplicationContext(), "No se ha guardado la asignatura", Toast.LENGTH_SHORT).show();
                }


            }
        });
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

    /*@Override
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
    }*/

}
