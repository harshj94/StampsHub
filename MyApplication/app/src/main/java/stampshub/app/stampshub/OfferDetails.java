package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class OfferDetails extends AppCompatActivity {

    ParseObject offerDetails;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        Intent i=getIntent();
        Bundle b=i.getExtras();

        tv=(TextView)findViewById(R.id.offertitle);

        String objectId=b.getString("objectId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");

        Toast.makeText(getApplicationContext(),objectId,Toast.LENGTH_LONG).show();

        try
        {
            offerDetails=query.get(objectId);
        }
        catch (ParseException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        tv.setText(offerDetails.getString("OfferTitle"));
    }
}
