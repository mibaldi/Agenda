/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package drive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.mikel.agenda.ActividadPrincipal;
import com.mikel.agenda.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import BD.BD;

/**
 * An activity that creates a text file in the App Folder.
 */
public class Exportar extends BaseDemoActivity {
    private static final String TAG = "Backup";
    private static final int REQUEST_CODE_OPENER = 200;
   static DriveId folderId;
   static String folderIdString="";
    TextView fichero,file;
    static Button subir;
    private DriveId mFolderDriveId;
    String titulo;
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        setContentView(R.layout.activity_exportar);
        fichero=(TextView) findViewById(R.id.fichero);
        subir=(Button) findViewById(R.id.subir);
        fichero.setText(folderIdString);
        if (folderIdString.matches("")){
            subir.setVisibility(View.INVISIBLE);
        }else{
            subir.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    folderId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    if(folderId!=null){
                            folderIdString=folderId.getResourceId();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    public void escoger(View view){
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { DriveFolder.MIME_TYPE })
                .build(getGoogleApiClient());
        try {

            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }

    }
    public void subir(View view){
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), folderIdString)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }
            mFolderDriveId = result.getDriveId();
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        }
    };
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }
                    DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), mFolderDriveId);
                    final DriveContents driveContents = result.getDriveContents();
                    OutputStream outputStream = driveContents.getOutputStream();
                    byte[] buffer= getDBBytes();
                    try {
                        outputStream.write(buffer);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    titulo="agenda"+dateFormat.format(cal.getTime())+".db";
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(titulo)
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();
                    folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    showMessage("Created a file: " + titulo);
                    folderIdString="";
                    Intent intent1 = new Intent(Exportar.this, ActividadPrincipal.class);
                    startActivity(intent1);
                    finish();
                }
            };


    byte[] getDBBytes() {
        Context ctx = getApplicationContext();
        byte[] out = null;
        try {
            File from = ctx.getDatabasePath(BD.DB_NAME);
            if (from.exists())
                out = strm2Bytes(new FileInputStream(from));
        } catch (Exception e) {}
        return out;
    }
    public byte[] strm2Bytes(InputStream is) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        BufferedInputStream bufIS = null;
        if (is != null) try {
            bufIS = new BufferedInputStream(is);
            int cnt = 0;
            while ((cnt = bufIS.read(buffer)) >= 0) {
                byteBuffer.write(buffer, 0, cnt);
            }
        } catch (Exception e) {}
        finally { try { if (bufIS != null) bufIS.close(); } catch (IOException e) {}}
        return byteBuffer.toByteArray();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_drive, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accounts:
                chooseAccount();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
