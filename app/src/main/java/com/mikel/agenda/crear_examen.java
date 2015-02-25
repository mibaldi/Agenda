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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

import BD.*;


public class crear_examen extends ActionBarActivity{

    Calendar calendar= Calendar.getInstance();
    int a,m,d;
    Button btn,guardar;
    TextView fecha;
    Spinner spnClients;
    EditText etNombre;
    private BD objAsignaturas;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter adapter;
    TimePicker hora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_examen);
        objAsignaturas = new BD(this);
        cursor=objAsignaturas.getAsignaturasCursor();
        //String[]from={EAsignatura.FIELD_ID, };
        //int[]to=new int[]{android.R.id.text1,android.R.id.text2};
        String[] from = {EAsignatura.FIELD_NOMBRE};
        int[] to = new int[]{android.R.id.text1};
        adapter= new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cursor,from,to);
        spnClients = (Spinner) findViewById(R.id.spinner_id);
        spnClients.setAdapter(adapter);
        fecha=(TextView)findViewById(R.id.textView7);
        btn=(Button)findViewById(R.id.btnFecha);
        a=calendar.get(Calendar.YEAR);
        m=calendar.get(Calendar.MONTH)+1;
        d=calendar.get(Calendar.DAY_OF_MONTH);
        guardar=(Button)findViewById(R.id.GuardarExamen);
        etNombre= (EditText) findViewById(R.id.editText2);
        hora=(TimePicker)findViewById(R.id.timePicker);






    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_examen, menu);
        return true;
    }

    public void EscogerFecha(View view){
        DatePickerDialog.OnDateSetListener mdpd=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                a=year;
                m=monthOfYear+1;
                d=dayOfMonth;

                fecha.setText(d+"/"+m+"/"+a);
            }
        };
       DatePickerDialog dpd = new DatePickerDialog(this,mdpd,a,m-1,d);
        dpd.show();
    }
    public void Guardar(View view){
        BD  helper = new BD(crear_examen.this);
        EExamen examen = new EExamen();
        Cursor c= (Cursor)spnClients.getSelectedItem();
        String text = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        String nombre= etNombre.getText().toString();
        if (nombre.matches("")){
            Toast.makeText(getApplicationContext(), "Campo de nombre obligatorio ", Toast.LENGTH_SHORT).show();
        }else{
            examen.setNombre(nombre);
            examen.setAsignatura(text);
            examen.setFecha(d + "/" + m + "/" + a);
            String horaString = hora.getCurrentHour()+":"+hora.getCurrentMinute();
            examen.setHora(horaString);
            helper.insertarExamen(examen);
            Toast.makeText(getApplicationContext(), "Examen guardado", Toast.LENGTH_SHORT).show();
        }

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
