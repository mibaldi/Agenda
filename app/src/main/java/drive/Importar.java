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
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.mikel.agenda.ActividadPrincipal;
import com.mikel.agenda.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import BD.BD;

/**
 * An activity that creates a text file in the App Folder.
 */
public class Importar extends BaseDemoActivity {
    private static final String TAG = "importar";
    private static final int REQUEST_CODE_OPENER = 200;
   static DriveId folderId;
    static String fileIdString="";
    TextView file;
    static Button bajar;
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        setContentView(R.layout.activity_importar);
        file=(TextView) findViewById(R.id.file);
       bajar=(Button)findViewById(R.id.recuperar2);
        file.setText(fileIdString);
        if (fileIdString.matches("")){
            bajar.setVisibility(View.INVISIBLE);
        }else{
            bajar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK ) {
                    folderId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    if(folderId!=null){
                            fileIdString=folderId.getResourceId();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    public void recuperar(View view){
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain", "text/html" })
                .build(getGoogleApiClient());
        try {

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
                    Importar.this).execute(result.getDriveId());
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

            setDB(is);

        }
    }
    public void setDB(InputStream is) {
        //BD.closeBD();
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
            fileIdString="";
            Intent intent1 = new Intent(Importar.this, ActividadPrincipal.class);
            startActivity(intent1);
            finish();
        } catch (Exception e) {}
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
