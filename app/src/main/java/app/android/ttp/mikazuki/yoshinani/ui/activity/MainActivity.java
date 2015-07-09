package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.MainFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.SubFragment;
import app.android.ttp.mikazuki.yoshinani.ui.listener.ToolBarListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ToolBarListener, MainFragment.OnFragmentInteractionListener, SubFragment.OnFragmentInteractionListener{

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_main) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new MainFragment())
                            .addToBackStack(null)
                            .commit();
                    mDrawerLayout.closeDrawers();
                    return true;
                }else if (itemId == R.id.menu_sub) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SubFragment())
                            .addToBackStack(null)
                            .commit();
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();
    }

    @Override
    public void goToMainView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSubView() {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, new SubFragment())
//                .addToBackStack(null)
//                .commit();

        Intent i = new Intent(this, PostActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuClicked() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

}

