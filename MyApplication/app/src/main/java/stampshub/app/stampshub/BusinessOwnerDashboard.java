package stampshub.app.stampshub;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Field;


public class BusinessOwnerDashboard extends AppCompatActivity {

    ParseUser user;
    Button sendOffer;
    EditText offer_title;
    ParseObject offer=new ParseObject("Offer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner_dashboard);

        getOverflowMenu();

        android.support.v7.app.ActionBar ab=getSupportActionBar();
        assert ab != null;
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        sendOffer=(Button)findViewById(R.id.offerSend);
        offer_title=(EditText)findViewById(R.id.offertitle);

        user=ParseUser.getCurrentUser();


        sendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                offer.put("OfferTitle",offer_title.getText().toString());
                offer.put("Biz_name",user.getString("firstname_biz"));
                offer.put("user", ParseUser.getCurrentUser().getObjectId());
                offer.saveInBackground();
                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_business_owner_dashboard, menu);
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

            case R.id.action_settings:
                return true;

            case R.id.logout:
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }
    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
