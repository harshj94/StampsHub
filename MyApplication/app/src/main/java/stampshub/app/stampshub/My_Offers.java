package stampshub.app.stampshub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class My_Offers extends android.support.v4.app.Fragment {

    View offers;
    public static ListView listView;
    String parseUser;
    BuyerDashboard bd=new BuyerDashboard();
    List<ParseObject> lst;
    public static ArrayList<Item> items = new ArrayList<>();
    ParseObject pushData,myoffer;
    ParseQuery<ParseObject> query;
    String objectId;
    public static OffersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        offers=inflater.inflate(R.layout.my_offers,container,false);

        listView=(ListView)offers.findViewById(R.id.offerslist1);
        parseUser=ParseUser.getCurrentUser().getObjectId();

        query=ParseQuery.getQuery("myoffer");
        query.whereEqualTo("user", parseUser);
        query.orderByDescending("createdAt");
        query.setLimit(1000);
        query.fromLocalDatastore();

        try
        {
            lst=query.find();
        }
        catch (ParseException e)
        {
            e.getMessage();
        }

        items.clear();

        for(int i=0;i<lst.size();i++)
        {
            pushData = lst.get(i);

            String offerobject=pushData.getString("offer");
            query=ParseQuery.getQuery("Offer");

            try
            {
                myoffer=query.get(offerobject);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            String offertitle = myoffer.getString("OfferTitle");
            String biz_name = myoffer.getString("Biz_name");
            objectId=myoffer.getObjectId();
            items.add(new Item(offertitle, biz_name,objectId));
        }
        adapter = new OffersAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(getActivity(),Myofferdetails.class);
                Item i1=items.get(position);
                objectId=i1.getObjectId();
                i.putExtra("objectId",objectId);
                startActivity(i);
                getActivity().finish();
            }
        });
        return offers;
    }

    public class UpdateOffers extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            parseUser=ParseUser.getCurrentUser().getObjectId();

            query=ParseQuery.getQuery("myoffer");
            query.whereEqualTo("user", parseUser);
            query.orderByDescending("createdAt");
            query.setLimit(1000);

            try
            {
                ParseObject.unpinAll("myoffer");
                lst=query.find();
                ParseObject.pinAll("myoffer",lst);
            }

            catch (ParseException e)
            {
                e.getMessage();
            }

            items.clear();

            for(int i=0;i<lst.size();i++)
            {
                pushData = lst.get(i);

                String offerobject=pushData.getString("offer");
                query=ParseQuery.getQuery("Offer");

                try
                {
                    myoffer=query.get(offerobject);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                String offertitle = myoffer.getString("OfferTitle");
                String biz_name = myoffer.getString("Biz_name");
                objectId=myoffer.getObjectId();
                items.add(new Item(offertitle, biz_name,objectId));
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            bd.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }

    public void populateOffers()
    {
        new UpdateOffers().execute();
    }

}
