package login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.mikel.agenda.ActividadPrincipal;
import com.mikel.agenda.R;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;



public class MainActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlusClient.OnAccessRevokedListener {

    //Variables staticos
    protected static GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    protected static ConnectionResult mConnectionResult;
    protected static String accountName = null;
    protected static String token = null;
    private static Context context = null;
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400; // Profile pic image size in pixels

    //Objetos UI
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout, llCargandoLayout, llSignInLayout;

    //Variables de clase
    private boolean isProcessSignIn = false;
    private boolean isSignedIn = false;
    private final String TAG = getClass().getSimpleName();
    private ProgressDialog mConnectionProgressDialog;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Actualizamos el context
        context = this.getApplicationContext();

        //Cargar botones etc.
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        llCargandoLayout = (LinearLayout) findViewById(R.id.llCargando);
        llSignInLayout = (LinearLayout) findViewById(R.id.llSignIn);

        //Logica del UI. 0 == mostrar cargando
        Log.e("Prueba", String.valueOf(getIntent().getIntExtra("acceso", 0)));
        /*if (getIntent().getIntExtra("acceso", 0)==1) isProcessSignIn = true;
        else isProcessSignIn = false;*/
        isProcessSignIn = true;
        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        //Actualizar UI
        updateUI();
        // Se tiene que mostrar esta barra de progreso si no se resuelve el fallo de conexión.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");

        //Cargar API de Google
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


        if (isProcessSignIn) {
            mGoogleApiClient.connect();
        } else {
            Intent intent = new Intent(getApplicationContext() , ActividadPrincipal.class);
            startActivity(intent);
        }
        new CallAPI().execute("https://www.googleapis.com/calendar/v3/users/me/calendarList?key=AIzaSyCCZnU1voxq4tqrS74fc_dRmRxf1TS9izU");
        //75:32:B6:8E:90:66:5B:48:92:F1:41:B6:6E:A8:39:CF:CF:43:BD:6C
        //AIzaSyCCZnU1voxq4tqrS74fc_dRmRxf1TS9izU
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
        Toast.makeText(this, accountName + Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getId() + " is connected.", Toast.LENGTH_LONG).show();

        isSignedIn = true;

        // Get user's information
        getProfileInformation();
        // Update the UI after signin
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {
        isSignedIn = false;
        mGoogleApiClient.connect();
        updateUI();
    }

   /* @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
        // Guarda el resultado y resuelve el fallo de conexión con el clic de un usuario.
        mConnectionResult = result;
    }*/

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }




    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }



    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;
                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccessRevoked(ConnectionResult connectionResult) {
        // mPlusClient is now disconnected and access has been revoked.
        // We should now delete any data we need to comply with the
        // developer properties. To reset ourselves to the original state,
        // we should now connect again. We don't have to disconnect as that
        // happens as part of the call.
        mGoogleApiClient.connect();

        // Hide the sign out buttons, show the sign in button.
        isSignedIn=false;
        updateUI();
    }


    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            isSignedIn = false;
            updateUI();
        }
    }

    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status arg0) {
                    Log.e(TAG, "User access revoked!");
                    isSignedIn = false;
                    mGoogleApiClient.connect();
                    updateUI();
                }
            });
        }
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI() {
        if (!isProcessSignIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
            llSignInLayout.setVisibility(View.GONE);
            llCargandoLayout.setVisibility(View.VISIBLE);
        } else {
            llSignInLayout.setVisibility(View.VISIBLE);
            llCargandoLayout.setVisibility(View.GONE);
            if (isSignedIn) {
                btnSignIn.setVisibility(View.GONE);
                btnSignOut.setVisibility(View.VISIBLE);
                btnRevokeAccess.setVisibility(View.VISIBLE);
                llProfileLayout.setVisibility(View.VISIBLE);
            } else {
                btnSignIn.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.GONE);
                btnRevokeAccess.setVisibility(View.GONE);
                llProfileLayout.setVisibility(View.GONE);
            }
        }
    }


    protected static void actualizarToken(){
        AsyncTask actualizarTokenTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                Log.v("TOKEN ACTUALIZADO:", "entro1");
                if (context == null || accountName == null){
                    token = null;
                    Log.v("TOKEN ACTUALIZADO:", "entro2");
                    return null;
                }
                String scope = "oauth2:" + Scopes.PLUS_LOGIN;
                try {
                    // We can retrieve the token to check via
                    // tokeninfo or to pass to a service-side
                    // application.
                    Log.v("TOKEN ACTUALIZADO:", "entro3");
                    token = GoogleAuthUtil.getToken(context,  accountName, scope);
                    Log.v("TOKEN ACTUALIZADO:", "entro4");
                    Log.v("TOKEN ACTUALIZADO:", token);
                } catch (UserRecoverableAuthException e) {
                    // This error is recoverable, so we could fix this
                    // by displaying the intent to the user.
                    e.printStackTrace();
                    token = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    token = null;
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                    token = null;
                }
                Log.v("TOKEN ACTUALIZADO:", "entro5");
                return null;
            }
        };

        actualizarTokenTask.execute((Void) null);
    }
    private class CallAPI extends AsyncTask<String, String, String> {
        private String Content;
        String data ="";

        @Override
        protected String doInBackground(String... params) {
            String urlString=params[0]; // URL to call
            String respStr;
            respStr = requestGET(urlString);

            JSONObject respJSON;
            try {
                respJSON = new JSONObject(respStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return respStr;
        }

        protected void onPostExecute(String result) {

        }

    } // end CallAPI
    public static String requestGET(String url){

        String respStr="";

        HttpClient httpClient = new DefaultHttpClient();

        HttpGet get = new HttpGet(url);

        get.setHeader("content-type", "application/json");

        try {
            HttpResponse resp = httpClient.execute(get);
            respStr = EntityUtils.toString(resp.getEntity());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return respStr;

    }



}
