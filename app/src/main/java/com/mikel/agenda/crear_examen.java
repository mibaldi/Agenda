package com.mikel.agenda;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

import BD.*;


public class crear_examen extends ActionBarActivity{
    /*DateFormat formate =DateFormat.getDateInstance();
    Calendar calendar= Calendar.getInstance();*/
    Button btn;
    TextView fecha;
    private BD objAsignaturas;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_examen);
        objAsignaturas = new BD(this);
        cursor=objAsignaturas.getAsignaturasCursor();
        String[]from={EAsignatura.FIELD_ID, EAsignatura.FIELD_NOMBRE};
        int[]to=new int[]{android.R.id.text1,android.R.id.text2};
        adapter= new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to);
        Spinner spnClients = (Spinner) findViewById(R.id.spinner_id);
        spnClients.setAdapter(adapter);
        fecha=(TextView)findViewById(R.id.textView7);
        btn=(Button)findViewById(R.id.btnFecha);
       // btn.setOnClickListener(this);
        //updateDate();
    }
   /* public void updateDate(){
        fecha.setText(formate.format(calendar.getTime()));
    }
    public void setDate(){
        new DatePickerDialog(crear_examen.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }*/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_examen, menu);
        return true;
    }
    /*DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateDate();
        }
    };

    @Override
    public void onClick(View v) {
        setDate();
    }*/




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
