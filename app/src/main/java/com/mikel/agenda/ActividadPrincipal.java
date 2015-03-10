package com.mikel.agenda;


import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import BD.BD;
import login.MainActivity;
import mycalendar.CalendarInfo;
import mycalendar.CalendarModel;
////////////////////////////

public class ActividadPrincipal extends ActionBarActivity /*implements OnClickListener*/ {
    /////////////////////////////////////
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    private final static int ADD_OR_EDIT_CALENDAR_REQUEST = 3;
    private static final Level LOGGING_LEVEL = Level.CONFIG;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    public static GoogleAccountCredential credential;
    public static com.google.api.services.calendar.Calendar client;
    public static CalendarModel model = new CalendarModel();
    int numAsyncTasks;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actividad_principal);
        BD helper= new BD(this);
        // enable logging
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        // Google Accounts
        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Baldugenda")
                .build();
        model = new CalendarModel();

    }
    @Override
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                    //AsyncLoadCalendars.run(this);
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
                        // AsyncLoadCalendars.run(this);
                    }
                }
                break;

        }
    }
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, ActividadPrincipal.this, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });}

    /** Check that Google Play services APK is installed and up to date. */
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
    }



    /*@Override
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
    }*/



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
               // intent2.putExtra("client", (android.os.Parcelable) client);
                startActivity(intent2);
                break;
            case R.id.bt_login:
                intent2 = new Intent(ActividadPrincipal.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.calendario:
                intent2 = new Intent(ActividadPrincipal.this, mycalendar.CalendarSampleActivity.class);
                startActivity(intent2);
                break;

        }
    }
    private class cal extends AsyncTask<Boolean, Boolean, Boolean> {
        // ProgressDialog pd;

        ProgressDialog pd;
        @Override
        protected Boolean doInBackground(Boolean... params) {

            try {

                client.calendarList().list().setMinAccessRole("writer").setMinAccessRole("owner").setFields(CalendarInfo.FEED_FIELDS).execute();
                return true;
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                startActivityForResult(
                        userRecoverableException.getIntent(), 1);
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ActividadPrincipal.this);
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



        }
    }



}
