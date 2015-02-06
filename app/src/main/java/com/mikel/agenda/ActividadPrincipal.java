package com.mikel.agenda;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import com.mikel.agenda.R;

import BD.BD;


public class ActividadPrincipal extends ActionBarActivity /*implements OnClickListener*/ {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        BD helper= new BD(this);
    }


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
                break;
            case R.id.button3:
                break;
            case R.id.button5:
                intent2 = new Intent(ActividadPrincipal.this, crear_asignatura.class);
                startActivity(intent2);
                break;
        }
    }


}
