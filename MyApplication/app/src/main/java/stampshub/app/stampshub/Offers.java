package stampshub.app.stampshub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Offers extends android.support.v4.app.Fragment {

    View offers;
    ParseUser user;
    ParseObject pushData;
    ListView listView;

    ArrayList<String> listitems= new ArrayList<>();
    List<ParseObject> lst;
    ArrayAdapter<String> adapter;
    int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        offers=inflater.inflate(R.layout.offers,container,false);
        populateOffers();
        return offers;
    }

    public void populateOffers()
    {

        user=ParseUser.getCurrentUser();

        listView=(ListView)offers.findViewById(R.id.offerslist);

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

        adapter = new ArrayAdapter<>(offers.getContext(), android.R.layout.simple_list_item_1, listitems);
        listView.setAdapter(adapter);

    }

}
