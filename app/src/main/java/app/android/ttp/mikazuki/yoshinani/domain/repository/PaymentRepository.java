package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface PaymentRepository extends BaseRepository<Payment> {
    public void getAll(BaseCallback<List<Payment>> cb);
}
