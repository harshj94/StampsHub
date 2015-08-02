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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registered);

        android.support.v7.app.ActionBar ab=getSupportActionBar();
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        btnlogout=(Button)findViewById(R.id.logout);

        currentUser=ParseUser.getCurrentUser();

        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView fname = (TextView) findViewById(R.id.textView6);
        final TextView lname = (TextView) findViewById(R.id.textView8);
        final TextView email = (TextView) findViewById(R.id.textView10);
        final TextView gender = (TextView) findViewById(R.id.textView12);
        final TextView phnnum = (TextView) findViewById(R.id.textView14);
        final TextView dob = (TextView) findViewById(R.id.textView16);

        utype.setText(currentUser.getString("utype"));
        fname.setText(currentUser.getString("utype"));
        lname.setText(currentUser.getString("utype"));
        email.setText(currentUser.getEmail());
        gender.setText(currentUser.getString("utype"));
        phnnum.setText(currentUser.getString("utype"));
        dob.setText(currentUser.getString("utype"));


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.logOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
