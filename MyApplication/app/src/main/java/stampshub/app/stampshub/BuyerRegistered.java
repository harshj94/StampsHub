package stampshub.app.stampshub;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import stampshub.app.stampshub.Library.DatabaseHandler;


public class BuyerRegistered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registered);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        HashMap<String, String> user = new HashMap<String, String>();
        user = db.getUserDetails();

        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView fname = (TextView) findViewById(R.id.textView6);
        final TextView lname = (TextView) findViewById(R.id.textView8);
        final TextView email = (TextView) findViewById(R.id.textView10);
        final TextView gender = (TextView) findViewById(R.id.textView12);
        final TextView phnnum = (TextView) findViewById(R.id.textView14);
        final TextView dob = (TextView) findViewById(R.id.textView16);
        final TextView created_at = (TextView) findViewById(R.id.textView18);

        utype.setText(user.get("utype"));
        fname.setText(user.get("first_name"));
        lname.setText(user.get("last_name"));
        email.setText(user.get("email_id"));
        gender.setText(user.get("user_gender"));
        phnnum.setText(user.get("phone_number"));
        dob.setText(user.get("date_of_birth"));
        created_at.setText(user.get("created_at"));
    }

}
