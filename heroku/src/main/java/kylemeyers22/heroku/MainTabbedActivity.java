package kylemeyers22.heroku;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import kylemeyers22.heroku.adapters.TabAdapter;
import kylemeyers22.heroku.apiObjects.Team;
import kylemeyers22.heroku.utils.Constants;

public class MainTabbedActivity extends AppCompatActivity implements TeamFragment.TeamsUpdated {
    @Override
    public void sendTeams(ArrayList<Team> teamList) {
        GameFragment gameFrag = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.gameFragment);
        System.out.println("TABBED ACTIVITY: " + teamList.toString());
        gameFrag.storeTeams(teamList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Players"));
        tabLayout.addTab(tabLayout.newTab().setText("Teams"));
        tabLayout.addTab(tabLayout.newTab().setText("Games Played"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(Constants.offScreenLimit);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
