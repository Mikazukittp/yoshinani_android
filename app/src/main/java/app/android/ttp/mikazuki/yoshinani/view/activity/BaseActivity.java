package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.Subscribe;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent;
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh(false);
    }

    protected void goTo(Class<? extends Activity> activity) {
        goTo(activity, true, null);
    }

    protected void goTo(Class<? extends Activity> activity,
                        boolean addToBackStack) {
        goTo(activity, addToBackStack, null);
    }

    protected void goTo(Class<? extends Activity> activity,
                        boolean addToBackStack,
                        Bundle bundle) {
        Intent i = new Intent(this, activity);
        if (addToBackStack) {
            i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            i.putExtras(bundle);
        }
        startActivity(i);
        if (!addToBackStack) {
            finish();
        }
    }

    protected void replaceFragment(Fragment fragment,
                                   @IdRes int layoutId) {
        replaceFragment(fragment, layoutId, true, null);
    }

    protected void replaceFragment(Fragment fragment,
                                   @IdRes int layoutId,
                                   boolean addToBackStack) {
        replaceFragment(fragment, layoutId, addToBackStack, null);
    }

    protected void replaceFragment(Fragment fragment,
                                   @IdRes int layoutId,
                                   boolean addToBackStack,
                                   Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutId, fragment, null);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * Refresh data<br/>
     * <br/>
     * This method is called on {@link app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity#onResume()} and {@link app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity#onEvent(RefreshEvent event)}<br/>
     * refreshForcibly is true when called from {@link app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity#onEvent(RefreshEvent event)} and is false {@link app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity#onResume()}<br/>
     * <br/>
     * This is blank method by default.
     * Concrete process should be implemented in Subclass
     *
     * @param refreshForcibly whether update forcibly or not
     */
    public void refresh(boolean refreshForcibly) {
    }

    @Subscribe
    public void onEvent(ActivityTransitionEvent event) {
        goTo(event.getDestinationActivity(), event.doesAddToBackStack(), event.getBundle());
    }

    @Subscribe
    public void onEvent(FragmentTransitionEvent event) {
        replaceFragment(event.getDestinationFragment(), R.id.fragment_container, event.doesAddToBackStack(), event.getBundle());
    }

    @Subscribe
    public void onEvent(UnauthorizedEvent event) {
        goTo(LoginActivity.class, false);
    }

    @Subscribe
    public void onEvent(RefreshEvent event) {
        refresh(true);
    }

    @Subscribe
    public void onEvent(ErrorEvent event) {
        new AlertDialog.Builder(this).setTitle(event.getTitle())
                .setMessage(event.getMessage())
                .setPositiveButton("閉じる", null)
                .create()
                .show();
    }


}
