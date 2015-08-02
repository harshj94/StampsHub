package stampshub.app.stampshub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class My_Offers extends android.support.v4.app.Fragment {

    View my_offers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_offers=inflater.inflate(R.layout.my_offers,container,false);


        return my_offers;
    }
}
