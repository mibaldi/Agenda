package com.mikel.agenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BD.BD;
import BD.EAsignatura;
import BD.EExamen;



public class Examenes extends ActionBarActivity implements ListView.OnItemClickListener, DrawerLayout.DrawerListener {
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
    private static final int CONTEXT_EDIT = 0;
    private static final int CONTEXT_DELETE = 1;


    HashMap<String, List<String>> listDataChild;

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
        helper= new BD(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.Lista2);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        registerForContextMenu(expListView);
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
        /*expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    String nombre=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    Toast.makeText(getApplicationContext(),nombre,Toast.LENGTH_SHORT).show();
                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.
                    examenes=helper.buscarExamen(nombre);
                    examenes.moveToFirst();
                    //String rowName = examenes.getString(examenes.getColumnIndexOrThrow(EExamen.FIELD_NOMBRE));
                    rowId2= examenes.getInt(examenes.getColumnIndexOrThrow(EExamen.FIELD_ID));
                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });*/

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
        /*expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    String nombre=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.

                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });*/



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info=(ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
        int type=ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {


            // Show context menu for children
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.add(0, CONTEXT_EDIT, 0, R.string.edit);
            menu.add(0, CONTEXT_DELETE, 0, R.string.delete);
        }


    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int AsigPosition = (int) info.position;

        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        final String nombre=listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        examenes=helper.buscarExamen(nombre);
        examenes.moveToFirst();
        final int rowId = examenes.getInt(examenes.getColumnIndexOrThrow(EExamen.FIELD_ID));
        switch (item.getItemId()) {
            case CONTEXT_EDIT:


                Intent intent = new Intent(Examenes.this, Modificar_Examen.class);
                intent.putExtra("ID",String.valueOf(rowId));
                startActivity(intent);
                return true;

            case CONTEXT_DELETE:
                new AlertDialog.Builder(this).setTitle(R.string.delete_title)
                        .setMessage(nombre)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                long res=helper.deleteExamen(String.valueOf(rowId),nombre);
                                if(res==0){
                                    Toast.makeText(getApplicationContext(), "No se pueden borrar asignaturas con examenes", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Examen borrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .create()
                        .show();
                return true;

        }

        return super.onContextItemSelected(item);
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), ActividadPrincipal.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
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
        for (int i=0; i<listDataHeader.size();i++){
            listDataChild.put(listDataHeader.get(i),listas.get(i));
        }
    }


}