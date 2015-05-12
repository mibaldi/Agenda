package com.mikel.agenda;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import BD.BD;
import BD.EAsignatura;
import BD.EExamen;
import BD.ENota;
import mycalendar.CalendarInfo;
import mycalendar.CalendarModel;
import mycalendar.Utils;

//import java.util.Calendar;

/////////////////////////////////



public class crear_examen extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    java.util.Calendar calendar= java.util.Calendar.getInstance();
    int a,m,d,mes;
    Button btn,guardar;
    TextView fecha;
    Spinner spnAsig,spnCal;
    EditText etNombre,etNotaMax,etNota;
    private BD objAsignaturas;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    String nombre="";

    /////////////////////////////////////
    
    com.google.api.services.calendar.Calendar client;
    CalendarModel model;
    float notaMax;
    ArrayAdapter<CalendarInfo> adapter2;
    Boolean resp=false;
    String calendarId,eventoId="";
    EExamen examen;
    ENota nota;
    BD helper;
    Boolean nota_correcta=true;
    String descripción="";

    private Button mPickTime;
    private int mHour;
    private int mMinute;
    static final int TIME_DIALOG_ID = 0;
    long respuesta=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_examen);
        objAsignaturas = new BD(this);
        cursor=objAsignaturas.getAsignaturasCursor();

        etNotaMax=(EditText)findViewById(R.id.notaMax);
        etNotaMax.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float val = Float.parseFloat(s.toString());
                    if(val > notaMax) {
                        s.replace(0, s.length(), String.valueOf(notaMax));
                    } else if(val < 0) {
                        s.replace(0,s.length(),"0");

                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }
            }
        });
        String[] from = {EAsignatura.FIELD_NOMBRE};
        int[] to = new int[]{android.R.id.text1};
        adapter= new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,from,to);

        spnAsig = (Spinner) findViewById(R.id.spinner_id);
        spnCal = (Spinner) findViewById(R.id.spinner_cal);
        spnAsig.setAdapter(adapter);
        this.spnAsig.setOnItemSelectedListener(this);

        fecha=(TextView)findViewById(R.id.textView7);
        btn=(Button)findViewById(R.id.btnFecha);
        a=calendar.get(java.util.Calendar.YEAR);
        m=calendar.get(java.util.Calendar.MONTH);
        d=calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mes=m+1;
        fecha.setText(d+"/"+mes+"/"+a);
        guardar=(Button)findViewById(R.id.GuardarExamen);
        etNombre= (EditText) findViewById(R.id.editText2);
       // etvalor= (EditText) findViewById(R.id.valor);

        mPickTime=(Button)findViewById(R.id.button3);
        mPickTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showDialog(TIME_DIALOG_ID);
            }
        });
        //etvalor.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute=calendar.get(Calendar.MINUTE);
        updateDisplay();

        client=ActividadPrincipal.client;
        model=ActividadPrincipal.model;
        new cal().execute();




    }
    public void descripcion(View view){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(crear_examen.this);
        alertDialog.setTitle("DESCRIPCIÓN");
        alertDialog.setMessage("Inserta la descripción");

        final EditText input = new EditText(crear_examen.this);
        input.setText(descripción);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("ACEPTAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        descripción = input.getText().toString();
                    }
                });

        alertDialog.setNegativeButton("CANCELAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    private void updateDisplay(){
        mPickTime.setText(
                new StringBuilder()
                .append(pad(mHour)).append(":")
                .append(pad(mMinute)));
    }
    private static String pad(int c){
        if (c>=10)
            return String.valueOf(c);
        else
            return "0"+ String.valueOf(c);
    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay,int minute){
                  mHour=hourOfDay;
                  mMinute=minute;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener,mHour,mMinute,true);//true para 24 horas
        }
        return null;
    }

    public void EscogerFecha(View view){
        DatePickerDialog.OnDateSetListener mdpd=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                a=year;
                m=monthOfYear;
                d=dayOfMonth;

                calendar.set(Calendar.YEAR, a);
                calendar.set(Calendar.DAY_OF_MONTH, d);
                calendar.set(Calendar.MONTH,m);
                mes=m+1;
                fecha.setText(d+"/"+mes+"/"+a);

            }
        };
       DatePickerDialog dpd = new DatePickerDialog(this,mdpd,a,m,d);
        dpd.show();
    }

    public void Guardar(View view) {
        String text = "";
        nombre = etNombre.getText().toString();
        if (spnAsig.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Campo Asignatura obligatorio ", Toast.LENGTH_SHORT).show();
        }
        else if(nombre.matches("")){
            Toast.makeText(getApplicationContext(), "Campo Nombre obligatorio ", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor c = (Cursor) spnAsig.getSelectedItem();
            text = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));

             helper= new BD(crear_examen.this);
             examen= new EExamen();
            examen.setNombre(nombre);
            examen.setAsignatura(text);
            examen.setFecha(d + "/" + mes + "/" + a);
            calendar.set(java.util.Calendar.MINUTE,mMinute);
            calendar.set(Calendar.HOUR_OF_DAY, mHour);

            String horaString = mPickTime.getText().toString();
            examen.setHora(horaString);
            examen.setDescripcion(descripción);

           ////PRUEBA
            nota=new ENota();
            nota.setAsignatura(text);
            nota.setExamen(nombre);
            nota.setNota(Float.parseFloat("0"));
            nota.setNota_sobre(Float.parseFloat(etNotaMax.getText().toString()));
            if (nota.getNota()>nota.getNota_sobre()){
                nota_correcta=false;
                if(etNota.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }else{
                nota_correcta=true;
            }
            ///////
            if (!nota_correcta){
                Toast.makeText(getApplicationContext(), "La nota no puede ser mayor que el maximo", Toast.LENGTH_SHORT).show();
            }
            else if (resp) {
                examen.setTipoGuardado("Ambos");
                new CallAPI().execute();
            } else {
                examen.setTipoGuardado("Local");
               respuesta= helper.insertarExamen(examen);
                if (respuesta==-1 ){
                        Toast.makeText(getApplicationContext(), "No se ha podido insertar en local", Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor=helper.buscarExamen(examen.getNombre());
                    cursor.moveToPosition(0);
                    String rowId = cursor.getString(cursor.getColumnIndexOrThrow(EExamen.FIELD_ID));
                    Intent intent = new Intent(crear_examen.this, Examen.class);
                    intent.putExtra("ID",String.valueOf(rowId));
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Examen guardado en local", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (cursor!=null){
            cursor.moveToPosition(position);
            String asig=cursor.getString(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
            notaMax=objAsignaturas.getNotaRestante(asig);
            etNotaMax.setText(String.valueOf(notaMax));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class CallAPI extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(crear_examen.this);
            pd.setMessage("Creando examen");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... params) {
            int id=spnCal.getSelectedItemPosition();
            CalendarInfo CI = adapter2.getItem(id);

            calendarId=CI.id;
            Event event= new Event();

            event.setSummary(nombre);
            event.setLocation("Donostia");
            /*Event.Reminders reminders=new Event.Reminders();
            EventReminder eventReminder=new EventReminder();
            eventReminder.setMethod("popup");
            eventReminder.setMinutes(15);
            ArrayList array_eventReminder=new ArrayList();
            array_eventReminder.add(eventReminder);

            reminders.setOverrides(array_eventReminder);
            reminders.setUseDefault(false);
            event.setReminders(reminders);*/

            Date startDate =calendar.getTime();
            DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC+1"));
            event.setStart(new EventDateTime().setDateTime(start));

            Date endDate = new Date(startDate.getTime()+3600000);
            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC+1"));
            event.setEnd(new EventDateTime().setDateTime(end));
            try {

                Event createdEvent = client.events().insert(calendarId, event).execute();
                eventoId=createdEvent.getId();
                examen.setCalendarioid(calendarId);
                examen.setEventoid(eventoId);
                examen.setCalendarionombre(CI.summary);
                respuesta=helper.insertarExamen(examen);
                if(respuesta==-1 || !nota_correcta){
                    client.events().delete(calendarId,eventoId).execute();
                }else{
                    helper.insertarNota(nota);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return calendarId;
        }

        @Override
        protected void onPostExecute(String s) {
            if (pd!=null){
                pd.dismiss();
            }
            if (respuesta==-1){
                    Toast.makeText(getApplicationContext(), "No se ha podido insertar", Toast.LENGTH_SHORT).show();
            }else{
                Cursor cursor=helper.buscarExamen(examen.getNombre());
                cursor.moveToPosition(0);
                String rowId = cursor.getString(cursor.getColumnIndexOrThrow(EExamen.FIELD_ID));
                Intent intent = new Intent(crear_examen.this, Examen.class);
                intent.putExtra("ID",String.valueOf(rowId));
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Examen guardado en Local y en Google Calendar", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class cal extends AsyncTask<Boolean, Boolean, Boolean> {
       // ProgressDialog pd;
       CalendarList feed = null;
        ProgressDialog pd;
        @Override
        protected Boolean doInBackground(Boolean... params) {

            try {
                feed = client.calendarList().list().setMinAccessRole("writer").setMinAccessRole("owner").setFields(CalendarInfo.FEED_FIELDS).execute();
                return true;
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                startActivityForResult(
                        userRecoverableException.getIntent(), 1);
                return false;
            } catch (IOException e) {
                Utils.logAndShow(crear_examen.this, "crearexamen", e);
                return false;
            }

        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(crear_examen.this);
            pd.setMessage("buscando calendarios");
            pd.setCancelable(false);
            pd.show();
        }



        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pd!=null){

                pd.dismiss();
            }
            if (!result){

                /*Intent i = new Intent(crear_examen.this, ActividadPrincipal.class);
                startActivity(i);*/

            }else{
                resp=result;
                model.reset(feed.getItems());

                adapter2 = new ArrayAdapter<CalendarInfo>(crear_examen.this, android.R.layout.simple_list_item_1, model.toSortedArray()) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // by default it uses toString; override to use summary instead
                        TextView view = (TextView) super.getView(position, convertView, parent);
                        CalendarInfo calendarInfo = getItem(position);
                        view.setText(calendarInfo.summary);
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        // by default it uses toString; override to use summary instead
                        TextView view = (TextView) super.getView(position, convertView, parent);
                        CalendarInfo calendarInfo = getItem(position);
                        view.setText(calendarInfo.summary);
                        return view;
                    }
                };
                spnCal.setAdapter(adapter2);
            }


        }
    }
}
