package stampshub.app.stampshub;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class BuyerRegister extends AppCompatActivity {

    EditText fname, lname, email, phonenumber,securityans, password;
    RadioGroup gender;
    Spinner securityques;
    Button dateofbirth,registerbuyer;
    String gendertemp,dateofbirth_temp,secques_temp;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    TextView dateofbirth_disp;
    ParseUser buyer;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_register);

        fname=(EditText)findViewById(R.id.reg_firstname);
        lname=(EditText)findViewById(R.id.reg_lastname);
        email=(EditText)findViewById(R.id.reg_email);
        phonenumber=(EditText)findViewById(R.id.reg_phnnum);
        securityans=(EditText)findViewById(R.id.reg_securityans);
        password=(EditText)findViewById(R.id.reg_password);

        gender=(RadioGroup)findViewById(R.id.gender);

        securityques=(Spinner)findViewById(R.id.reg_security_question);

        registerbuyer=(Button)findViewById(R.id.btnRegister);
        dateofbirth=(Button)findViewById(R.id.datepicker);

        dateofbirth_disp=(TextView)findViewById(R.id.dateshow);


        registerbuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetAsync(v);
            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getGender();
            }
        });

        Calendar newCalendar = Calendar.getInstance();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateofbirth_temp=dateFormatter.format(newDate.getTime());
                dateofbirth_disp.setText("Your Date of Birth: "+dateofbirth_temp);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security_question, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        securityques.setAdapter(adapter);

        securityques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secques_temp= parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secques_temp = parent.getItemAtPosition(0).toString();
            }
        });
    }

    public void getGender()
    {
        int var=gender.getCheckedRadioButtonId();
        RadioButton rb1=(RadioButton)findViewById(var);
        gendertemp=rb1.getText().toString();
    }

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(BuyerRegister.this);
            nDialog.setMessage("Loading..");
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
                new AlertDialog.Builder(BuyerRegister.this)
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

        String utype, first_name, last_name, email_id, user_gender,phone_number, date_of_birth, security_question,security_answer,user_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            utype="Buyer";
            first_name=fname.getText().toString();
            last_name=lname.getText().toString();
            email_id=email.getText().toString();
            user_gender=gendertemp;
            phone_number=phonenumber.getText().toString();
            date_of_birth=dateofbirth_temp;
            security_question=secques_temp;
            security_answer=securityans.getText().toString();
            user_password=password.getText().toString();

            pDialog = new ProgressDialog(BuyerRegister.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            buyer=new ParseUser();
            buyer.put("utype",utype);
            buyer.put("first_business_name",first_name);
            buyer.put("last_name_address1", last_name);
            buyer.setEmail(email_id);
            buyer.put("user_gender_country", user_gender);
            buyer.put("phone_number_postcode",phone_number);
            buyer.put("date_of_birth_address2",date_of_birth);
            buyer.put("security_question",security_question);
            buyer.put("security_answer", security_answer);
            buyer.setPassword(user_password);

            buyer.setUsername(email_id);

            buyer.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        i=-111;
                    }
                    else
                    {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        i=e.getCode();
                    }
                }
            });
            return i;
        }

        @Override
        protected void onPostExecute(Integer i){

            pDialog.cancel();
            if(i!=0)
            {
                new AlertDialog.Builder(BuyerRegister.this)
                        .setTitle("Error")
                        .setMessage("Error in registration"+i)
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else
            {
                new AlertDialog.Builder(BuyerRegister.this)
                        .setTitle("Success")
                        .setMessage("You are succesfully registered.")
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                Intent i1=new Intent(getApplicationContext(),BuyerRegistered.class);
                startActivity(i1);
                finish();
            }

        }
    }
    public void NetAsync(View view) {
        new NetCheck().execute();
    }
}
