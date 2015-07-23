package stampshub.app.stampshub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import stampshub.app.stampshub.Library.DatabaseHandlerBusinessowner;

/**
 * Created by user on 13/07/2015.
 */
public class BusinessOwnerRegistered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_registered);

        DatabaseHandlerBusinessowner db = new DatabaseHandlerBusinessowner(getApplicationContext());

        HashMap<String, String> user = new HashMap<String, String>();
        user = db.getUserDetails();

        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView bname = (TextView) findViewById(R.id.textView6);
        final TextView email = (TextView) findViewById(R.id.textView8);
        final TextView address1 = (TextView) findViewById(R.id.textView10);
        final TextView address2 = (TextView) findViewById(R.id.textView12);
        final TextView address3 = (TextView) findViewById(R.id.textView14);
        final TextView country = (TextView) findViewById(R.id.textView16);
        final TextView postcode = (TextView) findViewById(R.id.textView18);

        utype.setText(user.get("utype"));
        bname.setText(user.get("business_name"));
        email.setText(user.get("email_id"));
        address1.setText(user.get("address1"));
        address2.setText(user.get("address2"));
        address3.setText(user.get("address3"));
        country.setText(user.get("country"));
        postcode.setText(user.get("postcode"));
    }
}
