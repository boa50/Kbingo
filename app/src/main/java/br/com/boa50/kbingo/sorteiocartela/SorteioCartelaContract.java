package br.com.boa50.kbingo.sorteiocartela;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;

public interface SorteioCartelaContract {

    interface View extends BaseView {
        void apresentarCartela(String numeroCartela);
    }

    interface Presenter extends BasePresenter<View> {
        void sortearCartela();
    }
}
