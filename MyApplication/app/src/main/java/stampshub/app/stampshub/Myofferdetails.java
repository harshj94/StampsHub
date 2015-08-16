package stampshub.app.stampshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Myofferdetails extends AppCompatActivity {

    ParseObject offerDetails;
    TextView tv;
    String objectId;

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

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Intent i=getIntent();
        Bundle b=i.getExtras();
        tv=(TextView)findViewById(R.id.offertitle);

        objectId=b.getString("objectId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
        query.fromLocalDatastore();

        try
        {
            offerDetails=query.get(objectId);
        }
        catch (ParseException e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        tv.setText(offerDetails.getString("OfferTitle"));

    }
}
