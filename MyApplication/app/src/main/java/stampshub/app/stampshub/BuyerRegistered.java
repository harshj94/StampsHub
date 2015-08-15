package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;


public class BuyerRegistered extends AppCompatActivity {

    Button btnlogout;
    ParseUser currentUser;
    TextView linktodashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registered);

        android.support.v7.app.ActionBar ab=getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        btnlogout=(Button)findViewById(R.id.logout);

        currentUser=ParseUser.getCurrentUser();

        linktodashboard=(TextView)findViewById(R.id.link_to_login);
        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView fname = (TextView) findViewById(R.id.textView6);
        final TextView lname = (TextView) findViewById(R.id.textView8);
        final TextView email = (TextView) findViewById(R.id.textView10);
        final TextView gender = (TextView) findViewById(R.id.textView12);
        final TextView phnnum = (TextView) findViewById(R.id.textView14);
        final TextView dob = (TextView) findViewById(R.id.textView16);

        utype.setText(currentUser.getString("utype"));
        fname.setText(currentUser.getString("firstname_biz"));
        lname.setText(currentUser.getString("lastname_add1"));
        email.setText(currentUser.getEmail());
        gender.setText(currentUser.getString("gender_country"));
        phnnum.setText(currentUser.getString("phn_postcode"));
        dob.setText(currentUser.getString("dob_add2"));

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        linktodashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), BuyerDashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();

            }
        });
    }

}
