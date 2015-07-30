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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class BusinessOwnerRegister extends AppCompatActivity {
    EditText Business_name, Business_email, Business_address1, Business_address2, Business_country, Business_postcode, Business_pass, security_ans;
    Spinner Business_sequestion;
    String secques_temp;
    Button registerbizowner;
    ParseUser buyer;
    TextView link_to_login;
    Integer i;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_register);

        Business_name = (EditText) findViewById(R.id.biz_firstname);
        Business_email = (EditText) findViewById(R.id.biz_email);
        Business_address1 = (EditText) findViewById(R.id.biz_add1);
        Business_address2 = (EditText) findViewById(R.id.biz_add2);
        Business_country = (EditText) findViewById(R.id.biz_country);
        Business_postcode = (EditText) findViewById(R.id.biz_postcode);
        Business_pass = (EditText) findViewById(R.id.reg_password);
        security_ans = (EditText) findViewById(R.id.reg_securityans);

        Business_sequestion = (Spinner) findViewById(R.id.reg_security_question);

        registerbizowner = (Button) findViewById(R.id.btnRegister);

        link_to_login=(TextView)findViewById(R.id.link_to_login);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_question, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Business_sequestion.setAdapter(adapter);

        Business_sequestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secques_temp = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secques_temp = parent.getItemAtPosition(0).toString();
            }
        });

        registerbizowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetAsync(v);
            }
        });

        link_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                finish();
            }
        });

    }


    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(BusinessOwnerRegister.this);
            nDialog.setMessage("Loading...");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
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
                new ProcessRegister().execute();
            } else {
                nDialog.dismiss();
                new AlertDialog.Builder(BusinessOwnerRegister.this)
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

    private class ProcessRegister extends AsyncTask<String, String, Integer> {

        /**
         * Defining Process dialog
         */
        private ProgressDialog pDialog;

        String utype, business_name, email_id, address1, address2, country, postcode, security_question, security_answer, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            utype = "Businessowner";
            business_name = Business_name.getText().toString();
            email_id = Business_email.getText().toString();
            address1 = Business_address1.getText().toString();
            address2 = Business_address2.getText().toString();
            country = Business_country.getText().toString();
            postcode = Business_postcode.getText().toString();
            security_question = secques_temp;
            security_answer = security_ans.getText().toString();
            password = Business_pass.getText().toString();

            pDialog = new ProgressDialog(BusinessOwnerRegister.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {

            buyer=new ParseUser();

            buyer.setUsername(email_id);
            buyer.setEmail(email_id);
            buyer.setPassword(password);

            buyer.put("utype", utype);
            buyer.put("firstname_biz",business_name);
            buyer.put("lastname_add1", address1);
            buyer.put("gender_country", country);
            buyer.put("phn_postcode",postcode);
            buyer.put("dob_add2",address2);
            buyer.put("sec_ques", security_question);
            buyer.put("sec_ans", security_answer);

            try {
                buyer.signUp();
                i=0;
            } catch (ParseException e) {
                i=e.getCode();
                e.printStackTrace();
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            pDialog.dismiss();
            if(i==0)
            {
                Intent i1=new Intent(getApplicationContext(),BusinessOwnerRegistered.class);
                startActivity(i1);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error code: " + i, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void NetAsync(View view) {
        new NetCheck().execute();
    }

}



