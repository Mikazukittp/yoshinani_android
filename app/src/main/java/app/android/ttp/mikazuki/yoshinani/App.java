package app.android.ttp.mikazuki.yoshinani;

import android.app.Application;
import android.content.Context;

import app.android.ttp.mikazuki.yoshinani.di.component.AppComponent;
import app.android.ttp.mikazuki.yoshinani.di.component.DaggerAppComponent;
import app.android.ttp.mikazuki.yoshinani.di.module.RepositoryModule;

/**
 * Created by haijimakazuki on 15/09/09.
 */
public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(this))
                .build();
    }

    public AppComponent component() {
        return component;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
