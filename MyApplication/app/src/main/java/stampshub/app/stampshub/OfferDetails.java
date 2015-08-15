package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class OfferDetails extends AppCompatActivity {

    ParseObject offerDetails;
    Button btnoffer;
    TextView tv;
    List<ParseObject> lst;
    ParseObject myoffer=new ParseObject("myoffer");

    ParseQuery<ParseObject> parseQuery;
    String objectId;

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),BuyerDashboard.class);
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        Intent i=getIntent();
        Bundle b=i.getExtras();

        btnoffer=(Button)findViewById(R.id.btnoffer);

        tv=(TextView)findViewById(R.id.offertitle);

        objectId=b.getString("objectId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");

        try
        {
            offerDetails=query.get(objectId);

        }
        catch (ParseException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        tv.setText(offerDetails.getString("OfferTitle"));

        btnoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parseQuery=ParseQuery.getQuery("myoffer");
                parseQuery.whereEqualTo("user",ParseUser.getCurrentUser().getObjectId());
                parseQuery.whereEqualTo("offer",objectId);
                try
                {
                    lst=parseQuery.find();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                if(lst.size()==0)
                {
                    int i=0;
                    myoffer.put("user",ParseUser.getCurrentUser().getObjectId());
                    myoffer.put("offer",objectId);
                    myoffer.put("stampscount",i);
                    try
                    {
                        myoffer.save();
                        Toast.makeText(getApplicationContext(),"Offer Successfully added to your account.",Toast.LENGTH_LONG).show();
                    }
                    catch (ParseException e)
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"You are already enrolled to this offer!!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
