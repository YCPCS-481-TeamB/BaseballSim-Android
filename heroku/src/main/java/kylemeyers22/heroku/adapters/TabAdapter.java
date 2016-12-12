package kylemeyers22.heroku.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kylemeyers22.heroku.ApprovalsFragment;
import kylemeyers22.heroku.GameFragment;
import kylemeyers22.heroku.PlayerFragment;
import kylemeyers22.heroku.TeamFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    private int numTabs;

    public TabAdapter(FragmentManager fragMan, int numberTabs) {
        super(fragMan);
        this.numTabs = numberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlayerFragment();
            case 1:
                return new TeamFragment();
            case 2:
                return new GameFragment();
            case 3:
                return new ApprovalsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
