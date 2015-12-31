package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.android.ttp.mikazuki.yoshinani.App;
import app.android.ttp.mikazuki.yoshinani.di.component.AppComponent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AppComponent getAppComponent() {
        return App.get(this).component();
    }

}
