package app.android.ttp.mikazuki.yoshinani.di.component;

import javax.inject.Singleton;

import app.android.ttp.mikazuki.yoshinani.di.module.RepositoryModule;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import app.android.ttp.mikazuki.yoshinani.ui.activity.LoginActivity;
import dagger.Component;

/**
 * Created by haijimakazuki on 15/09/09.
 */

@Singleton
@Component(modules = {
//        AppModule.class,
        RepositoryModule.class
})
public interface AppComponent {
//    void inject(App app);
    void inject(LoginActivity activity);

    UserRepository getUserRepository();
//    AnalyticsManager getAnalyticsManager();
//    FindItemsInteractor getFindItemsInteractor();
}