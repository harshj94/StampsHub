package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class BusinessOwnerRegistered extends AppCompatActivity {

    ParseUser currentUser;
    Button btnlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_registered);

        android.support.v7.app.ActionBar ab=getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        btnlogout=(Button)findViewById(R.id.login);

        currentUser=ParseUser.getCurrentUser();

        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView bname = (TextView) findViewById(R.id.textView6);
        final TextView email = (TextView) findViewById(R.id.textView8);
        final TextView address1 = (TextView) findViewById(R.id.textView10);
        final TextView address2 = (TextView) findViewById(R.id.textView12);
        final TextView country = (TextView) findViewById(R.id.textView14);
        final TextView postcode = (TextView) findViewById(R.id.textView16);

        utype.setText(currentUser.getString("utype"));
        bname.setText(currentUser.getString("firstname_biz"));
        email.setText(currentUser.getEmail());
        address1.setText(currentUser.getString("lastname_add1"));
        address2.setText(currentUser.getString("dob_add2"));
        country.setText(currentUser.getString("gender_country"));
        postcode.setText(currentUser.getString("phn_postcode"));

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
