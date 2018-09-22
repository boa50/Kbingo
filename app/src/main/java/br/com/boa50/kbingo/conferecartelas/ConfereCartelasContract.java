package br.com.boa50.kbingo.conferecartelas;

import java.util.ArrayList;

import br.com.boa50.kbingo.BasePresenter;
import br.com.boa50.kbingo.BaseView;

public interface ConfereCartelasContract {
    interface View extends BaseView {
        void apresentarCartelas(ArrayList<String> cartelas);
        void apresentarCartelasFiltradas(ArrayList<String> cartelas);
    }

    interface Presenter extends BasePresenter<View> {
//        void subscribe(View view, ArrayList<String> cartelasGanhadoras, String textoPadrao);
//        void subscribe(View view, String textoPadrao);
//        ArrayList<String> getCartelasGanhadoras();
//        int getCartelasGanhadorasSize();
        void filtrarCartelas(String filtro);
    }

}
