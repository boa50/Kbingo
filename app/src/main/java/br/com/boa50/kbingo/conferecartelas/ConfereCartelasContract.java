package br.com.boa50.kbingo.conferecartelas;

import java.util.ArrayList;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;

public interface ConfereCartelasContract {
    interface View extends BaseView {
        void apresentarCartelas();
        void apresentarCartelasFiltradas();
    }

    interface Presenter extends BasePresenter<View> {
        void subscribe(View view, ArrayList<String> cartelasGanhadoras, String textoPadrao);
        ArrayList<String> getCartelasGanhadoras();
        int getCartelasGanhadorasSize();
        void filtrarCartelas(String filtro);
    }

}
