package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class BuyerDashboard extends AppCompatActivity {

    ParseUser user;
    ParseObject pushData;
    ListView listView;

    ArrayList<String> listitems=new ArrayList<>();
    List<ParseObject> lst;
    ArrayAdapter<String> adapter;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);

        user=ParseUser.getCurrentUser();

        listView=(ListView)findViewById(R.id.listView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
        query.whereNotEqualTo("OfferTitle", "fff");
        query.setLimit(1000);
        try
        {
            lst=query.find();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        for(i=0;i<lst.size();i++)
        {
            pushData=lst.get(i);
            listitems.add(pushData.getString("OfferTitle"));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitems);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_buyer_dashboard, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {

            case R.id.refresh:
                listitems.clear();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
                query.whereNotEqualTo("OfferTitle", "fff");
                query.setLimit(1000);
                try
                {
                    lst=query.find();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                for(i=0;i<lst.size();i++)
                {
                    pushData=lst.get(i);
                    listitems.add(pushData.getString("OfferTitle"));
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listitems);
                listView.setAdapter(adapter);

                break;

            case R.id.logout:
                user.logOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
