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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    TextView linktoregister, forgotpassword;
    EditText emailid, password;
    Button login;
    ParseUser user;
    Integer i;
    ParseInstallation installation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar ab=getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        user=new ParseUser();
        user=ParseUser.getCurrentUser();
        if(user.getObjectId()!=null)
        {
            installation = ParseInstallation.getCurrentInstallation();
            installation.put("user",ParseUser.getCurrentUser());
            installation.put("utype",user.getString("utype"));
            try {
                installation.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String s=user.getString("utype");
            Intent i;
            if(s.equals("Buyer"))
            {
                i=new Intent(getApplicationContext(),BuyerDashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
            else if(s.equals("Businessowner"))
            {
                i=new Intent(getApplicationContext(),BusinessOwnerDashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
        }

        linktoregister = (TextView) findViewById(R.id.link_to_register);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        emailid = (EditText) findViewById(R.id.emailid);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.btnLogin);

        linktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SelectUserType.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetAsync(v);
            }
        });
    }


    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        /**
         * Gets current device state and checks for working internet connection by trying Google.
         */
        @Override
        protected Boolean doInBackground(String... args) {

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
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean th) {

            if (th) {
                nDialog.dismiss();
                new ProcessLogin().execute();
            } else {
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

    private class ProcessLogin extends AsyncTask<String, String, Integer> {


        private ProgressDialog pDialog;

        String email, userpassword;

        @Override
        protected void onPreExecute()
        {
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
        protected Integer doInBackground(String... args)
        {

            try {
                ParseUser.logIn(email,userpassword);
                i=0;
            } catch (ParseException e) {
                e.printStackTrace();
                i=e.getCode();
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            pDialog.dismiss();
            if(i==0)
            {
                user=ParseUser.getCurrentUser();
                if(user!=null)
                {
                    installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user",ParseUser.getCurrentUser());
                    installation.put("utype",user.getString("utype"));
                    try {
                        installation.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String s=user.getString("utype");
                    Intent i1;
                    if(s.equals("Buyer"))
                    {
                        i1=new Intent(getApplicationContext(),BuyerDashboard.class);
                        startActivity(i1);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    }
                    else if(s.equals("Businessowner"))
                    {
                        i1=new Intent(getApplicationContext(),BusinessOwnerDashboard.class);
                        startActivity(i1);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    }
                    finish();

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error code: "+i,Toast.LENGTH_LONG).show();
            }

        }
    }

    public void NetAsync(View view) {
        new NetCheck().execute();
    }

}

