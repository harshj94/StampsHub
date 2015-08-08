package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Offers extends Fragment {

    public static View offers;
    ParseUser user;
    ParseObject pushData;
    public static ListView listView;

    public static ArrayList<Item> items = new ArrayList<>();
    List<ParseObject> lst;
    int i;
    public static OffersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        offers = inflater.inflate(R.layout.offers, container, false);
        listView = (ListView) offers.findViewById(R.id.offerslist);
        user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
        query.whereNotEqualTo("OfferTitle", "fff");
        query.orderByDescending("createdAt");
        query.setLimit(1000);
        try {
            lst = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        items.clear();
        for (i = 0; i < lst.size(); i++) {
            pushData = lst.get(i);
            String offertitle = pushData.getString("OfferTitle");
            String biz_name = pushData.getString("Biz_name");
            String objectId=pushData.getObjectId();
            items.add(new Item(offertitle, biz_name,objectId));

        }
        adapter = new OffersAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(getActivity(),OfferDetails.class);
                startActivity(i);

            }
        });
        return offers;
    }

    public void populateOffers() {
        items.clear();
        user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
        query.whereNotEqualTo("OfferTitle", "fff");
        query.orderByDescending("createdAt");
        query.setLimit(1000);
        try
        {
            lst = query.find();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        for (i = 0; i < lst.size(); i++)
        {
            pushData = lst.get(i);
            String offertitle = pushData.getString("OfferTitle");
            String biz_name = pushData.getString("Biz_name");
            String objectId=pushData.getObjectId();
            items.add(new Item(offertitle, biz_name,objectId));
        }
        adapter.notifyDataSetChanged();
    }


}
