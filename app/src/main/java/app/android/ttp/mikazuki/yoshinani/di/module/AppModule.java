package app.android.ttp.mikazuki.yoshinani.di.module;

import android.app.Application;

import javax.inject.Singleton;

import app.android.ttp.mikazuki.yoshinani.App;
import dagger.Module;
import dagger.Provides;

/**
 * Created by haijimakazuki on 15/09/09.
 */
@Module
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

}
