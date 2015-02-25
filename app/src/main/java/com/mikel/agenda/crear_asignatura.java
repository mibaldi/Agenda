package com.mikel.agenda;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.util.Joiner;
import com.mikel.agenda.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import BD.BD;
import BD.EAsignatura;


public class crear_asignatura extends ActionBarActivity {
    EditText texto;
    RadioGroup radioEvalucionGroup;
    RadioButton radioEleccion;
    LinearLayout ll2;
    int totalEditText=0;
    List<EditText> allEds = new ArrayList<EditText>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_asignatura);
        ll2 = (LinearLayout)findViewById(R.id.ll2);
        texto=(EditText)findViewById(R.id.texto);
        radioEvalucionGroup = (RadioGroup)findViewById(R.id.radioEvaluacionGroup);

        Button ib = (Button) findViewById(R.id.ib);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp= new EditText(crear_asignatura.this);
                allEds.add(temp);
                int id=totalEditText+1;
                temp.setId(id);
                temp.setWidth(ll2.getWidth());
                ll2.addView(temp);


            }
        });
        Button btGuardar = (Button) findViewById(R.id.btGuardar);
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}
