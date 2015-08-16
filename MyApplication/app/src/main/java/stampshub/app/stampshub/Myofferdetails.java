package stampshub.app.stampshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Myofferdetails extends AppCompatActivity {

    ParseObject offerDetails,forstampcount;
    TextView tv,stampscount;
    String objectId,user;
    List<ParseObject> lst;
    Button addstamp;

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),BuyerDashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myofferdetails);

        user= ParseUser.getCurrentUser().getObjectId();

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Intent i=getIntent();
        Bundle b=i.getExtras();

        addstamp=(Button)findViewById(R.id.addstamp);
        tv=(TextView)findViewById(R.id.offertitle);
        stampscount=(TextView)findViewById(R.id.stampscount);

        objectId=b.getString("objectId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
        query.fromLocalDatastore();

        ParseQuery<ParseObject> query2=ParseQuery.getQuery("myoffer");
        query2.whereEqualTo("offer", objectId);
        query2.whereEqualTo("user", user);
        query2.fromLocalDatastore();

        try
        {
            offerDetails=query.get(objectId);
            lst=query2.find();
        }
        catch (ParseException e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        forstampcount=lst.get(0);
        tv.setText(offerDetails.getString("OfferTitle"));
        stampscount.setText(" "+forstampcount.getInt("stampscount"));

        addstamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forstampcount.increment("stampscount");
                try
                {
                    forstampcount.save();
                    forstampcount.pin();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                stampscount.setText(" "+forstampcount.getInt("stampscount"));
            }
        });

    }
}
