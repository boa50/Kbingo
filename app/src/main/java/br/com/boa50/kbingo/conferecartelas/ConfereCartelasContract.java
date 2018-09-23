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
        void filtrarCartelas(String filtro);
    }

}
