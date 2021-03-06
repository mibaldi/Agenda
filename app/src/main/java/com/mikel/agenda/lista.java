package com.mikel.agenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import BD.BD;
import BD.EAsignatura;


public class lista extends ActionBarActivity implements View.OnClickListener {
    private BD objAsignaturas;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter adapter;
    private EditText et;
    private ImageButton bt;
    private static final int CONTEXT_EDIT = 0;
    private static final int CONTEXT_DELETE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        et=(EditText)findViewById(R.id.editText);
        bt=(ImageButton)findViewById(R.id.imageButton);
        objAsignaturas = new BD(this);
        cursor=objAsignaturas.getAsignaturasCursor();
        String[]from={EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_EVALUACION};
        int[]to=new int[]{android.R.id.text1,android.R.id.text2};
        list=(ListView)findViewById(R.id.lista_listView);
        adapter= new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to);

        list.setAdapter(adapter);
        registerForContextMenu(list);
                // registra una accion para el evento click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /// Obtiene el valor de la casilla elegida
                String rowName = cursor.getString(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
                int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_ID));
                Intent intent = new Intent(lista.this, Asignatura.class);
                intent.putExtra("ID",String.valueOf(rowId));

                startActivity(intent);
                // muestra un mensaje
               // Toast.makeText(getApplicationContext(), "Haz hecho click en " + rowId, Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_EDIT, 0, R.string.edit);
        menu.add(0, CONTEXT_DELETE, 0, R.string.delete);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int AsigPosition = (int) info.position;
            cursor.moveToPosition(AsigPosition);
            String rowName = cursor.getString(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
            int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_ID));
            final EAsignatura asignatura = new EAsignatura();
            asignatura.setId(rowId);
        asignatura.setNombre(rowName);
            switch (item.getItemId()) {
                case CONTEXT_EDIT:
                    Intent intent = new Intent(lista.this, Modificar_Asignatura.class);
                    intent.putExtra("ID",String.valueOf(rowId));
                    startActivity(intent);
                    return true;

                case CONTEXT_DELETE:
                    new AlertDialog.Builder(this).setTitle(R.string.delete_title)
                            .setMessage(asignatura.getNombre())
                            .setCancelable(true)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                   if( objAsignaturas.deleteAsignatura(asignatura.getId(),asignatura.getNombre())==-1){
                                       Toast.makeText(getApplicationContext(), "No se pueden borrar asignaturas con examenes", Toast.LENGTH_SHORT).show();
                                   }else{
                                       Toast.makeText(getApplicationContext(), "Asignatura borrada", Toast.LENGTH_SHORT).show();
                                       finish();
                                       startActivity(getIntent());
                                   }
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create()
                            .show();
                    return true;

            }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imageButton){
           new cargar().execute();

        }
    }


    private class cargar extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (et.getText().toString().matches("")){
                cursor=objAsignaturas.getAsignaturasCursor();
            }else{
                cursor= objAsignaturas.buscarAsignatura(et.getText().toString());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.changeCursor(cursor);
            Toast.makeText(getApplicationContext(), "Finalizado...", Toast.LENGTH_LONG).show();
        }
    }

}
