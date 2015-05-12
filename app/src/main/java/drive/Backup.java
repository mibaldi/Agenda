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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.mikel.agenda.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public class Backup extends BaseDemoActivity {
    private static final String TAG = "Backup";
    private static final int REQUEST_CODE_OPENER = 1;
   static DriveId folderId;
   static String folderIdString="";
    static String fileIdString="";
    TextView fichero,file;
    static Button subir;
    private DriveId mFolderDriveId;
    String titulo;
    String modo="";
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        setContentView(R.layout.activity_backup);
        fichero=(TextView) findViewById(R.id.fichero);
        file=(TextView) findViewById(R.id.file);
        subir=(Button) findViewById(R.id.subir);
        fichero.setText(folderIdString);
        file.setText(fileIdString);
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
                        if(modo.compareTo("escoger")==0){
                            folderIdString=folderId.getResourceId();
                        }else{
                            fileIdString=folderId.getResourceId();
                        }
                    }



                }
                //finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
   /* final private ResultCallback<DriveResource.MetadataResult> metadataRetrievedCallback = new
            ResultCallback<DriveResource.MetadataResult>() {
                @Override
                public void onResult(DriveResource.MetadataResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG, "Problem while trying to fetch metadata.");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("Baldugenda").build();
                        Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                .createFolder(getGoogleApiClient(), changeSet)
                                .setResultCallback(folderCreatedCallback);
                        return;
                    }

                    Metadata metadata = result.getMetadata();
                    if(metadata.isTrashed()){
                       /MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("Baldugenda").build();
                        Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                .createFolder(getGoogleApiClient(), changeSet)
                                .setResultCallback(folderCreatedCallback);
                        Log.v(TAG, "Folder is trashed");
                    }else{

                        Log.v(TAG, "Folder is not trashed");
                    }

                }
            };
   ResultCallback<DriveFolder.DriveFolderResult> folderCreatedCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            folderId = result.getDriveFolder().getDriveId();
            folderIdString=folderId.encodeToString();
            guardarConfiguracion();
            showMessage("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };*/

    public void escoger(View view){
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { DriveFolder.MIME_TYPE })
                .build(getGoogleApiClient());
        try {
            modo="escoger";
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
                   /* DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), mFolderDriveId);
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream outputStream = driveContents.getOutputStream();
                            Writer writer = new OutputStreamWriter(outputStream);
                            try {
                                writer.write("Hello World!");
                                writer.close();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("New file")
                                    .setMimeType("text/plain")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();*/


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


                   /* MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();*/
                    folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                   /* final DriveContents driveContents = result.getDriveContents();
                    OutputStream outputStream = driveContents.getOutputStream();
                    byte[] buffer= getDBBytes();
                    try {
                        outputStream.write(buffer);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("agenda.db")
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    Drive.DriveApi.getAppFolder(getGoogleApiClient())
                            .createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);*/
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
                }
            };


    public void recuperar(View view){
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain", "text/html" })
                .build(getGoogleApiClient());
        try {
            modo="recuperar";
            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            Log.w(TAG, "Unable to send intent", e);
        }
    }
    public void importar(View view){
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), fileIdString)
                .setResultCallback(idCallback2);
    }
    final private ResultCallback<DriveApi.DriveIdResult> idCallback2 = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            new RetrieveDriveFileContentsAsyncTask(
                    Backup.this).execute(result.getDriveId());
        }
    };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {
        InputStream is;
        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = "";
            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), params[0]);
            DriveApi.DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();

        is = driveContents.getInputStream();
