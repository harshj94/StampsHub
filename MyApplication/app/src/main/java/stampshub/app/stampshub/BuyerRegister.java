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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import stampshub.app.stampshub.Library.DatabaseHandler;
import stampshub.app.stampshub.Library.UserFunctions;


public class BuyerRegister extends AppCompatActivity {

    EditText fname, lname, email, phonenumber,securityans, password;
    RadioGroup gender;
    Spinner securityques;
    Button dateofbirth,registerbuyer;
    String gendertemp,dateofbirth_temp,secques_temp;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    TextView dateofbirth_disp;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UTYPE = "utype";
    private static String KEY_FIRSTNAME = "first_name";
    private static String KEY_LASTNAME = "last_name";
    private static String KEY_EMAIL = "email_id";
    private static String KEY_GENDER="user_gender";
    private static String KEY_PHNNUM="phone_number";
    private static String KEY_DOB="date_of_birth";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";



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
        protected void onPostExecute(Boolean th) {

            if (th == true) {
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


    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

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
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.registerUser(utype, first_name, last_name, email_id, user_gender,phone_number, date_of_birth, security_question,security_answer,user_password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if (Integer.parseInt(res) == 1) {
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        new AlertDialog.Builder(BuyerRegister.this)
                                .setTitle("Success")
                                .setMessage("You are successfully registered.")
                                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();


                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_UTYPE),json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_GENDER), json_user.getString(KEY_PHNNUM), json_user.getString(KEY_DOB),json_user.getString(KEY_CREATED_AT));
                        /**
                         * Stores registered data in SQlite Database
                         * Launch Registered screen
                         **/

                        Intent registered = new Intent(getApplicationContext(), BuyerRegistered.class);

                        /**
                         * Close all views before launching Registered screen
                         **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);
                        finish();
                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        new AlertDialog.Builder(BuyerRegister.this)
                                .setTitle("Error")
                                .setMessage("User already exists")
                                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    } else if (Integer.parseInt(red) == 3) {
                        pDialog.dismiss();
                        new AlertDialog.Builder(BuyerRegister.this)
                                .setTitle("Error")
                                .setMessage("Invalid Email id")
                                .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }

                } else {
                    pDialog.dismiss();
                    new AlertDialog.Builder(BuyerRegister.this)
                            .setTitle("Error")
                            .setMessage("Unknown error occured in registration")
                            .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void NetAsync(View view) {
        new NetCheck().execute();
    }
}
