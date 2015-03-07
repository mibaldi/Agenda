package com.mikel.agenda;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

import BD.BD;
import BD.EAsignatura;
import BD.EExamen;
import mycalendar.CalendarInfo;
import mycalendar.CalendarModel;
import mycalendar.Utils;

//import java.util.Calendar;

/////////////////////////////////



public class crear_examen extends ActionBarActivity{

    java.util.Calendar calendar= java.util.Calendar.getInstance();
    int a,m,d,mes;
    Button btn,guardar;
    TextView fecha;
    Spinner spnClients,spnCal;
    EditText etNombre;
    private BD objAsignaturas;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter adapter;
    TimePicker hora;
    String nombre="";

    /////////////////////////////////////
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    private final static int ADD_OR_EDIT_CALENDAR_REQUEST = 3;
    private static final Level LOGGING_LEVEL = Level.CONFIG;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;
    com.google.api.services.calendar.Calendar client;
    CalendarModel model;
    int numAsyncTasks;
    ArrayAdapter<CalendarInfo> adapter2;


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
        adapter= new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,from,to);

        spnClients = (Spinner) findViewById(R.id.spinner_id);
        spnCal = (Spinner) findViewById(R.id.spinner_cal);
        spnClients.setAdapter(adapter);

        fecha=(TextView)findViewById(R.id.textView7);
        btn=(Button)findViewById(R.id.btnFecha);
        a=calendar.get(java.util.Calendar.YEAR);
        m=calendar.get(java.util.Calendar.MONTH);
        d=calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mes=m+1;
        fecha.setText(d+"/"+mes+"/"+a);
        guardar=(Button)findViewById(R.id.GuardarExamen);
        etNombre= (EditText) findViewById(R.id.editText2);
        hora=(TimePicker)findViewById(R.id.timePicker);

        client=ActividadPrincipal.client;
        model=ActividadPrincipal.model;
        new cal().execute();




    }
    /* @Override
     protected void onResume() {
         super.onResume();
         if (checkGooglePlayServicesAvailable()) {
             haveGooglePlayServices();
         }
     }

     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main_menu, menu);
         return super.onCreateOptionsMenu(menu);
     }
     verride
    /* public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.menu_refresh:
                 //AsyncLoadCalendars.run(this);
                 break;
             case R.id.menu_accounts:
                 chooseAccount();
                 return true;
         }
         return super.onOptionsItemSelected(item);
     }
 */
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
    public void Guardar(View view){
        BD  helper = new BD(crear_examen.this);
        EExamen examen = new EExamen();
        Cursor c= (Cursor)spnClients.getSelectedItem();
        String text = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        nombre= etNombre.getText().toString();
        if (nombre.matches("")){
            Toast.makeText(getApplicationContext(), "Campo de nombre obligatorio ", Toast.LENGTH_SHORT).show();
        }else{
            examen.setNombre(nombre);
            examen.setAsignatura(text);
            examen.setFecha(d + "/" + mes + "/" + a);
            calendar.set(java.util.Calendar.MINUTE, hora.getCurrentMinute());
            calendar.set(Calendar.HOUR_OF_DAY, hora.getCurrentHour());
            String horaString = hora.getCurrentHour()+":"+hora.getCurrentMinute();
            examen.setHora(horaString);
            helper.insertarExamen(examen);
            new CallAPI().execute();
            Toast.makeText(getApplicationContext(), "Examen guardado", Toast.LENGTH_SHORT).show();
        }

    }


    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int id=spnCal.getSelectedItemPosition();
            CalendarInfo CI = adapter2.getItem(id);
            String calendarId=CI.id;
            //String calendarId="hb2a3khkmlgt0gbel078t35v3g@group.calendar.google.com";
            Event event= new Event();
            event.setSummary(nombre);
            event.setLocation("Donostia");
            Event.Reminders reminders=new Event.Reminders();
            EventReminder eventReminder=new EventReminder();
            eventReminder.setMethod("popup");
            eventReminder.setMinutes(15);
            ArrayList array_eventReminder=new ArrayList();
            array_eventReminder.add(eventReminder);

            reminders.setOverrides(array_eventReminder);
            reminders.setUseDefault(false);
            event.setReminders(reminders);

            Date startDate =calendar.getTime();
            DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC+1"));
            event.setStart(new EventDateTime().setDateTime(start));

            Date endDate = new Date(startDate.getTime()+3600000);
            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC+1"));
            event.setEnd(new EventDateTime().setDateTime(end));
            try {

                Event createdEvent = client.events().insert(calendarId, event).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return calendarId;
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

            }else{
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

    ////////////////////////////////////////////////
    /*void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, crear_examen.this, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });}

    /// Check that Google Play services APK is installed and up to date.
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }
    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        } else {
            // load calendars
            //AsyncLoadCalendars.run(this);
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }*/

}
