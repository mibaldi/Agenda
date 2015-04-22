package com.mikel.agenda;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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


public class Modificar_Examen extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private String ID;
    private EExamen ExamenEscogido;
    private ENota NotaEscogido;
    EExamen examenModificado;
    ENota notaModificada;
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    int a, m, d, mes;
    Button btn, guardar;
    TextView fecha, tvNombre;
    Spinner spnAsig, spnCal;
    EditText etNotaMax, etNota;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    String nombre = "";

    /////////////////////////////////////

    com.google.api.services.calendar.Calendar client;
    CalendarModel model;
    float notaMax;
    ArrayAdapter<CalendarInfo> adapter2;
    Boolean resp = false;
    String calendarId, eventoId = "";
    EExamen examen;
    ENota nota;
    BD helper;
    Boolean nota_correcta = true;


    private Button mPickTime;
    private int mHour;
    private int mMinute;
    static final int TIME_DIALOG_ID = 0;
    long respuesta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar__examen);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        Log.e("ID", ID);
        helper = new BD(this);

        cursor = helper.getAsignaturasCursor();
        client = ActividadPrincipal.client;
        ExamenEscogido = helper.getEExamen(ID);
        NotaEscogido = helper.getNotaExamenObj(ExamenEscogido.getNombre());
        etNotaMax = (EditText) findViewById(R.id.notaMax);
        etNotaMax.setText(String.valueOf(NotaEscogido.getNota_sobre()));
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
                    if (val > notaMax) {
                        s.replace(0, s.length(), String.valueOf(notaMax));
                    } else if (val < 0) {
                        s.replace(0, s.length(), "0");

                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }
            }
        });
        etNota = (EditText) findViewById(R.id.nota);
        etNota.setText(String.valueOf(NotaEscogido.getNota()));
        etNota.addTextChangedListener(new TextWatcher() {

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
                    float n = Float.parseFloat(etNotaMax.getText().toString());
                    if (val > n) {
                        s.replace(0, s.length(), String.valueOf(n));
                    } else if (val < 0) {
                        s.replace(0, s.length(), "0");

                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }
            }
        });
        String[] from = {EAsignatura.FIELD_NOMBRE};
        int[] to = new int[]{android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);

        spnAsig = (Spinner) findViewById(R.id.spinner_id);
        spnCal = (Spinner) findViewById(R.id.spinner_cal);
        spnAsig.setAdapter(adapter);
        int cpos = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            cursor.moveToPosition(i);
            String temp = cursor.getString(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
            if (temp.contentEquals(ExamenEscogido.getAsignatura())) {
                Log.d("TAG", "Found match");
                cpos = i;
                break;
            }
        }
        spnAsig.setSelection(cpos);
        this.spnAsig.setOnItemSelectedListener(this);

        fecha = (TextView) findViewById(R.id.textView7);
        btn = (Button) findViewById(R.id.btnFecha);
        a = calendar.get(java.util.Calendar.YEAR);
        m = calendar.get(java.util.Calendar.MONTH);
        d = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mes = m + 1;
        fecha.setText(ExamenEscogido.getFecha());
        guardar = (Button) findViewById(R.id.GuardarExamen);
        tvNombre = (TextView) findViewById(R.id.nombre);
        tvNombre.setText(ExamenEscogido.getNombre());

        mPickTime = (Button) findViewById(R.id.button3);
        mPickTime.setText(ExamenEscogido.getHora());
        mPickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        //updateDisplay();

        client = ActividadPrincipal.client;
        model = ActividadPrincipal.model;
        new cal().execute();
    }

    private void updateDisplay() {
        mPickTime.setText(
                new StringBuilder()
                        .append(pad(mHour)).append(":")
                        .append(pad(mMinute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, true);//true para 24 horas
        }
        return null;
    }

    public void EscogerFecha(View view) {
        DatePickerDialog.OnDateSetListener mdpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                a = year;
                m = monthOfYear;
                d = dayOfMonth;

                calendar.set(Calendar.YEAR, a);
                calendar.set(Calendar.DAY_OF_MONTH, d);
                calendar.set(Calendar.MONTH, m);
                mes = m + 1;
                fecha.setText(d + "/" + mes + "/" + a);

            }
        };
        DatePickerDialog dpd = new DatePickerDialog(this, mdpd, a, m, d);
        dpd.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            String asig = cursor.getString(cursor.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
            notaMax = helper.getNotaRestante(asig) + NotaEscogido.getNota_sobre();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Guardar(View view) {
        String text = "";
        Cursor c = (Cursor) spnAsig.getSelectedItem();
        text = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        examenModificado = new EExamen();
        examenModificado.setId(ExamenEscogido.getId());
        examenModificado.setNombre(ExamenEscogido.getNombre());
        examenModificado.setAsignatura(text);
        examenModificado.setFecha(fecha.getText().toString());
        examenModificado.setHora(mPickTime.getText().toString());
        String horaRota = mPickTime.getText().toString();
        String[] campos = horaRota.split(":");
        calendar.set(java.util.Calendar.MINUTE, Integer.parseInt(campos[1]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(campos[0]));
        notaModificada = new ENota();
        notaModificada.setExamen(ExamenEscogido.getNombre());
        notaModificada.setAsignatura(text);
        notaModificada.setNota(Float.parseFloat(etNota.getText().toString()));
        notaModificada.setNota_sobre(Float.parseFloat(etNotaMax.getText().toString()));
        if (notaModificada.getNota() > notaModificada.getNota_sobre()) {
            nota_correcta = false;
            if (etNota.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        } else {
            nota_correcta = true;
        }

        if (!nota_correcta) {
            Toast.makeText(getApplicationContext(), "La nota no puede ser mayor que el maximo", Toast.LENGTH_SHORT).show();
        } else if (resp) {
            examenModificado.setTipoGuardado("Ambos");
            new CallAPI().execute();
        } else {
            examenModificado.setTipoGuardado("Local");
            respuesta = helper.updateExamen(examenModificado, notaModificada);
            if (respuesta > 1) {
                Toast.makeText(getApplicationContext(), "Modificación realizada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Modificar_Examen.this, Examen.class);
                intent.putExtra("ID", String.valueOf(ExamenEscogido.getId()));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido modificar el examen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            int id = spnCal.getSelectedItemPosition();
            CalendarInfo CI = adapter2.getItem(id);
            calendarId = CI.id;
            Event event = new Event();

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

            Date startDate = calendar.getTime();
            DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC+1"));


            Date endDate = new Date(startDate.getTime() + 3600000);
            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC+1"));

            try {
                Event evento = client.events().get(ExamenEscogido.getCalendarioid(), ExamenEscogido.getEventoid()).execute();
                evento.setStart(new EventDateTime().setDateTime(start));
                evento.setEnd(new EventDateTime().setDateTime(end));
                Event updatedEvent = client.events().update(ExamenEscogido.getCalendarioid(), evento.getId(), evento).execute();
                updatedEvent.setAttendeesOmitted(true);


                Event updatedEvent2 = client.events().move(ExamenEscogido.getCalendarioid(), updatedEvent.getId(), calendarId).execute();
                updatedEvent2.setAttendeesOmitted(true);
                eventoId = updatedEvent.getId();
                examenModificado.setCalendarioid(calendarId);
                examenModificado.setEventoid(eventoId);
                examenModificado.setCalendarionombre(CI.summary);
                respuesta = helper.updateExamen(examenModificado, notaModificada);

                   /* eventoId=updatedEvent.getId();
                    examenModificado.setCalendarioid(calendarId);
                    examenModificado.setEventoid(eventoId);
                    respuesta=helper.updateExamen(examenModificado, notaModificada);*/

                /*Event updatedEvent2 =client.events().move(ExamenEscogido.getCalendarioid(),updatedEvent.getId(), calendarId).execute();
                updatedEvent2.setAttendeesOmitted(true);*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            return calendarId;
        }

        @Override
        protected void onPostExecute(String s) {
            if (respuesta != 2) {
                Toast.makeText(getApplicationContext(), "No se ha podido actualizar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Examen modificado en Local y en Google Calendar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Modificar_Examen.this, Examen.class);
                intent.putExtra("ID", String.valueOf(ExamenEscogido.getId()));
                startActivity(intent);
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
                Utils.logAndShow(Modificar_Examen.this, "ModificarExamen", e);
                return false;
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Modificar_Examen.this);
            pd.setMessage("buscando calendarios");
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pd != null) {

                pd.dismiss();
            }
            if (!result) {

                /*Intent i = new Intent(crear_examen.this, ActividadPrincipal.class);
                startActivity(i);*/

            } else {
                resp = result;
                model.reset(feed.getItems());

                adapter2 = new ArrayAdapter<CalendarInfo>(Modificar_Examen.this, android.R.layout.simple_list_item_1, model.toSortedArray()) {

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


                spnCal.setSelection(getIndex(spnCal, ExamenEscogido.getCalendarionombre()));

            }


        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            String s = spinner.getItemAtPosition(i).toString();
            String[] campos = s.split("=");
            String s2 = campos[2].substring(0, campos[2].length() - 1);
            if (s2.equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
