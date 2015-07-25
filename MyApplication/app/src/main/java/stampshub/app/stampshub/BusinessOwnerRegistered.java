package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by user on 13/07/2015.
 */
public class BusinessOwnerRegistered extends AppCompatActivity {

    ParseUser currentUser;
    Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_registered);

        btnlogout=(Button)findViewById(R.id.login);

        currentUser=ParseUser.getCurrentUser();

        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView bname = (TextView) findViewById(R.id.textView6);
        final TextView email = (TextView) findViewById(R.id.textView8);
        final TextView address1 = (TextView) findViewById(R.id.textView10);
        final TextView address2 = (TextView) findViewById(R.id.textView12);
        final TextView address3 = (TextView) findViewById(R.id.textView14);
        final TextView country = (TextView) findViewById(R.id.textView16);
        final TextView postcode = (TextView) findViewById(R.id.textView18);

        utype.setText(currentUser.getString("utype"));

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.logOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
