package stampshub.app.stampshub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
    BuyerDashboard bd= new BuyerDashboard();
    public static ListView listView;
    String objectId;

    public static ArrayList<Item> items = new ArrayList<>();
    public static List<ParseObject> lst;
    int i;
    public static OffersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        offers = inflater.inflate(R.layout.offers, container, false);
        listView = (ListView) offers.findViewById(R.id.offerslist);

        updateOffers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(getActivity(),OfferDetails.class);
                Item i1=items.get(position);
                objectId=i1.getObjectId();
                i.putExtra("objectId",objectId);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
                getActivity().finish();
            }
        });
        return offers;
    }

    public void populateOffers()
    {
        new UpdateOffers().execute();
    }

    public class UpdateOffers extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            items.clear();
            user = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
            query.whereNotEqualTo("OfferTitle", "fff");
            query.orderByDescending("createdAt");
            query.setLimit(1000);
            try
            {
                ParseObject.unpinAll("Offer");
                lst = query.find();
                ParseObject.pinAll("Offer",lst);

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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            bd.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    adapter.notifyDataSetChanged();
                }
            });
            BuyerDashboard.setRefreshActionButtonState(false);
        }
    }

    public void updateOffers(){
        new updateList().execute();
    }


    public class updateList extends AsyncTask<Void,Void,Void>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            nDialog = new ProgressDialog(getActivity());
            nDialog.setTitle("");
            nDialog.setMessage("Refreshing...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            user = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Offer");
            query.whereNotEqualTo("OfferTitle", "fff");
            query.orderByDescending("createdAt");
            query.setLimit(1000);
            query.fromLocalDatastore();
            try
            {
                lst = query.find();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            items.clear();
            for (i = 0; i < lst.size(); i++) {
                pushData = lst.get(i);
                String offertitle = pushData.getString("OfferTitle");
                String biz_name = pushData.getString("Biz_name");
                objectId=pushData.getObjectId();
                items.add(new Item(offertitle, biz_name,objectId));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            nDialog.dismiss();

            bd.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new OffersAdapter(getActivity(), items);
                    listView.setAdapter(adapter);
                }
            });
        }
    }
}
