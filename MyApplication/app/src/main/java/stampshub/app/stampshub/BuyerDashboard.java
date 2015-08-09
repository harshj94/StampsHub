package stampshub.app.stampshub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class BuyerDashboard extends AppCompatActivity {

    ViewPager vp;
    TabPageAdapter tpa;
    ActionBar actionBar;
    private static Menu optionsMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);

        getOverflowMenu();

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setLogo(R.mipmap.logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        vp = (ViewPager) findViewById(R.id.pager);
        tpa = new TabPageAdapter(getSupportFragmentManager());
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int i) {
                actionBar = getSupportActionBar();
                actionBar.setSelectedNavigationItem(i);
            }


        });
        vp.setAdapter(tpa);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        android.support.v7.app.ActionBar.TabListener tablistener = new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
                if (tab.getText().equals("Offers")) {
                    vp.setCurrentItem(0);
                }
                if (tab.getText().equals("My Offers")) {
                    vp.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }
        };


        actionBar.addTab(actionBar.newTab().setText("Offers").setTabListener(tablistener));
        actionBar.addTab(actionBar.newTab().setText("My Offers").setTabListener(tablistener));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu=menu;
        getMenuInflater().inflate(R.menu.menu_buyer_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh)
        {
            setRefreshActionButtonState(true);
            Offers offers=new Offers();
            offers.populateOffers();
            //setRefreshActionButtonState(false);
            Toast.makeText(getApplicationContext(),"refresh clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.logout)
        {
            ParseUser.logOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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

    public static void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}
