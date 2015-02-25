package com.mikel.agenda;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import BD.*;



public class Examenes extends ActionBarActivity implements ListView.OnItemClickListener, DrawerListener {

    private ActionBar actionBar;
    private DrawerLayout cajon;
    private ListView opciones;
    private ActionBarDrawerToggle toggle;
    Cursor cursor;
    private SimpleCursorAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examenes);

        actionBar = getSupportActionBar();

        /**INDICAR TITULO Y SUBTITULO**/
        actionBar.setTitle("Lista de Examenes");
        actionBar.setSubtitle("Examenes:");

        /**CONFIGURAR CAJON**/
        String[] valores = getResources().getStringArray(R.array.cajon);
        cajon = (DrawerLayout) findViewById(R.id.drawer_layout);
        opciones = (ListView) findViewById(R.id.left_drawer);
        opciones.setAdapter(new ArrayAdapter<String>(this, R.layout.plantilla_cajon, valores));
        opciones.setOnItemClickListener(this);

        /**APLICAR SOMBRA AL CAJON**/
        cajon.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        /**HABILITAR ICONO DE LA APLICACION PARA EL MENU DE NAVEGACION**/
        toggle = new ActionBarDrawerToggle(this, cajon, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        cajon.setDrawerListener(this);


        ListView lista2= (ListView) findViewById(R.id.Lista2);
        BD helper= new BD(this);
        cursor=helper.getExamenesCursor();
        String[]from={EExamen.FIELD_ID, EExamen.FIELD_NOMBRE};
        int[]to=new int[]{android.R.id.text1,android.R.id.text2};
        //lista2=(ListView)findViewById(R.id.lista_listView);
        adapter= new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to);
        lista2.setAdapter(adapter);
        lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /// Obtiene el valor de la casilla elegida
                String rowName = cursor.getString(cursor.getColumnIndexOrThrow(EExamen.FIELD_NOMBRE));
                int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(EExamen.FIELD_ID));
                Intent intent = new Intent(Examenes.this, Examen.class);
                intent.putExtra("ID",String.valueOf(rowId));

                startActivity(intent);
                // muestra un mensaje
                // Toast.makeText(getApplicationContext(), "Haz hecho click en " + rowId, Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
       Intent intent1;
        switch (arg2) {
            case 0:
                intent1 = new Intent(Examenes.this, ActividadPrincipal.class);
                startActivity(intent1);
                Toast.makeText(getApplicationContext(), "Principal", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                intent1 = new Intent(Examenes.this, crear_asignatura.class);
                startActivity(intent1);
                Toast.makeText(getApplicationContext(), "Crear Asignatura", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                intent1 = new Intent(Examenes.this, crear_examen.class);
                startActivity(intent1);
                Toast.makeText(getApplicationContext(), "Crear Examen", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                intent1 = new Intent(Examenes.this, lista.class);
                startActivity(intent1);
                Toast.makeText(getApplicationContext(), "Lista Asignaturas", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "COMPARTIR", Toast.LENGTH_SHORT).show();
                break;
        }
        cajon.closeDrawer(opciones);
    }


    /**METODOS PARA MANEJAR LA APERTURA Y CIERRE DEL NAVIGATION DRAWER DESDE EL ICONO DE LA APLICACION**/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**METODOS PARA MANEJAR LOS EVENTOS DE APERTURA Y CIERRE DEL NAVIGATION DRAWER**/
    @Override
    public void onDrawerClosed(View arg0) {
        //actionBar.setTitle("EJ ActionBar");
    }
    @Override
    public void onDrawerOpened(View arg0) {
        //actionBar.setTitle("Menu Principal");
    }
    @Override
    public void onDrawerSlide(View arg0, float arg1) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onDrawerStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }


}