/*
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {
                Log.e(TAG, "IOException while reading from the stream", e);
            }*/

            driveContents.discard(getGoogleApiClient());

            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                showMessage("Error while reading from the file");
                return;
            }
            //InputStream is = new ByteArrayInputStream(result.getBytes());
            setDB(is);
            //showMessage("File contents: " + result);
        }
    }





    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    showMessage("Selected file's ID: " + driveId);
                }
                finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    /*
    String driveID="";
    DriveId folderId;
    public String EXISTING_FOLDER_ID;


    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("Baldugenda").build();
        Drive.DriveApi.getRootFolder(getGoogleApiClient())
                .createFolder(getGoogleApiClient(), changeSet)
                .setResultCallback(folderCreatedCallback);
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FOLDER_ID)
                .setResultCallback(idCallback);

    }

    ResultCallback<DriveFolder.DriveFolderResult> folderCreatedCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            folderId = result.getDriveFolder().getDriveId();

            showMessage("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };
    /*final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
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

    final private ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }
                    DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), mFolderDriveId);
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();
                    folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                }
            };

    final private ResultCallback<DriveFileResult> fileCallback =
            new ResultCallback<DriveFileResult>() {
                @Override
                public void onResult(DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    showMessage("Created a file: " + result.getDriveFile().getDriveId());
                }
            };

    /*@Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
        /*final ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
            @Override
            public void onResult(DriveApi.DriveIdResult result) {
                if (!result.getStatus().isSuccess()) {
                    Drive.DriveApi.newDriveContents(getGoogleApiClient())
                            .setResultCallback(driveContentsCallback);
                    return;
                }else{
                    DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), result.getDriveId());
                    new EditContentsAsyncTask(Backup.this).execute(file);
                }

            }
        };
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FILE_ID)
                .setResultCallback(idCallback);
    }

    final private ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create new file contents");
                return;
            }

            final DriveContents driveContents = result.getDriveContents();
            OutputStream outputStream = driveContents.getOutputStream();
            byte[] buffer= getDBBytes();
            try {
                outputStream.write(buffer);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle("agenda.db")
                    .setMimeType("text/plain")
                    .setStarred(true)
                    .build();

            Drive.DriveApi.getAppFolder(getGoogleApiClient())
                    .createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                    .setResultCallback(fileCallback);
        }
    };

    final private ResultCallback<DriveFileResult> fileCallback = new
            ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the file");
                return;
            }
           driveID =result.getDriveFile().getDriveId().toString();
            showMessage("Created a file in App Folder: "
                    + result.getDriveFile().getDriveId());
            guardarConfiguracion();
        }
    };
    public class EditContentsAsyncTask extends ApiClientAsyncTask<DriveFile, Void, Boolean> {

        public EditContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected Boolean doInBackgroundConnected(DriveFile... args) {
            DriveFile file = args[0];
            try {
                DriveContentsResult driveContentsResult = file.open(
                        getGoogleApiClient(), DriveFile.MODE_WRITE_ONLY, null).await();
                if (!driveContentsResult.getStatus().isSuccess()) {
                    return false;
                }
                DriveContents driveContents = driveContentsResult.getDriveContents();
                OutputStream outputStream = driveContents.getOutputStream();
               byte[] buffer= getDBBytes();
                outputStream.write(buffer);
                com.google.android.gms.common.api.Status status =
                        driveContents.commit(getGoogleApiClient(), null).await();
                return status.getStatus().isSuccess();
            } catch (IOException e) {
                Log.e(TAG, "IOException while appending to the output stream", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                showMessage("Error while editing contents");
                return;
            }
            showMessage("Successfully edited contents");
        }
    }*/
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
    public void setDB(InputStream is) {
        BD.closeBD();
        Context ctx = getApplicationContext();
        try {
            File to = ctx.getDatabasePath(BD.DB_NAME);
            if (to.exists())
                to.delete();
            strm2File(is, to);
        } catch (Exception e) {}
    }
    private void strm2File(InputStream inStrm, File fl) {
        try {

            OutputStream outStrm = new FileOutputStream(fl);
            try {
                final byte[] buffer = new byte[1024];
                BufferedInputStream bufIS = null;
                if (inStrm != null) try {
                    bufIS = new BufferedInputStream(inStrm);
                    int cnt = 0;
                    while ((cnt = bufIS.read(buffer)) >= 0) {
                        outStrm.write(buffer, 0, cnt);
                    }outStrm.flush();
                }finally {outStrm.close();}} catch (Exception e) {}
            inStrm.close();
            showMessage("importacion realizada");
        } catch (Exception e) {}
    }



    //guardar configuración aplicación Android usando SharedPreferences
    public void guardarConfiguracion()
    {
        SharedPreferences prefs =
                getSharedPreferences("preferenciasApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("driveID",folderIdString);
        editor.putString("folderID",folderIdString);
        editor.commit();
    }
}
