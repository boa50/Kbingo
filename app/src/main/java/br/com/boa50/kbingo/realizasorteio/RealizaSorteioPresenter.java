package br.com.boa50.kbingo.realizasorteio;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.BaseView;
import br.com.boa50.kbingo.data.Pedra;
import br.com.boa50.kbingo.data.source.PedrasRepository;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boa50 on 3/11/18.
 */

public class RealizaSorteioPresenter implements RealizaSorteioContract.Presenter {

    private RealizaSorteioContract.View mRealizaSorteioView;

    private final PedrasRepository mPedrasRespository;

    private final BaseSchedulerProvider mScheduleProvider;

    private CompositeDisposable mCompositeDisposable;

    private List<Pedra> mPedras;

    private int mUltimaPedraPosicao;

    @Inject
    RealizaSorteioPresenter(PedrasRepository pedrasRespository, BaseSchedulerProvider schedulerProvider) {
        mPedrasRespository = checkNotNull(pedrasRespository, "Pedras Repository cannot be null");
        mScheduleProvider = checkNotNull(schedulerProvider, "Schedule Provider cannot null");

        mCompositeDisposable = new CompositeDisposable();

        mUltimaPedraPosicao = -1;
    }

    @Override
    public void subscribe(BaseView view) {
        mRealizaSorteioView = (RealizaSorteioContract.View) view;
        carregarPedras();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mRealizaSorteioView = null;
    }

    @Override
    public void sortearPedra() {
        mRealizaSorteioView.apresentarPedra(mPedras.get(++mUltimaPedraPosicao).getValorPedra());

        mPedras.get(mUltimaPedraPosicao).setmSorteada(true);

        mRealizaSorteioView.apresentarPedras(mPedras);

        if (mUltimaPedraPosicao == (mPedras.size() - 1))
            mRealizaSorteioView.apresentarFimSorteio();
    }

    private void carregarPedras(){
        if (mPedras == null) {
            mCompositeDisposable.clear();

            Disposable disposable = mPedrasRespository
                    .getPedras()
                    .flatMap(Flowable::fromIterable)
                    .toList()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            pedras -> {
                                mPedras = pedras;
                                Collections.shuffle(mPedras);
                            },
                            throwable -> mPedras = null
                    );

            mCompositeDisposable.add(disposable);
        }

        mRealizaSorteioView.apresentarPedras(mPedras);
    }
}
