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
    LinearLayout ll,ll2;
    int totalEditText=0;
    List<EditText> allEds = new ArrayList<EditText>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_crear_asignatura);
        ScrollView sv = new ScrollView(this);
        ll= new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        TextView tvNombre= new TextView(this);
        tvNombre.setText("Nombre:");

        ll.addView(tvNombre);
        final EditText texto = new EditText(this);
        ll.addView(texto);
        TextView tvEnlace= new TextView(this);
        tvEnlace.setText("Enlaces:");
        ll.addView(tvEnlace);
        /*ImageButton ib=new ImageButton(this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ib.setImageBitmap(bmp);*/
        Button ib =new Button(this);
        ib.setText("Agregar Enlace");
        ScrollView sv2 = new ScrollView(this);
        ll.addView(sv2);
        ll2= new LinearLayout(this);
        ll2.setOrientation(LinearLayout.VERTICAL);
        sv2.addView(ll2);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp= new EditText(crear_asignatura.this);
                allEds.add(temp);
                int id=totalEditText+1;
                temp.setId(id);
                ll2.addView(temp);


            }
        });
        ll.addView(ib);
        Button btGuardar = new Button(this);
        btGuardar.setText("Guardar");

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD  helper = new BD(crear_asignatura.this);
                EAsignatura asig = new EAsignatura();
                asig.setNombre(texto.getText().toString());
                ArrayList<String> a= new ArrayList<String>();

                for (int i=0;i<allEds.size();i++){
                    if (!TextUtils.isEmpty(allEds.get(i).getText())) a.add(allEds.get(i).getText().toString());
                    //Log.d("Edit",allEds.get(i).getText().toString());
                }
                String enlaces=Joiner.on(';').join(a);
                Log.d("Edit",enlaces);
                asig.setEnlaces(enlaces);
                helper.insertarAsignatura(asig);
                Toast.makeText(getApplicationContext(), "AsignaturaObj guardada", Toast.LENGTH_LONG).show();
            }
        });
        ll.addView(btGuardar);
        // texto=(EditText)findViewById(R.id.nombreAsig);
        this.setContentView(sv);
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

    /*public void insertar(View view){
        BD  helper = new BD(crear_asignatura.this);
        EAsignatura asig = new EAsignatura();
        asig.setNombre(texto.getText().toString());
        helper.insertarAsignatura(asig);
        Toast.makeText(getApplicationContext(), "AsignaturaObj guardada", Toast.LENGTH_LONG).show();
    }*/

}
