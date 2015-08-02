package stampshub.app.stampshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hp on 01-08-2015.
 */
public class TabPageAdapter extends FragmentStatePagerAdapter {
    public TabPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new Offers();
            case 1:
                return new My_Offers();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
