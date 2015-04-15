package com.mikel.agenda;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BD.BD;
import BD.EAsignatura;
import BD.EExamen;



public class Examenes extends ActionBarActivity {
    BD helper;
    private ActionBar actionBar;
    private DrawerLayout cajon;
    private ListView opciones;
    private ActionBarDrawerToggle toggle;
    Cursor examenes,asignaturas;
    private SimpleCursorAdapter adapter;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;

    HashMap<String, List<String>> listDataChild;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examenes);

       /* *//*actionBar = getSupportActionBar();

        *//**INDICAR TITULO Y SUBTITULO**//*
        actionBar.setTitle("Lista de Examenes");
        actionBar.setSubtitle("Examenes:");

        *//**CONFIGURAR CAJON**//*
        String[] valores = getResources().getStringArray(R.array.cajon);
        cajon = (DrawerLayout) findViewById(R.id.drawer_layout);
        opciones = (ListView) findViewById(R.id.left_drawer);
        opciones.setAdapter(new ArrayAdapter<String>(this, R.layout.plantilla_cajon, valores));
        opciones.setOnItemClickListener(this);

        *//**APLICAR SOMBRA AL CAJON**//*
        cajon.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        *//**HABILITAR ICONO DE LA APLICACION PARA EL MENU DE NAVEGACION**//*
        toggle = new ActionBarDrawerToggle(this, cajon, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        cajon.setDrawerListener(this);
*/

        //ListView lista2= (ListView) findViewById(R.id.Lista2);
        helper= new BD(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.Lista2);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                /*Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                /// Obtiene el valor de la casilla elegida
                String nombre=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                examenes=helper.buscarExamen(nombre);
                examenes.moveToFirst();
                //String rowName = examenes.getString(examenes.getColumnIndexOrThrow(EExamen.FIELD_NOMBRE));
                int rowId = examenes.getInt(examenes.getColumnIndexOrThrow(EExamen.FIELD_ID));
                Intent intent = new Intent(Examenes.this, Examen.class);
                intent.putExtra("ID",String.valueOf(rowId));

                startActivity(intent);
                return false;
            }
        });
        /*String[]from={EExamen.FIELD_NOMBRE, EExamen.FIELD_ASIGNATURA};
        int[]to=new int[]{android.R.id.text1,android.R.id.text2};

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
        });*/


    }


 /*   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
    }*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), ActividadPrincipal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

  /*  *//**METODOS PARA MANEJAR LA APERTURA Y CIERRE DEL NAVIGATION DRAWER DESDE EL ICONO DE LA APLICACION**//*
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


    *//**METODOS PARA MANEJAR LOS EVENTOS DE APERTURA Y CIERRE DEL NAVIGATION DRAWER**//*
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

    }*/
    /*
     * Preparing the list data
     */
    private void prepareListData() {



        asignaturas=helper.getAsignaturasExamenesCursor();

        List<List>listas=new ArrayList<List>();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        while (asignaturas.moveToNext()) {
            String name=asignaturas.getString(asignaturas.getColumnIndex(EAsignatura.FIELD_NOMBRE));
            listDataHeader.add(name);
            List<String> temp = new ArrayList<String>();
            examenes=helper.getExamenesAsignaturaCursor(name);

                while(examenes.moveToNext()){
                    String nameExam=examenes.getString(examenes.getColumnIndex(EExamen.FIELD_NOMBRE));
                    temp.add(nameExam);
                }


            listas.add(temp);
        }
       /* // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");


        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");*/
        //Toast.makeText(getApplicationContext(), "listDataHeader.size="+listDataHeader.size()+"listas.size="+listas.size(), Toast.LENGTH_LONG).show();
        for (int i=0; i<listDataHeader.size();i++){
            listDataChild.put(listDataHeader.get(i),listas.get(i));
        }

        /*listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }


}