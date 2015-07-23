package stampshub.app.stampshub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import stampshub.app.stampshub.Library.DatabaseHandler;
import stampshub.app.stampshub.Library.DatabaseHandlerBusinessowner;
import stampshub.app.stampshub.Library.UserFunctions;


public class LoginActivity extends AppCompatActivity {

    TextView linktoregister,forgotpassword;
    String usertypeselected;
    RadioGroup usertype;
    EditText emailid,password;
    Button login;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_UTYPE = "utype";
    private static String KEY_FIRSTNAME = "first_name";
    private static String KEY_LASTNAME = "last_name";
    private static String KEY_EMAIL = "email_id";
    private static String KEY_GENDER="user_gender";
    private static String KEY_PHNNUM="phone_number";
    private static String KEY_DOB="date_of_birth";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_bname = "business_name";
    private static String KEY_bemail = "email_id";
    private static String KEY_baddr1 = "address1";
    private static String KEY_baddr2 = "address2";
    private static String KEY_baddr3 = "address3";
    private static String KEY_bcountry = "country";
    private static String KEY_bpostcode = "postcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usertype=(RadioGroup)findViewById(R.id.usertype);

        linktoregister=(TextView)findViewById(R.id.link_to_register);
        forgotpassword=(TextView)findViewById(R.id.forgotpassword);

        emailid=(EditText)findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.password);

        login=(Button)findViewById(R.id.btnLogin);

        linktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SelectUserType.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        usertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getUserType();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetAsync(v);
            }
        });

//        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//        DatabaseHandlerBusinessowner db1 = new DatabaseHandlerBusinessowner(getApplicationContext());
//        int i=db.getRowCount();
//        int j=db1.getRowCount();
//        if(i==1)
//        {
//            Intent upanel = new Intent(getApplicationContext(), BuyerRegistered.class);
//            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(upanel);
//        }
//        else if(j==1)
//        {
//            Intent upanel = new Intent(getApplicationContext(), BusinessOwnerRegistered.class);
//            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(upanel);
//        }
    }

    public void getUserType()
    {
        int var=usertype.getCheckedRadioButtonId();
        RadioButton rb1=(RadioButton)findViewById(var);
        usertypeselected=rb1.getText().toString();
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error")
                        .setMessage("You are not connected to internet")
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String email,userpassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = emailid.getText().toString();
            userpassword = password.getText().toString();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUserbiz(email, userpassword);
                return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");

                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
//                        UserFunctions logout = new UserFunctions();
//                        logout.logoutUser(getApplicationContext());

                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent upanel;
                            DatabaseHandlerBusinessowner db = new DatabaseHandlerBusinessowner(getApplicationContext());
                            db.adduser(json_user.getString(KEY_UTYPE), json_user.getString(KEY_bname), json_user.getString(KEY_bemail), json_user.getString(KEY_baddr1), json_user.getString(KEY_baddr2), json_user.getString(KEY_baddr3), json_user.getString(KEY_bcountry), json_user.getString(KEY_bpostcode), json_user.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                            upanel = new Intent(getApplicationContext(), BuyerRegistered.class);
                            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }
                    else
                    {

                        pDialog.dismiss();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage("Incorrect Username or Password")
                                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }

}

