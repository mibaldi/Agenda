package com.mikel.agenda;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

import BD.BD;
import BD.EAsignatura;
import BD.EExamen;

//import java.util.Calendar;

/////////////////////////////////



public class crear_examen extends ActionBarActivity{

    java.util.Calendar calendar= java.util.Calendar.getInstance();
    int a,m,d,mes;
    Button btn,guardar;
    TextView fecha;
    Spinner spnClients;
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
    int numAsyncTasks;


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
        a=calendar.get(java.util.Calendar.YEAR);
        m=calendar.get(java.util.Calendar.MONTH);
        d=calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mes=m+1;
        fecha.setText(d+"/"+mes+"/"+a);
        guardar=(Button)findViewById(R.id.GuardarExamen);
        etNombre= (EditText) findViewById(R.id.editText2);
        hora=(TimePicker)findViewById(R.id.timePicker);

        client=ActividadPrincipal.client;



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
            String calendarId="hb2a3khkmlgt0gbel078t35v3g@group.calendar.google.com";
            Event event= new Event();
            event.setSummary(nombre);
            event.setLocation("Donostia");
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
