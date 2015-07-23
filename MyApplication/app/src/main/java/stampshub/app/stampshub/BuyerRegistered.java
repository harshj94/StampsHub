package stampshub.app.stampshub;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.HashMap;

import stampshub.app.stampshub.Library.DatabaseHandler;
import stampshub.app.stampshub.Library.UserFunctions;


public class BuyerRegistered extends AppCompatActivity {

    Button btnlogout;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_registered);

        btnlogout=(Button)findViewById(R.id.logout);

        currentUser=ParseUser.getCurrentUser();
        final TextView utype = (TextView) findViewById(R.id.textView4);
        final TextView fname = (TextView) findViewById(R.id.textView6);
        final TextView lname = (TextView) findViewById(R.id.textView8);
        final TextView email = (TextView) findViewById(R.id.textView10);
        final TextView gender = (TextView) findViewById(R.id.textView12);
        final TextView phnnum = (TextView) findViewById(R.id.textView14);
        final TextView dob = (TextView) findViewById(R.id.textView16);
        final TextView created_at = (TextView) findViewById(R.id.textView18);



        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
