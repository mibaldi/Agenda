package com.mikel.agenda;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.logging.Logger;
import BD.BD;
import login.MainActivity;
import mycalendar.*;
////////////////////////////
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.mikel.agenda.R;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadPrincipal extends ActionBarActivity /*implements OnClickListener*/ {
   /* private static final Level LOGGING_LEVEL = Level.CONFIG;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final String TAG = "CalendarSampleActivity";

    private static final int CONTEXT_EDIT = 0;

    private static final int CONTEXT_DELETE = 1;

    private static final int CONTEXT_BATCH_ADD = 2;

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;

    static final int REQUEST_AUTHORIZATION = 1;

    static final int REQUEST_ACCOUNT_PICKER = 2;

    private final static int ADD_OR_EDIT_CALENDAR_REQUEST = 3;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();

    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    GoogleAccountCredential credential;

    CalendarModel model = new CalendarModel();

    ArrayAdapter<CalendarInfo> adapter;

    com.google.api.services.calendar.Calendar client;

    int numAsyncTasks;*/


    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        BD helper= new BD(this);
        /////////////////////////////////////////
        // enable logging
        /*Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Baldugenda")
                .build();*/
    }


    //////////////////////
   /* void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, ActividadPrincipal.this, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (checkGooglePlayServicesAvailable()) {
            haveGooglePlayServices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    AsyncLoadCalendars.run(this);
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        AsyncLoadCalendars.run(this);
                    }
                }
                break;
            case ADD_OR_EDIT_CALENDAR_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Calendar calendar = new Calendar();
                    calendar.setSummary(data.getStringExtra("summary"));
                    String id = data.getStringExtra("id");
                    if (id == null) {
                        new AsyncInsertCalendar(this, calendar).execute();
                    } else {
                        calendar.setId(id);
                        new AsyncUpdateCalendar(this, id, calendar).execute();
                    }
                }
                break;
        }
    }

    /** Check that Google Play services APK is installed and up to date. */
   /* private boolean checkGooglePlayServicesAvailable() {
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
            AsyncLoadCalendars.run(this);
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private void startAddOrEditCalendarActivity(CalendarInfo calendarInfo) {
        Intent intent = new Intent(this, AddOrEditCalendarActivity.class);
        if (calendarInfo != null) {
            intent.putExtra("id", calendarInfo.id);
            intent.putExtra("summary", calendarInfo.summary);
        }
        startActivityForResult(intent, ADD_OR_EDIT_CALENDAR_REQUEST);
    }
    */


    /////////////////////////



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);

        return true;
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



    public void onClickButton(View v) {
        Intent intent1,intent2;
        switch (v.getId()){
            case R.id.button:
               /* AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Write your message here.");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();*/
                Log.e("HolaMundo", "Se ha pulsado lista");
               intent1 = new Intent(ActividadPrincipal.this, lista.class);
                startActivity(intent1);
                break;
            case R.id.button2:
                intent1 = new Intent(ActividadPrincipal.this, Examenes.class);
                startActivity(intent1);
                break;
            case R.id.button5:
                intent2 = new Intent(ActividadPrincipal.this, crear_asignatura.class);
                startActivity(intent2);
                break;
            case R.id.CrearExamen:
                intent2 = new Intent(ActividadPrincipal.this, crear_examen.class);
                startActivity(intent2);
                break;
            case R.id.bt_login:
                intent2 = new Intent(ActividadPrincipal.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.calendario:
                intent2 = new Intent(ActividadPrincipal.this, CalendarSampleActivity.class);
                startActivity(intent2);
                break;
            /*case R.id.crear:
                CalendarInfo calendarInfo = new CalendarInfo("hb2a3khkmlgt0gbel078t35v3g@group.calendar.google.com","prueba");
                new events(ActividadPrincipal.this,calendarInfo);
               // new events(CalendarSampleActivity.this,calendarInfo).execute();
                break;*/
        }
    }



}
