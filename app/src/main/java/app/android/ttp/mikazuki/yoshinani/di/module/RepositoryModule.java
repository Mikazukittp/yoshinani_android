package app.android.ttp.mikazuki.yoshinani.di.module;

import android.content.Context;

import javax.inject.Singleton;

import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitGroupRepository;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitPaymentRepository;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitUserRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.GroupRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Created by haijimakazuki on 15/09/09.
 */
@Module
public class RepositoryModule {

    private Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public GroupRepository provideGroupRepository() {
        return new RetrofitGroupRepository(context);
    }

    @Provides
    @Singleton
    public PaymentRepository providePaymentRepository() {
        return new RetrofitPaymentRepository(context);
    }

    @Provides
    public UserRepository provideUserRepository() {
        return new RetrofitUserRepository(context);
    }

}
